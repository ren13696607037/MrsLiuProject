package com.hylsmart.yihui.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.mall.fragment.CategoryListFragment;
import com.hylsmart.yihui.util.activities.BaseHomeActivity;

public class CategoryListActivity extends BaseHomeActivity {
	  private CategoryListFragment mFragment;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        initContent();
	    }
	    private void initContent(){
	        if (mFragment == null) {
	            mFragment = (CategoryListFragment) Fragment.instantiate(this, CategoryListFragment.class.getName());
	            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	            ft.add(android.R.id.content, mFragment);
	            ft.commit();
	        }
	    }
}
