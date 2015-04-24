package com.futrena.restws.controller;

import com.futrena.restws.models.ShoppingCart;
import com.futrena.restws.models.ShoppingCartDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

import java.util.List;
import java.util.UUID;

/**
 * Created by haoqiao on 15-04-22.
 */

@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartDao shoppingCartDao;


    @RequestMapping("/shoppingCart/{userEmail}/")
    @Secured("API")
    public ResponseEntity<Object> getUserCartItems(@PathVariable String userEmail){
        List<ShoppingCart> cart;
        try {
            cart = shoppingCartDao.findByBuyerEmail(userEmail);
        }
        catch (Exception ex){
          return new ResponseEntity<Object>(ex,HttpStatus.OK);
        }
        return new ResponseEntity<Object>(cart, HttpStatus.OK);
    }

    @RequestMapping("/shoppingCart/{userEmail}/{productID}/addToCart")
    @Secured("API")
    public ResponseEntity<Object> AddToCart(@RequestBody ShoppingCart cart, @PathVariable String userEmail, @PathVariable String productID){

        try{
            cart.setProductCartID(UUID.randomUUID().toString());
            cart.setProductID(productID);
            cart.setBuyerEmail(userEmail);

            shoppingCartDao.save(cart);
        }
        catch (Exception ex){
            return new ResponseEntity(ex, HttpStatus.OK);
        }

        return new ResponseEntity<Object>(cart, HttpStatus.OK);
    }

}
