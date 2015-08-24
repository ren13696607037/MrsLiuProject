package com.techfly.liutaitai;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.util.Utility;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class YiHuiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
    /**
     * Intialize the request manager and the image cache
     */
    private void init() {
        RequestManager.init(this);
        initImageLoader();
        Utility.getScreenSize(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    private void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024) 
                .diskCacheFileCount(100)
                .tasksProcessingOrder(QueueProcessingType.LIFO) // Not
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }
    /**
     * Create the image cache. Uses Memory Cache by default. Change to Disk for
     * a Disk based LRU implementation.
     */
   
    
}
