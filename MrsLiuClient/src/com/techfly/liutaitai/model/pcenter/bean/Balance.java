package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;

public class Balance implements Serializable{
	private String mId;
	private String mPrice;
	private String mSend;
	private String mTime;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmPrice() {
		return mPrice;
	}
	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	public String getmSend() {
		return mSend;
	}
	public void setmSend(String mSend) {
		this.mSend = mSend;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	@Override
	public String toString() {
		return "Balance [mId=" + mId + ", mPrice=" + mPrice + ", mSend="
				+ mSend + ", mTime=" + mTime + "]";
	}
	
}
