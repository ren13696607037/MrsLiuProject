package com.techfly.liutaitai.util.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.techfly.liutaitai.util.AppManager;

public class BaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();
	}
}
