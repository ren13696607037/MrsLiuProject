package com.techfly.liutaitai.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.update.UrlEncode;

public class Utility {
	private static Dialog mDialog;

	/**
	 * getScreen size
	 * 
	 * @param activity
	 */
	public static void getScreenSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(dm);
		Constant.SCREEN_WIDTH = dm.widthPixels;
		Constant.SCREEN_HEIGHT = dm.heightPixels;
		Constant.SCREEN_DENSITY = dm.density;
	}

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public static boolean isInternetWorkValid(Context context) {
		if (context != null) {
			ConnectivityManager conMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conMgr.getActiveNetworkInfo() != null
					&& conMgr.getActiveNetworkInfo().isAvailable()
					&& conMgr.getActiveNetworkInfo().isConnected()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	// 判断是否为手机号码
	public static boolean isPhone(String strPhone) {
		String str = "^1[3|4|5|7|8][0-9]\\d{8}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(strPhone);
		return m.matches();
	}

	// 判断是否为银行卡号
	public static boolean isCard(String card) {
		String str = "[0-9]{19}";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(card);
		return m.matches();
	}

	public static boolean isValidUrl(String url) {
		if (!TextUtils.isEmpty(url) && !url.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static boolean isSDCardExist(Context context) {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String readTextFile(InputStream inputStream) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String str = null;
		byte buf[] = new byte[1024];

		int len;

		try {

			while ((len = inputStream.read(buf)) != -1) {

				outputStream.write(buf, 0, len);

			}
		} catch (IOException e) {
			AppLog.Loge("Fly", "IOException" + e.getMessage());
		} finally {
			try {
				if (null != outputStream) {
					str = outputStream.toString();
					outputStream.close();
					outputStream = null;
				}
				if (null != inputStream) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				AppLog.Loge("Fly", "IOException" + e.getMessage());
			}

		}
		return str;

	}

	public static long getContentLength(
			final Map<String, List<String>> resProperty) {
		if (null != resProperty) {
			List<String> contentLengthProperty = resProperty
					.get("Content-Length");
			long resFileSize = 0;
			if (contentLengthProperty != null
					&& contentLengthProperty.size() > 0) {
				resFileSize = Long.parseLong(contentLengthProperty.get(0));
				return resFileSize;
			}
		}
		return -1;
	}

	public static Map<String, List<String>> executeHttpHeadRequest(
			final String urlStr) {
		try {
			String newUrl = UrlEncode.encodeUTF8(urlStr);
			URL url = new URL(newUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15 * 1000);
			conn.setRequestMethod("HEAD");
			conn.connect();
			Map<String, List<String>> headers = conn.getHeaderFields();
			conn.disconnect();
			return headers;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public static void resizeViewHeight(final View calculatedview, final View
	 * ResizedView, final float scale){
	 * ResizedView.getViewTreeObserver().addOnGlobalLayoutListener(new
	 * OnGlobalLayoutListener() {
	 * 
	 * @Override public void onGlobalLayout() { int width =
	 * calculatedview.getWidth(); int height = (int)(width*scale);
	 * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
	 * height); ResizedView.setLayoutParams(params);
	 * ResizedView.getViewTreeObserver().removeGlobalOnLayoutListener(this); }
	 * }); }
	 */

	public static void resizeViewHeight(final View calculatedview,
			final View resizedView, final float scale) {
		resizedView.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						int width = resizedView.getMeasuredWidth();
						int height = (int) (width * scale);
						AppLog.Logd("width:" + width + ",height:" + height);
						ViewGroup.LayoutParams params = resizedView
								.getLayoutParams();
						resizedView.setLayoutParams(params);
						resizedView.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						return true;
					}
				});
	}

	public static String List2String(ArrayList<String> list) {
		String string = "";
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				string += list.get(i) + ",";
			}
		}
		return string;
	}

	public static ArrayList<String> String2List(String string) {
		ArrayList<String> list = new ArrayList<String>();
		String[] strings = string.split(",");
		if (strings.length > 0) {
			int size = strings.length;
			for (int i = 0; i < size; i++) {
				if (!"".equals(strings[i])) {
					list.add(strings[i]);
				}
			}
		}
		return list;
	}

	public static void getAllList(ArrayList<?> dataList, ArrayList<?> list) {
		if (dataList != null && list != null) {
			for (int i = 0; i < dataList.size(); i++) {
				if (list.contains(dataList.get(i))) {
					dataList.remove(i);
				}
			}
		}
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		if (is == null) {
			return "";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void setText(Context context, TextView textView,
			String string, int format) {
		if (string != null && !"null".equals(string)
				&& !TextUtils.isEmpty(string)) {
			if (format != -1) {
				textView.setText(context.getString(format, string));
			} else {
				textView.setText(string);
			}
		} else {
			if (format != -1) {
				textView.setText(context.getString(format,
						context.getString(R.string.unknown)));
			} else {
				textView.setText(R.string.unknown);
			}
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount()));
		listView.setLayoutParams(params);
	}

	public static SpannableString setText(Context context, String text,
			int start, int end) {
		SpannableString ss = new SpannableString(text);
		// 用颜色标记文本
		ss.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.color_blue)), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	public static final String getSimpDate() {
		String curTime = "";
		SimpleDateFormat formatter;
		java.util.Date currentDate = new java.util.Date();
		formatter = new SimpleDateFormat("yyyy-M-d H:m:s");
		currentDate = Calendar.getInstance().getTime();
		curTime = formatter.format(currentDate);
		return curTime;
	}

	public static int getSeconds(long seconds) {
		int secondNum = (int) (seconds % 60);
		return secondNum;
	}

	public static int getHours(long seconds) {
		int hourNum = (int) (seconds / 3600);
		return hourNum;
	}

	public static int getMinutes(long seconds) {
		int minuteNum = (int) ((seconds / 60) % 60);
		return minuteNum;
	}

	public static void call(final Context mActivity, final String phone) {
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(mActivity, R.string.illegal_phone,
					Toast.LENGTH_SHORT).show();
			return;
		}
		mDialog = new Dialog(mActivity, R.style.MyDialog);
		mDialog = AlertDialogUtils.displayMyAlertChoice(mActivity, "提示",
				"确认拨打电话  " + phone + "  ？", "确认", new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phone));
						mActivity.startActivity(intent);
						mDialog.dismiss();
					}
				}, "取消", null);
		mDialog.show();
	}

	public static long Date2Millis(String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}
	public static boolean getActivityName(Activity activity){
    	String name=null;
    	ActivityManager manager = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE) ;
	     List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
	     if(runningTaskInfos!=null){
	    	 name=runningTaskInfos.get(0).baseActivity.toString();
	    	 AppLog.Loge("xll", name+"-=-=-="+activity.getLocalClassName());
	    	 if(name.contains(activity.getLocalClassName())){
	    		 return true;
	    	 }
	     }
	     return false;
    }
	
	public static void go2Activity(Activity activity,Class<?> activity2,Bundle bundle){
    	Intent intent=null;
    	intent=new Intent(activity,activity2);
    	if(bundle!=null){
    		intent.putExtras(bundle);
    	}
    	activity.startActivity(intent);
    	activity.finish();
    }
	
	/**
	 * 小数点后两位
	 * 
	 * @param d
	 * @return
	 */

	public static String dot2(double d) {
		DecimalFormat df = new DecimalFormat("#.00");
		String s = df.format(d);
		if (s.startsWith(".")) {
			s = "0" + s;
		}
		return s;

	}

}
