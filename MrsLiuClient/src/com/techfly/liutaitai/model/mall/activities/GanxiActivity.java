package com.techfly.liutaitai.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.mall.fragment.GanxiFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class GanxiActivity extends BaseActivity{
    private GanxiFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
    }

    private void initContent() {
        if (mFragment == null) {
            mFragment = (GanxiFragment) Fragment.instantiate(this,GanxiFragment.class.getName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, mFragment);
            ft.commit();
        }
    }
}
