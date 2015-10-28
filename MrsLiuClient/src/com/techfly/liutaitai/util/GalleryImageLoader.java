package com.techfly.liutaitai.util;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:52
 */
public class GalleryImageLoader implements com.techfly.liutaitai.util.ImageLoader {
	private DisplayImageOptions mOptions;
	public GalleryImageLoader(DisplayImageOptions options){
		if(options == null){
			this.mOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(false)
            .cacheOnDisk(false)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
		}else{
			this.mOptions = options;
		}
	}

    @Override
    public void displayImage(final ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, mOptions);
    }
}
