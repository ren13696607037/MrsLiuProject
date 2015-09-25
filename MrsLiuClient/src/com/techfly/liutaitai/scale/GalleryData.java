package com.techfly.liutaitai.scale;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryData implements Parcelable{
    private String mTitle;
    private int mNum;
    private String mText;

    public GalleryData() {
        super();
    }

    public GalleryData(String mTitle, String mText) {
        super();
        this.mTitle = mTitle;
        this.mText = mText;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmNum() {
        return mNum;
    }

    public void setmNum(int mNum) {
        this.mNum = mNum;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    @Override
    public String toString() {
        return "Gallery [mTitle=" + mTitle + ", mNum=" + mNum + ", mText="
                + mText + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<GalleryData> CREATOR = new Creator(){  
  
        @Override  
        public GalleryData createFromParcel(Parcel source) {  
            GalleryData data = new GalleryData();  
            data.setmTitle(source.readString());
            data.setmNum(source.readInt());
            data.setmText(source.readString());
            return data;  
        }

        @Override
        public GalleryData[] newArray(int arg0) {
            return new GalleryData[arg0];
        }  
  
        
    };  

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeString(mTitle);
        arg0.writeInt(mNum);
        arg0.writeString(mText);
    }

}
