package com.futrena.restws.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Julien Lu
 */

public class PasswordResetRequest
{
    @JsonProperty("reset_token")
    private String resetToken;
    
    @JsonProperty("email")
    private String email;

    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank(message = "new password is required")
    @JsonProperty("new_password")
    private String newPassword;

    public String getResetToken()
    {
        return resetToken;
    }

    public String getOldPassword()
    {
        return oldPassword;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setResetToken(String resetToken)
    {
        this.resetToken = resetToken;
    }

    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
