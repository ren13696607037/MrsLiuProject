package com.hylsmart.yihui.model.mall.bean;

import java.io.Serializable;

public class SubStandarrd implements Serializable {

	private static final long serialVersionUID = 5L;
	private int mId;
	private int mStock;

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public int getmStock() {
		return mStock;
	}

	public void setmStock(int mStock) {
		this.mStock = mStock;
	}

}
