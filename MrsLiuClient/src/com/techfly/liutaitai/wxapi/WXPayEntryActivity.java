package com.techfly.liutaitai.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.wenxin.Keys;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.AppManager;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SmartToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "Fly";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_order_finish);

		api = WXAPIFactory.createWXAPI(this, Keys.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX) {
			return;
		}
		AppLog.Logd("Shi", "resp.errCode==" + resp.errCode);
		if (resp.errCode == 0) {
			if (AppManager.getAppManager().currentActivity() instanceof TakingOrderActivity) {
				TakingOrderActivity ac = (TakingOrderActivity) AppManager
						.getAppManager().currentActivity();
				ac.showOrderFinishFragment(ac.getBundleInfo());
				this.finish();
			} else if (AppManager.getAppManager().currentActivity() instanceof ServiceOrderActivity) {
				ServiceOrderActivity ac = (ServiceOrderActivity) AppManager
						.getAppManager().currentActivity();
				ac.showOrderFinishFragment(ac.getBundleInfo());
				this.finish();
			} else if (AppManager.getAppManager().currentActivity() instanceof RechargeActivity) {
				Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
				this.finish();
				AppManager.getAppManager().currentActivity()
						.setResult(Constant.BALANCE_SUCCESS);
				AppManager.getAppManager().currentActivity().finish();
			}

		}else if (resp.errCode == -1) {
			SmartToast.makeText(this, R.string.error_wx_pay,
					Toast.LENGTH_SHORT).show();
			finish();
		} else if (resp.errCode == -2) {
			SmartToast.makeText(this, R.string.error_cacel_wx_pay,
					Toast.LENGTH_SHORT).show();
			finish();
		} else if (resp.errCode == -3) {
			SmartToast.makeText(getApplicationContext(),
					R.string.error_wx_pay_send, Toast.LENGTH_SHORT).show();
			finish();
		} else if (resp.errCode == -4) {
			SmartToast.makeText(getApplicationContext(),
					R.string.error_wx_pay_get_right, Toast.LENGTH_SHORT)
					.show();
			finish();
		} else if (resp.errCode == -5) {
			SmartToast.makeText(getApplicationContext(),
					R.string.error_wx_pay_no_support, Toast.LENGTH_SHORT)
					.show();
			finish();
		} else {
			SmartToast.makeText(getApplicationContext(),
					R.string.error_wx_pay, Toast.LENGTH_SHORT).show();
			finish();
		}

	}
}