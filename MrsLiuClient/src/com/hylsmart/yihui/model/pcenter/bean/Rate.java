package com.hylsmart.yihui.model.pcenter.bean;

public class Rate {
	private String mId;
	private String mName;
	private String mTime;
	private String mContent;
	private String mScore;
	private String mProductId;
	private String mProductImage;
	private String mProductName;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	public String getmContent() {
		return mContent;
	}
	public void setmContent(String mContent) {
		this.mContent = mContent;
	}
	public String getmScore() {
		return mScore;
	}
	public void setmScore(String mScore) {
		this.mScore = mScore;
	}
	public String getmProductId() {
		return mProductId;
	}
	public void setmProductId(String mProductId) {
		this.mProductId = mProductId;
	}
	public String getmProductImage() {
		return mProductImage;
	}
	public void setmProductImage(String mProductImage) {
		this.mProductImage = mProductImage;
	}
	public String getmProductName() {
		return mProductName;
	}
	public void setmProductName(String mProductName) {
		this.mProductName = mProductName;
	}
	@Override
	public String toString() {
		return "Rate [mId=" + mId + ", mName=" + mName + ", mTime=" + mTime
				+ ", mContent=" + mContent + ", mScore=" + mScore
				+ ", mProductId=" + mProductId + ", mProductImage="
				+ mProductImage + ", mProductName=" + mProductName + "]";
	}
	
}
