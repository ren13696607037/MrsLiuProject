package com.hylsmart.yihui.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.pcenter.fragment.PcenterInfoFragment;
import com.hylsmart.yihui.util.activities.BaseActivity;

public class PcenterInfoActivity extends BaseActivity {
	private PcenterInfoFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(PcenterInfoFragment) Fragment.instantiate(this, PcenterInfoFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
