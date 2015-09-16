package com.techfly.liutaitai.model.pcenter.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.model.pcenter.fragment.RechargeFragment;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class RechargeActivity extends BaseActivity {
	private ProgressDialog mDialog;
	private RechargeFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(RechargeFragment) Fragment.instantiate(this, RechargeFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mDialog!=null){
            mDialog .dismiss();
            mDialog =null;
        }
    }
    @Override
    protected void onPause() {
        if(mDialog!=null){
            mDialog .dismiss();
            mDialog =null;
        }
        super.onPause();
    }
    
    public void setDialog(ProgressDialog dialog) {
        mDialog = dialog;
        
    }
}
