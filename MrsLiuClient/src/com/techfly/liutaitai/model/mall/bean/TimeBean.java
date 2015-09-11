package com.techfly.liutaitai.model.mall.bean;

public class TimeBean {
    private long timeMill;
    private String time;
    private boolean misSelect;
    public long getTimeMill() {
        return timeMill;
    }

    public void setTimeMill(long timeMill) {
        this.timeMill = timeMill;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMisSelect() {
        return misSelect;
    }

    public void setMisSelect(boolean misSelect) {
        this.misSelect = misSelect;
    }
}
