package com.futrena.restws.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by haoqiao on 15-04-07.
 */

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userId;
    private String username;
    private String password;
    private String email;
    private String first;
    private String last;
    private String phone;
    private Date createOn;
    private Date lastLoginOn;
    private String address;
    private String city;
    private String province;
    private String country;
    private String postal;
    private String resetToken;
    private String paypalAccount;
    private String stripeAccessToken;
    private String stripeRefreshToken;
    private String stripePublishableKey;
    private String stripeUserId;
        
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId()
    {
        return id;
    }

    public String getFirst()
    {
        return first;
    }

    public String getLast()
    {
        return last;
    }

    public String getPhone()
    {
        return phone;
    }

    public Date getCreateOn()
    {
        return createOn;
    }

    public Date getLastLoginOn()
    {
        return lastLoginOn;
    }

    public String getAddress()
    {
        return address;
    }

    public String getCity()
    {
        return city;
    }

    public String getCountry()
    {
        return country;
    }

    public String getPostal()
    {
        return postal;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setFirst(String first)
    {
        this.first = first;
    }

    public void setLast(String last)
    {
        this.last = last;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setCreateOn(Date createOn)
    {
        this.createOn = createOn;
    }

    public void setLastLoginOn(Date lastLoginOn)
    {
        this.lastLoginOn = lastLoginOn;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setPostal(String postal)
    {
        this.postal = postal;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getResetToken()
    {
        return resetToken;
    }

    public void setResetToken(String resetToken)
    {
        this.resetToken = resetToken;
    }

	public String getPaypalAccount() {
		return paypalAccount;
	}

	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}

	public String getStripeAccessToken() {
		return stripeAccessToken;
	}

	public void setStripeAccessToken(String stripeAccessToken) {
		this.stripeAccessToken = stripeAccessToken;
	}

	public String getStripeRefreshToken() {
		return stripeRefreshToken;
	}

	public void setStripeRefreshToken(String stripeRefreshToken) {
		this.stripeRefreshToken = stripeRefreshToken;
	}

	public String getStripePublishableKey() {
		return stripePublishableKey;
	}

	public void setStripePublishableKey(String stripePublishableKey) {
		this.stripePublishableKey = stripePublishableKey;
	}

	public String getStripeUserId() {
		return stripeUserId;
	}

	public void setStripeUserId(String stripeUserId) {
		this.stripeUserId = stripeUserId;
	}
    
    

}
