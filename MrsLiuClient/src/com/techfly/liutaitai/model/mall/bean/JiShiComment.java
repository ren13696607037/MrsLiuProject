package com.techfly.liutaitai.model.mall.bean;

import java.util.ArrayList;

public class JiShiComment {
 private float avearge;
 
 private int count;
 
 private ArrayList<Comments> list ;

public float getAvearge() {
    return avearge;
}

public void setAvearge(float avearge) {
    this.avearge = avearge;
}

public int getCount() {
    return count;
}

public void setCount(int count) {
    this.count = count;
}

public ArrayList<Comments> getList() {
    return list;
}

public void setList(ArrayList<Comments> list) {
    this.list = list;
}
}
