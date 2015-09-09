package com.techfly.liutaitai.model.mall.bean;

import com.techfly.liutaitai.model.pcenter.bean.AddressManage;

public class ConfirmOrder {
    private float mDeliverFee;
    private boolean mIsUseVoucher;
    private String addressId;
    private String address;
    private String phone;
    private  String name;
   private AddressManage mAddressManage;
    public float getmDeliverFee() {
        return mDeliverFee;
    }

    public void setmDeliverFee(float mDeliverFee) {
        this.mDeliverFee = mDeliverFee;
    }

    public boolean ismIsUseVoucher() {
        return mIsUseVoucher;
    }

    public void setmIsUseVoucher(boolean mIsUseVoucher) {
        this.mIsUseVoucher = mIsUseVoucher;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressManage getmAddressManage() {
        return mAddressManage;
    }

    public void setmAddressManage(AddressManage mAddressManage) {
        this.mAddressManage = mAddressManage;
    }
}
