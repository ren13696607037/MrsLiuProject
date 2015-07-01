package com.hylsmart.yihui.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.mall.fragment.PicAndTextDetailFragment;
import com.hylsmart.yihui.util.activities.BaseActivity;

public class PicAndTextDetailActivity extends BaseActivity {

	private PicAndTextDetailFragment mFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		onInitContent();
	}

	private void onInitContent() {
		mFragment = (PicAndTextDetailFragment) Fragment.instantiate(this,
				PicAndTextDetailFragment.class.getName());
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(android.R.id.content, mFragment);
		ft.commit();
	}

}
