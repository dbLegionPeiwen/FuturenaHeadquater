package com.futrena.restws.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by haoqiao on 15-04-22.
 */

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String productCartID;

    @Column(nullable = false)
    private String productID;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String buyerEmail;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private Boolean processState;

    public String getProductCartID() {
        return productCartID;
    }

    public void setProductCartID(String productCartID) {
        this.productCartID = productCartID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Boolean isProcessState() {
        return processState;
    }

    public void setProcessState(Boolean processState) {
        this.processState = processState;
    }
}
