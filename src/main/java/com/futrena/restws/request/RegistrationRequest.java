package com.futrena.restws.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Julien Lu
 */

public class RegistrationRequest
{
    @NotBlank(message = "email is required")
    @JsonProperty("email")
    private String email;
    
    @NotBlank(message = "password is required")
    @JsonProperty("password")
    private String password;
    
//    @NotBlank(message = "first name is required")
    @JsonProperty("first")
    private String first;
    
//    @NotBlank(message = "last name is required")
    @JsonProperty("last")
    private String last;
    
//    @NotBlank(message = "phone number is required")
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("province")
    private String province;
    
    @JsonProperty("country")
    private String country;
    
    @JsonProperty("postal")
    private String postal;

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
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

    public void setPassword(String password)
    {
        this.password = password;
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
}
