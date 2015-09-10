package com.techfly.liutaitai.model.pcenter.bean;

public class TechAccount {
	private String mId;
	private String mType;
	private String mBank;
	private String mName;
	private String mAccount;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	public String getmBank() {
		return mBank;
	}
	public void setmBank(String mBank) {
		this.mBank = mBank;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmAccount() {
		return mAccount;
	}
	public void setmAccount(String mAccount) {
		this.mAccount = mAccount;
	}
	@Override
	public String toString() {
		return "TechAccount [mId=" + mId + ", mType=" + mType + ", mBank="
				+ mBank + ", mName=" + mName + ", mAccount=" + mAccount + "]";
	}
	
}
