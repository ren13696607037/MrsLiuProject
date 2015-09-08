package com.techfly.liutaitai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class SplashActivity extends BaseActivity {
    private String TAG = SplashActivity.class.getSimpleName();
    private static final long DELAY = 1000;
    private static final int MSG_HOME=0x101;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_HOME:
            	handleHomeSwitch();
            	break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.Loge(TAG, "onCreate");
        setContentView(R.layout.activity_splash);
        Utility.getScreenSize(this);
        mHandler.sendEmptyMessageDelayed(MSG_HOME, DELAY);
    }
    private synchronized void handleHomeSwitch() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(MSG_HOME);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
