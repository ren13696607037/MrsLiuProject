package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.techfly.liutaitai.model.mall.bean.Product;

public class MyOrder implements Serializable{
	private String mId;
	private String mNum;
	private String mImg;
	private String mTitle;
	private String mTotalPrice;
	private String mTime;
	private int mState;
	private ArrayList<Product> mList;
	private String mPrice;
	private String mExpCom;
	private String mExpShortName;
	public String getmExpCom() {
		return mExpCom;
	}
	public void setmExpCom(String mExpCom) {
		this.mExpCom = mExpCom;
	}
	public String getmExpNo() {
		return mExpNo;
	}
	public void setmExpNo(String mExpNo) {
		this.mExpNo = mExpNo;
	}
	private String mExpNo;
	public String getmPrice() {
		return mPrice;
	}
	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}
	public ArrayList<Product> getmList() {
		return mList;
	}
	public void setmList(ArrayList<Product> mList) {
		this.mList = mList;
	}
	public String getmNote() {
		return mNote;
	}
	public void setmNote(String mNote) {
		this.mNote = mNote;
	}
	public Address getmAddress() {
		return mAddress;
	}
	public void setmAddress(Address mAddress) {
		this.mAddress = mAddress;
	}
	public int getmPay() {
		return mPay;
	}
	public void setmPay(int mPay) {
		this.mPay = mPay;
	}
	public String getmFree() {
		return mFree;
	}
	public void setmFree(String mFree) {
		this.mFree = mFree;
	}
	private String mNote;
	private Address mAddress;
	private int mPay;
	private String mFree;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmNum() {
		return mNum;
	}
	public void setmNum(String mNum) {
		this.mNum = mNum;
	}
	public String getmImg() {
		return mImg;
	}
	public void setmImg(String mImg) {
		this.mImg = mImg;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmTotalPrice() {
		return mTotalPrice;
	}
	public void setmTotalPrice(String mTotalPrice) {
		this.mTotalPrice = mTotalPrice;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	public int getmState() {
		return mState;
	}
	public void setmState(int mState) {
		this.mState = mState;
	}
	public String getmExpShortName() {
		return mExpShortName;
	}
	public void setmExpShortName(String mExpShortName) {
		this.mExpShortName = mExpShortName;
	}
	
}
