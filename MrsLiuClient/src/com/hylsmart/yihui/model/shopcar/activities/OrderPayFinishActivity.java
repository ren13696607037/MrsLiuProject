package com.hylsmart.yihui.model.shopcar.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.pcenter.fragment.AboutUsFragment;
import com.hylsmart.yihui.model.shopcar.fragment.FinishOrderFragment;

public class OrderPayFinishActivity extends FragmentActivity{
    private FinishOrderFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(FinishOrderFragment) Fragment.instantiate(this, FinishOrderFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
