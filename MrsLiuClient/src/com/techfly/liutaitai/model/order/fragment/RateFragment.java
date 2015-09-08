package com.techfly.liutaitai.model.order.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.order.activities.RateActivity;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class RateFragment extends CommonFragment{
	private RatingBar mRatingBar;
	private EditText mContenText;
	private Button mButton;
	private RateActivity mActivity;
	private String mServiceId;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (RateActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mServiceId = mActivity.getIntent().getStringExtra(IntentBundleKey.SERVICE_ID);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_service_rate, container, false);
		return view;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onInitView(view);
	}
	private void onInitView(View view){
		setTitleText(R.string.rate_title);
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		
		
	}

	@Override
	public void requestData() {
		
	}

}
