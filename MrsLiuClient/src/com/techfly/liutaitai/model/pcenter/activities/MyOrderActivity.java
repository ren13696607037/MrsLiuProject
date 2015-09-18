package com.techfly.liutaitai.model.pcenter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.techfly.liutaitai.MainActivity;
import com.techfly.liutaitai.model.pcenter.fragment.MyOrderFragment;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.activities.BaseActivity;

public class MyOrderActivity extends BaseActivity {
	private MyOrderFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Utility.getScreenSize(this);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(MyOrderFragment) Fragment.instantiate(this, MyOrderFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
    @Override
    public void onBackPressed() {
    	int push=getIntent().getIntExtra(IntentBundleKey.PUSH, -1);
		if(push!=-1){
			if(!Utility.getActivityName(MyOrderActivity.this)){
				this.finish();
			}else{
				Bundle bundle=new Bundle();
				bundle.putInt(IntentBundleKey.PUSH, push);
				Utility.go2Activity(MyOrderActivity.this,MainActivity.class,bundle);
			}
		}else{
			this.finish();
		}
    }
}
