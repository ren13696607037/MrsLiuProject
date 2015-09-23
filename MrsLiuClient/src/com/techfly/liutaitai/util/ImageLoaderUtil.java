package com.techfly.liutaitai.util;

import android.graphics.Bitmap.Config;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.techfly.liutaitai.R;

public class ImageLoaderUtil {

	 public static DisplayImageOptions mUserIconLoaderOptions = new
	  DisplayImageOptions
	 .Builder().showImageForEmptyUri(R.drawable.head_default)
	 .bitmapConfig(Config
	 .RGB_565).showImageOnFail(R.drawable.head_default).cacheInMemory
	  (true).cacheOnDisk(true)
	  .showImageOnLoading(R.drawable.head_default).displayer(new
	  SimpleBitmapDisplayer()).build();
	 

	public static DisplayImageOptions mHallIconLoaderOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_hall_no_url)
			.showImageOnLoading(R.drawable.ic_hall_no_url)
			.bitmapConfig(Config.RGB_565)
			.showImageOnFail(R.drawable.ic_hall_no_url).cacheInMemory(true)
			.cacheOnDisk(true).displayer(new SimpleBitmapDisplayer()).build();
	

    public static DisplayImageOptions mGridIconLoaderOptions = new DisplayImageOptions.Builder()
    .showImageForEmptyUri(R.drawable.ic_hall_no_url)
    .showImageOnLoading(R.drawable.ic_hall_no_url)
    .bitmapConfig(Config.RGB_565)
    .showImageOnFail(R.drawable.ic_hall_no_url).cacheInMemory(true)
    .cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(10)).build();
	
	public static DisplayImageOptions mOrderServiceIconLoaderOptions = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.ic_hall_no_url)
	.showImageOnLoading(R.drawable.ic_hall_no_url)
	.bitmapConfig(Config.RGB_565)
	.showImageOnFail(R.drawable.ic_hall_no_url).cacheInMemory(true)
	.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(10)).build();
	
     public static DisplayImageOptions mBannerLoaderOptions = new DisplayImageOptions.Builder()
       .showImageForEmptyUri(R.drawable.banner_default)
       .showImageOnLoading(R.drawable.banner_default)
       .bitmapConfig(Config.RGB_565)
       .showImageOnFail(R.drawable.banner_default).cacheInMemory(true)
       .cacheOnDisk(true).displayer(new SimpleBitmapDisplayer()).build();
     
     public static DisplayImageOptions mDetailsLoaderOptions = new DisplayImageOptions.Builder()
     .showImageForEmptyUri(R.drawable.service_details_default)
     .showImageOnLoading(R.drawable.service_details_default)
     .bitmapConfig(Config.RGB_565)
     .showImageOnFail(R.drawable.service_details_default).cacheInMemory(true)
     .cacheOnDisk(true).displayer(new SimpleBitmapDisplayer()).build();

}
