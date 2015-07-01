package com.hylsmart.yihui.model.pcenter.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Logistics implements Serializable{
	private int mResultCode;//返回标识码
	private String mReason;//返回信息
	private ArrayList<Content> mList;//物流列表
	public int getmResultCode() {
		return mResultCode;
	}
	public void setmResultCode(int mResultCode) {
		this.mResultCode = mResultCode;
	}
	public String getmReason() {
		return mReason;
	}
	public void setmReason(String mReason) {
		this.mReason = mReason;
	}
	public ArrayList<Content> getmList() {
		return mList;
	}
	public void setmList(ArrayList<Content> mList) {
		this.mList = mList;
	}
	
}
