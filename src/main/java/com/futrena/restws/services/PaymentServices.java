package com.futrena.restws.services;

import com.futrena.restws.controller.ProductController;
import com.futrena.restws.models.*;
import com.futrena.restws.request.FullProductRequest;
import com.futrena.restws.request.ProductID;
import com.futrena.restws.request.ProductRequest;
import com.futrena.restws.response.ResponseMessage;
import com.futrena.restws.utils.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
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

@Service
@PropertySource("classpath:/application.properties")
public class PaymentServices {
	
	private static final Logger log = Logger.getLogger(PaymentServices.class);

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

	@Value("${paypal.user}")
	private String PAYPAL_USER;

	@Value("paypal.pwd}")
	private String PAYPAL_PWD;

	@Value("${paypal.signature}")
	private String PAYPAL_SIGNATURE;
	
	@Value("${paypal.returnurl}")
	private String PAYPAL_RETURNURL;

	@Value("${paypal.cancelurl}")
	private String PAYPAL_CANCELURL;

	@Value("${paypal.version}")
	private String PAYPAL_VERSION;	
	
    public String CreatePaypalCheckoutParas (HttpServletRequest request, String productCartID) {
		String paras="";		
		HttpSession session = request.getSession(true);
		
    	List<ShoppingCart> shoppingCart= shoppingCartDao.findByProductCartID(productCartID);
		String Payment_Amount=Float.toString(getAmtInCart(productCartID));
		List<String> mechants = getMerchantsInCart(productCartID);
		int nIndex=0;
		String email=shoppingCart.get(0).getBuyerEmail();
		String country=getShippingCountry(email);
		
		if ( mechants.size() > 6 ) {
			return "6";
		}
		
  		paras= paras + "USER=" + PAYPAL_USER;
   		paras= paras + "&PWD=" + PAYPAL_PWD;		//# the caller account Password
   		paras= paras + "&SIGNATURE=" + PAYPAL_SIGNATURE;	//# the caller account Signature
   		paras= paras + "&METHOD=SetExpressCheckout";			//# API operation
		paras= paras + "&RETURNURL=" + PAYPAL_RETURNURL + "/paypal/" + productCartID;	//# URL displayed to buyer after authorizing transaction
		paras= paras + "&CANCELURL=" + PAYPAL_CANCELURL + "/paypal/cancel/" + productCartID;	//# URL displayed to buyer after canceling transaction
		paras= paras + "&VERSION=" + PAYPAL_VERSION;			//# API version

		float totalPayAmt=0;
		
		for (String mechant : mechants ) {
			
			String paypalAccount = getPayPalAccount( mechant );
			if (paypalAccount.equalsIgnoreCase("")) return "-1";
			
			String sIndex = Integer.toString(nIndex);
			List<Product> products=getProductsByMerchantsInCart(productCartID,mechant);
			float taxRatio = 0;
									
			float PAYMENTREQUEST_ITEMAMT = getAmtByMerchantsInCart(productCartID,mechant);
			float PAYMENTREQUEST_TAXAMT = PAYMENTREQUEST_ITEMAMT * taxRatio;
			float PAYMENTREQUEST_INSURANCEAMT=PaypalUtil.getShippingAmt(country,products);
			float PAYMENTREQUEST_SHIPDISCAMT=0;
			float PAYMENTREQUEST_AMT=PAYMENTREQUEST_ITEMAMT + PAYMENTREQUEST_TAXAMT + PAYMENTREQUEST_INSURANCEAMT - PAYMENTREQUEST_SHIPDISCAMT;
			totalPayAmt += PAYMENTREQUEST_AMT;
			
			String PAYMENTREQUEST_SELLERPAYPALACCOUNTID=paypalAccount;
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
    
	  /**
	   * Move a shopping cart to BuyerHistory table after a succesful payment transaction.
	   * 
	   * @param productCartID the Shopping cart ID
	   * @param paras the parameters that we sent to paypal
	   * @param nvp the contents the got from PAYPAL's return during the first call
	   * @return TRUE if record added succesfully, otherwise FALSE
	   */    
    public boolean addBuyerHistory(String productCartID, String paras, HashMap<String, String> nvp ){
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
	    		buyerHistory.setPaymentKey(nvp.get("TOKEN").toString());
	    		buyerHistoryDao.save(buyerHistory);    
	    		ret=true;
	    	}
    	}catch(Exception e){
    		log.error(e.getMessage());
    	}
    	return ret;
    }
    
