package com.techfly.liutaitai.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.MyApplyFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class MyApplyActivity extends BaseActivity {
	private MyApplyFragment mFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		onInitContent();
	}
	private void onInitContent(){
		mFragment = (MyApplyFragment) Fragment.instantiate(this, MyApplyFragment.class.getName());
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(android.R.id.content, mFragment);
		ft.commit();
	}
}
