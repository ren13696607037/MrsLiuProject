package com.techfly.liutaitai.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.RegisterFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class RegisterActivity extends BaseActivity {
	private RegisterFragment mFragment;
//	private RegisterEmailFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(RegisterFragment) Fragment.instantiate(this, RegisterFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
