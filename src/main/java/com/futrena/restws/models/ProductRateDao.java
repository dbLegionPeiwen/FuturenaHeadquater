package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;


/**
 * Created by haoqiao on 15-04-15.
 */

@Transactional
public interface ProductRateDao extends CrudRepository<ProductRate, Long> {

    ProductRate findByProductID(String productID);

}

