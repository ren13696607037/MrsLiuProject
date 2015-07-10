package com.techfly.liutaitai.model.order.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.techfly.liutaitai.model.mall.bean.Product;



public class Order implements Serializable{
	private String mId;//订单预约id
	private String mNo;//订单预约编号
	private String mTime;//预约时间
	private String mStoreName;//商店名
	private String mStoreId;//商店id
	private String mOrderContent;//订单列表的显示内容
	private String mPrice;//订单总价
	private int mState;//订单状态
	private String mName;//客户名
	private String mPhone;//客户手机
	private String mAddress;//上门地址
	private String mOrderTime; //服务时间
	private String mNote;//备注
	private String mFree;//配送费
	private String mVoucher;//抵用券
	private int mSystem;//订单类型
	private ArrayList<Product> mList;//商品列表
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmNo() {
		return mNo;
	}
	public void setmNo(String mNo) {
		this.mNo = mNo;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	public String getmStoreName() {
		return mStoreName;
	}
	public void setmStoreName(String mStoreName) {
		this.mStoreName = mStoreName;
	}
	public String getmStoreId() {
		return mStoreId;
	}
	public void setmStoreId(String mStoreId) {
		this.mStoreId = mStoreId;
	}
	public String getmOrderContent() {
		return mOrderContent;
	}
	public void setmOrderContent(String mOrderContent) {
		this.mOrderContent = mOrderContent;
	}
	public String getmPrice() {
		return mPrice;
	}
	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	public int getmState() {
		return mState;
	}
	public void setmState(int mState) {
		this.mState = mState;
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
	public String getmNote() {
		return mNote;
	}
	public void setmNote(String mNote) {
		this.mNote = mNote;
	}
	public String getmFree() {
		return mFree;
	}
	public void setmFree(String mFree) {
		this.mFree = mFree;
	}
	public String getmVoucher() {
		return mVoucher;
	}
	public void setmVoucher(String mVoucher) {
		this.mVoucher = mVoucher;
	}
	public int getmSystem() {
		return mSystem;
	}
	public void setmSystem(int mSystem) {
		this.mSystem = mSystem;
	}
	public ArrayList<Product> getmList() {
		return mList;
	}
	public void setmList(ArrayList<Product> mList) {
		this.mList = mList;
	}
	
}
