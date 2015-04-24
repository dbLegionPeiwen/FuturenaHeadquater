
package com.futrena.restws.models;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author haoqiao
 *
 */

@Entity
public class UserFavourates {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String email;
	private String productName;
	private String productID;
	private Date viewedAt;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public Date getViewedAt() {
		return viewedAt;
	}
	public void setViewedAt(Date viewedAt) {
		this.viewedAt = viewedAt;
	}
	

}
