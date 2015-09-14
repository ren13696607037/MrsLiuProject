package com.techfly.liutaitai.bean;

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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mCode;
        result = prime * result + ((mData == null) ? 0 : mData.hashCode());
        result = prime * result
                + ((mMessage == null) ? 0 : mMessage.hashCode());
        result = prime * result + ((object == null) ? 0 : object.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResultInfo other = (ResultInfo) obj;
        if (mCode != other.mCode)
            return false;
        if (mData == null) {
            if (other.mData != null)
                return false;
        } else if (!mData.equals(other.mData))
            return false;
        if (mMessage == null) {
            if (other.mMessage != null)
                return false;
        } else if (!mMessage.equals(other.mMessage))
            return false;
        if (object == null) {
            if (other.object != null)
                return false;
        } else if (!object.equals(other.object))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "ResultInfo [mCode=" + mCode + ", mMessage=" + mMessage
                + ", mData=" + mData + ", object=" + object + "]";
    }
    
}
