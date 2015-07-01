package com.hylsmart.yihui.model.shopcar.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.R;

public class TakingOrderActivity extends FragmentActivity{
    private Fragment mTakeOrderFragment;
    private Fragment mOrderCreateFragment;
    private Fragment mOrderFinishFragment;
    private Bundle mBundle;
    public Bundle getBundleInfo(){
        return mBundle;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_taking_order);
        onInitContent();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    private void onInitContent() {
        mTakeOrderFragment = getSupportFragmentManager().findFragmentById(R.id.taking_order);
        mOrderCreateFragment = getSupportFragmentManager().findFragmentById(R.id.order_create);
        mOrderFinishFragment = getSupportFragmentManager().findFragmentById(R.id.order_finish);
        showTakingOrderFragment();
    }
    
    public void showOrderCreateFragment(Bundle bundle){
        mBundle =bundle;
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
    }
    public void showTakingOrderFragment(){
        FragmentTransaction ft =   getSupportFragmentManager().beginTransaction();
        ft.show(mTakeOrderFragment);
        ft.commitAllowingStateLoss();
    }

   
  
}
