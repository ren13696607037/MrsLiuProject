package com.hylsmart.yihui.model.mall.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {

	private int mCategoryId;
	private String mCategoryName;
	private ArrayList<SubCategory> mSubCategoryList;
	private ArrayList<SubCategoryBanner> mSubCategoryBannerList;

	public int getmCategoryId() {
		return mCategoryId;
	}

	public void setmCategoryId(int mCategoryId) {
		this.mCategoryId = mCategoryId;
	}

	public String getmCategoryName() {
		return mCategoryName;
	}

	public void setmCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public ArrayList<SubCategory> getmSubCategoryList() {
		return mSubCategoryList;
	}

	public void setmSubCategoryList(ArrayList<SubCategory> mSubCategoryList) {
		this.mSubCategoryList = mSubCategoryList;
	}

	public ArrayList<SubCategoryBanner> getmSubCategoryBannerList() {
		return mSubCategoryBannerList;
	}

	public void setmSubCategoryBannerList(
			ArrayList<SubCategoryBanner> mSubCategoryBannerList) {
		this.mSubCategoryBannerList = mSubCategoryBannerList;
	}

}
