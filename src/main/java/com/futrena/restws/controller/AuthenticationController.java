package com.futrena.restws.controller;

import com.futrena.restws.models.Authentication;
import com.futrena.restws.models.AuthenticationDao;
import com.futrena.restws.models.User;
import com.futrena.restws.models.UserDao;
import com.futrena.restws.request.AuthenticationRequest;
import com.futrena.restws.response.AuthenticationResponse;
import com.futrena.restws.response.ResponseMessage;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Date;
import java.util.UUID;

/**
 * @author Julien Lu
 */

@RestController

public class AuthenticationController
{
    private String CLIENT_ID = "DBLI";
    private String CLIENT_SECRET = "2649c5732a1b";
    private Integer EXPIRE_IN = 1800;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationDao authenticationDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/authenticate")
    @Secured("API")
    public ResponseEntity<Object> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest,
            BindingResult bindingResult, HttpSession session){

        if (bindingResult.hasErrors())
        {
            ResponseMessage responseMessage = new ResponseMessage("400","The authentication request is INVALID");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        if(!"password".equals(authenticationRequest.getGrantType()))
        {
            ResponseMessage responseMessage = new ResponseMessage("401","Unknown grant type.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        User usersFromDao = userDao.findByEmail(authenticationRequest.getEmail());
        User user = null;

        if(usersFromDao == null)
        {
            ResponseMessage responseMessage = new ResponseMessage("500","Email not found.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }
        else
        {
            user = usersFromDao;
        }

        if(!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword()))
        {
            ResponseMessage responseMessage = new ResponseMessage("401","Password incorrect.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        if(!CLIENT_ID.equals(authenticationRequest.getClientId()))
        {
            ResponseMessage responseMessage = new ResponseMessage("401","Client ID incorrect.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        if(!CLIENT_SECRET.equals(authenticationRequest.getClientSecret()))
        {
            ResponseMessage responseMessage = new ResponseMessage("401","Client secret incorrect.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

        Authentication authentication = authenticationDao.findByEmail(authenticationRequest.getEmail());
        try {        
            if(authentication == null)
            {
                authentication = new Authentication();
                authentication.setAccessToken(UUID.randomUUID().toString());
                authentication.setEmail(authenticationRequest.getEmail());
                authentication.setUpdateOn(new Date());
                authentication.setExpireIn(EXPIRE_IN);
                authenticationDao.save(authentication);
                
                user.setLastLoginOn(new Date());
                userDao.save(user);
            }
            else
            {
                authentication.setAccessToken(UUID.randomUUID().toString());
                authentication.setEmail(authenticationRequest.getEmail());
                authentication.setUpdateOn(new Date());
                authentication.setExpireIn(EXPIRE_IN);
                authenticationDao.save(authentication);
                
                user.setLastLoginOn(new Date());
                userDao.save(user);
            }
        }
        catch (Exception ex) {
            ResponseMessage responseMessage = new ResponseMessage("500","There was an internal server error.");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setCreateOn(authentication.getUpdateOn());
        authenticationResponse.setExpireIn(authentication.getExpireIn());
        authenticationResponse.setEmail(authentication.getEmail());
        authenticationResponse.setUserId(user.getUserId());
        
        String encodedAccessToken = Base64.encodeBase64String((authentication.getEmail() + ":" + authentication.getAccessToken()).getBytes());
        authenticationResponse.setAccessToken(encodedAccessToken);

        session.setAttribute("email",authenticationRequest.getEmail());

        return new ResponseEntity<Object>(authenticationResponse, HttpStatus.OK);
    }
    
}
