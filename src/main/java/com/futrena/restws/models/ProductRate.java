package com.futrena.restws.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by haoqiao on 15-04-15.
 */

@Entity
public class ProductRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String productID;
    private String productName;
    private float ownerCNY;
    private float ownerUSD;
    private float ownerCAD;
    private float designerCNY;
    private float designerUSD;
    private float designerCAD;
    private float regularCNY;
    private float regularUSD;
    private float regularCAD;

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

    public float getOwnerCNY() {
        return ownerCNY;
    }

    public void setOwnerCNY(float ownerCNY) {
        this.ownerCNY = ownerCNY;
    }

    public float getOwnerUSD() {
        return ownerUSD;
    }

    public void setOwnerUSD(float ownerUSD) {
        this.ownerUSD = ownerUSD;
    }

    public float getOwnerCAD() {
        return ownerCAD;
    }

    public void setOwnerCAD(float ownerCAD) {
        this.ownerCAD = ownerCAD;
    }

    public float getDesignerCNY() {
        return designerCNY;
    }

    public void setDesignerCNY(float designerCNY) {
        this.designerCNY = designerCNY;
    }

    public float getDesignerUSD() {
        return designerUSD;
    }

    public void setDesignerUSD(float designerUSD) {
        this.designerUSD = designerUSD;
    }

    public float getDesignerCAD() {
        return designerCAD;
    }

    public void setDesignerCAD(float designerCAD) {
        this.designerCAD = designerCAD;
    }

    public float getRegularCNY() {
        return regularCNY;
    }

    public void setRegularCNY(float regularCNY) {
        this.regularCNY = regularCNY;
    }

    public float getRegularUSD() {
        return regularUSD;
    }

    public void setRegularUSD(float regularUSD) {
        this.regularUSD = regularUSD;
    }

    public float getRegularCAD() {
        return regularCAD;
    }

    public void setRegularCAD(float regularCAD) {
        this.regularCAD = regularCAD;
    }
}
