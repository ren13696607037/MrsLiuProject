package com.techfly.liutaitai.model.home.bean;

import java.util.ArrayList;

public class Category {

	private String mName;
	private ArrayList<CategoryItem> mItems;
	private CategoryShow mCategoryShow;
	private ArrayList<CategoryShow> mCommodityShow;
	
	public CategoryShow getmCategoryShow() {
		return mCategoryShow;
	}
	public void setmCategoryShow(CategoryShow mCategoryShow) {
		this.mCategoryShow = mCategoryShow;
	}
	public ArrayList<CategoryShow> getmCommodityShow() {
		return mCommodityShow;
	}
	public void setmCommodityShow(ArrayList<CategoryShow> mCommodityShow) {
		this.mCommodityShow = mCommodityShow;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public ArrayList<CategoryItem> getmItems() {
		return mItems;
	}
	public void setmItems(ArrayList<CategoryItem> mItems) {
		this.mItems = mItems;
	}
	
	
}
