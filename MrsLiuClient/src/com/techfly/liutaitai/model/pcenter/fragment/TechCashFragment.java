package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.TechCashInfoParser;
import com.techfly.liutaitai.model.pcenter.activities.TechAccountActivity;
import com.techfly.liutaitai.model.pcenter.activities.TechCashActivity;
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
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class TechCashFragment extends CommonFragment implements OnClickListener{
	private TechCashActivity mActivity;
	private User mUser;
	private TextView mCashPrice;
	private RelativeLayout mCashAccountLayout;
	private TextView mCashAccount;
	private TextView mCashAccountText;
	private TextView mCashName;
	private TextView mCashBank;
	private Button mButton;
	private boolean isSubmit;
	private TechAccount mTechAccount = null;
	private String mPrice = null;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TechCashActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        mPrice = mActivity.getIntent().getStringExtra(IntentBundleKey.TECH_MONEY);
        startReqTask(TechCashFragment.this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_cash,
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
    	setTitleText(R.string.service_text4);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mCashPrice = (TextView) view.findViewById(R.id.cash_text);
    	mCashAccount = (TextView) view.findViewById(R.id.cash_account);
    	mCashAccountText = (TextView) view.findViewById(R.id.cash_account_text);
    	mCashAccountLayout = (RelativeLayout) view.findViewById(R.id.cash_rel);
    	mCashBank = (TextView) view.findViewById(R.id.cash_bank);
    	mCashName = (TextView) view.findViewById(R.id.cash_name);
    	mButton = (Button) view.findViewById(R.id.cash_btn);
    	
    	mCashAccountLayout.setOnClickListener(this);
    	mButton.setOnClickListener(this);
    	
    	mCashPrice.setText(getString(R.string.cash_text, mPrice));
    }
    
    private void setData(){
    	if(mTechAccount != null){
    		mCashAccountText.setVisibility(View.INVISIBLE);
    		mCashAccount.setVisibility(View.VISIBLE);
    		mCashName.setVisibility(View.VISIBLE);
    		if(mTechAccount.getmType() != null && Integer.valueOf(mTechAccount.getmType()) == 0){
    			mCashBank.setVisibility(View.VISIBLE);
    			mCashBank.setText(getString(R.string.cash_card_text, mTechAccount.getmBank()));
    			mCashAccount.setText(getString(R.string.cash_card_text1, mTechAccount.getmAccount()));
    			mCashName.setText(getString(R.string.cash_card_text2, mTechAccount.getmName()));
    		}else if(mTechAccount.getmType() != null &&Integer.valueOf(mTechAccount.getmType()) == 1){
    			mCashAccount.setText(getString(R.string.cash_alipay_text1, mTechAccount.getmAccount()));
    			mCashName.setText(getString(R.string.cash_alipay_text2, mTechAccount.getmName()));
    		}else if(mTechAccount.getmType() != null &&Integer.valueOf(mTechAccount.getmType()) == 2){
    			mCashAccount.setText(getString(R.string.cash_weixin_text1, mTechAccount.getmAccount()));
    			mCashName.setText(getString(R.string.cash_alipay_text2, mTechAccount.getmName()));
    		}
    	}
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		if(isSubmit){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_CASH_URL);
			url.setmGetParamPrefix(JsonKey.TechnicianKey.MONEY).setmGetParamValues(mPrice);
			url.setmGetParamPrefix(JsonKey.TechnicianKey.BID).setmGetParamValues(mTechAccount.getmId());
			param.setmParserClassName(CommonParser.class.getName());
		}else{
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_CASH_INFO_URL);
			param.setmParserClassName(TechCashInfoParser.class.getName());
		}
		
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
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
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					if(object instanceof ResultInfo){
						ResultInfo info = (ResultInfo) object;
						if(info.getmCode() == 0){
							showSmartToast(R.string.submit_success, Toast.LENGTH_SHORT);
							mActivity.finish();
						}else{
							if(info.getmMessage()!=null&&!TextUtils.isEmpty(info.getmMessage())){
								showSmartToast(R.string.submit_fail, Toast.LENGTH_SHORT);
							}else{
								showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
							}
						}
					}else if(object instanceof TechAccount){
						mTechAccount = (TechAccount) object;
						setData();
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
		case R.id.cash_rel:
			Intent intent = new Intent(mActivity, TechAccountActivity.class);
			intent.putExtra(IntentBundleKey.TECH_CASH_ID, mTechAccount);
			startActivity(intent);
			break;
		case R.id.cash_btn:
			if(mTechAccount != null){
				isSubmit = true;
				startReqTask(TechCashFragment.this);
			}
			break;

		default:
			break;
		}
	}

}
