package com.techfly.liutaitai.model.order.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.order.fragment.ServiceOrderFragment;
import com.techfly.liutaitai.util.activities.BaseHomeActivity;


public class OrderActivity extends BaseHomeActivity {
	private ServiceOrderFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(ServiceOrderFragment) Fragment.instantiate(this, ServiceOrderFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
