package com.techfly.liutaitai.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.TechCashFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class TechCashActivity extends BaseActivity {
	private TechCashFragment mFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		onInitContent();
	}
	private void onInitContent(){
		mFragment=(TechCashFragment) Fragment.instantiate(this, TechCashFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
	}
}
