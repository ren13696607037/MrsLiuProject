package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.MyServiceParser;
import com.techfly.liutaitai.model.pcenter.activities.TechAccountActivity;
import com.techfly.liutaitai.model.pcenter.bean.MyService;
import com.techfly.liutaitai.model.pcenter.bean.TechAccount;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class TechAccountFragment extends CommonFragment implements OnClickListener{
	private TechAccountActivity mActivity;
	private CheckBox mCard;
	private CheckBox mAlipay;
	private CheckBox mWeixin;
	private EditText mCardName;
	private EditText mCardNumber;
	private EditText mCardUserName;
	private EditText mAlipayName;
	private EditText mAlipayUserName;
	private EditText mWeixinName;
	private EditText mWeixinUserName;
	private Button mButton;
	private User mUser;
	private TechAccount mTechAccount;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TechAccountActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        mTechAccount = (TechAccount) mActivity.getIntent().getSerializableExtra(IntentBundleKey.TECH_CASH_ID);
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
    	
    	mCard = (CheckBox) view.findViewById(R.id.tech_account_card);
    	mCardName = (EditText) view.findViewById(R.id.tech_account_card_name);
    	mCardNumber = (EditText) view.findViewById(R.id.tech_account_card_number);
    	mCardUserName = (EditText) view.findViewById(R.id.tech_account_card_username);
    	mAlipay = (CheckBox) view.findViewById(R.id.tech_account_alipay);
    	mAlipayName = (EditText) view.findViewById(R.id.tech_account_alipay_name);
    	mAlipayUserName = (EditText) view.findViewById(R.id.tech_account_alipay_username);
    	mWeixin = (CheckBox) view.findViewById(R.id.tech_account_weixin);
    	mWeixinName = (EditText) view.findViewById(R.id.tech_account_weixin_name);
    	mWeixinUserName = (EditText) view.findViewById(R.id.tech_account_weixin_username);
    	mButton = (Button) view.findViewById(R.id.tech_account_btn);
    	
    	mButton.setOnClickListener(this);
    	
    	mCard.setOnClickListener(this);
    	mWeixin.setOnClickListener(this);
    	mAlipay.setOnClickListener(this);
    	
//    	if(mTechAccount != null && mTechAccount.getmType() != null){
//    		if(Integer.valueOf(mTechAccount.getmType()) == 0){
//    			mCard.setChecked(true);
//    			mCardName.setText(mTechAccount.getmBank());
//    			mCardNumber.setText(mTechAccount.getmAccount());
//    			mCardUserName.setText(mTechAccount.getmName());
//    		}else if(Integer.valueOf(mTechAccount.getmType()) == 1){
//    			mAlipay.setChecked(true);
//    			mAlipayName.setText(mTechAccount.getmAccount());
//    			mAlipayUserName.setText(mTechAccount.getmName());
//    		}else if(Integer.valueOf(mTechAccount.getmType()) == 2){
//    			mWeixin.setChecked(true);
//    			mWeixinName.setText(mTechAccount.getmAccount());
//    			mWeixinUserName.setText(mTechAccount.getmName());
//    		}
//    	}
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ACCOUNT_URL);
		if(mCard.isChecked()){
			url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("0");
			url.setmGetParamPrefix(JsonKey.TechnicianKey.BANK).setmGetParamValues(mCardName.getText().toString());
			url.setmGetParamPrefix(JsonKey.TechnicianKey.ACCOUNT).setmGetParamValues(mCardNumber.getText().toString());
			url.setmGetParamPrefix(JsonKey.TechnicianKey.NAME).setmGetParamValues(mCardUserName.getText().toString());
		}else if(mAlipay.isChecked()){
			url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("1");
			url.setmGetParamPrefix(JsonKey.TechnicianKey.ACCOUNT).setmGetParamValues(mAlipayName.getText().toString());
			url.setmGetParamPrefix(JsonKey.TechnicianKey.NAME).setmGetParamValues(mAlipayUserName.getText().toString());
		}else if(mWeixin.isChecked()){
			url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("2");
			url.setmGetParamPrefix(JsonKey.TechnicianKey.ACCOUNT).setmGetParamValues(mWeixinName.getText().toString());
			url.setmGetParamPrefix(JsonKey.TechnicianKey.NAME).setmGetParamValues(mWeixinUserName.getText().toString());
		}
		if(mTechAccount != null){
			url.setmGetParamPrefix(JsonKey.TechnicianKey.BID).setmGetParamValues(mTechAccount.getmId());
		}
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
						mActivity.finish();
						showSmartToast(R.string.submit_success, Toast.LENGTH_SHORT);
					}else{
						showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tech_account_btn:
			if((mCard.isChecked() && mCardName.length() > 0 && mCardNumber.length() > 0 && mCardUserName.length() > 0)||
					(mAlipay.isChecked() && mAlipayName.length() > 0 && mAlipayUserName.length() > 0) ||
					(mWeixin.isChecked() && mWeixinName.length() > 0 && mWeixinUserName.length() > 0)
					){
				if(mCard.isChecked()){
					if(!Utility.isCard(mCardNumber.getText().toString())){
						showSmartToast("请输入正确的银行卡号", Toast.LENGTH_SHORT);
					}else{
						startReqTask(TechAccountFragment.this);
					}
				}else{
					startReqTask(TechAccountFragment.this);
				}
				
			}
			break;
		case R.id.tech_account_alipay:
			setCheck(2);
			break;
		case R.id.tech_account_card:
			setCheck(1);
			break;
		case R.id.tech_account_weixin:
			setCheck(3);
			break;

		default:
			break;
		}
	}
	private void setCheck(int type){
		mAlipayName.setEnabled(true);
		mAlipayUserName.setEnabled(true);
		mWeixinName.setEnabled(true);
		mWeixinUserName.setEnabled(true);
		mCardName.setEnabled(true);
		mCardUserName.setEnabled(true);
		mCardNumber.setEnabled(true);
		mCardName.setText("");
		mCardNumber.setText("");
		mCardUserName.setText("");
		mAlipayName.setText("");
		mAlipayUserName.setText("");
		mWeixinName.setText("");
		mWeixinUserName.setText("");
		switch (type) {
		case 1:
			mCard.setChecked(true);
			mWeixin.setChecked(false);
			mAlipay.setChecked(false);
			mAlipayName.setEnabled(false);
			mAlipayUserName.setEnabled(false);
			mWeixinName.setEnabled(false);
			mWeixinUserName.setEnabled(false);
			mCardName.requestFocus();
			break;
		case 2:
			mCard.setChecked(false);
			mWeixin.setChecked(false);
			mAlipay.setChecked(true);
			mWeixinName.setEnabled(false);
			mWeixinUserName.setEnabled(false);
			mCardName.setEnabled(false);
			mCardUserName.setEnabled(false);
			mCardNumber.setEnabled(false);
			mAlipayName.requestFocus();
			break;
		case 3:
			mCard.setChecked(false);
			mWeixin.setChecked(true);
			mAlipay.setChecked(false);
			mAlipayName.setEnabled(false);
			mAlipayUserName.setEnabled(false);
			mCardName.setEnabled(false);
			mCardUserName.setEnabled(false);
			mCardNumber.setEnabled(false);
			mWeixinName.requestFocus();
	break;

		default:
			break;
		}
	}
}
