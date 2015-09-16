package com.techfly.liutaitai.bizz.wenxin.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.wenxin.Keys;
import com.techfly.liutaitai.util.AppLog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "Fly";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_finish);
    	api = WXAPIFactory.createWXAPI(this, Keys.APP_ID);
        api.handleIntent(getIntent(), this);
        AppLog.Loge("Fly", "onCreate=====");
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	    AppLog.Loge("Fly", "req====="+req.openId);
        AppLog.Loge("Fly", "req====="+req.transaction);
	}

	@Override
	public void onResp(BaseResp resp) {
	    AppLog.Loge("Fly", "resp====="+resp.toString());
	    AppLog.Loge("Fly", "resp====="+resp.openId);
	    AppLog.Loge("Fly", "resp====="+resp.transaction);
	    AppLog.Loge("Fly", "resp====="+resp.toString());
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
			builder.show();
		}
	}
}