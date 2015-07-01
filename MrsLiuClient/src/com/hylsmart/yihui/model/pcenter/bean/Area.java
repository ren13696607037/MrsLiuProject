package com.hylsmart.yihui.model.pcenter.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Area implements Serializable{
	private String mId;
	private String mName;
	private ArrayList<City> mList;
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public ArrayList<City> getmList() {
		return mList;
	}
	public void setmList(ArrayList<City> mList) {
		this.mList = mList;
	}
	
	@Override
	public String toString() {
		return "Area [mId=" + mId + ", mName=" + mName + ", mList=" + mList
				+ "]";
	}

}
