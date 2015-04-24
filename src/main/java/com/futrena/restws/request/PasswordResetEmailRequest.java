package com.futrena.restws.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Julien Lu
 */

public class PasswordResetEmailRequest
{
    @NotBlank(message = "email is required")
    @JsonProperty("email")
    private String email;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
