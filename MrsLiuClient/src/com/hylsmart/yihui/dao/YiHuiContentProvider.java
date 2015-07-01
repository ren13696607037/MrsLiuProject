package com.hylsmart.yihui.dao;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class YiHuiContentProvider extends ContentProvider{
    private static final String PACKAGENAME = "com.hylsmart.yihui.provider";
    private static final UriMatcher sUriMatcher ;
    private YiHuiSQLiteHelper mSQLHelper = null;
    /**
     * A projection map used to select columns from the database
     */
    /**
     * A projection map used to select columns from the database
     */
    private static HashMap<String,String> sHistoryProjectionMap;
    private static HashMap<String,String> sShopCarProjectionMap;
    private static final int SEARCH_HISTORY = 2;
    private static final int SHOP_CAR = 3;
    public static final Uri CONTENT_URI = Uri.parse("content://"+PACKAGENAME);
    private ContentResolver mContentResolver;
    //Creates and initializes the URI matcher
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DataBaseInfo.AUTHORITY, DataBaseInfo.History.TABLE_NAME, SEARCH_HISTORY);
        sUriMatcher.addURI(DataBaseInfo.AUTHORITY, DataBaseInfo.ShopCar.TABLE_NAME, SHOP_CAR);
        sHistoryProjectionMap = new HashMap<String,String>();
        sHistoryProjectionMap.put(DataBaseInfo.History.COLUMN_NAME_ID, DataBaseInfo.History.COLUMN_NAME_ID);
        sHistoryProjectionMap.put(DataBaseInfo.History.COLUMN_NAME_TYPE, DataBaseInfo.History.COLUMN_NAME_TYPE);
        sHistoryProjectionMap.put(DataBaseInfo.History.COLUMN_NAME_PRO_NAME, DataBaseInfo.History.COLUMN_NAME_PRO_NAME);
        sHistoryProjectionMap.put(DataBaseInfo.History.COLUMN_NAME_PRO_ID, DataBaseInfo.History.COLUMN_NAME_PRO_ID);
        
        sShopCarProjectionMap = new HashMap<String,String>();
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_ID, DataBaseInfo.ShopCar.COLUMN_NAME_ID);
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_AMOUNT, DataBaseInfo.ShopCar.COLUMN_NAME_PRO_AMOUNT);
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_IMAGE, DataBaseInfo.ShopCar.COLUMN_NAME_PRO_IMAGE);
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_MARKET_PRICE, DataBaseInfo.ShopCar.COLUMN_NAME_PRO_MARKET_PRICE);
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_PRICE, DataBaseInfo.ShopCar.COLUMN_NAME_PRO_PRICE);
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_NAME, DataBaseInfo.ShopCar.COLUMN_NAME_PRO_NAME);
        sShopCarProjectionMap.put(DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID, DataBaseInfo.ShopCar.COLUMN_NAME_PRO_ID);
    }
    
    
    @Override
    public boolean onCreate() {
        final Context context = getContext();
        mSQLHelper = YiHuiSQLiteHelper.getInstance(context);
        mContentResolver = context.getContentResolver();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //Choose the projection and adjust the "where" clause based on URI pattern-matching.
        switch (sUriMatcher.match(uri)) {
            // If the incoming URI is for allRes, chooses the allRes projection
            case SEARCH_HISTORY:
            	setQueryCondition(false, qb, DataBaseInfo.History.TABLE_NAME, sHistoryProjectionMap, null);
            	break;
            case SHOP_CAR:
                setQueryCondition(false, qb, DataBaseInfo.ShopCar.TABLE_NAME, sShopCarProjectionMap, null);
               break;
            default:
                // If the URI doesn't match any of the known patterns, throw an exception.
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Opens the database object in "read" mode, since no writes need to be done.
        SQLiteDatabase db = mSQLHelper.getReadableDatabase();

        /*
         * Performs the query. If no problems occur trying to read the database, then a Cursor
         * object is returned; otherwise, the cursor variable contains null. If no records were
         * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
         */
        Cursor c = qb.query(
            db,            // The database to query
            projection,    // The columns to return from the query
            selection,     // The columns for the where clause
            selectionArgs, // The values for the where clause
            null,          // don't group the rows
            null,          // don't filter by row groups
            sortOrder        // The sort order
        );

        // Tells the Cursor what URI to watch, so it knows when its source data changes
        c.setNotificationUri(mContentResolver, uri);
        return c;
    }

    private void setQueryCondition(boolean type,SQLiteQueryBuilder sqb,String tableName,HashMap<String, String> map,String whereStr){
        sqb.setTables(tableName);
        sqb.setProjectionMap(map);
        if(type){
            sqb.appendWhere(whereStr);
        } 
    }
    
    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.paradise.appInfos";
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        int matcher = sUriMatcher.match(uri);
        // Validates the incoming URI. Only the full provider URI is allowed for inserts.
        switch (matcher) {
            case SEARCH_HISTORY:
            	return insertHistory(uri , initialValues);
            case SHOP_CAR:
                return insertShopCar(uri , initialValues);
            default :
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
    private Uri insertShopCar(Uri uri,ContentValues initialValues ){
        ContentValues values;
        if(initialValues != null){
            values = new ContentValues(initialValues);
        }else{
            values = new ContentValues();
        }
        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        long rowId = 0;
        rowId = db.insert(DataBaseInfo.ShopCar.TABLE_NAME, null, values);
        if(rowId > 0){
            Uri noteUri = ContentUris.withAppendedId(DataBaseInfo.ShopCar.CONTENT_ID_URI_BASE, rowId);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }
    private Uri insertHistory(Uri uri,ContentValues initialValues ){
    	ContentValues values;
    	if(initialValues != null){
    		values = new ContentValues(initialValues);
    	}else{
    		values = new ContentValues();
    	}
    	SQLiteDatabase db = mSQLHelper.getWritableDatabase();
    	long rowId = 0;
    	rowId = db.insert(DataBaseInfo.History.TABLE_NAME, null, values);
    	if(rowId > 0){
    		Uri noteUri = ContentUris.withAppendedId(DataBaseInfo.History.CONTENT_ID_URI_BASE, rowId);
    		return noteUri;
    	}
    	throw new SQLException("Failed to insert row into " + uri);
    }
   
    
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
     // Opens the database object in "write" mode.
        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        int count;
        // Does the delete based on the incoming URI pattern.
        switch (sUriMatcher.match(uri)) {

            // If the incoming pattern matches the general pattern for cities, does a delete
            // based on the incoming "where" columns and arguments.
            case SEARCH_HISTORY:
            	 count = deleteTable(db, DataBaseInfo.History.TABLE_NAME,  where, whereArgs);
            case SHOP_CAR:
                count = deleteTable(db, DataBaseInfo.ShopCar.TABLE_NAME,  where, whereArgs);
            	 break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // mContentResolver.notifyChange(uri, null);

        // Returns the number of rows deleted.
        return count;
    }
        
    private int deleteTable(SQLiteDatabase db,String tableName,String where,String[] whereArgs){
        return db.delete(tableName, where, whereArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mSQLHelper.getWritableDatabase();
        int count =0;
        // Does the update based on the incoming URI pattern
        switch (sUriMatcher.match(uri)) {
            // If the incoming URI matches the general notes pattern, does the update based on
            // the incoming data.
            case SEARCH_HISTORY:
            	count = updateTable(db, DataBaseInfo.History.TABLE_NAME, values, selection, selectionArgs);
                 mContentResolver.notifyChange(uri, null);
                 return 0;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
      
    }

    private int updateTable(SQLiteDatabase db,String table,ContentValues values, String selection,
            String[] selectionArgs){
        return db.update(
                table, // The database table name.
                values,                   // A map of column names and new values to use.
                selection,                    // The where clause column names.
                selectionArgs                 // The where clause column values to select on.
            );
    }

}
