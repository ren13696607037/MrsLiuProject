package com.hylsmart.yihui.model.mall.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Standard implements Serializable {

	private static final long serialVersionUID = 4L;
	private String mValue;
	private ArrayList<String> mGoodsIdList;// 原规格数据格式
	private ArrayList<SubStandarrd> mSubList;// 下级规格
	private boolean isHave = true;
	private boolean isChecked = false;
	private boolean isUserChecked = false;
	private int mLinearIndex = -1;

	public ArrayList<String> getmGoodsIdList() {
		return mGoodsIdList;
	}

	public void setmGoodsIdList(ArrayList<String> mGoodsIdList) {
		this.mGoodsIdList = mGoodsIdList;
	}

	public boolean isHave() {
		return isHave;
	}

	public void setHave(boolean isHave) {
		this.isHave = isHave;
	}

	public String getmValue() {
		return mValue;
	}

	public void setmValue(String mValue) {
		this.mValue = mValue;
	}

	public int getmLinearIndex() {
		return mLinearIndex;
	}

	public void setmLinearIndex(int mLinearIndex) {
		this.mLinearIndex = mLinearIndex;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public ArrayList<SubStandarrd> getmSubList() {
		return mSubList;
	}

	public void setmSubList(ArrayList<SubStandarrd> mSubList) {
		this.mSubList = mSubList;
	}

	public boolean isUserChecked() {
		return isUserChecked;
	}

	public void setUserChecked(boolean isUserChecked) {
		this.isUserChecked = isUserChecked;
	}

}
