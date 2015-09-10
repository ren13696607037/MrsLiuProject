package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.TechAccountActivity;
import com.techfly.liutaitai.model.pcenter.activities.TechCashActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class TechCashFragment extends CommonFragment implements OnClickListener{
	private TechCashActivity mActivity;
	private User mUser;
	private TextView mCashPrice;
	private TextView mCashAccount;
	private Button mButton;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TechCashActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
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
    	mButton = (Button) view.findViewById(R.id.cash_btn);
    	
    	mCashAccount.setOnClickListener(this);
    	mButton.setOnClickListener(this);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_CASH_URL);
		url.setmGetParamPrefix(JsonKey.TechnicianKey.MONEY).setmGetParamValues("");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cash_account:
			Intent intent = new Intent(mActivity, TechAccountActivity.class);
			startActivity(intent);
			break;
		case R.id.cash_btn:
			startReqTask(TechCashFragment.this);
			break;

		default:
			break;
		}
	}

}
