package com.techfly.liutaitai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;

public final class FileTool {

	private static Context mContext;

	public final static byte[] loadFile(URI filename) throws IOException {
		File f = new File(filename);
		FileInputStream input = new FileInputStream(f);
		int len = input.available();
		byte[] buffer = new byte[len];
		input.read(buffer, 0, len);
		input.close();
		return buffer;
	}

	public static File getTempDir() {
		File dir = new File("mnt/sdcard/DCIM/Camera/");
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				dir = new File("mnt/sdcard2/DCIM/Camera/");
				if (!dir.exists()) {
					if (!dir.mkdir()) {
						dir = mContext.getFilesDir();
						Log.e("FILE", "is so bad!");
					}
				}
			}
		}

		return dir;
	}

	public static File getCamearDir() {
		File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM" + File.separator + "Camera");
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				dir = new File("/mnt/sdcard2" + File.separator + "DCIM" + File.separator + "Camera");
				if (!dir.exists()) {
					if (!dir.mkdir()) {
						dir = mContext.getFilesDir();
						Log.e("FILE", "is so bad!");
					}
				}
			}
		}
		AppLog.Logd("file", Environment.getExternalStorageDirectory().toString());
		AppLog.Logd("dir", dir.toString());
		return dir;
	}

	public static File getImageCacheDir(Context context) {
		File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + mContext.getPackageName() + File.separator + "cache");
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				dir = new File("/mnt/sdcard2" + File.separator + mContext.getPackageName() + File.separator + "cache");
				if (!dir.exists()) {
					if (!dir.mkdir()) {
						dir = context.getFilesDir();
						Log.e("FILE", "is so bad!");
					}
				}
			}
		}

		return dir;
	}

	public static void clearImageDir() {
		File dir = getImageCacheDir(mContext);
		for (File f : dir.listFiles()) {
			Log.w("FileTool", "clear temp file " + f.getPath());
			f.delete();
		}
	}

	public static File createTempFile(String prefix, String suffix) {
		// Log.e("FILE", Environment.getExternalStorageDirectory().toString());
		// File dir = getTempDir();

		File dir = getCamearDir();
		File f = null;

		f = new File(dir, prefix + suffix);
		return f;
	}

	public static String saveTempBitmap(Bitmap bitmap) {
		try {
			File file = FileTool.createTempFile(getNowTime(), ".jpg");
			FileOutputStream os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 75, os);
			// 缩放
			// Bitmap.createScaledBitmap(bitmap, 130, 160, true);
			os.flush();
			os.close();
			return file.getPath();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setContext(Context applicationContext) {
		mContext = applicationContext;
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return yyyyMMddHHmmss
	 */
	protected static String getNowTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
	}

	/**
	 * 检查sd是否存在
	 * 
	 * @return false:不存在，反之
	 */
	public static Boolean SDCardIsExsit() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			LogUtils.i("SD card is not avaiable/writeable right now.");
			return false;
		}

		return true;
	}

}
