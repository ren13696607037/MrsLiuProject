package com.hylsmart.yihui.bean;

public class ResultInfo {
    private int mCode =-1;
    private String mMessage;
    private String mData;
    private Object object;
   
    public String getmMessage() {
        return mMessage;
    }
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
    public String getmData() {
        return mData;
    }
    public void setmData(String mData) {
        this.mData = mData;
    }
    public int getmCode() {
        return mCode;
    }
    public void setmCode(int mCode) {
        this.mCode = mCode;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
}
