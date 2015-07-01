package com.hylsmart.yihui.model.mall.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.hylsmart.yihui.model.mall.fragment.TuangouFragment;
import com.hylsmart.yihui.util.activities.BaseActivity;

public class TuanGouActivity extends BaseActivity {
    private TuangouFragment mFragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        onInitContent();
    }
    private void onInitContent(){
        mFragment=(TuangouFragment) Fragment.instantiate(this, TuangouFragment.class.getName());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, mFragment);
        ft.commit();
    }
}
