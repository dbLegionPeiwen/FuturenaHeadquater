package com.futrena.restws.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @author Julien Lu
 */

public class UserResponse
{
    @JsonSerialize
    @JsonProperty("email")
    private String email;

    @JsonSerialize
    @JsonProperty("user_id")
    private String userId;
    
    @JsonSerialize
    @JsonProperty("create_on")
    private Date createOn;
    
    @JsonSerialize
    @JsonProperty("first")
    private String first;
    
    @JsonSerialize
    @JsonProperty("last")
    private String last;
    
    @JsonSerialize
    @JsonProperty("phone")
    private String phone;
    
    @JsonSerialize
    @JsonProperty("address")
    private String address;
    
    @JsonSerialize
    @JsonProperty("city")
    private String city;
    
    @JsonSerialize
    @JsonProperty("province")
    private String province;
    
    @JsonSerialize
    @JsonProperty("country")
    private String country;
    
    @JsonSerialize
    @JsonProperty("postal")
    private String postal;
    
    @JsonSerialize
    @JsonProperty("last_login_on")
    private Date lastLoginOn;

    public String getEmail()
    {
        return email;
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

    public void setEmail(String email)
    {
        this.email = email;
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

    public String getUserId()
    {
        return userId;
    }

    public Date getCreateOn()
    {
        return createOn;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setCreateOn(Date createOn)
    {
        this.createOn = createOn;
    }

    public Date getLastLoginOn()
    {
        return lastLoginOn;
    }

    public void setLastLoginOn(Date lastLoginOn)
    {
        this.lastLoginOn = lastLoginOn;
    }
    

}
