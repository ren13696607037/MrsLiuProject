package com.techfly.liutaitai.util.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.util.fragment.CitySelectFragment;


public class CitySelectActivity extends BaseActivity {
   
    @Override
	public void onBackPressed() {
    	mFragment.onBackPressed();
		super.onBackPressed();
	}
	private CitySelectFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
    }
    private void initContent(){
        if (mFragment == null) {
            mFragment = (CitySelectFragment) Fragment.instantiate(this, CitySelectFragment.class.getName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, mFragment);
            ft.commit();
        }
    }
    
}
