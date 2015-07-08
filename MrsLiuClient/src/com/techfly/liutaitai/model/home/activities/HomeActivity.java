package com.techfly.liutaitai.model.home.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.home.fragment.HomeFragment2;
import com.techfly.liutaitai.util.activities.BaseHomeActivity;

public class HomeActivity extends BaseHomeActivity{
    private HomeFragment2 mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(HomeFragment2) Fragment.instantiate(this, HomeFragment2.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
    
}
