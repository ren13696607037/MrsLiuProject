package com.techfly.liutaitai.model.pcenter.bean;

import java.util.ArrayList;

public class RequestRate {
	private ArrayList<Rate> mRates;
	private String mCount;
	private String mAverge;
	public ArrayList<Rate> getmRates() {
		return mRates;
	}
	public void setmRates(ArrayList<Rate> mRates) {
		this.mRates = mRates;
	}
	public String getmCount() {
		return mCount;
	}
	public void setmCount(String mCount) {
		this.mCount = mCount;
	}
	public String getmAverge() {
		return mAverge;
	}
	public void setmAverge(String mAverge) {
		this.mAverge = mAverge;
	}
	@Override
	public String toString() {
		return "RequestRate [mRates=" + mRates + ", mCount=" + mCount
				+ ", mAverge=" + mAverge + "]";
	}
	
}
