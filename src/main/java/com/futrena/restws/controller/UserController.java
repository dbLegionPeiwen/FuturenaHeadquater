package com.futrena.restws.controller;


import com.futrena.restws.models.Authentication;
import com.futrena.restws.models.AuthenticationDao;
import com.futrena.restws.models.User;
import com.futrena.restws.models.UserDao;
import com.futrena.restws.request.PasswordResetEmailRequest;
import com.futrena.restws.request.PasswordResetRequest;
import com.futrena.restws.request.RegistrationRequest;
import com.futrena.restws.response.ResponseMessage;
import com.futrena.restws.response.UserResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Julien Lu
 */

@RestController
public class UserController 
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationDao authenticationDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Autowired
    UserController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @RequestMapping("/user/{id}")
    @Secured("API")
    public ResponseEntity<Object> getUserDetails(@RequestHeader("Authentication-Header") String authenticationToken,
            @PathVariable String id){        
        HashMap<String,Object> authenticationResponse = validateAuthenticationToken(authenticationToken, id);
        if (authenticationResponse.get("errorResponse") != null)
        {
            return new ResponseEntity<Object>(authenticationResponse.get("errorResponse"), HttpStatus.OK);
        }
        else
        {
            User user = (User) authenticationResponse.get("successResponse");
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(user.getUserId());
            userResponse.setEmail(user.getEmail());
            userResponse.setFirst(user.getFirst());
            userResponse.setLast(user.getLast());
            userResponse.setPhone(user.getPhone());
            userResponse.setCreateOn(user.getCreateOn());
            userResponse.setLastLoginOn(user.getLastLoginOn());
            userResponse.setAddress(user.getAddress());
            userResponse.setCity(user.getCity());
            userResponse.setProvince(user.getProvince());
            userResponse.setCountry(user.getCountry());
            userResponse.setPostal(user.getPostal());
            return new ResponseEntity<Object>(userResponse, HttpStatus.OK);
        }
    }

    @RequestMapping("/user/register")
    @Secured("API")
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody RegistrationRequest registrationRequest,
            BindingResult bindingResult){

        if (bindingResult.hasErrors())
        {
            ResponseMessage responseMessage = new ResponseMessage("400","The guests request is INVALID");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        if(userDao.findByEmail(registrationRequest.getEmail()) != null)
        {
            ResponseMessage responseMessage = new ResponseMessage("500","Email already registered.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }
        try {
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setFirst(registrationRequest.getFirst());
            user.setLast(registrationRequest.getLast());
            user.setPhone(registrationRequest.getPhone());
            user.setCreateOn(new Date());
            user.setAddress(registrationRequest.getAddress());
            user.setCity(registrationRequest.getCity());
            user.setProvince(registrationRequest.getProvince());
            user.setCountry(registrationRequest.getCountry());
            user.setPostal(registrationRequest.getPostal());
            userDao.save(user);

            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(user.getUserId());
            userResponse.setEmail(user.getEmail());
            userResponse.setFirst(user.getFirst());
            userResponse.setLast(user.getLast());
            userResponse.setPhone(user.getPhone());
            userResponse.setCreateOn(user.getCreateOn());
            userResponse.setLastLoginOn(user.getLastLoginOn());
            userResponse.setAddress(user.getAddress());
            userResponse.setCity(registrationRequest.getCity());
            userResponse.setProvince(registrationRequest.getProvince());
            userResponse.setCountry(registrationRequest.getCountry());
            userResponse.setPostal(registrationRequest.getPostal());
            return new ResponseEntity<Object>(userResponse, HttpStatus.OK);
        }
        catch (Exception ex) {
            ResponseMessage responseMessage = new ResponseMessage("500","There was an internal server error.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }
    }

    @RequestMapping("/user/pwreset/email")
    @Secured("API")
    public ResponseEntity<Object> postUserResetPasswordEmail(@Valid @RequestBody PasswordResetEmailRequest passwordResetEmailRequest) throws MessagingException{
        User user = userDao.findByEmail(passwordResetEmailRequest.getEmail());
        if (user == null)
        {
            ResponseMessage responseMessage = new ResponseMessage("500","Email not found.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        String passwordRestToken = Base64.encodeBase64String((user.getEmail()+":"+new Date().getTime()).getBytes());
        user.setResetToken(passwordRestToken);
        userDao.save(user);
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent("Hi there,<br>Some one has made an password reset request for your account. If you do wish to reset your password, please click the link below:<br><a href=\"http://localhost/user/pwreset/"+passwordRestToken+"\">"
                + "http://localhost/user/pwreset/" + passwordRestToken+"</a>", "text/html");
        helper.setTo(user.getEmail());
        helper.setSubject("Password Reset");
        helper.setFrom("dblitest@gmail.com");
        javaMailSender.send(mimeMessage);

        ResponseMessage successResponse = new ResponseMessage("200","Password reset email sent.");
        return new ResponseEntity<Object>(successResponse, HttpStatus.OK);   
    }

    @RequestMapping("/user/pwreset")
    @Secured("API")
    public ResponseEntity<Object> postUserResetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) throws MessagingException, UnsupportedEncodingException{
        User user = null;
        String email = null;
        if (passwordResetRequest.getResetToken() != null)
        {
            List<String> decodedToken = decodeToken(passwordResetRequest.getResetToken());
//            byte[] decoded = Base64.decodeBase64(passwordResetRequest.getResetToken());
//            email = new String(decoded, "UTF-8");
            user = userDao.findByEmail(decodedToken.get(0));
            if (user == null)
            {
                ResponseMessage responseMessage = new ResponseMessage("500","Email not found.");
                return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
            }

            if (!passwordResetRequest.getResetToken().equals(user.getResetToken()))
            {
                ResponseMessage responseMessage = new ResponseMessage("500","Invalid token.");
                return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
            }
        }
        else
        {
            email = passwordResetRequest.getEmail();
            user = userDao.findByEmail(email);
            if (user == null)
            {
                ResponseMessage successResponse = new ResponseMessage("500","Email not found.");
                return new ResponseEntity<Object>(successResponse, HttpStatus.OK);  
            }
            
            if(!passwordEncoder.matches(passwordResetRequest.getOldPassword(), user.getPassword()))
            {
                ResponseMessage responseMessage = new ResponseMessage("401","Password incorrect.");
                return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
            }
        }
        

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        user.setResetToken(null);
        userDao.save(user);
        


        ResponseMessage successResponse = new ResponseMessage("200","Password reset successfully.");
        return new ResponseEntity<Object>(successResponse, HttpStatus.OK);   
    }
    
    public static List<String> decodeToken(String token) throws UnsupportedEncodingException
    {
        byte[] decoded = Base64.decodeBase64(token);
        String inputdecodedAccessToken = new String(decoded, "UTF-8");
        List<String> decodedAccessToken = new ArrayList<String>();
        //email
        decodedAccessToken.add(inputdecodedAccessToken.split(":")[0]);
        //raw access token
        decodedAccessToken.add(inputdecodedAccessToken.split(":")[1]);
        return decodedAccessToken;
    }

    public HashMap<String,Object> validateAuthenticationToken(String authenticationToken, String id)
    {
        HashMap<String,Object> authenticationResult = new HashMap<String,Object>();
        if(authenticationToken == null)
        {
            ResponseMessage responseMessage = new ResponseMessage("401","The authentication header is INVALID");
            authenticationResult.put("responseMessage", responseMessage);
            return authenticationResult;
        }

        try
        {
            List<String> decodedAccessToken = decodeToken(authenticationToken);
            User user = userDao.findByEmail(decodedAccessToken.get(0));
            if (user == null || !user.getUserId().equals(id))
            {
                ResponseMessage responseMessage = new ResponseMessage("401","Authentication token is invalid.");
                authenticationResult.put("responseMessage", responseMessage);
                return authenticationResult;
            }            
            Authentication authentication = authenticationDao.findByEmail(decodedAccessToken.get(0));

            if (authentication == null || !authentication.getAccessToken().equals(decodedAccessToken.get(1)))
            {
                ResponseMessage responseMessage = new ResponseMessage("401","Access token is invalid.");
                authenticationResult.put("responseMessage", responseMessage);
                return authenticationResult;
            }

            long timeDiff = new Date().getTime() - authentication.getUpdateOn().getTime();
            //Compare time diff in milliseconds
            if(timeDiff>authentication.getExpireIn()*1000)
            {
                ResponseMessage responseMessage = new ResponseMessage("401","Authentication token has expired.");
                authenticationResult.put("responseMessage", responseMessage);
                return authenticationResult;
            }

            authenticationResult.put("successResponse",user);
            return authenticationResult;
        }
        catch (Exception e)
        {
            ResponseMessage responseMessage = new ResponseMessage("500","There was an internal server error.");
            authenticationResult.put("responseMessage", responseMessage);
            return authenticationResult;
        }       
    }

}
