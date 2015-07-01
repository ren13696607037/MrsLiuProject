package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.AboutUsActivity;
import com.techfly.liutaitai.model.pcenter.activities.HelpActivity;
import com.techfly.liutaitai.model.pcenter.activities.SettingActivity;
import com.techfly.liutaitai.model.pcenter.activities.SuggestActivity;
import com.techfly.liutaitai.update.UpdateMgr;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class SettingFragment extends CommonFragment implements OnClickListener{
	private SettingActivity mActivity;
	private RelativeLayout mHelp;//帮助说明
	private RelativeLayout mAbout;//关于我们
	private RelativeLayout mSuggest;//意见反馈
	private RelativeLayout mUpdate;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(SettingActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.setting_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mAbout=(RelativeLayout) view.findViewById(R.id.setting_about);
    	mHelp=(RelativeLayout) view.findViewById(R.id.setting_help);
    	mSuggest=(RelativeLayout) view.findViewById(R.id.setting_suggest);
    	mUpdate = (RelativeLayout) view.findViewById(R.id.version_update);
    	mAbout.setOnClickListener(this);
    	mHelp.setOnClickListener(this);
    	mSuggest.setOnClickListener(this);
    	mUpdate.setOnClickListener(this);
    }
	@Override
	public void requestData() {
		
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.setting_about:
			intent=new Intent(mActivity,AboutUsActivity.class);
			break;
		case R.id.setting_help:
			intent=new Intent(mActivity,HelpActivity.class);
			break;
		case R.id.setting_suggest:
			intent=new Intent(mActivity,SuggestActivity.class);
			break;
		case R.id.version_update:
		    UpdateMgr.getInstance(mActivity).checkUpdateInfo(null, false);
		    break;
		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}

}