	  /**
	   * Add transaction record for paypal payment when got a token from paypal. Need to keep the transaction info
	   * 
	   * @param productCartID the Shopping cart ID
	   * @param paras the parameters that we sent to paypal
	   * @param nvp the contents the got from PAYPAL's return during the first call
	   * @return TRUE if record added succesfully, otherwise FALSE
	   */     
    public boolean addTransactionHistory(String productCartID, String paras, HashMap<String, String> nvp ){
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
		    	transactionHistory.setPaymentKey(nvp.get("TOKEN").toString());
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
  
	  /**
	   * Add transaction record. No matter the transaction is succesfull or failed.
	   * 
	   * @param productCartID the Shopping cart ID
	   * @param request the HttpServletRequest. Need to keep all return contents from Stripe/Paypal
	   * @param response the HttpServletResponse. For future function extension
	   * @return TRUE if record added succesfully, otherwise FALSE
	   */    
    public boolean addTransactionHistoryPostPay(String productCartID, HttpServletRequest request,  HttpServletResponse response ){
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
	    	transactionHistory.setPaymentKey("");	    	
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
 
	  /**
	   * Add transaction record. No matter the transaction is succesfull or failed.
	   * 
	   * @param productCartID the Shopping cart ID
	   * @param paymentKey the Token that got from Paypal/Stripe
	   * @param owner the Seller
	   * @param request the HttpServletRequest. Need to keep all return contents from Stripe/Paypal
	   * @param response the HttpServletResponse. For future function extension
	   * @return TRUE if record added succesfully, otherwise FALSE
	   */    
  public boolean addTransactionHistoryPostPayForStripe(String productCartID, String paymentKey, String owner, HttpServletRequest request,  HttpServletResponse response ){
  	boolean ret=false;
  	List<String> mechants = getMerchantsInCart(productCartID);
  	List<ShoppingCart> shoppingCarts = shoppingCartDao.findByProductCartID(productCartID);
  	
  	try{
  			float totalAmt=getAmtByMerchantsInCart(productCartID,owner);
  			TransactionHistory transactionHistory = new TransactionHistory();
	    	transactionHistory.setProductCartID(productCartID);
	    	transactionHistory.setTransactionID("?");
	    	transactionHistory.setBuyer(shoppingCarts.get(0).getBuyerEmail());
	    	transactionHistory.setTotalAmt(totalAmt);
	    	transactionHistory.setSeller(owner);
	    	transactionHistory.setPayMethod("Stripe");
	    	transactionHistory.setPaymentKey(paymentKey);	    	
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

  /**
   * Move a shopping cart to BuyerHistory table after a succesful payment transaction.
   * 
   * @param productCartID the Shopping cart ID
   * @param paras the parameters that we sent to paypal
   * @param nvp the contents the got from PAYPAL's return during the first call
   * @return TRUE if record added succesfully, otherwise FALSE
   */    
public boolean addBuyerHistoryForStripe(String productCartID, String processState, String transactionID, String paymentToken){
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
    		buyerHistory.setProcessState(processState);
    		buyerHistory.setTransactionID(transactionID);
    		buyerHistory.setPayMethod("Stripe");
    		buyerHistory.setPaymentKey(paymentToken);
    		buyerHistoryDao.save(buyerHistory);       		
    	}
    	
    	for (ShoppingCart shoppingCart : shoppingCarts) {
    		shoppingCartDao.delete(shoppingCart);
    	} 
	
    	ret=true;
	}catch(Exception e){
		log.error(e.getMessage());
	}
	return ret;
}

   
   public String getPayPalAccount( String productID)
   {
	   Product product = productDao.findByProductID(productID);	  	   
	   if ( product != null ) {		   
		   String owner = product.getOwner();		   
		   if ( owner != null ) {
			   User user = userDao.findByEmail(owner);
			   if ( user != null ) {
				   return user.getPaypalAccount();
			   }
		   }
	   }
	   
	   return "";
   }	   
 
   
   public boolean sendOrderEmail(String productCartID ) {
	   System.out.println("sendOrderEmail done");
	   return false;	   
   }
   
}
