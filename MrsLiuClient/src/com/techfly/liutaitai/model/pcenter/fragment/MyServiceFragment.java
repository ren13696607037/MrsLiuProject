package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.MyServiceParser;
import com.techfly.liutaitai.model.pcenter.activities.MyServiceActivity;
import com.techfly.liutaitai.model.pcenter.bean.MyService;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class MyServiceFragment extends CommonFragment{
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
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(this);
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
		ImageLoader.getInstance().displayImage(mService.getmTechnician().getmHeader(), mHeader);
		mName.setText(mService.getmTechnician().getmName());
		mPrice.setText(mService.getmPrice());
	}

}
