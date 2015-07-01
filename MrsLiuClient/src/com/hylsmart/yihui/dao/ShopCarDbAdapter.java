package com.hylsmart.yihui.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.text.TextUtils;

import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Lists;

public class ShopCarDbAdapter extends DbAdapter{
private ContentResolver mResolver;
    
    public ShopCarDbAdapter(Context context) {
        super(context);
        mResolver = context.getContentResolver();
    }

    @Override
    public void deleteData() {
        mResolver.delete(DataBaseInfo.ShopCar.CONTENT_URI, null, null);
        AppLog.Loge("delete the download item info");
    }

    @Override
    public List<Persistence> getDataList() {
        ArrayList<Persistence> mlist = new ArrayList<Persistence>();
        Cursor cursor = mResolver.query(DataBaseInfo.ShopCar.CONTENT_URI, null, null, null, "id DESC");
       try {
        if(cursor != null && cursor.moveToFirst()) {
            do{
                Product product = new Product();
                String id = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID));
                String name = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_NAME));
                String price  = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_PRICE));
                String amount = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_AMOUNT));
                String icon = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_IMAGE));
                if(!TextUtils.isEmpty(name)){
                    product.setmAmount(Integer.parseInt(amount));
                    product.setmImg(icon);
                    product.setmName(name);
                    product.setmId(id);
                    product.setmPrice(Float.parseFloat(price));
                    mlist.add(product);
                }
             }while(cursor.moveToNext());
        }
       } catch (Exception e) {
           AppLog.Loge("Query database Exception===="+e.getMessage());
       }finally{
           if(cursor!=null){
               cursor.close();
               cursor =null;
           }
       
       }
        return mlist;
    }
    
    public List<Persistence> getDataList(String selectiion, String[] selectionArags) {
        ArrayList<Persistence> mlist = new ArrayList<Persistence>();
        Cursor cursor = mResolver.query(DataBaseInfo.ShopCar.CONTENT_URI, null, selectiion, selectionArags, "id DESC");
       try {
        if(cursor != null && cursor.moveToFirst()) {
            do{
                Product product = new Product();
                String id = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID));
                String name = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_NAME));
                String price  = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_PRICE));
                String amount = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_AMOUNT));
                String icon = cursor.getString(cursor.getColumnIndex(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_IMAGE));
                if(!TextUtils.isEmpty(name)){
                    product.setmAmount(Integer.parseInt(amount));
                    product.setmImg(icon);
                    product.setmName(name);
                    product.setmId(id);
                    product.setmPrice(Float.parseFloat(price));
                    mlist.add(product);
                }
             }while(cursor.moveToNext());
        }
       } catch (Exception e) {
           AppLog.Loge("Query database Exception===="+e.getMessage());
       }finally{
           if(cursor!=null){
               cursor.close();
               cursor =null;
           }
       
       }
        return mlist;
    }

    @Override
    public void addData(Persistence data) {
        ArrayList<ContentProviderOperation> cpoList = Lists.newArrayList();
        Product product = (Product) data;
        ContentValues values = createProductContentValue(product);
        cpoList.add(ContentProviderOperation.newInsert(DataBaseInfo.ShopCar.CONTENT_URI).withValues(values).build());
       
        try {
            mResolver.applyBatch(DataBaseInfo.AUTHORITY, cpoList);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }finally{
            cpoList.clear();
        }
    }
    
    @Override
    public void addDataList(List<Persistence> data) {
        ArrayList<ContentProviderOperation> cpoList = Lists.newArrayList();
        for(int i =0;i<data.size();i++){
            Product product = (Product) data.get(i);
            ContentValues values = createProductContentValue(product);
            cpoList.add(ContentProviderOperation.newInsert(DataBaseInfo.ShopCar.CONTENT_URI).withValues(values).build());
        }
        try {
            mResolver.applyBatch(DataBaseInfo.AUTHORITY, cpoList);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }finally{
            cpoList.clear();
        }
    }

    private ContentValues createProductContentValue(Product item) {
        ContentValues values = new ContentValues();
        values.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_NAME, item.getmName());
        values.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID, item.getmId());
        values.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_AMOUNT, item.getmAmount()+"");
        values.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_IMAGE, item.getmImg());
        values.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_PRICE, item.getmPrice());
        return values;
    }

    @Override
    public void deleteData(String unique) {
        String where = DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID+ " = " + unique;
        mResolver.delete(DataBaseInfo.ShopCar.CONTENT_URI, where, null);
        AppLog.Loge("delete the download item info");
    }
}
