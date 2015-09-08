package com.techfly.liutaitai.model.order.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.order.activities.RateActivity;
import com.techfly.liutaitai.model.order.parser.ServiceOrderParser;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
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
		
		mRatingBar = (RatingBar) view.findViewById(R.id.service_rate_bar);
		mContenText = (EditText) view.findViewById(R.id.service_rate_content);
		mButton = (Button) view.findViewById(R.id.service_rate_btn);
		
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startReqTask(RateFragment.this);
			}
		});
		
	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SERVICE_RATE_URL);
			url.setmGetParamPrefix(JsonKey.RateKey.ID).setmGetParamValues(mServiceId);
			url.setmGetParamPrefix(JsonKey.RateKey.CONTENT).setmGetParamValues(mContenText.getText().toString());
			url.setmGetParamPrefix(JsonKey.RateKey.STARS).setmGetParamValues((int)(mRatingBar.getRating()/2) + "");
			url.setmGetParamPrefix(JsonKey.RateKey.TYPE).setmGetParamValues("1");
			param.setmParserClassName(CommonParser.class.getName());
		param.setmIsLogin(true);
//		param.setmId(mUser.getmId());
//		param.setmToken(mUser.getmToken());
		param.setmId("1");
		param.setmToken("440a07c991c4bbae3bcd52746e6a9d32");
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}
   
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                if(!isDetached()){
                	mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                    ResultInfo info = (ResultInfo) object;
                    if(info.getmCode() == 0){
                    	
                    }else{
                    	
                    }
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                AppLog.Loge(" data failed to load"+error.getMessage());
            }
       };
    }

}
