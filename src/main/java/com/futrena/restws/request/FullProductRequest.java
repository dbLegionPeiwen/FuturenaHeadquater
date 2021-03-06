package com.futrena.restws.request;

import java.util.Date;

/**
 * Created by haoqiao on 15-04-14.
 */
public class FullProductRequest {

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
    private String Owner;
    private String Designer;
    private String Regular;
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

    public void setMaterial(String material) {
        Material = material;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getDesigner() {
        return Designer;
    }

    public void setDesigner(String designer) {
        Designer = designer;
    }

    public String getRegular() {
        return Regular;
    }

    public void setRegular(String regular) {
        Regular = regular;
    }

    public long getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(long availableAmount) {
        this.availableAmount = availableAmount;
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
