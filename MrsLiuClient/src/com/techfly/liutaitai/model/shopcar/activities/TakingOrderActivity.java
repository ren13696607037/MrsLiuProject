package com.techfly.liutaitai.model.shopcar.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.shopcar.fragment.CreateOrderSucFragment;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class TakingOrderActivity extends BaseActivity{
    private Fragment mTakeOrderFragment;
    private CreateOrderSucFragment mOrderCreateFragment;
    private Fragment mOrderFinishFragment;
    private Bundle mBundle;
    private ProgressDialog mDialog;
    private boolean mIsFromOrder =false;
    @Override
    protected void onStop() {
        super.onStop();
        if(mDialog!=null){
            mDialog .dismiss();
            mDialog = null;
        }
    }
    @Override
    protected void onPause() {
        if(mDialog!=null){
            mDialog .dismiss();
            mDialog =null;
        }
        super.onPause();
    }
    public Bundle getBundleInfo(){
        return mBundle;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_taking_order);
        mIsFromOrder  = getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_ORDER, false);
        onInitContent();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    private void onInitContent() {
        mTakeOrderFragment = getSupportFragmentManager().findFragmentById(R.id.taking_order);
        mOrderCreateFragment = (CreateOrderSucFragment) getSupportFragmentManager().findFragmentById(R.id.order_create);
        mOrderFinishFragment = getSupportFragmentManager().findFragmentById(R.id.order_finish);
        if(mIsFromOrder){
            mBundle = getIntent().getBundleExtra(IntentBundleKey.DATA);
            showOrderCreateFragment(mBundle);
        }else{
            showTakingOrderFragment();
        }
    }
    
    public void showOrderCreateFragment(Bundle bundle){
        mBundle =bundle;
        mOrderCreateFragment.onShowDisplay(mBundle);
        FragmentTransaction ft =   getSupportFragmentManager().beginTransaction();
        ft.hide(mTakeOrderFragment);
        ft.show(mOrderCreateFragment);
        ft.hide(mOrderFinishFragment);
        ft.commitAllowingStateLoss();
    }
    
    public void showOrderFinishFragment(Bundle bundle){
        mBundle =bundle;
        FragmentTransaction ft =   getSupportFragmentManager().beginTransaction();
        ft.hide(mTakeOrderFragment);
        ft.hide(mOrderCreateFragment);
        ft.show(mOrderFinishFragment);
        ft.commitAllowingStateLoss();
        if(mBundle.getInt(IntentBundleKey.ORDER_PAY_METHOD, 0)==Constant.PAY_ALIPAY){
            onRequestPaySuccess(2);
        }else if(mBundle.getInt(IntentBundleKey.ORDER_PAY_METHOD, 0)==Constant.PAY_WENXIN ){
            onRequestPaySuccess(1);
        }
        
    }
 
    public void showTakingOrderFragment(){
        FragmentTransaction ft =   getSupportFragmentManager().beginTransaction();
        ft.show(mTakeOrderFragment);
        ft.commitAllowingStateLoss();
    }

    
    private void onRequestPaySuccess(int type) {
        RequestParam param = new RequestParam();
        User user = SharePreferenceUtils.getInstance(this).getUser();
        int userId = 0;
        if (user != null) {
            userId = Integer.parseInt(user.getmId());
        }
        if (userId == 0) {
            return;
        }
        param.setmIsLogin(true);
        param.setmId(user .getmId());
        param.setmToken(user .getmToken());
//        param.setPostRequestMethod();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+"common/payData");
        url.setmGetParamPrefix("type");
        url.setmGetParamValues(type+"");
        
        url.setmGetParamPrefix("id");
        url.setmGetParamValues(mBundle.getString(IntentBundleKey.ORDER_ID, ""));
        param.setmParserClassName(CommonParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(this, createSuccessReqSuccessListener(), createMyReqErrorListener(), param);
    }
    private Response.Listener<Object> createSuccessReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd("Fly", object.toString());
            }
            };
        }
    
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              
            
            }
        };
    }
    public void setDialog(ProgressDialog dialog) {
        mDialog = dialog;
        
    }
  
}
