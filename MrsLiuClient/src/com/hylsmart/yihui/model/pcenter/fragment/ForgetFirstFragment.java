package com.hylsmart.yihui.model.pcenter.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.pcenter.activities.ForgetFirstActivity;
import com.hylsmart.yihui.model.pcenter.activities.ForgetNextActivity;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class ForgetFirstFragment extends CommonFragment implements OnClickListener{
	private ForgetFirstActivity mActivity;
	private Button mButton;
	private EditText mEtCode;
	private TextView mTvCode;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(ForgetFirstActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgetfirst, container, false);
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
    	setTitleText(R.string.forget_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mButton=(Button) view.findViewById(R.id.first_btn);
    	mButton.setOnClickListener(this);
    }
	@Override
	public void requestData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.first_btn:
			startActivity(new Intent(mActivity,ForgetNextActivity.class));
			break;
		case R.id.first_code:
			
			break;

		default:
			break;
		}
	}

}
