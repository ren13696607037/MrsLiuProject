package com.techfly.liutaitai.model.mall.bean;

import java.util.ArrayList;

public class CategoryInfo {
	private String id;
	private String name;
	private ArrayList<CategoryInfo> mDatas;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CategoryInfo> getmDatas() {
		return mDatas;
	}

	public void setmDatas(ArrayList<CategoryInfo> mDatas) {
		this.mDatas = mDatas;
	}

}
