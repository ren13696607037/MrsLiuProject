package com.hylsmart.yihui.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 显示相关的工具类
 * 
 * @author hubin
 * @email 7629654@qq.com
 * @date 2014-11-07
 */
public class ShowUtils {

	/** 将dp转换成px */
	public static int dip2px(Context context,float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/** px转成为dp */
	public static int px2dip(Context context,float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕宽高
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenSize(Context context) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
		return localDisplayMetrics;
	}

	/**
	 * 显示Toast
	 * 
	 * @param message
	 */
	private static Toast toast;

	public static void showToast(Context context,String message) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * 显示Toast
	 * 
	 * @param resID
	 */
	public static void showToast(Context context,int resID) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, resID, Toast.LENGTH_SHORT);
		toast.show();
	}

}
