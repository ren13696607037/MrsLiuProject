package com.hylsmart.yihui.dao;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBaseInfo {

    private static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.hylsmart.yihui.provider";
    
    

    public static final class History implements BaseColumns{
    	
    	public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/history");
    	private static final String PATH_ALLRESINFO_ID = "/historys/";
    	public static final Uri CONTENT_ID_URI_BASE
         = Uri.parse(SCHEME + AUTHORITY + PATH_ALLRESINFO_ID);
    	
    	public static final String TABLE_NAME = "history";
    	
    	public static final String COLUMN_NAME_ID="id";
    	public static final String COLUMN_NAME_TYPE="type";// 热门搜索 ，最近搜索
    	public static final String COLUMN_NAME_PRO_NAME="name";
    	   public static final String COLUMN_NAME_PRO_ID="pro_id";
    	 
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA1 = "data1";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA2 = "data2";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA3 = "data3";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA4 = "data4";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA5 = "data5";
    }
public static final class ShopCar implements BaseColumns{
        
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/shopcar");
        private static final String PATH_ALLRESINFO_ID = "/shopcar/";
        public static final Uri CONTENT_ID_URI_BASE
         = Uri.parse(SCHEME + AUTHORITY + PATH_ALLRESINFO_ID);
        
        public static final String TABLE_NAME = "shopcar";
        
        public static final String COLUMN_NAME_ID="id";
        public static final String COLUMN_NAME_TYPE="type";// 热门搜索 ，最近搜索
        public static final String COLUMN_NAME_PRO_NAME="name";// 商品名字
        public static final String COLUMN_NAME_PRO_ID="pro_id";// 商品ID
        public static final String COLUMN_NAME_PRO_AMOUNT="count";// 商品数量
        public static final String COLUMN_NAME_PRO_IMAGE="icon";// 商品ICON
        public static final String COLUMN_NAME_PRO_PRICE="price"; // 商品价格
        public static final String COLUMN_NAME_PRO_MARKET_PRICE="market_price"; // 商品市场价格
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA1 = "data1";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA2 = "data2";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA3 = "data3";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA4 = "data4";
        
        /**
         * Column name to use in the future 
         * <P>Type: TEXT </P>
         */
        public static final String COLUMN_NAME_DATA5 = "data5";
    }
}
