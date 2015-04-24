package com.futrena.restws.controller;

import com.futrena.restws.models.Product;
import com.futrena.restws.models.ProductDao;
import com.futrena.restws.models.ProductRate;
import com.futrena.restws.models.ProductRateDao;
import com.futrena.restws.models.UserDao;
import com.futrena.restws.request.FullProductRequest;
import com.futrena.restws.request.ProductID;
import com.futrena.restws.request.ProductRequest;
import com.futrena.restws.response.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by haoqiao on 15-04-14.
 */

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductRateDao productRateDao;

    @RequestMapping("/product/get/{productName}")
    @Secured("API")
    public List<Product> GetAllProductByName(@PathVariable String productName){
        List<Product> p;
        p = productDao.findByProductName(productName);
        return p;
    }

    @RequestMapping("/product/{id}")
    @Secured("API")
    public Product GetProductById(@PathVariable String id){
        Product p;
        p = productDao.findByProductID(id);
        return p;
    }

    @RequestMapping("/product/getByType")
    @Secured("API")
    public List<Product> GetALLProductByType(@RequestBody String productType){
        List<Product> p = productDao.findAllByProductType(productType);
        return p;
    }

    @RequestMapping("/product/getByState/{state}")
    @Secured("API")
    public List<Product> GetALLProductByState(@PathVariable String state){
        List<Product> p = productDao.findAllByProductState(state);
        return p;
    }


    @RequestMapping("/product/deleteById")
    @Secured("API")
    public ResponseEntity<Object> DeleteProductById(@RequestBody ProductID productId){
        if (productDao.findByProductID(productId.getId())!=null) {
            Product pro = productDao.findByProductID(productId.getId());
            productDao.delete(pro);
            ResponseMessage responseMessage = new ResponseMessage("200", "Success delete:  "+pro.getProductName());
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }
        else {
            ResponseMessage responseMessage = new ResponseMessage("404", "Product not found");
            return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
        }

    }


    @RequestMapping("/product/add")
    @Secured("API")
    public ResponseEntity<Object> AddProduct(@RequestBody ProductRequest product){
        Product p = new Product();
        try {
                p.setProductID(UUID.randomUUID().toString());
                p.setProductName(product.getProductName());
                p.setDescription(product.getDescription());
                p.setHeight(product.getHeight());
                p.setWidth(product.getWidth());
                p.setLength(product.getLength());
                p.setCreateDate(product.getCreateDate());
                p.setMaterial(product.getProductMaterial());
                p.setProductType(product.getProductType());
                p.setVideoURL(product.getVideoURL());
                p.setPrice(product.getPrice());
                p.setState("pending");
                productDao.save(p);

                ProductRate _rate = new ProductRate();
                _rate.setProductID(p.getProductID());
                _rate.setProductName(p.getProductName());
                _rate.setDesignerCAD(0);
                _rate.setDesignerCNY(0);
                _rate.setDesignerUSD(0);
                _rate.setOwnerCAD(0);
                _rate.setOwnerCNY(0);
                _rate.setOwnerUSD(0);
                _rate.setRegularCAD(0);
                _rate.setRegularCNY(0);
                _rate.setRegularUSD(0);
                productRateDao.save(_rate);

            }

        catch (Exception ex) {
                return new ResponseEntity<Object>(ex, HttpStatus.OK);
            }

        return new ResponseEntity<Object>(p, HttpStatus.OK);
    }

    @RequestMapping("/product/update/{id}")
    @Secured("API")
    public ResponseEntity<Object> UpdateProduct(@RequestBody ProductRequest product, @PathVariable String id){
        Product p = productDao.findByProductID(id);
        try {

                p.setProductName(product.getProductName());
                p.setDescription(product.getDescription());
                p.setHeight(product.getHeight());
                p.setWidth(product.getWidth());
                p.setLength(product.getLength());
                p.setCreateDate(product.getCreateDate());
                p.setMaterial(product.getProductMaterial());
                p.setProductType(product.getProductType());
                p.setVideoURL(product.getVideoURL());
                p.setState("pending");
                productDao.save(p);
            }

        catch (Exception ex) {
                return new ResponseEntity<Object>(ex, HttpStatus.OK);
            }

        return new ResponseEntity<Object>(p, HttpStatus.OK);
    }

    @RequestMapping("/product/fullyUpdate/{id}")
    @Secured("API")
    public ResponseEntity<Object> UpdateFullProduct(@RequestBody FullProductRequest product, @PathVariable String id){
        Product p = productDao.findByProductID(id);
        try {

            p.setProductName(product.getProductName());
            p.setDescription(product.getDescription());
            p.setHeight(product.getHeight());
            p.setWidth(product.getWidth());
            p.setLength(product.getLength());
            p.setCreateDate(product.getCreateDate());
            p.setMaterial(product.getMaterial());
            p.setProductType(product.getProductType());
            p.setVideoURL(product.getVideoURL());
            p.setState(product.getProductState());
            p.setDesigner(product.getDesigner());
            p.setOwner(product.getOwner());
            p.setRegular(product.getRegular());
            p.setAvailableAmount(product.getAvailableAmount());
            p.setVideoURL(product.getVideoURL());
            p.setPrice(product.getPrice());
            p.setDeliveryFeeCA(product.getDeliveryFeeCA());
            p.setDeliveryFeeUS(product.getDeliveryFeeUS());
            p.setDeliveryFeeCH(product.getDeliveryFeeCH());

            productDao.save(p);
        }

        catch (Exception ex) {
            return new ResponseEntity<Object>(ex, HttpStatus.OK);
        }

        return new ResponseEntity<Object>(p, HttpStatus.OK);
    }
}
