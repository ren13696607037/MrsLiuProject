package com.techfly.liutaitai.model.shopcar.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.shopcar.fragment.ShopCarHomeFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class ShopCarActivity extends BaseActivity{
    private Fragment mShopCarFragment;
    private Fragment mEmptyShopCarFragment;
    private Fragment mUnLoginShopCarFragment;
    private int type;// 0 ,1 ,2 , 4分表表示 干洗,生鲜，鲜花，奢侈品
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        OnShopCarLisManager.getShopCarLisManager().onRegisterShopCarListener(this);
//        setContentView(R.layout.activity_shop_car);
//        type = getIntent().getIntExtra(IntentBundleKey.TYPE, 0);
//        onInitShopCart();
//    }
    private ShopCarHomeFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
    }

    private void initContent() {
        if (mFragment == null) {
            mFragment = (ShopCarHomeFragment) Fragment.instantiate(this,ShopCarHomeFragment.class.getName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, mFragment);
            ft.commit();
        }
    }
//    public void onInitShopCart() {
//        RequestParam param = new RequestParam();
//        HttpURL url = new HttpURL();
//        User user = SharePreferenceUtils.getInstance(this).getUser();
//        int userId = 0;
//        if (user != null) {
//            userId = Integer.parseInt(user.getmId());
//        }
//        if (userId == 0) {
//            return;
//        }
//        param.setmIsLogin(true);
//        param.setmId(user .getmId());
//        param.setmToken(user .getmToken());
//        
//        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
//                + Constant.SHOP_CARD_REQUEST_URL);
//        url.setmGetParamPrefix("city");
//        url.setmGetParamValues(SharePreferenceUtils.getInstance(this).getArea().getmId());
//        url.setmGetParamPrefix("type");
//        url.setmGetParamValues(type+"");
//        param.setmHttpURL(url);
//        param.setmParserClassName(ShopCartParser.class.getName());
//        RequestManager.getRequestData(this, creatSuccessListener(),
//                creatErrorListener(), param);//
//    }
//
//    private Response.Listener<Object> creatSuccessListener() {
//        return new Response.Listener<Object>() {
//
//            @Override
//            public void onResponse(Object obj) {
//                onInitContent();
//            }
//        };
//    }
//
//    private Response.ErrorListener creatErrorListener() {
//        return new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                AppLog.Loge(" data failed to load" + error.getMessage());
//
//            }
//        };
//    }
//    @Override
//    protected void onDestroy() {
//        OnShopCarLisManager.getShopCarLisManager().onUnRegisterShopCarListener(this);
//        super.onDestroy();
//    }
//    
//    private void onInitContent() {
//        mShopCarFragment = getSupportFragmentManager().findFragmentById(R.id.shop_car);
//        mEmptyShopCarFragment = getSupportFragmentManager().findFragmentById(R.id.empty_shop_car);
//        mUnLoginShopCarFragment = getSupportFragmentManager().findFragmentById(R.id.unlogin_shop_car);
//        onRefreshContent();
//    }
//    
//    @Override
//    public void notify(Bundle bundle) {
//            onRefreshContent();
//    }
//    
//    private void onRefreshContent() {
//        FragmentTransaction ft =   getSupportFragmentManager().beginTransaction();
//        if (authLogin()) {
//            if (ShopCar.getShopCar().getShopAmountSum() > 0){
//                ft.hide(mEmptyShopCarFragment);
//                ft.show(mShopCarFragment);
//                ft.hide(mUnLoginShopCarFragment);
//            }else{
//                ft.show(mEmptyShopCarFragment);
//                ft.hide(mShopCarFragment);
//                ft.hide(mUnLoginShopCarFragment);
//            }
//         
//        } else {
//            ft.hide(mEmptyShopCarFragment);
//            ft.hide(mShopCarFragment);
//            ft.show(mUnLoginShopCarFragment);
//        }
//        ft.commitAllowingStateLoss();
//    }
//    
//    private boolean authLogin() {
//        User user = SharePreferenceUtils.getInstance(this).getUser();
//        if (user != null && !TextUtils.isEmpty(user.getmId())) {
//            if (Integer.parseInt(user.getmId()) > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
}
