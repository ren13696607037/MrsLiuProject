package com.techfly.liutaitai.model.pcenter.bean;

import java.io.Serializable;

public class Content implements Serializable{
    private int mId;
    private String mContent;
    private String mTime;
    private String mMessage;
    public int getmId() {
        return mId;
    }
    public void setmId(int mId) {
        this.mId = mId;
    }
    public String getmContent() {
        return mContent;
    }
    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
    public String getmTime() {
        return mTime;
    }
    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
    public String getmMessage() {
        return mMessage;
    }
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
    
}
