package com.futrena.restws.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by haoqiao on 15-04-14.
 */
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String productID;
    private String productName;
    private String description;
    private String productType;
    private float price;
    private String videoURL;
    private float length;
    private float height;
    private float width;
    private String Material;
    private String productState;
    private Date createDate;
    private String owner;
    private String designer;
    private String regular;
    private long availableAmount;
    private float deliveryFeeUS;
    private float deliveryFeeCA;
    private float deliveryFeeCH;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String productMaterial) {
        this.Material = productMaterial;
    }

    public String getState() {
        return productState;
    }

    public void setState(String productState) {
        this.productState = productState;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public long getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(long availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public float getDeliveryFeeUS() {
        return deliveryFeeUS;
    }

    public void setDeliveryFeeUS(float deliveryFeeUS) {
        this.deliveryFeeUS = deliveryFeeUS;
    }

    public float getDeliveryFeeCA() {
        return deliveryFeeCA;
    }

    public void setDeliveryFeeCA(float deliveryFeeCA) {
        this.deliveryFeeCA = deliveryFeeCA;
    }

    public float getDeliveryFeeCH() {
        return deliveryFeeCH;
    }

    public void setDeliveryFeeCH(float deliveryFeeCH) {
        this.deliveryFeeCH = deliveryFeeCH;
    }
 
}
