package com.hylsmart.yihui.model.pcenter.bean;

import java.io.Serializable;

public class Address implements Serializable{
	private String mId;
	private String mName;
	private String mPhone;
	private String mAddress;
	private String mAddressId;
	private boolean mIsSelect;
	private String mZipcode;
	private String mDetail;
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
	public String getmPhone() {
		return mPhone;
	}
	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public boolean ismIsSelect() {
		return mIsSelect;
	}
	public void setmIsSelect(boolean mIsSelect) {
		this.mIsSelect = mIsSelect;
	}
	@Override
	public String toString() {
		return "Address [mId=" + mId + ", mName=" + mName + ", mPhone="
				+ mPhone + ", mAddress=" + mAddress + ", mIsSelect="
				+ mIsSelect + "]";
	}
	public String getmAddressId() {
		return mAddressId;
	}
	public void setmAddressId(String mAddressId) {
		this.mAddressId = mAddressId;
	}
	public String getmZipcode() {
		return mZipcode;
	}
	public void setmZipcode(String mZipcode) {
		this.mZipcode = mZipcode;
	}
	public String getmDetail() {
		return mDetail;
	}
	public void setmDetail(String mDetail) {
		this.mDetail = mDetail;
	}

}
