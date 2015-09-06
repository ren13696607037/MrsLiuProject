package com.techfly.liutaitai.model.pcenter.bean;

public class MyService {
	private String mOrderingNum;//进行中的订单数
	private String mId;
	private Technician mTechnician;
	private String mPrice;
	private String mServiceingNum;//待服务的订单数量
	private String mOrderNum;//待接订单数量
	private String mType;//技师类型
	public String getmOrderingNum() {
		return mOrderingNum;
	}
	public void setmOrderingNum(String mOrderingNum) {
		this.mOrderingNum = mOrderingNum;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public Technician getmTechnician() {
		return mTechnician;
	}
	public void setmTechnician(Technician mTechnician) {
		this.mTechnician = mTechnician;
	}
	public String getmPrice() {
		return mPrice;
	}
	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	public String getmServiceingNum() {
		return mServiceingNum;
	}
	public void setmServiceingNum(String mServiceingNum) {
		this.mServiceingNum = mServiceingNum;
	}
	public String getmOrderNum() {
		return mOrderNum;
	}
	public void setmOrderNum(String mOrderNum) {
		this.mOrderNum = mOrderNum;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	@Override
	public String toString() {
		return "MyService [mOrderingNum=" + mOrderingNum + ", mId=" + mId
				+ ", mTechnician=" + mTechnician + ", mPrice=" + mPrice
				+ ", mServiceingNum=" + mServiceingNum + ", mOrderNum="
				+ mOrderNum + ", mType=" + mType + "]";
	}
	
}
