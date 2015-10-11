package com.techfly.liutaitai.model.mall.bean;

public class TimePoints {
    private long mTimeMills;
    @Override
    public boolean equals(Object o) {
        TimePoints p = (TimePoints) o;
        if(p.getmTimeMills()==mTimeMills){
            return true;
        }
        return false;
    }

    private boolean mIsWholeHours;
    private boolean mBeforeHalfJHours;//true 表示前半个小时 被预约，false 表示 后半个小时被预约

    public long getmTimeMills() {
        return mTimeMills;
    }

    public void setmTimeMills(long mTimeMills) {
        this.mTimeMills = mTimeMills;
    }

    public boolean ismIsWholeHours() {
        return mIsWholeHours;
    }

    public void setmIsWholeHours(boolean mIsWholeHours) {
        this.mIsWholeHours = mIsWholeHours;
    }

    public boolean ismBeforeHalfJHours() {
        return mBeforeHalfJHours;
    }

    public void setmBeforeHalfJHours(boolean mBeforeHalfJHours) {
        this.mBeforeHalfJHours = mBeforeHalfJHours;
    }
}
