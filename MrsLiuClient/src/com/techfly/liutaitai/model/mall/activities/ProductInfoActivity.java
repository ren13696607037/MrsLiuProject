package com.techfly.liutaitai.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.mall.fragment.ProductInfoFragment;
import com.techfly.liutaitai.util.AppManager;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class ProductInfoActivity extends BaseActivity {

	private ProductInfoFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		initContent();
	}

	private void initContent() {
		if (mFragment == null) {
			mFragment = (ProductInfoFragment) Fragment.instantiate(this,
					ProductInfoFragment.class.getName());
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(android.R.id.content, mFragment);
			ft.commit();
		}
	}

}
