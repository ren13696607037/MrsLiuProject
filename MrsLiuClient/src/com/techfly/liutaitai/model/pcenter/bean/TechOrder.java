package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;

public class TechOrder implements Serializable{
	private String mId;
    
    private String mOrderNo;// 订单编号
    
    private String mOrderTime;// 下单时间
    
    private String mServiceName;// 服务名称
    
    private String mServiceIcon;// 服务类型的图像

    private String mServicePrice;// 服务价格
   
    private String mServiceStatus;// 服务状态，进行中，已结束
    
    private String mCustomerName;// 服务客户的名称
    
    private String mCustomerPhone;// 服务客户的联系方式
    
    private String mCustomerAddress;// 服务客户的联系地址
    
    private String mCustomerTime;//服务时间
    
    private String mVoucher;//抵用卷价格
    private String mStartTime;

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmOrderNo() {
		return mOrderNo;
	}

	public void setmOrderNo(String mOrderNo) {
		this.mOrderNo = mOrderNo;
	}

	public String getmOrderTime() {
		return mOrderTime;
	}

	public void setmOrderTime(String mOrderTime) {
		this.mOrderTime = mOrderTime;
	}

	public String getmServiceName() {
		return mServiceName;
	}

	public void setmServiceName(String mServiceName) {
		this.mServiceName = mServiceName;
	}

	public String getmServiceIcon() {
		return mServiceIcon;
	}

	public void setmServiceIcon(String mServiceIcon) {
		this.mServiceIcon = mServiceIcon;
	}

	public String getmServicePrice() {
		return mServicePrice;
	}

	public void setmServicePrice(String mServicePrice) {
		this.mServicePrice = mServicePrice;
	}

	public String getmServiceStatus() {
		return mServiceStatus;
	}

	public void setmServiceStatus(String mServiceStatus) {
		this.mServiceStatus = mServiceStatus;
	}

	public String getmCustomerName() {
		return mCustomerName;
	}

	public void setmCustomerName(String mCustomerName) {
		this.mCustomerName = mCustomerName;
	}

	public String getmCustomerPhone() {
		return mCustomerPhone;
	}

	public void setmCustomerPhone(String mCustomerPhone) {
		this.mCustomerPhone = mCustomerPhone;
	}

	public String getmCustomerAddress() {
		return mCustomerAddress;
	}

	public void setmCustomerAddress(String mCustomerAddress) {
		this.mCustomerAddress = mCustomerAddress;
	}

	public String getmCustomerTime() {
		return mCustomerTime;
	}

	public void setmCustomerTime(String mCustomerTime) {
		this.mCustomerTime = mCustomerTime;
	}

	public String getmVoucher() {
		return mVoucher;
	}

	public void setmVoucher(String mVoucher) {
		this.mVoucher = mVoucher;
	}

	@Override
	public String toString() {
		return "TechOrder [mId=" + mId + ", mOrderNo=" + mOrderNo
				+ ", mOrderTime=" + mOrderTime + ", mServiceName="
				+ mServiceName + ", mServiceIcon=" + mServiceIcon
				+ ", mServicePrice=" + mServicePrice + ", mServiceStatus="
				+ mServiceStatus + ", mCustomerName=" + mCustomerName
				+ ", mCustomerPhone=" + mCustomerPhone + ", mCustomerAddress="
				+ mCustomerAddress + ", mCustomerTime=" + mCustomerTime
				+ ", mVoucher=" + mVoucher + "]";
	}

	public String getmStartTime() {
		return mStartTime;
	}

	public void setmStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}
    
}
