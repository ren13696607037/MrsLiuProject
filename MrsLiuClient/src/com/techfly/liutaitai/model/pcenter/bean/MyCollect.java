package com.techfly.liutaitai.model.pcenter.bean;

public class MyCollect {
	private String mId;
	private String mImg;
	private String mTitle;
	private String mPrice;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmImg() {
		return mImg;
	}
	public void setmImg(String mImg) {
		this.mImg = mImg;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmPrice() {
		return mPrice;
	}
	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	@Override
	public String toString() {
		return "MyCollect [mId=" + mId + ", mImg=" + mImg + ", mTitle="
				+ mTitle + ", mPrice=" + mPrice + "]";
	}
	
}
