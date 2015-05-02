package com.futrena.restws.request;

import java.util.ArrayList;

public class ImageBuffer {
	
	private ArrayList<String> buffer;
	private String productID;

	public ArrayList<String> getBuffer() {
		return buffer;
	}

	public void setBuffer(ArrayList<String> buffer) {
		this.buffer = buffer;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductName(String productID) {
		this.productID = productID;
	}

}
