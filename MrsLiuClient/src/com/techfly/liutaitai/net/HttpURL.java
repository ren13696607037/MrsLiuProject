package com.techfly.liutaitai.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.text.TextUtils;



public class HttpURL {
    private String mBaseUrl;
    private String mGetParamPrefix1;
    public void setmGetParamPrefix1(String mGetParamPrefix1) {
        this.mGetParamPrefix1 = mGetParamPrefix1;
    }

    private String mGetParamValues1;
    private String mGetParamPrefix2;
    private String mGetParamValues2;

    private String mGetParamPrefix3;
    private String mGetParamValues3;

    private String mGetParamPrefix4;
    private String mGetParamValues4;

    private String mGetParamPrefix5;
    
    private List<String> mParamValues = new ArrayList<String>();
    private List<String> mParamPrefix  = new ArrayList<String>();
    
    public String getmGetParamPrefix1() {
        return mGetParamPrefix1;
    }

  public HttpURL setmGetParamPrefix(String paramPrefix) {
      mParamPrefix.add(paramPrefix);
      return this;
    }
    
  public HttpURL setmGetParamValues(String paramPrefix) {
      mParamValues.add(paramPrefix);
      return this;
  }
    public String getmGetParamValues1() {
        return mGetParamValues1;
    }

    public void setmGetParamValues1(String mGetParamValues1) {
        this.mGetParamValues1 = mGetParamValues1;
    }

    public String getmGetParamPrefix2() {
        return mGetParamPrefix2;
    }

    public void setmGetParamPrefix2(String mGetParamPrefix2) {
        this.mGetParamPrefix2 = mGetParamPrefix2;
    }

    public String getmGetParamValues2() {
        return mGetParamValues2;
    }

    public void setmGetParamValues2(String mGetParamValues2) {
        this.mGetParamValues2 = mGetParamValues2;
    }

    public String getmGetParamPrefix3() {
        return mGetParamPrefix3;
    }

    public void setmGetParamPrefix3(String mGetParamPrefix3) {
        this.mGetParamPrefix3 = mGetParamPrefix3;
    }

    public String getmGetParamValues3() {
        return mGetParamValues3;
    }

    public void setmGetParamValues3(String mGetParamValues3) {
        this.mGetParamValues3 = mGetParamValues3;
    }

    public String getmGetParamPrefix4() {
        return mGetParamPrefix4;
    }

    public void setmGetParamPrefix4(String mGetParamPrefix4) {
        this.mGetParamPrefix4 = mGetParamPrefix4;
    }

    public String getmGetParamValues4() {
        return mGetParamValues4;
    }

    public void setmGetParamValues4(String mGetParamValues4) {
        this.mGetParamValues4 = mGetParamValues4;
    }

    public String getmGetParamPrefix5() {
        return mGetParamPrefix5;
    }

    public void setmGetParamPrefix5(String mGetParamPrefix5) {
        this.mGetParamPrefix5 = mGetParamPrefix5;
    }

    public String getmGetParamValues5() {
        return mGetParamValues5;
    }

    public void setmGetParamValues5(String mGetParamValues5) {
        this.mGetParamValues5 = mGetParamValues5;
    }

    public String getmGetParamPrefix6() {
        return mGetParamPrefix6;
    }

    public void setmGetParamPrefix6(String mGetParamPrefix6) {
        this.mGetParamPrefix6 = mGetParamPrefix6;
    }

    public String getmGetParamValues6() {
        return mGetParamValues6;
    }

    public void setmGetParamValues6(String mGetParamValues6) {
        this.mGetParamValues6 = mGetParamValues6;
    }

    private String mGetParamValues5;

    private String mGetParamPrefix6;
    private String mGetParamValues6;

    public String getmBaseUrl() {
        return mBaseUrl;
    }

    public void setmBaseUrl(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }
    public Map<String,String> getPostMap(){
        Map<String,String>  map = new HashMap<String, String>();
        if(mParamValues.size()>=1 && mParamValues.size()==mParamPrefix.size()){
            for(int i=0;i<mParamPrefix.size();i++){
                map .put(mParamPrefix.get(i),mParamValues.get(i));
            }
        }
        return map;
        
    }
    private class URLbuilder {
        Uri.Builder uriBuilder = null;
        public URLbuilder() {
            
        }
        public String buildUrl(){
            if (!TextUtils.isEmpty(mBaseUrl)) {
                //
                uriBuilder = Uri.parse(mBaseUrl).buildUpon();
            }else{
                return "";
            }
            if(mParamValues.size()>=1 && mParamValues.size()==mParamPrefix.size()){
                for(int i=0;i<mParamPrefix.size();i++){
                    uriBuilder.appendQueryParameter(mParamPrefix.get(i), mParamValues.get(i));
                }
            }else{
                if(!TextUtils.isEmpty(mGetParamValues1) && !TextUtils.isEmpty(mGetParamPrefix1)){
                    uriBuilder.appendQueryParameter(mGetParamPrefix1, mGetParamValues1);
                }
                if(!TextUtils.isEmpty(mGetParamValues2) && !TextUtils.isEmpty(mGetParamPrefix2)){
                    uriBuilder.appendQueryParameter(mGetParamPrefix2, mGetParamValues2);
                }
                if(!TextUtils.isEmpty(mGetParamValues3) && !TextUtils.isEmpty(mGetParamPrefix3)){
                    uriBuilder.appendQueryParameter(mGetParamPrefix3, mGetParamValues3);
                }
                if(!TextUtils.isEmpty(mGetParamValues4) && !TextUtils.isEmpty(mGetParamPrefix4)){
                    uriBuilder.appendQueryParameter(mGetParamPrefix4, mGetParamValues4);
                }
                if(!TextUtils.isEmpty(mGetParamValues5) && !TextUtils.isEmpty(mGetParamPrefix5)){
                    uriBuilder.appendQueryParameter(mGetParamPrefix5, mGetParamValues5);
                }
                if(!TextUtils.isEmpty(mGetParamValues6) && !TextUtils.isEmpty(mGetParamPrefix6)){
                    uriBuilder.appendQueryParameter(mGetParamPrefix6, mGetParamValues6);
                }
            }
          
            try {
                return new String(uriBuilder.toString().getBytes(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "";
        }

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        URLbuilder buidler = new URLbuilder();
       return buidler .buildUrl();
    }

}
