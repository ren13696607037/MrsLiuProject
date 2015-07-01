package com.hylsmart.yihui.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.pcenter.fragment.MyCollectFragment;
import com.hylsmart.yihui.util.activities.BaseActivity;

public class MyCollectActivity extends BaseActivity {
	private MyCollectFragment mFragment;
	@Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(MyCollectFragment) Fragment.instantiate(this, MyCollectFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
