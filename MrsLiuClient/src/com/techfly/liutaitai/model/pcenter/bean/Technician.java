package com.techfly.liutaitai.model.pcenter.bean;

public class Technician {
	private int mId;
	private String mTimes;
	private String mStar;
	private String mName;
	private String mSex;
	private String mHeader;
	private String mType;
	private String mCity;
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getmTimes() {
		return mTimes;
	}
	public void setmTimes(String mTimes) {
		this.mTimes = mTimes;
	}
	public String getmStar() {
		return mStar;
	}
	public void setmStar(String mStar) {
		this.mStar = mStar;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmSex() {
		return mSex;
	}
	public void setmSex(String mSex) {
		this.mSex = mSex;
	}
	public String getmHeader() {
		return mHeader;
	}
	public void setmHeader(String mHeader) {
		this.mHeader = mHeader;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	public String getmCity() {
		return mCity;
	}
	public void setmCity(String mCity) {
		this.mCity = mCity;
	}
	@Override
	public String toString() {
		return "Technician [mId=" + mId + ", mTimes=" + mTimes + ", mStar="
				+ mStar + ", mName=" + mName + ", mSex=" + mSex + ", mHeader="
				+ mHeader + ", mType=" + mType + ", mCity=" + mCity + "]";
	}
	
}
