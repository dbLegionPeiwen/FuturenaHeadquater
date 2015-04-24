package com.futrena.restws.controller;

import com.futrena.restws.models.ProductRate;
import com.futrena.restws.models.ProductRateDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by haoqiao on 15-04-22.
 */

@RestController
public class ProductRateController {

    @Autowired
    private ProductRateDao productRateDao;

    @RequestMapping("/product/{productID}/getRate")
    @Secured("API")
    public ResponseEntity<Object> GetRate (@PathVariable String productID){

        ProductRate rate = new ProductRate();

        try{
            rate = productRateDao.findByProductID(productID);
        }
        catch (Exception ex){
            return new ResponseEntity<Object>(ex, HttpStatus.OK);
        }

        return new ResponseEntity<Object>(rate, HttpStatus.OK);
    }

    @RequestMapping("/product/{productID}/updateRate")
    @Secured("API")
    public ResponseEntity<Object> UpdateRate(@RequestBody ProductRate productRate, @PathVariable String productID){

        ProductRate rate = new ProductRate();
        try{

            rate = productRateDao.findByProductID(productID);

            rate.setDesignerCAD(productRate.getDesignerCAD());
            rate.setDesignerUSD(productRate.getDesignerUSD());
            rate.setDesignerCNY(productRate.getDesignerCNY());
            rate.setOwnerCAD(productRate.getOwnerCAD());
            rate.setOwnerUSD(productRate.getOwnerUSD());
            rate.setOwnerCNY(productRate.getOwnerCNY());
            rate.setRegularCNY(productRate.getRegularCNY());
            rate.setRegularUSD(productRate.getRegularUSD());
            rate.setRegularCAD(productRate.getRegularCAD());

            productRateDao.save(rate);

        }
        catch (Exception ex){
            return new ResponseEntity<Object>(ex, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(rate, HttpStatus.OK);
    }
}
