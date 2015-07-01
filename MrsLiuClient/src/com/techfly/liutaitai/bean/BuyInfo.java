package com.techfly.liutaitai.bean;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techfly.liutaitai.model.mall.bean.Product;

public class BuyInfo {
    @Expose
    @SerializedName("customerId")
    private int userId;
    @Expose
    @SerializedName("addressId")
    private int addressId;
    @Expose
    @SerializedName("memo")
    private String memo;
    @Expose
    @SerializedName("type")
    private int type = 0;
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getAddressId() {
        return addressId;
    }
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public float getDelivery() {
        return delivery;
    }
    public void setDelivery(float delivery) {
        this.delivery = delivery;
    }
    public float getCost() {
        return cost;
    }
    public void setCost(float cost) {
        this.cost = cost;
    }
    public float getAllPrice() {
        return allPrice;
    }
    public void setAllPrice(float allPrice) {
        this.allPrice = allPrice;
    }
    public List<Product> getProductList() {
        return productList;
    }
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
    @Expose
    @SerializedName("delivery")
    private float delivery;
    @Expose
    @SerializedName("cost")
    private float cost;
    @Expose
    @SerializedName("allPrice")
    private float allPrice;
    @Expose
    @SerializedName("commodities")
    private List<Product> productList;
}
