package com.techfly.liutaitai.dao;
import com.techfly.liutaitai.util.AppLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class YiHuiSQLiteHelper extends SQLiteOpenHelper {
    private static YiHuiSQLiteHelper sSingleton = null;
    private static final String DATABASE_NAME = "yihui.db";
    private static final int DATABASE_VERSION = 302;
    public YiHuiSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static synchronized YiHuiSQLiteHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new YiHuiSQLiteHelper(context);
        }
        return sSingleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        bootStrapDB(db);
    }

    private void bootStrapDB(SQLiteDatabase db) {
        createNecessaryTables(db);
    }
    
    private void createNecessaryTables(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + DataBaseInfo.History.TABLE_NAME + "(" +
        		DataBaseInfo.History.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  ,"+
        		DataBaseInfo.History.COLUMN_NAME_TYPE +" INTEGER ," +
        	    DataBaseInfo.History.COLUMN_NAME_PRO_ID +" TEXT ," +
                DataBaseInfo.History.COLUMN_NAME_PRO_NAME+ " TEXT "+ ")");
        		createShopCarTb(db);
    }
    
  
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppLog.Logd("Fly", "onUpgrade");
        if(newVersion>oldVersion && newVersion==302){
            createShopCarTb(db);
        }
             
    }
    
    private void createShopCarTb(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + DataBaseInfo.ShopCar.TABLE_NAME + "(" +
                DataBaseInfo.ShopCar.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  ,"+
                DataBaseInfo.ShopCar.COLUMN_NAME_TYPE +" INTEGER ," +
                DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID +" TEXT ," +
                DataBaseInfo.ShopCar.COLUMN_NAME_PRO_IMAGE +" TEXT ," +
                DataBaseInfo.ShopCar.COLUMN_NAME_PRO_MARKET_PRICE +" TEXT ," +
                DataBaseInfo.ShopCar.COLUMN_NAME_PRO_PRICE +" TEXT ," +
                DataBaseInfo.ShopCar.COLUMN_NAME_PRO_AMOUNT +" TEXT ," +
                DataBaseInfo.ShopCar.COLUMN_NAME_PRO_NAME+ " TEXT "+ ")");
    }

}
