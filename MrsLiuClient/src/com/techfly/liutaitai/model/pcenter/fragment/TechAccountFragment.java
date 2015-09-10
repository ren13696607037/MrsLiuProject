package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.MyServiceParser;
import com.techfly.liutaitai.model.pcenter.activities.TechAccountActivity;
import com.techfly.liutaitai.model.pcenter.bean.MyService;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class TechAccountFragment extends CommonFragment{
	private TechAccountActivity mActivity;
	private RadioButton mCard;
	private RadioButton mAlipay;
	private RadioButton mWeixin;
	private EditText mCardName;
	private EditText mCardNumber;
	private EditText mCardUserName;
	private EditText mAlipayName;
	private EditText mAlipayUserName;
	private EditText mWeixinName;
	private EditText mWeixinUserName;
	private Button mButton;
	private User mUser;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TechAccountActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_account,
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
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	setTitleText(R.string.tech_account_title);
    	
    	mCard = (RadioButton) view.findViewById(R.id.tech_account_card);
    	mCardName = (EditText) view.findViewById(R.id.tech_account_card_name);
    	mCardNumber = (EditText) view.findViewById(R.id.tech_account_card_number);
    	mCardUserName = (EditText) view.findViewById(R.id.tech_account_card_username);
    	mAlipay = (RadioButton) view.findViewById(R.id.tech_account_alipay);
    	mAlipayName = (EditText) view.findViewById(R.id.tech_account_alipay_name);
    	mAlipayUserName = (EditText) view.findViewById(R.id.tech_account_alipay_username);
    	mWeixin = (RadioButton) view.findViewById(R.id.tech_account_weixin);
    	mWeixinName = (EditText) view.findViewById(R.id.tech_account_weixin_name);
    	mWeixinUserName = (EditText) view.findViewById(R.id.tech_account_weixin_username);
    	mButton = (Button) view.findViewById(R.id.tech_account_btn);
    	
    	mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((mCard.isChecked() && mCardName.length() > 0 && mCardNumber.length() > 0 && mCardUserName.length() > 0)||
						(mAlipay.isChecked() && mAlipayName.length() > 0 && mAlipayUserName.length() > 0) ||
						(mWeixin.isChecked() && mWeixinName.length() > 0 && mWeixinUserName.length() > 0)
						){
					startReqTask(TechAccountFragment.this);
				}
			}
		});
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ACCOUNT_URL);
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(CommonParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				ResultInfo info = (ResultInfo) object;
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
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
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}
}
