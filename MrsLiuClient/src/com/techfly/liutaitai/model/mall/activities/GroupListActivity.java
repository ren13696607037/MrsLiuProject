package com.techfly.liutaitai.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.mall.fragment.GroupListFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class GroupListActivity extends BaseActivity {
	private GroupListFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContent();
	}

	private void initContent() {
		if (mFragment == null) {
			mFragment = (GroupListFragment) Fragment.instantiate(this, GroupListFragment.class.getName());
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, mFragment);
			ft.commit();
		}
	}
}
