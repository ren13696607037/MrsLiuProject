package com.techfly.liutaitai.dao;

import java.util.List;

import android.content.Context;

public class DbAdapter implements IDbAdapter{
   
    protected Context mContext;
    
    public DbAdapter(Context context){
        if(mContext != null){
           mContext = context;
        }
    }
    
    @Override
    public List<Persistence> getDataList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addData(Persistence data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateData(Persistence data) {
        // TODO Auto-generated method stub 
        
    }

    @Override
    public void deleteData(String unique) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDataList(List<Persistence> data) {
        // TODO Auto-generated method stub
        
    }

}
