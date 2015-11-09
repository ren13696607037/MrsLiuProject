package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.techfly.liutaitai.model.mall.bean.Product;

public class MyOrder implements Serializable {
	private static final long serialVersionUID = 1001L;
	private String mId;// 仅仅是个ID
	private String mNum;// 订单号
	private String mImg;// 图片？
	private String mTitle;// i do not now what it is
	private String mTotalPrice;// 总价格
	private String mTime;// 应该是创建订单的时间
	private int mState;// 状态码
	private ArrayList<Product> mList;// 产品列表
	private String mPrice;//
	private String mExpCom;
	private String mExpShortName;
	private String mExpNo;

	private int mType = -1;// 订单类型，分类 0 洗衣 1生鲜 2鲜花 4奢侈品
	private int mTotalCount;// 产品总数量
	private String mUnit;// 单位
	private double mDeliverFee;// 运费
	private String mCustomerName;// 客户姓名和电话
	private String mCustomerAddr;// 配送地址
	private double mOffsetValue=0;// 抵价券金额
	private String mTips;// 备注
	private int mPayType;// 支付方式

	public String getmTips() {
		return mTips;
	}

	public void setmTips(String mTips) {
		this.mTips = mTips;
	}

	public double getmOffsetValue() {
		return mOffsetValue;
	}

	public void setmOffsetValue(double mOffsetValue) {
		this.mOffsetValue = mOffsetValue;
	}

	public String getmCustomerName() {
		return mCustomerName;
	}

	public void setmCustomerName(String mCustomerName) {
		this.mCustomerName = mCustomerName;
	}

	public String getmCustomerAddr() {
		return mCustomerAddr;
	}

	public void setmCustomerAddr(String mCustomerAddr) {
		this.mCustomerAddr = mCustomerAddr;
	}

	public double getmDeliverFee() {
		return mDeliverFee;
	}

	public void setmDeliverFee(double mDeliverFee) {
		this.mDeliverFee = mDeliverFee;
	}

	public String getmUnit() {
		return mUnit;
	}

	public void setmUnit(String mUnit) {
		this.mUnit = mUnit;
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	public int getmTotalCount() {
		return mTotalCount;
	}

	public void setmTotalCount(int mTotalCount) {
		this.mTotalCount = mTotalCount;
	}

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

	public AddressManage getmAddress() {
		return mAddress;
	}

	public void setmAddress(AddressManage mAddress) {
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
	private AddressManage mAddress;
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

	public int getmPayType() {
		return mPayType;
	}

	public void setmPayType(int mPayType) {
		this.mPayType = mPayType;
	}

}
