package com.techfly.liutaitai.net;

import java.util.Map;

import com.android.volley.Request.Method;

public class RequestParam implements IRequestParam {
    private HttpURL mHttpURL;
    
    private String mParserClassName;
    
    private int   mRequestMethod=-1;
    
    private boolean mIsLogin;
    
    private String mToken;
    
    private String mId;
    
    public HttpURL getmHttpURL() {
        return mHttpURL;
    }

    public void setmHttpURL(HttpURL mHttpURL) {
        this.mHttpURL = mHttpURL;
    }
   
    public void setPostRequestMethod() {
        mRequestMethod =Method.POST;
    }
    public void setDeleteRequestMethod() {
        mRequestMethod =Method.DELETE;
    }
    public void setPutRequestMethod() {
        mRequestMethod =Method.PUT;
    }
    @Override
    public String buildRequestUrl() {
        // TODO Auto-generated method stub
        return mHttpURL.toString();
    }

    public String getmParserClassName() {
        return mParserClassName;
    }

    public void setmParserClassName(String mParserClassName) {
        this.mParserClassName = mParserClassName;
    }
    public Map<String,String> getPostParamMap(){
        return mHttpURL.getPostMap();
    }
    @Override
    public int requestMethod() {
        return  mRequestMethod;
    }

    public boolean ismIsLogin() {
        return mIsLogin;
    }

    public void setmIsLogin(boolean mIsLogin) {
        this.mIsLogin = mIsLogin;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

}
