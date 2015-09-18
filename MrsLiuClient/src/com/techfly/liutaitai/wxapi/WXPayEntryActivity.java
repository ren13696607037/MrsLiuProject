package com.techfly.liutaitai.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.wenxin.Keys;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.util.AppManager;
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
		if(resp.errCode==0){
		  if(AppManager.getAppManager().currentActivity() instanceof TakingOrderActivity){
		      TakingOrderActivity ac = (TakingOrderActivity) AppManager.getAppManager().currentActivity() ;
		      ac.showOrderFinishFragment(ac.getBundleInfo());
		      this.finish();
		  }else  if(AppManager.getAppManager().currentActivity() instanceof ServiceOrderActivity){
		      ServiceOrderActivity ac = (ServiceOrderActivity) AppManager.getAppManager().currentActivity() ;
              ac.showOrderFinishFragment(ac.getBundleInfo());
              this.finish();
          }else  if(AppManager.getAppManager().currentActivity() instanceof RechargeActivity){
              this.finish();
          }
		    
		  
	      
		}
	
	   
	}
}