package com.hylsmart.yihui.model.home.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.home.fragment.HomeFragment;
import com.hylsmart.yihui.util.activities.BaseHomeActivity;

public class HomeActivity extends BaseHomeActivity{
    private HomeFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(HomeFragment) Fragment.instantiate(this, HomeFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
    
}
