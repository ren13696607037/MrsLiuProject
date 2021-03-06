package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.MyServiceParser;
import com.techfly.liutaitai.model.pcenter.activities.MyOrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyServiceActivity;
import com.techfly.liutaitai.model.pcenter.activities.RateListActivity;
import com.techfly.liutaitai.model.pcenter.activities.TechCashActivity;
import com.techfly.liutaitai.model.pcenter.activities.TechnicianInfoActivity;
import com.techfly.liutaitai.model.pcenter.bean.MyService;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class MyServiceFragment extends CommonFragment implements OnClickListener{
	private MyServiceActivity mActivity;
	private ImageView mHeader;
	private TextView mName;
	private TextView mSex;
	private TextView mCount;
	private TextView mContinueNum;
	private TextView mServicingNum;
	private TextView mTakingNum;
	private TextView mPrice;
	private RelativeLayout mInfo;
	private RelativeLayout mTaking;
	private RelativeLayout mContinue;
	private RelativeLayout mServicing;
	private RelativeLayout mAll;
	private RelativeLayout mApply;
	private RelativeLayout mRate;
	private User mUser;
	private MyService mService;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MyServiceActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myservice,
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
    	setTitleText(R.string.pcenter_service);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mAll = (RelativeLayout) view.findViewById(R.id.service_all);
    	mApply = (RelativeLayout) view.findViewById(R.id.service_apply);
    	mContinue = (RelativeLayout) view.findViewById(R.id.service_continue);
    	mContinueNum = (TextView) view.findViewById(R.id.serivce_continue_num);
    	mCount = (TextView) view.findViewById(R.id.service_count);
    	mHeader = (ImageView) view.findViewById(R.id.service_img);
    	mInfo = (RelativeLayout) view.findViewById(R.id.service_info);
    	mName = (TextView) view.findViewById(R.id.service_name);
    	mPrice = (TextView) view.findViewById(R.id.service_price);
    	mRate = (RelativeLayout) view.findViewById(R.id.service_rate);
    	mServicing = (RelativeLayout) view.findViewById(R.id.service_servicing);
    	mServicingNum = (TextView) view.findViewById(R.id.serivce_servicing_num);
    	mSex = (TextView) view.findViewById(R.id.service_sex);
    	mTaking = (RelativeLayout) view.findViewById(R.id.service_taking);
    	mTakingNum = (TextView) view.findViewById(R.id.serivce_taking_num);
    	
    	mInfo.setOnClickListener(this);
    	mContinue.setOnClickListener(this);
    	mServicing.setOnClickListener(this);
    	mTaking.setOnClickListener(this);
    	mAll.setOnClickListener(this);
    	mApply.setOnClickListener(this);
    	mRate.setOnClickListener(this);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SERVICE_URL);
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(MyServiceParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mService = (MyService) object;
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					setData();
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}
	private void setData(){
		ImageLoader.getInstance().displayImage(mService.getmTechnician().getmHeader(), mHeader,ImageLoaderUtil.mUserIconLoaderOptions);
		mName.setText(mService.getmTechnician().getmName());
		mPrice.setText(mService.getmPrice());
		if("0".equals(mService.getmOrderNum())){
			mTakingNum.setVisibility(View.INVISIBLE);
		}else{
			mTakingNum.setVisibility(View.VISIBLE);
			mTakingNum.setText(mService.getmOrderNum());
		}
		if("0".equals(mService.getmServiceingNum())){
			mServicingNum.setVisibility(View.INVISIBLE);
		}else{
			mServicingNum.setVisibility(View.VISIBLE);
			mServicingNum.setText(mService.getmServiceingNum());
		}
		if("0".equals(mService.getmOrderingNum())){
			mContinueNum.setVisibility(View.INVISIBLE);
		}else{
			mContinueNum.setVisibility(View.VISIBLE);
			mContinueNum.setText(mService.getmOrderingNum());
		}
		mSex.setText(mService.getmType());
		mCount.setText(getString(R.string.service_count, mService.getmTechnician().getmTimes()));
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.service_info:
			intent = new Intent(mActivity, TechnicianInfoActivity.class);
			break;
		case R.id.service_continue:
			intent = new Intent(mActivity, MyOrderActivity.class);
			intent.putExtra(IntentBundleKey.ORDER_ID, 3);
			break;
		case R.id.service_all:
			intent = new Intent(mActivity, MyOrderActivity.class);
			break;
		case R.id.service_apply:
			intent = new Intent(mActivity, TechCashActivity.class);
			intent.putExtra(IntentBundleKey.TECH_MONEY, mService.getmPrice());
			break;
		case R.id.service_rate:
			intent = new Intent(mActivity, RateListActivity.class);
			intent.putExtra(IntentBundleKey.TECH_ID, mService.getmId());
			break;
		case R.id.service_servicing:
			intent = new Intent(mActivity, MyOrderActivity.class);
			intent.putExtra(IntentBundleKey.ORDER_ID, 2);
			break;
		case R.id.service_taking:
			intent = new Intent(mActivity, MyOrderActivity.class);
			intent.putExtra(IntentBundleKey.ORDER_ID, 1);
			break;

		default:
			break;
		}
		if(intent != null){
			startActivity(intent);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(this);
	}

}
