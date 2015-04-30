package com.futrena.restws.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by york on 15-04-27.
 */

@Entity
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	private String productCartID;
	private String transactionID;
	private String buyer;
	private float totalAmt;
	private String seller;
	private String payMethod;
	private String paypalAccount;
	private String paypalToken;
	private String request;
	private String response;
	private String STATUS;
	private Date updated;
	private Date created;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProductCartID() {
		return productCartID;
	}
	public void setProductCartID(String productCartID) {
		this.productCartID = productCartID;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public float getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(float totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getPaypalAccount() {
		return paypalAccount;
	}
	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}
	public String getPaypalToken() {
		return paypalToken;
	}
	public void setPaypalToken(String paypalToken) {
		this.paypalToken = paypalToken;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	
}
