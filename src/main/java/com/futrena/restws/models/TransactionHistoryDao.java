package com.futrena.restws.models;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

import java.util.List;

@Transactional
public interface TransactionHistoryDao  extends CrudRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByProductCartID(String productCartID);
    List<TransactionHistory> findByTransactionID(String transactionID);
    List<TransactionHistory> findByPaypalAccount(String paypalAccount);
    //List<TransactionHistory> findByPaypalToken(String paypalToken);
    List<TransactionHistory> findBySeller(String seller);
    List<TransactionHistory> findByBuyer(String buyer);
}
