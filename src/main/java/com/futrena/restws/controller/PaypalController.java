package com.futrena.restws.controller;

import com.futrena.restws.models.*;
import com.futrena.restws.request.FullProductRequest;
import com.futrena.restws.request.ProductID;
import com.futrena.restws.request.ProductRequest;
import com.futrena.restws.response.ResponseMessage;
import com.futrena.restws.utils.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by York on 15-04-28.
 */

@RestController
public class PaypalController {
	
	private static final Logger log = Logger.getLogger(ProductController.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ProductRateDao productRateDao;
	
	@Autowired
	private UserPaymentDao userPayment;
	
	@Autowired
	private ShoppingCartDao shoppingCartDao;
	
	@Autowired
	private UserTransactionDao userTransactionDao;
	   
	@PersistenceContext
	private EntityManager  entityManager;

	@Autowired
	private TransactionHistoryDao transactionHistoryDao;
	
	@Autowired
	private BuyerHistoryDao buyerHistoryDao;
	
	private static final String paypalURL="https://api-3t.sandbox.paypal.com/nvp";    
	private static final String USER="york.tips-facilitator_api1.gmail.com";
	private static final String PWD="L7KDD899XGJJAGU5";		
	private static final String SIGNATURE="A5Z-BjK07zqPlx4Dr-.DzaEoPvQvA6XUTwdcE-J8E9c4JwdaLSAZkeF8";
	private static final String VERSION="98.0";
	private static final String RETURNURL="http://www.futrena.com";
	private static final String CANCELURL="http://www.futrena.com";	
	private static final String PAYPAL_HOST="api-3t.sandbox.paypal.com";
		
	@RequestMapping(value="/checkout/paypal/{productCartID}", method=RequestMethod.GET)
	public ResponseEntity<Object> SetExpressCheckout(HttpServletRequest request, @PathVariable String productCartID){
		
		ResponseMessage responseMessage;
		List<ShoppingCart> s = shoppingCartDao.findByProductCartID(productCartID);
		String paras=CreatePaypalCheckoutParas(request,productCartID);
 	    UUID unique_id = UUID.randomUUID();
 	    
	    HashMap<String, String> nvp=PaypalUtil.httpsCall(PAYPAL_HOST, paypalURL, paras, unique_id.toString());
	    log.info("SetExpressCheckout");
	    addTransactionHistory(productCartID, paras, nvp );	    	  
	    addPaymentHistory(productCartID, paras, nvp );

	    String paypal_token=nvp.get("TOKEN")!=null?nvp.get("TOKEN").toString() : "";
	    if (paypal_token.equalsIgnoreCase("")) {
	    	responseMessage = new ResponseMessage("ERROR",nvp.toString());
	    } else {
	    	responseMessage = new ResponseMessage("paypal_token",nvp.get("TOKEN").toString());
	    }
	    
	    return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
	}
	
	@RequestMapping(value="/paypal/{productCartID}", method=RequestMethod.GET)
	public ResponseEntity<Object> PostPayPalProcess(HttpServletRequest request,  HttpServletResponse response, @PathVariable String productCartID){
		String transactionID=""; 		//?
		String processState="DONE";		//?
		String paypalMsg=PaypalUtil.httpRequestDump(request);	
		String payment_status = request.getParameter("payment_status")!=null ? request.getParameter("payment_status") : "";
		String mc_gross = request.getParameter("mc_gross")!=null ? request.getParameter("mc_gross") : "";
		String payment_gross = request.getParameter("payment_gross")!=null ? request.getParameter("payment_gross") : "";
		String payment_type = request.getParameter("payment_status")!=null ? request.getParameter("payment_type") : "";
		String pending_reason = request.getParameter("pending_reason")!=null ? request.getParameter("pending_reason") : "";
		ResponseMessage responseMessage;
		
		try{				
			//1. Update buyerhistory table
			List<ShoppingCart> s = shoppingCartDao.findByProductCartID(productCartID);		
			addTransactionHistoryPostPay(productCartID, request,  response );
			
			List<BuyerHistory> buyerHistorys = buyerHistoryDao.findByProductCartID(productCartID);
			for (BuyerHistory buyerHistory :buyerHistorys ) {
				buyerHistory.setTransactionID(transactionID);
				buyerHistory.setProcessState(payment_status);
				buyerHistory.setCreateDate(new Date());
				buyerHistoryDao.save(buyerHistory);
			}
		
			//empty shorppingcard
			 if (shoppingCartDao.findByProductCartID(productCartID)!=null) {
				 List<ShoppingCart> shoppingCarts = shoppingCartDao.findByProductCartID(productCartID);	
				 for (ShoppingCart shoppingCart :shoppingCarts ){
					 shoppingCartDao.delete(shoppingCart);
				 }
			 }

			 responseMessage = new ResponseMessage("PAYPAL RETURN",paypalMsg);
			 
		}catch(Exception e){ 
			responseMessage = new ResponseMessage("PAYPAL RETURN", paypalMsg + "--" + e.getMessage());			
		}
		
		return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
	}		
	
	
	@RequestMapping(value="/paypal/cancel/{productCartID}", method=RequestMethod.GET)
	public ResponseEntity<Object> PayPalCancelProcess(HttpServletRequest request,  HttpServletResponse response, @PathVariable String productCartID){
		String processState="CANCELED";		//?		
		List<ShoppingCart> s = shoppingCartDao.findByProductCartID(productCartID);		
		String paypalMsg=PaypalUtil.httpRequestDump(request);	
		String payment_status = request.getParameter("payment_status")!=null ? request.getParameter("payment_status") : "";
		String mc_gross = request.getParameter("mc_gross")!=null ? request.getParameter("mc_gross") : "";
		String payment_gross = request.getParameter("payment_gross")!=null ? request.getParameter("payment_gross") : "";
		String payment_type = request.getParameter("payment_status")!=null ? request.getParameter("payment_type") : "";
		String pending_reason = request.getParameter("pending_reason")!=null ? request.getParameter("pending_reason") : "";
		ResponseMessage responseMessage;
		
		List<BuyerHistory> buyerHistorys = buyerHistoryDao.findByProductCartID(productCartID);
		try{
			for (BuyerHistory buyerHistory :buyerHistorys ) {
				buyerHistory.setProcessState(processState);
				buyerHistory.setCreateDate(new Date());
				buyerHistoryDao.save(buyerHistory);
			}
			responseMessage = new ResponseMessage("USER CANCELED", paypalMsg );
		}catch(Exception e){ 
			responseMessage = new ResponseMessage("USER CANCELED", paypalMsg + "--" + e.getMessage());			
		}
		
		return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
	}		

	
	
    public String CreatePaypalCheckoutParas (HttpServletRequest request, String productCartID) {
		String paras="";
		HttpSession session = request.getSession(true);
		
    	List<ShoppingCart> shoppingCart= shoppingCartDao.findByProductCartID(productCartID);
		String Payment_Amount=Float.toString(getAmtInCart(productCartID));
		String METHOD="SetExpressCheckout";	    		
		List<String> mechants = getMerchantsInCart(productCartID);
		int nIndex=0;
		String email=shoppingCart.get(0).getBuyerEmail();
		String country=getShippingCountry(email);
		
  		paras= paras + "USER=" + USER;
   		paras= paras + "&PWD=" + PWD;		//# the caller account Password
   		paras= paras + "&SIGNATURE=" + SIGNATURE;	//# the caller account Signature
   		paras= paras + "&METHOD=" + METHOD;			//# API operation
		paras= paras + "&RETURNURL=" + RETURNURL + "/paypal/" + productCartID;	//# URL displayed to buyer after authorizing transaction
		paras= paras + "&CANCELURL=" + CANCELURL + "/paypal/cancel/" + productCartID;	//# URL displayed to buyer after canceling transaction
		paras= paras + "&VERSION=" + VERSION;			//# API version
		float totalPayAmt=0;
		
		for (String mechant : mechants ) {
			
			String sIndex = Integer.toString(nIndex);
			List<Product> products=getProductsByMerchantsInCart(productCartID,mechant);
			float taxRatio = 0;
			
			float PAYMENTREQUEST_ITEMAMT = getAmtByMerchantsInCart(productCartID,mechant);
			float PAYMENTREQUEST_TAXAMT = PAYMENTREQUEST_ITEMAMT * taxRatio;
			float PAYMENTREQUEST_INSURANCEAMT=PaypalUtil.getShippingAmt(country,products);
			float PAYMENTREQUEST_SHIPDISCAMT=0;
			float PAYMENTREQUEST_AMT=PAYMENTREQUEST_ITEMAMT + PAYMENTREQUEST_TAXAMT + PAYMENTREQUEST_INSURANCEAMT - PAYMENTREQUEST_SHIPDISCAMT;
			totalPayAmt += PAYMENTREQUEST_AMT;
			
			String PAYMENTREQUEST_SELLERPAYPALACCOUNTID=PaypalUtil.getPayPalAccount(sIndex);
			String PAYMENTREQUEST_INSURANCEOPTIONOFFERED="FALSE";
			String PAYMENTREQUEST_PAYMENTACTION="Order";
			String PAYMENTREQUEST_PAYMENTREQUESTID=productCartID + "_" + "PAYMENT" + Integer.toString(nIndex);
			String PAYMENTREQUEST_DESC =PaypalUtil.getPaymentDesc(productCartID, products);
			
			if ( PAYMENTREQUEST_AMT != 0 && !PAYMENTREQUEST_SELLERPAYPALACCOUNTID.equals("")) {
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_CURRENCYCODE=CND";//PAYMENTREQUEST_0_CURRENCYCODE;
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_AMT=" + PaypalUtil.float2Str(PAYMENTREQUEST_AMT);
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_ITEMAMT=" + PaypalUtil.float2Str(PAYMENTREQUEST_ITEMAMT);
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_TAXAMT=" + PaypalUtil.float2Str(PAYMENTREQUEST_TAXAMT);
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_DESC=" + PAYMENTREQUEST_DESC;
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_INSURANCEAMT=" + PaypalUtil.float2Str(PAYMENTREQUEST_INSURANCEAMT);
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_SHIPDISCAMT=" + PaypalUtil.float2Str(PAYMENTREQUEST_SHIPDISCAMT);
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_SELLERPAYPALACCOUNTID=" + PAYMENTREQUEST_SELLERPAYPALACCOUNTID;
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_INSURANCEOPTIONOFFERED=" + PAYMENTREQUEST_INSURANCEOPTIONOFFERED;
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_PAYMENTACTION=" + PAYMENTREQUEST_PAYMENTACTION;
				paras= paras + "&PAYMENTREQUEST_" + sIndex + "_PAYMENTREQUESTID=" + PAYMENTREQUEST_PAYMENTREQUESTID;
				nIndex=nIndex+1;
				
				int nIndex2 = 0;
				for ( Product product : products ) {
					String sIndex2 = Integer.toString(nIndex2);
					String L_PAYMENTREQUEST_NAME=product.getProductID() + " - " + product.getProductName();
					String L_PAYMENTREQUEST_NUMBER = product.getDescription();
					String L_PAYMENTREQUEST_QTY=Integer.toString(getProductQuantityInCart(productCartID,product.getProductID()));
					String L_PAYMENTREQUEST_DESC= product.getDescription();
					float L_PAYMENTREQUEST_ITEMAMT=product.getPrice() * Integer.parseInt(L_PAYMENTREQUEST_QTY);
					float L_PAYMENTREQUEST_INSURANCEAMT=PaypalUtil.getShippingAmt(country,product); //shipping fee
					float L_PAYMENTREQUEST_SHIPDISCAMT=0;
					float L_PAYMENTREQUEST_AMT=L_PAYMENTREQUEST_ITEMAMT  + L_PAYMENTREQUEST_INSURANCEAMT - L_PAYMENTREQUEST_SHIPDISCAMT;
					float L_PAYMENTREQUEST_TAXAMT = L_PAYMENTREQUEST_AMT * taxRatio;
					if (L_PAYMENTREQUEST_AMT != 0 ) {			
						paras= paras + "&L_PAYMENTREQUEST_" + sIndex + "_NAME" + nIndex2 + "=" + L_PAYMENTREQUEST_NAME;
						paras= paras + "&L_PAYMENTREQUEST_" + sIndex + "_NUMBER" + nIndex2 + "=" + L_PAYMENTREQUEST_NUMBER;
						paras= paras + "&L_PAYMENTREQUEST_" + sIndex + "_QTY" + nIndex2 + "=" + L_PAYMENTREQUEST_QTY;
						paras= paras + "&L_PAYMENTREQUEST_" + sIndex + "_TAXAMT" + nIndex2 + "=" + PaypalUtil.float2Str(L_PAYMENTREQUEST_TAXAMT);
						paras= paras + "&L_PAYMENTREQUEST_" + sIndex + "_AMT" + nIndex2 + "=" + PaypalUtil.float2Str(L_PAYMENTREQUEST_AMT); //itemamout
						paras= paras + "&L_PAYMENTREQUEST_" + sIndex + "_DESC" + nIndex2 + "=" + L_PAYMENTREQUEST_DESC;
						nIndex2=nIndex2+1;
					}
				}
			}
			
		}
		
		paras= paras + "&ALLOWNOTE=1&NOSHIPPING=1";
		
		session.setAttribute("Payment_Amount", String.valueOf(totalPayAmt)); 
		
		return paras;
    }

    
    
    public List<String> getMerchantsInCart(String productCartID){
		String sql="";
		
		sql=sql + "SELECT OWNER";
		sql=sql + " FROM product ";
		sql=sql + " WHERE productid IN (SELECT productid FROM shopping_cart WHERE product_cartid='" + productCartID + "')";
		sql=sql + " GROUP BY OWNER";
		sql=sql + " ORDER BY OWNER";
		
		List<String> owners = new ArrayList<String>();
		
		Query query=this.entityManager.createNativeQuery(sql);
		List<Object[]> results = query.getResultList();
		for (Object[] result : results) {
			owners.add((String)result[0]);			
		}
		
		return owners;
    }
  
    public List<Product> getProductsByMerchantsInCart(String productCartID, String owner){
 		String where=" where productid IN ";
 		where = where + " (SELECT productid FROM shopping_cart WHERE product_cartid='" + productCartID + "')";
 		where = where + " and OWNER = '" + owner + "'";
 		
 		Query query=this.entityManager.createQuery("from Product " + where); 		
 		List list = query.getResultList();
 		
 		return list;
    }
    
    public int getProductQuantityInCart(String productCartID, String productid){
    	String sql = "SELECT SUM(x.quantity) FROM ShoppingCart x WHERE ";
    	sql=sql + " productCartID='" + productCartID + "' and productid='" + productid + "'";
    	
    	Query q = this.entityManager.createQuery(sql);
    	return  (int) q.getSingleResult ();
    }
    
    
    public float getAmtByMerchantsInCart(String productCartID, String owner){
    	float amt=0;
    	List<Product> products=getProductsByMerchantsInCart(productCartID, owner);
    	for (Product product : products) {
    		int nQuantiry=getProductQuantityInCart(productCartID,product.getProductID());
    		amt=amt + nQuantiry * product.getPrice();
    	}
    	
    	return amt;
    }
    
    public float getAmtInCart(String productCartID){
    	float amt=0;
    	List<String> owners=getMerchantsInCart(productCartID);
    	
    	for (String owner : owners) {
    		amt=amt + getAmtByMerchantsInCart(productCartID, owner);
    	}
    	
    	return amt;
    } 

    public String getShippingCountry(String eamil){
    	User user=userDao.findByEmail(eamil);
    	String country=user.getCountry();
    	if (eamil.equalsIgnoreCase("China") || eamil.equalsIgnoreCase("China") ) return "CHN";
    	if (eamil.equalsIgnoreCase("USA") || eamil.equalsIgnoreCase("US") ) return "US";
    	return "CA";
    }
    
    private boolean addPaymentHistory(String productCartID, String paras, HashMap<String, String> nvp ){
    	boolean ret=false;
    	List<ShoppingCart> shoppingCarts = shoppingCartDao.findByProductCartID(productCartID);
    	try{
	    	for (ShoppingCart shoppingCart : shoppingCarts) {
	    		BuyerHistory buyerHistory = new BuyerHistory();
	    		buyerHistory.setProductCartID(productCartID);
	    		buyerHistory.setProductID(shoppingCart.getProductID());
	    		buyerHistory.setProductName(shoppingCart.getProductName());
	    		buyerHistory.setBuyerEmail(shoppingCart.getBuyerEmail());
	    		buyerHistory.setCreateDate(new Date());
	    		buyerHistory.setQuantity((int)shoppingCart.getQuantity());
	    		buyerHistory.setProcessState("Order");
	    		buyerHistory.setTransactionID("");
	    		buyerHistory.setPayMethod("PayPal");
	    		buyerHistory.setPaypalAccount("");
	    		buyerHistory.setPaypalToken(nvp.get("TOKEN").toString());
	    		buyerHistoryDao.save(buyerHistory);    
	    		ret=true;
	    	}
    	}catch(Exception e){
    		log.error(e.getMessage());
    	}
    	return ret;
    }
    
    private boolean addTransactionHistory(String productCartID, String paras, HashMap<String, String> nvp ){
    	boolean ret=false;
    	List<String> mechants = getMerchantsInCart(productCartID);
    	List<ShoppingCart> shoppingCarts = shoppingCartDao.findByProductCartID(productCartID);
    	int nIndex=0;
    	try{
	    	for (String mechant : mechants ) {
	    		String sIndex = Integer.toString(nIndex);
	    		TransactionHistory transactionHistory = new TransactionHistory();
		    	transactionHistory.setProductCartID(productCartID);
		    	transactionHistory.setTransactionID("");
		    	transactionHistory.setBuyer(shoppingCarts.get(0).getBuyerEmail());
		    	
		    	Object amt=nvp.get("PAYMENTREQUEST_" + sIndex + "_AMT");
		    	if ( amt != null ) {
		    		transactionHistory.setTotalAmt(Float.parseFloat(amt.toString()));
		    	}
		    	
		    	Object seller=nvp.get("PAYMENTREQUEST_" + sIndex + "_SELLERPAYPALACCOUNTID");
		    	if ( seller != null ) {
		    		transactionHistory.setSeller(seller.toString());
		    	}
	
		    	Object payMethod = nvp.get("PAYMENTREQUEST_" + sIndex + "_SELLERPAYPALACCOUNTID");
		    	if ( seller != null ) {
		    		transactionHistory.setSeller(seller.toString());
		    	}
		    	
		    	transactionHistory.setPayMethod("PayPal");
		    	transactionHistory.setPaypalAccount(seller.toString());
		    	transactionHistory.setPaypalToken(nvp.get("TOKEN").toString());
		    	transactionHistory.setRequest(paras);
		    	transactionHistory.setResponse(nvp.toString());
		    	transactionHistory.setSTATUS("ORDER");
		    	transactionHistory.setUpdated(new Date());
		    	transactionHistory.setCreated(new Date());
		    	transactionHistoryDao.save(transactionHistory);
		    	nIndex=nIndex+1;
	    	}
	    	ret=true;
    	}catch(Exception e){
    		log.error(e.getMessage());
    	}
    	return ret;
    }
    
    //This is called by PAYPAL. i.e., PAYPAY's return URL
    private boolean addTransactionHistoryPostPay(String productCartID, HttpServletRequest request,  HttpServletResponse response ){
    	boolean ret=false;
    	List<String> mechants = getMerchantsInCart(productCartID);
    	List<ShoppingCart> shoppingCarts = shoppingCartDao.findByProductCartID(productCartID);
    	
    	try{
    		TransactionHistory transactionHistory = new TransactionHistory();
	    	transactionHistory.setProductCartID(productCartID);
	    	transactionHistory.setTransactionID("?");
	    	transactionHistory.setBuyer("");
    		//transactionHistory.setTotalAmt(0);
    		transactionHistory.setSeller("");
	    	transactionHistory.setPayMethod("PayPal");
	    	transactionHistory.setPaypalAccount("");
	    	transactionHistory.setPaypalToken("");	    	
	    	transactionHistory.setRequest(PaypalUtil.httpRequestDump(request));
	    	transactionHistory.setResponse(response.toString());
	    	transactionHistory.setSTATUS("PAID");
	    	transactionHistory.setUpdated(new Date());
	    	transactionHistory.setCreated(new Date());
	    	transactionHistoryDao.save(transactionHistory);
	    	ret=true;
    	}catch(Exception e){
    		log.error(e.getMessage());
    	}
    	return ret;
    }
    
}

