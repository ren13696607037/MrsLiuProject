package com.techfly.liutaitai.util;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.techfly.liutaitai.R;

/**
 * ImageCacheLoader工具�?
 * 
 * @auther lxj
 * @version 1.0
 */
public class ImageCacheUtil {

	/**
	 * 必须在application create的时候调�? *
	 * 
	 * @param context
	 */

	public static void init(Context context) {
		ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().threadPoolSize(5)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)) // Not
																				// necessary
				.build();
		ImageLoader.getInstance().init(config);
	}

	// options
	public interface OPTIONS {
		public DisplayImageOptions default_options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().displayer(new BitmapDisplayer() {
//					@Override
//					public Bitmap display(Bitmap arg0, ImageView arg1) {
//						arg1.setImageBitmap(arg0);
//						return arg0;
//					}

					@Override
					public void display(Bitmap arg0, ImageAware arg1,
							LoadedFrom arg2) {
						// TODO Auto-generated method stub
						arg1.setImageBitmap(arg0);
					}
				}).build();;

	}
	
	/**
	 * 初始化图片下载器，图片缓存地址<i>("/Android/data/[app_package_name]/cache/dirName")</i>
	 */
	public static ImageLoader initImageLoader(Context context,
			ImageLoader imageLoader, String dirName) {
		imageLoader = ImageLoader.getInstance();
		if (imageLoader.isInited()) {
			// 重新初始化ImageLoader时,需要释放资源.
			imageLoader.destroy();
		}
		imageLoader.init(initImageLoaderConfig(context, dirName));
		return imageLoader;
	}

	/**
	 * 配置图片下载器
	 * 
	 * @param dirName
	 *            文件名
	 */
	private static ImageLoaderConfiguration initImageLoaderConfig(
			Context context, String dirName) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3).memoryCacheSize(getMemoryCacheSize(context))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCache(new UnlimitedDiscCache(new File(dirName)))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		return config;
	}

	private static int getMemoryCacheSize(Context context) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
															// limit
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}
		return memoryCacheSize;
	}

}
