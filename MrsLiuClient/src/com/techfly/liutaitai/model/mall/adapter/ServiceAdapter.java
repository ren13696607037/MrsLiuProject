package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.techfly.liutaitai.model.mall.bean.Service;

public class ServiceAdapter extends BaseAdapter{
    private List<Service> mList;
    
    public ServiceAdapter(Context context,List<Service> list){
      mList = list;   
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Service> getmList() {
        return mList;
    }

    public void setmList(List<Service> mList) {
        this.mList = mList;
    }

}
