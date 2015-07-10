package com.techfly.liutaitai.model.order.bean;

import java.util.ArrayList;

public class Rate {
	private String mId;
	private String mName;
	private String mRate;//好评（3）中评（2）差评（1）
	private String mContent;
	private ArrayList<String> mImages;
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
	public String getmRate() {
		return mRate;
	}
	public void setmRate(String mRate) {
		this.mRate = mRate;
	}
	public String getmContent() {
		return mContent;
	}
	public void setmContent(String mContent) {
		this.mContent = mContent;
	}
	public ArrayList<String> getmImages() {
		return mImages;
	}
	public void setmImages(ArrayList<String> mImages) {
		this.mImages = mImages;
	}
	
	@Override
	public boolean equals(Object o) {
		Rate rate=(Rate) o;
		if (mName.equals(rate.getmName())) {
			return true;
		} else {
			return false;
		}
	}
	
}
