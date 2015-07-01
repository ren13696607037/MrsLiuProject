package com.techfly.liutaitai.model.home.bean;

import java.util.ArrayList;

public class SecKill {

	private String mStartTime;
	private int mStatus;
	private ArrayList<SecKillItem> mItems;
	
	public String getmStartTime() {
		return mStartTime;
	}
	public void setmStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}
	public int getmStatus() {
		return mStatus;
	}
	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
	}
	public ArrayList<SecKillItem> getmItems() {
		return mItems;
	}
	public void setmItems(ArrayList<SecKillItem> mItems) {
		this.mItems = mItems;
	}
	
	
}
