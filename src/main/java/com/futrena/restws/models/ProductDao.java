package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by haoqiao on 15-04-14.
 */

@Transactional
public interface ProductDao extends CrudRepository<Product, Long> {

    Product findByProductID(String productID);
    List<Product> findByProductName(String productName);
    List<Product> findAllByProductType(String productType);
    List<Product> findAllByCreateDate(Date createDate);
    List<Product> findAllByProductState(String productState);

}

