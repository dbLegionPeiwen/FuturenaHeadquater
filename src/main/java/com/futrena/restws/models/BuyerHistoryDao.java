package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

import java.util.List;

/**
 * Created by york on 15-04-28
 */

@Transactional
public interface BuyerHistoryDao  extends CrudRepository<BuyerHistory, Long> {
    List<BuyerHistory> findByProductCartID(String productCartID);
    List<BuyerHistory> findByProductID(String productID);    
    List<TransactionHistory> findByBuyerEmail(String buyerEmail);
    //List<TransactionHistory> findByPaypalToken(String paypalToken);
    List<TransactionHistory> findByTransactionID(String transactionID);
    List<TransactionHistory> findByPaypalAccount(String paypalAccount);
}
