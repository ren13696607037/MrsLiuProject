package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;

public class AddressManage implements Serializable{
	private String mId;//地址id
	private String mName;//姓名
	private String mPhone;//手机号码
	private String mCity;//城市
	private String mCircle;//商圈
	private String mDetail;//具体地址
	private boolean isDefault;//默认地址
	private String mCityId;//城市id
	public String getmCityId() {
		return mCityId;
	}
	public void setmCityId(String mCityId) {
		this.mCityId = mCityId;
	}
	public String getmCircleId() {
		return mCircleId;
	}
	public void setmCircleId(String mCircleId) {
		this.mCircleId = mCircleId;
	}
	private String mCircleId;//商圈id
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
	public String getmCity() {
		return mCity;
	}
	public void setmCity(String mCity) {
		this.mCity = mCity;
	}
	public String getmCircle() {
		return mCircle;
	}
	public void setmCircle(String mCircle) {
		this.mCircle = mCircle;
	}
	public String getmDetail() {
		return mDetail;
	}
	public void setmDetail(String mDetail) {
		this.mDetail = mDetail;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	@Override
	public String toString() {
		return "AddressManage [mId=" + mId + ", mName=" + mName + ", mPhone="
				+ mPhone + ", mCity=" + mCity + ", mCircle=" + mCircle
				+ ", mDetail=" + mDetail + ", isDefault=" + isDefault + "]";
	}
	
}
