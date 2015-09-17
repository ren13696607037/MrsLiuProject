package com.techfly.liutaitai.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.TechOrderDetailFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class OrderDetailActivity extends BaseActivity {
	private TechOrderDetailFragment mFragment;
	@Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(TechOrderDetailFragment) Fragment.instantiate(this, TechOrderDetailFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
