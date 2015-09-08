package com.techfly.liutaitai.model.pcenter.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.SearchLogisticsFragment;

public class SearchLogisticsActivity extends FragmentActivity {
	private SearchLogisticsFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContent();
    }
    private void initContent(){
        if (mFragment == null) {
            mFragment = (SearchLogisticsFragment) Fragment.instantiate(this, SearchLogisticsFragment.class.getName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, mFragment);
            ft.commit();
        }
    }
}
