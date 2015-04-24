package com.futrena.restws.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Julien Lu
 */

public class AuthenticationRequest
{
    @NotBlank(message = "grant_type is required")
    @JsonProperty("grant_type")
    private String grantType;
    
    @NotBlank(message = "email is required")
    @JsonProperty("email")
    private String email;
    
    @NotBlank(message = "password is required")
    @JsonProperty("password")
    private String password;
    
    @NotBlank(message = "client_id is required")
    @JsonProperty("client_id")
    private String clientId;
    
    @NotBlank(message = "client_secret is required")
    @JsonProperty("client_secret")
    private String clientSecret;

    public String getGrantType()
    {
        return grantType;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public String getClientId()
    {
        return clientId;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }

    public void setGrantType(String grantType)
    {
        this.grantType = grantType;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }
    
    
}
