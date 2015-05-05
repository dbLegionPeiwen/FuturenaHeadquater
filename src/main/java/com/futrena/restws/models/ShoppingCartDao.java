package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by haoqiao on 15-04-22.
 */
public interface ShoppingCartDao extends CrudRepository<ShoppingCart, Long>{

    List<ShoppingCart> findByBuyerEmail(String buyerEmail);
    List<ShoppingCart> findByProductCartID(String productCartID);

}
