package com.hylsmart.yihui.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.pcenter.fragment.ForgetNextFragment;
import com.hylsmart.yihui.util.activities.BaseActivity;

public class ForgetNextActivity extends BaseActivity {
	private ForgetNextFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(ForgetNextFragment) Fragment.instantiate(this, ForgetNextFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
