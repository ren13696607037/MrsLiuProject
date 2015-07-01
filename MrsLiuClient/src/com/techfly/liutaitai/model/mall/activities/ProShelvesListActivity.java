package com.techfly.liutaitai.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.mall.fragment.ProShelvesListFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class ProShelvesListActivity extends BaseActivity {
    private ProShelvesListFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(ProShelvesListFragment) Fragment.instantiate(this, ProShelvesListFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
