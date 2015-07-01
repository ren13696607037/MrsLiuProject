package com.hylsmart.yihui.bean;

import java.io.Serializable;

public class GroupBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4507898073161849786L;

	private String id;
	private String name;
	private String price;
	private String marketPrice;
	private String icon;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
