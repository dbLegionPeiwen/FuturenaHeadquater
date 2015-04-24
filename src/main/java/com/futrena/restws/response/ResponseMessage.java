package com.futrena.restws.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Julien Lu
 */

public class ResponseMessage
{
    @JsonSerialize
    @JsonProperty("status")
    private String status;
    
    @JsonSerialize
    @JsonProperty("message")
    private String message;
    

    public ResponseMessage(String status, String message)
    {
        super();
        this.status = status;
        this.message = message;
    }

    public String getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
    
}
