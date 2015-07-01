package com.hylsmart.yihui.model.mall.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class StandardClass implements Serializable{
	
	private static final long serialVersionUID = 3L;

	private String mClassName;
	private ArrayList<Standard> mArrayList;

	public String getmClassName() {
		return mClassName;
	}

	public void setmClassName(String mClassName) {
		this.mClassName = mClassName;
	}

	public ArrayList<Standard> getmArrayList() {
		return mArrayList;
	}

	public void setmArrayList(ArrayList<Standard> mArrayList) {
		this.mArrayList = mArrayList;
	}

}
