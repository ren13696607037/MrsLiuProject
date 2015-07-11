/**
 * 洗衣 产品
 */
package com.techfly.liutaitai.model.mall.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techfly.liutaitai.dao.Persistence;

public class Product extends Persistence implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> mImgArray;// 图片数组
	private float mMarketPrice;// 市场价
	private String mSN;//商品货号
	private int mType;// 商品类型，团购商品，秒杀商品，还是正常商品
	@Expose
	@SerializedName("price")
	private float mPrice;// 原价
	@Expose
	@SerializedName("quantity")
	private int mAmount = 1;// 购买商品的数量
	private String mName;// 商品名
	private int mCommentCount;// 商品评论数
	private Map<String, String> mIntroduceMap;// 商品属性 和商品属性值对应关系
	private boolean mIsCollect;// 是否收藏
	private String mImg;// 商品图标
	private boolean mIsCheck = false;// 是否在购物车总被选中
	private String mBelongCategory;// 商品属于哪类商品
	@Expose
	@SerializedName("id")
	private String mId;// 商品ID
	private boolean mEditable = false;// 购物车是否编辑状态
	private String mSale;
	// 规格没有？
	@Expose
    @SerializedName("productId")
	private int mProductId = -1;//用户所选择的商品货号
	private int mStoreCount;//库存
	private double mRebate;
	private String mCommentReputably;// 商品评论好评率
	private int mCommentRating;
	private ArrayList<Comments> mCommentsList;
	private ArrayList<StandardClass> mStandardClassList;
	private int mProductType;
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();

	}

	@Override
	public boolean equals(Object obj) {
		Product product = (Product) obj;
		if (mId == null) {
			return false;
		}
		if (mId.equals(product.getmId())) {
			return true;
		} else {
			return false;
		}

	}

	public List<String> getmImgArray() {
		return mImgArray;
	}

	public void setmImgArray(List<String> mImgArray) {
		this.mImgArray = mImgArray;
	}

	public float getmMarketPrice() {
		return mMarketPrice;
	}

	public void setmMarketPrice(float mMarketPrice) {
		this.mMarketPrice = mMarketPrice;
	}

	public float getmPrice() {
		return mPrice;
	}

	public void setmPrice(float mPrice) {
		this.mPrice = mPrice;
	}

	public int getmAmount() {
		return mAmount;
	}

	public void setmAmount(int mAmount) {
		this.mAmount = mAmount;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public int getmCommentCount() {
		return mCommentCount;
	}

	public void setmCommentCount(int mCommentCount) {
		this.mCommentCount = mCommentCount;
	}

	public Map<String, String> getmIntroduceMap() {
		return mIntroduceMap;
	}

	public void setmIntroduceMap(Map<String, String> mIntroduceMap) {
		this.mIntroduceMap = mIntroduceMap;
	}

	public boolean ismIsCollect() {
		return mIsCollect;
	}

	public void setmIsCollect(boolean mIsCollect) {
		this.mIsCollect = mIsCollect;
	}

	public String getmImg() {
		return mImg;
	}

	public void setmImg(String mImg) {
		this.mImg = mImg;
	}

	public boolean ismIsCheck() {
		return mIsCheck;
	}

	public void setmIsCheck(boolean mIsCheck) {
		this.mIsCheck = mIsCheck;
	}

	public String getmBelongCategory() {
		return mBelongCategory;
	}

	public void setmBelongCategory(String mBelongCategory) {
		this.mBelongCategory = mBelongCategory;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public boolean ismEditable() {
		return mEditable;
	}

	public void setmEditable(boolean mEditable) {
		this.mEditable = mEditable;
	}

	public String getmSale() {
		return mSale;
	}

	public void setmSale(String mSale) {
		this.mSale = mSale;
	}

	public int getmStoreCount() {
		return mStoreCount;
	}

	public void setmStoreCount(int mStoreCount) {
		this.mStoreCount = mStoreCount;
	}

	public double getmRebate() {
		return mRebate;
	}

	public void setmRebate(double mRebate) {
		this.mRebate = mRebate;
	}

	public String getmCommentReputably() {
		return mCommentReputably;
	}

	public void setmCommentReputably(String mCommentReputably) {
		this.mCommentReputably = mCommentReputably;
	}

	public ArrayList<Comments> getmCommentsList() {
		return mCommentsList;
	}

	public void setmCommentsList(ArrayList<Comments> mCommentsList) {
		this.mCommentsList = mCommentsList;
	}

	public int getmCommentRating() {
		return mCommentRating;
	}

	public void setmCommentRating(int mCommentRating) {
		this.mCommentRating = mCommentRating;
	}

	public ArrayList<StandardClass> getmStandardClassList() {
		return mStandardClassList;
	}

	public void setmStandardClassList(ArrayList<StandardClass> mStandardClassList) {
		this.mStandardClassList = mStandardClassList;
	}

	public String getmSN() {
		return mSN;
	}

	public void setmSN(String mSN) {
		this.mSN = mSN;
	}

	public int getmProductId() {
		return mProductId;
	}

	public void setmProductId(int mProductId) {
		this.mProductId = mProductId;
	}

	public int getmProductType() {
		return mProductType;
	}

	public void setmProductType(int mProductType) {
		this.mProductType = mProductType;
	}

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

   
}
