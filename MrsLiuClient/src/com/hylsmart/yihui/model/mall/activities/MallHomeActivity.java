package com.hylsmart.yihui.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.mall.fragment.MallHomeFragment;
import com.hylsmart.yihui.util.activities.BaseActivity;
import com.hylsmart.yihui.util.activities.BaseHomeActivity;

public class MallHomeActivity extends BaseActivity {
    private MallHomeFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(MallHomeFragment) Fragment.instantiate(this, MallHomeFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
