package com.futrena.restws.controller;

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

import com.futrena.restws.services.*;

/**
 * Created by York on 15-04-28.
 */

@RestController
@PropertySource("classpath:/application.properties")
public class PaypalController {
	
	private static final Logger log = Logger.getLogger(PaypalController.class);


	@Value("${paypal.host}")
	private String PAYPAL_HOST;

	@Value("${paypal.url}")
	private String PAYPAL_URL;
	
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
	
	@Autowired
	private PaymentServices paymentServices;
	
	@RequestMapping(value="/checkout/paypal/{productCartID}", method=RequestMethod.GET)
	public ResponseEntity<Object> SetExpressCheckout(HttpServletRequest request, @PathVariable String productCartID){
		
		ResponseMessage responseMessage;
		
		List<ShoppingCart> s = shoppingCartDao.findByProductCartID(productCartID);
		if ( s.size() < 1 ) {
			responseMessage = new ResponseMessage("ERROR","Empty Shopping Cart");
			return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
		}
		
		String paras=paymentServices.CreatePaypalCheckoutParas(request,productCartID);		
		if ( paras.equals("6") ) {
			responseMessage = new ResponseMessage("ERROR", "PAYPAL cann't make the payments that more than 6 Mechants");			
			return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);	
		}
						
 	    UUID unique_id = UUID.randomUUID();
 	    
	    HashMap<String, String> nvp=PaypalUtil.httpsCall(PAYPAL_HOST, PAYPAL_URL, paras, unique_id.toString());
	    log.info("SetExpressCheckout");
	    paymentServices.addTransactionHistory(productCartID, paras, nvp );	    	  
	    paymentServices.addBuyerHistory(productCartID, paras, nvp );

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
			paymentServices.addTransactionHistoryPostPay(productCartID, request,  response );
			
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
	
   
}


