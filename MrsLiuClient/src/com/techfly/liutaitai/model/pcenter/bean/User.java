package com.techfly.liutaitai.model.pcenter.bean;

public class User {
	private String mId;
	private String mPhone;
	private String mPass;
	private String mNick;
	private String mMessage;
	private String mImage;
	private String mToken;
	public String getmToken() {
		return mToken;
	}
	public void setmToken(String mToken) {
		this.mToken = mToken;
	}
	public String getmMoney() {
		return mMoney;
	}
	public void setmMoney(String mMoney) {
		this.mMoney = mMoney;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	private String mMoney;
	private String mType;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmPhone() {
		return mPhone;
	}
	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}
	public String getmPass() {
		return mPass;
	}
	public void setmPass(String mPass) {
		this.mPass = mPass;
	}
	public String getmNick() {
		return mNick;
	}
	public void setmNick(String mNick) {
		this.mNick = mNick;
	}
	public String getmMessage() {
		return mMessage;
	}
	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	public String getmImage() {
		return mImage;
	}
	public void setmImage(String mImage) {
		this.mImage = mImage;
	}
	@Override
	public String toString() {
		return "User [mId=" + mId + ", mPhone=" + mPhone + ", mPass=" + mPass
				+ ", mNick=" + mNick + ", mMessage=" + mMessage + ", mImage="
				+ mImage + ", mToken=" + mToken + ", mMoney=" + mMoney
				+ ", mType=" + mType + "]";
	}
	
}
