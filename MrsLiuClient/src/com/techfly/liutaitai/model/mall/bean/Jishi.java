package com.techfly.liutaitai.model.mall.bean;

import java.io.Serializable;

public class Jishi implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String mId;
    private String mImg;
    private String mSex;
    private String mName;
    private float mRating;
    private String mServiceTime;
    private int type;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public String getmServiceTime() {
        return mServiceTime;
    }

    public void setmServiceTime(String mServiceTime) {
        this.mServiceTime = mServiceTime;
    }

  

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
