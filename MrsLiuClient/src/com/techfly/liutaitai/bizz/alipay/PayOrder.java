package com.techfly.liutaitai.bizz.alipay;

public class PayOrder {
    private String mOrderNo;//外部订单号
    private String mMerchantId;//商户ID
    public String getmOrderNo() {
        return mOrderNo;
    }
    public void setmOrderNo(String mOrderNo) {
        this.mOrderNo = mOrderNo;
    }
    public String getmMerchantId() {
        return mMerchantId;
    }
    public void setmMerchantId(String mMerchantId) {
        this.mMerchantId = mMerchantId;
    }
    public String getmAccountId() {
        return mAccountId;
    }
    public void setmAccountId(String mAccountId) {
        this.mAccountId = mAccountId;
    }
    public String getmProductName() {
        return mProductName;
    }
    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }
    public String getmProductDesc() {
        return mProductDesc;
    }
    public void setmProductDesc(String mProductDesc) {
        this.mProductDesc = mProductDesc;
    }
    public String getmProductPrice() {
        return mProductPrice;
    }
    public void setmProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }
    public String getmNotifyUrl() {
        return mNotifyUrl;
    }
    public void setmNotifyUrl(String mNotifyUrl) {
        this.mNotifyUrl = mNotifyUrl;
    }
    private String mAccountId;// 收款账号
    private String mProductName;//商品名称
    private String mProductDesc;// 商品介绍
    private String mProductPrice;// 商品价格
    private String mNotifyUrl;// 通知地址
    
    
}
