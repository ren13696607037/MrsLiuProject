package com.techfly.liutaitai.model.mall.bean;

public class OrderEva {

	private int mProductId;
	private int mStar;
	private String mContent;
	private String mProductName;
	private boolean isShould;

	public int getmProductId() {
		return mProductId;
	}

	public void setmProductId(int mProductId) {
		this.mProductId = mProductId;
	}

	public int getmStar() {
		return mStar;
	}

	public void setmStar(int mStar) {
		this.mStar = mStar;
	}

	public String getmContent() {
		return mContent;
	}

	public void setmContent(String mContent) {
		this.mContent = mContent;
	}

	public String getmProductName() {
		return mProductName;
	}

	public void setmProductName(String mProductName) {
		this.mProductName = mProductName;
	}

	public boolean isShould() {
		return isShould;
	}

	public void setShould(boolean isShould) {
		this.isShould = isShould;
	}

}
