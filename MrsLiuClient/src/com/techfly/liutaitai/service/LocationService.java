package com.techfly.liutaitai.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;

public class LocationService extends Service {
	private boolean threadDisable = false;
	// 定位相关
	private LocationClient mLocClient;
	public MyListenner myListener = new MyListenner();
	boolean isFirstLoc = true;// 是否首次定位
	private int mCount = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		// 定位初始化
		mLocClient = new LocationClient(LocationService.this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!threadDisable) {
					try {
						if(mCount != 0){
							Thread.sleep(1000*60*15);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 发送广播
					if (Constant.mLocation != null) {
						mCount++;
						AppLog.Loge("xll", "locationservice is in -=-=-"+Constant.mLocation.getLatitude() + "-=-=-"
								+ Constant.mLocation.getLongitude());
						Intent intent = new Intent();
						intent.putExtra("lat", Constant.mLocation == null ? ""
								: Constant.mLocation.getLatitude() + "");
						intent.putExtra("lon", Constant.mLocation == null ? ""
								: Constant.mLocation.getLongitude() + "");
						intent.setAction("com.techfly.liutaitai.LocationService");
						sendBroadcast(intent);
					}
				}
			}
		}).start();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public class MyListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			if(isFirstLoc){
				isFirstLoc = false;
			}
			Constant.mLocation=location;
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onDestroy() {
		threadDisable = true;
		// 退出时销毁定位
		mLocClient.stop();
		super.onDestroy();
	}

}
