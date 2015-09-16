package com.techfly.liutaitai.model.mall.bean;

public class Service {
    private String mId;
    
    private String mServiceType;// 服务类型
    
    private String mServiceTime;// 服务时间
    
    private String mServicePerson;// 服务师傅
    
    private String mServiceName;// 服务名称
    
    private String mServiceIcon;// 服务类型的图像

    private String mServicePrice;// 服务价格
   
    private String mServiceStatus;// 服务状态，进行中，已结束
    
    private String mCustomerName;// 服务客户的名称
    
    private String mCustomerPhone;// 服务客户的联系方式
    
    private String mCustomerAddress;// 服务客户的联系地址
    
    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmServiceType() {
        return mServiceType;
    }

    public void setmServiceType(String mServiceType) {
        this.mServiceType = mServiceType;
    }

    public String getmServiceTime() {
        return mServiceTime;
    }

    public void setmServiceTime(String mServiceTime) {
        this.mServiceTime = mServiceTime;
    }

    public String getmServicePerson() {
        return mServicePerson;
    }

    public void setmServicePerson(String mServicePerson) {
        this.mServicePerson = mServicePerson;
    }

    public String getmServiceName() {
        return mServiceName;
    }

    public void setmServiceName(String mServiceName) {
        this.mServiceName = mServiceName;
    }

    public String getmServiceIcon() {
        return mServiceIcon;
    }

    public void setmServiceIcon(String mServiceIcon) {
        this.mServiceIcon = mServiceIcon;
    }

    public String getmServicePrice() {
        return mServicePrice;
    }

    public void setmServicePrice(String mServicePrice) {
        this.mServicePrice = mServicePrice;
    }

    public String getmServiceStatus() {
        return mServiceStatus;
    }

    public void setmServiceStatus(String mServiceStatus) {
        this.mServiceStatus = mServiceStatus;
    }

    public String getmCustomerName() {
        return mCustomerName;
    }

    public void setmCustomerName(String mCustomerName) {
        this.mCustomerName = mCustomerName;
    }

    public String getmCustomerPhone() {
        return mCustomerPhone;
    }

    public void setmCustomerPhone(String mCustomerPhone) {
        this.mCustomerPhone = mCustomerPhone;
    }

    public String getmCustomerAddress() {
        return mCustomerAddress;
    }

    public void setmCustomerAddress(String mCustomerAddress) {
        this.mCustomerAddress = mCustomerAddress;
    }

    public String getmCash() {
        return mCash;
    }

    public void setmCash(String mCash) {
        this.mCash = mCash;
    }

    public String getmNum() {
		return mNum;
	}

	public void setmNum(String mNum) {
		this.mNum = mNum;
	}

	public String getmTechId() {
		return mTechId;
	}

	public void setmTechId(String mTechId) {
		this.mTechId = mTechId;
	}

	public String getmAliNo() {
		return mAliNo;
	}

	public void setmAliNo(String mAliNo) {
		this.mAliNo = mAliNo;
	}

	private String mCash;// 代金券的数额
    private String mNum;//预约编号
    private String mTechId;
    private String mAliNo;
}
