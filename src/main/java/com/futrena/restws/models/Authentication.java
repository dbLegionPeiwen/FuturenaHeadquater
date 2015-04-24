package com.futrena.restws.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Julien Lu
 */

@Entity
public class Authentication
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String email;
    private String accessToken;
    private Date updateOn;
    private int expireIn;
    public long getId()
    {
        return id;
    }
    public String getEmail()
    {
        return email;
    }
    public String getAccessToken()
    {
        return accessToken;
    }
    public Date getUpdateOn()
    {
        return updateOn;
    }
    public int getExpireIn()
    {
        return expireIn;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }
    public void setUpdateOn(Date updateOn)
    {
        this.updateOn = updateOn;
    }
    public void setExpireIn(int expireIn)
    {
        this.expireIn = expireIn;
    }
    
    
}
