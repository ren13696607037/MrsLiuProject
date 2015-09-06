package com.techfly.liutaitai.model.pcenter.bean;

public class Voucher {
	private String mId;
	private String mNeed;
	private String mPrice;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmNeed() {
		return mNeed;
	}
	public void setmNeed(String mNeed) {
		this.mNeed = mNeed;
	}
	public String getmPrice() {
		return mPrice;
	}
	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	@Override
	public String toString() {
		return "Voucher [mId=" + mId + ", mNeed=" + mNeed + ", mPrice="
				+ mPrice + "]";
	}
	
}
