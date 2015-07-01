package com.hylsmart.yihui.model.home.bean;

import java.util.ArrayList;

public class HomeBean {

	private String mQQ;
	private String mWeixin;
	private String mShareUrl;
	private String mShareContent;
	private ArrayList<Banner> mCommodityBanner;
	private ArrayList<ProductGroupBuy> mTuanGouCard;
	private Advertisement mAdvertising;
	private ArrayList<Category> mCommodityCard;
	private ArrayList<HotGood> mCommodityHot;
	private SecKill mSecKill;
	
	
	
	public SecKill getmSecKill() {
		return mSecKill;
	}
	public void setmSecKill(SecKill mSecKill) {
		this.mSecKill = mSecKill;
	}
	public ArrayList<HotGood> getmCommodityHot() {
		return mCommodityHot;
	}
	public void setmCommodityHot(ArrayList<HotGood> mCommodityHot) {
		this.mCommodityHot = mCommodityHot;
	}
	public String getmQQ() {
		return mQQ;
	}
	public void setmQQ(String mQQ) {
		this.mQQ = mQQ;
	}
	public String getmWeixin() {
		return mWeixin;
	}
	public void setmWeixin(String mWeixin) {
		this.mWeixin = mWeixin;
	}
	public ArrayList<Banner> getmCommodityBanner() {
		return mCommodityBanner;
	}
	public void setmCommodityBanner(ArrayList<Banner> mCommodityBanner) {
		this.mCommodityBanner = mCommodityBanner;
	}
	public ArrayList<ProductGroupBuy> getmTuanGouCard() {
		return mTuanGouCard;
	}
	public void setmTuanGouCard(ArrayList<ProductGroupBuy> mTuanGouCard) {
		this.mTuanGouCard = mTuanGouCard;
	}
	public Advertisement getmAdvertising() {
		return mAdvertising;
	}
	public void setmAdvertising(Advertisement mAdvertising) {
		this.mAdvertising = mAdvertising;
	}
	public ArrayList<Category> getmCommodityCard() {
		return mCommodityCard;
	}
	public void setmCommodityCard(ArrayList<Category> mCommodityCard) {
		this.mCommodityCard = mCommodityCard;
	}
    public String getmShareUrl() {
        return mShareUrl;
    }
    public void setmShareUrl(String mShareUrl) {
        this.mShareUrl = mShareUrl;
    }
    public String getmShareContent() {
        return mShareContent;
    }
    public void setmShareContent(String mShareContent) {
        this.mShareContent = mShareContent;
    }
	
	
	
}
