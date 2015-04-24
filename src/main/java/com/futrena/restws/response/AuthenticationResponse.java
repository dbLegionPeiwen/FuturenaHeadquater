package com.futrena.restws.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @author Julien Lu
 */

public class AuthenticationResponse
{
    @JsonSerialize
    @JsonProperty("email")
    private String email;
    
    @JsonSerialize
    @JsonProperty("user_id")
    private String userId;
    
    @JsonSerialize
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonSerialize
    @JsonProperty("create_on")
    private Date createOn;
    
    @JsonSerialize
    @JsonProperty("expire_in")
    private int expireIn;

    public String getEmail()
    {
        return email;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public Date getCreateOn()
    {
        return createOn;
    }

    public int getExpireIn()
    {
        return expireIn;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public void setCreateOn(Date createOn)
    {
        this.createOn = createOn;
    }

    public void setExpireIn(int expireIn)
    {
        this.expireIn = expireIn;
    }
}
