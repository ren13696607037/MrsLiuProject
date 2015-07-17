package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.ChangePassActivity;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ChangePassFragment extends CommonFragment {
	private ChangePassActivity mActivity;
	private EditText mOldPass;
	private EditText mNewPass;
	private EditText mRePass;
	private Button mButton;
	private Handler mPassHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ChangePassActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.pinfo_pass);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mOldPass = (EditText) view.findViewById(R.id.changepass_old);
    	mNewPass = (EditText) view.findViewById(R.id.changepass_new);
    	mRePass = (EditText) view.findViewById(R.id.changepass_repass);
    	mButton = (Button) view.findViewById(R.id.changepass_btn);
    	mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
    }

	@Override
	public void requestData() {
		// TODO Auto-generated method stub

	}

}
