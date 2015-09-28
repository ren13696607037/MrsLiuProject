package com.techfly.liutaitai.scale.resource;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.scale.PerfUtil;
import com.techfly.liutaitai.scale.PerfUtil.PerfLevel;
import com.techfly.liutaitai.util.AppLog;

public class HttpImageResource extends BaseImageResource<String> implements Parcelable {

    private static final long serialVersionUID = -4950696682058723337L;
    private DisplayImageOptions mScaleOptions;
    private int mProgress;

    public HttpImageResource(List<String> urls) {
        mResources = urls;
        Config config;
        if (PerfUtil.getInstance().getPerfLevel() == PerfLevel.HIGH) {
            config = Config.ARGB_8888;
        } else {
            config = Config.RGB_565;
        }
        /** 图片不缩放，直接显示，要限制最大值 */
        mScaleOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.NONE).bitmapConfig(config)
                .showImageForEmptyUri(R.drawable.service_details_default)
                .showImageOnFail(R.drawable.service_details_default)
                /*.cacheInMemory(true).cacheOnDisk(true)*/
                /*.showImageOnLoading(R.drawable.image_error)*/
                /*.maxImageSize(new ImageSize(1920, Integer.MAX_VALUE))*/
                .cacheOnDisc().displayer(new SimpleBitmapDisplayer()).build();
    }

    @Override
    public synchronized void displayImage(Context context, ImageView view, int index) {
        if (index < this.size()) {
            ImageLoader.getInstance().displayImage(this.get(index), view,
                    mScaleOptions, new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                View view, Bitmap loadedImage) {
                            view.setBackgroundColor(Color.BLACK);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                View view) {
                        }
                    });
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void displayImage(Context context, ImageView view, int index, ProgressBar progressBar, final Handler handler,final LinearLayout layout) {
        if (index < this.size()) {
            showProgress(progressBar, handler);
            ImageLoader.getInstance().displayImage(this.get(index), view,
                    mScaleOptions, new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                FailReason failReason) {
                            AppLog.Loge("is load fail");
                            handler.sendEmptyMessage(2);
                            layout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                View view, Bitmap loadedImage) {
                            AppLog.Loge("is load complete");
                            handler.sendEmptyMessage(2);
                            layout.setVisibility(View.GONE);
                            view.setBackgroundColor(Color.BLACK);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                View view) {
                        }
                    });
        }
    }
    
    private void showProgress(ProgressBar mProgressBar,final Handler handler){
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        Thread thread=new Thread(new Runnable() {
            
            @Override
            public void run() {
                for(int i=0;i<50;i++){
                    try {
                        mProgress=i;
                        Thread.sleep(1000);
                        if(i==49){
                            Message msg=new Message();
                            msg.what=2;
                            handler.sendMessage(msg);
                        }else{
                            Message msg=new Message();
                            msg.what=1;
                            handler.sendMessage(msg);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

}
