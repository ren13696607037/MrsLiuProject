package com.techfly.liutaitai.model.order.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.order.fragment.ServiceDetailFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class ServiceDetailActivity extends BaseActivity {
	private ServiceDetailFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(ServiceDetailFragment) Fragment.instantiate(this, ServiceDetailFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
