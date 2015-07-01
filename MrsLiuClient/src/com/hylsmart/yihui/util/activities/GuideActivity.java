package com.hylsmart.yihui.util.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.SplashActivity;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.fragment.GuideFragment;

public class GuideActivity extends FragmentActivity{
    private GuideFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if(!SharePreferenceUtils.getInstance(this).isFirst()){
    	   Intent intent = new Intent(this,SplashActivity.class);
    	   startActivity(intent);
    	   finish();
       }else{
    	   initContent(); 
       }
    
    }
    private void initContent(){
        if (mFragment == null) {
            mFragment = (GuideFragment) Fragment.instantiate(this, GuideFragment.class.getName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, mFragment);
            ft.commit();
        }
    }
}
