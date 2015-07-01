package com.techfly.liutaitai.dao;

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

import com.techfly.liutaitai.model.home.bean.SearchHistory;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Lists;

public class SearchHistoryDbAdapter extends DbAdapter {

	private ContentResolver mResolver;
	
	public SearchHistoryDbAdapter(Context context) {
		super(context);
		mResolver = context.getContentResolver();
	}

	@Override
	public List<Persistence> getDataList() {
		ArrayList<Persistence> mlist = new ArrayList<Persistence>();
		Cursor cursor = mResolver.query(DataBaseInfo.History.CONTENT_URI, null, null, null, "id DESC");
	   try {
		if(cursor != null && cursor.moveToFirst()) {
			do{
				SearchHistory history = new SearchHistory();
				int id = cursor.getInt(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_ID));
				//int type = cursor.getInt(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_TYPE));
				String name = cursor.getString(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_PRO_NAME));
			    //String proId = cursor.getString(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_PRO_ID));
				if(!TextUtils.isEmpty(name)){
					history.setId(id);
					//history.setType(type);
					history.setName(name);
					//history.setProId(proId);
					mlist.add(history);
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
		Cursor cursor = mResolver.query(DataBaseInfo.History.CONTENT_URI, null, selectiion, selectionArags, "id DESC");
	   try {
		if(cursor != null && cursor.moveToFirst()) {
			do{
				SearchHistory history = new SearchHistory();
				int id = cursor.getInt(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_ID));
				//int type = cursor.getInt(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_TYPE));
				String name = cursor.getString(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_PRO_NAME));
		        //String proId = cursor.getString(cursor.getColumnIndex(DataBaseInfo.History.COLUMN_NAME_PRO_ID));
				if(!TextUtils.isEmpty(name)){
					history.setId(id);
					//history.setType(type);
					history.setName(name);
					//history.setProId(proId);
					mlist.add(history);
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
		SearchHistory history = (SearchHistory) data;
		ContentValues values = createHistoryContentValue(history);
		cpoList.add(ContentProviderOperation.newInsert(DataBaseInfo.History.CONTENT_URI).withValues(values).build());
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
	
	private ContentValues createHistoryContentValue(SearchHistory item) {
        ContentValues values = new ContentValues();
        //values.put(DataBaseInfo.History.COLUMN_NAME_TYPE, item.getType());
        values.put(DataBaseInfo.History.COLUMN_NAME_PRO_NAME, item.getName());
        //values.put(DataBaseInfo.History.COLUMN_NAME_ID, item.getId());
        return values;
    }

	@Override
	public void deleteData(String unique) {
		String where = DataBaseInfo.History.COLUMN_NAME_ID+ " = " + unique;
		mResolver.delete(DataBaseInfo.History.CONTENT_URI, where, null);
		AppLog.Loge("delete the download item info");
	}

}
