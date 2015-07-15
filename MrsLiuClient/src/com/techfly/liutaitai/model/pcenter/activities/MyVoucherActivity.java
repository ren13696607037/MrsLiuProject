package com.techfly.liutaitai.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.MyVoucherFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class MyVoucherActivity extends BaseActivity {
	private MyVoucherFragment mFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		onInitContent();
	}
	private void onInitContent(){
		mFragment=(MyVoucherFragment) Fragment.instantiate(this, MyVoucherFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
	}
}
