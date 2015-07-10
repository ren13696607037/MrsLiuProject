package com.techfly.liutaitai.model.order.activities;


import com.techfly.liutaitai.model.order.fragment.OrderFragment;
import com.techfly.liutaitai.util.activities.BaseHomeActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


public class OrderActivity extends BaseHomeActivity {
	private OrderFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(OrderFragment) Fragment.instantiate(this, OrderFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
