package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Province implements Serializable{
	private String mId;
	private String mName;
	private ArrayList<Area> mList;
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
	public ArrayList<Area> getmList() {
		return mList;
	}
	public void setmList(ArrayList<Area> mList) {
		this.mList = mList;
	}
	@Override
	public String toString() {
		return "Province [mId=" + mId + ", mName=" + mName + ", mList=" + mList
				+ "]";
	}
	
}
