package com.techfly.liutaitai.model.shopcar.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarListener;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.activities.BaseHomeActivity;

public class ShopCarHomeActivity extends BaseHomeActivity implements OnShopCarListener {
    private Fragment mShopCarFragment;
    private Fragment mEmptyShopCarFragment;
    private Fragment mUnLoginShopCarFragment;
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        OnShopCarLisManager.getShopCarLisManager().onRegisterShopCarListener(this);
        setContentView(R.layout.activity_shop_car);
        onInitContent();
    }
    
    @Override
    protected void onDestroy() {
        OnShopCarLisManager.getShopCarLisManager().onUnRegisterShopCarListener(this);
        super.onDestroy();
    }
    
    private void onInitContent() {
        mShopCarFragment = getSupportFragmentManager().findFragmentById(R.id.shop_car);
        mEmptyShopCarFragment = getSupportFragmentManager().findFragmentById(R.id.empty_shop_car);
        mUnLoginShopCarFragment = getSupportFragmentManager().findFragmentById(R.id.unlogin_shop_car);
        onRefreshContent();
    }
    
    @Override
    public void notify(Bundle bundle) {
            onRefreshContent();
    }
    
    private void onRefreshContent() {
        FragmentTransaction ft =   getSupportFragmentManager().beginTransaction();
        if (authLogin()) {
            if (ShopCar.getShopCar().getShopAmountSum() > 0){
                ft.hide(mEmptyShopCarFragment);
                ft.show(mShopCarFragment);
                ft.hide(mUnLoginShopCarFragment);
            }else{
                ft.show(mEmptyShopCarFragment);
                ft.hide(mShopCarFragment);
                ft.hide(mUnLoginShopCarFragment);
            }
         
        } else {
            ft.hide(mEmptyShopCarFragment);
            ft.hide(mShopCarFragment);
            ft.show(mUnLoginShopCarFragment);
        }
        ft.commitAllowingStateLoss();
    }
    
    private boolean authLogin() {
        User user = SharePreferenceUtils.getInstance(this).getUser();
        if (user != null && !TextUtils.isEmpty(user.getmId())) {
            if (Integer.parseInt(user.getmId()) > 0) {
                return true;
            }
        }
        return false;
    }
 
}
