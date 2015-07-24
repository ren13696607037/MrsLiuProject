package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.AddressManageParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangeNickActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangePassActivity;
import com.techfly.liutaitai.model.pcenter.activities.ForgetNextActivity;
import com.techfly.liutaitai.model.pcenter.activities.PcenterInfoActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class PcenterInfoFragment extends CommonFragment implements OnClickListener{
	private PcenterInfoActivity mActivity;
	private RelativeLayout mRlNick;
	private RelativeLayout mRlChange;
	private User mUser;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(PcenterInfoActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(PcenterInfoFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcenterinfo, container, false);
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
    	setTitleText(R.string.pinfo_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mRlChange = (RelativeLayout) view.findViewById(R.id.info_change);
    	mRlNick = (RelativeLayout) view.findViewById(R.id.info_nick);
    	
    	mRlChange.setOnClickListener(this);
    	mRlNick.setOnClickListener(this);
    }

	@Override
	public void requestData() {
//		RequestParam param = new RequestParam();
//		HttpURL url = new HttpURL();
//		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ADDRESS_URL);
//		url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL)
//				.setmGetParamValues(
//						SharePreferenceUtils.getInstance(mActivity)
//								.getUser().getmId());
//		param.setmHttpURL(url);
//		param.setmParserClassName(AddressManageParser.class.getName());
//		RequestManager.getRequestData(mActivity,
//				createMyReqSuccessListener(), createMyReqErrorListener(),
//				param);
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
//				AppLog.Logd(object.toString());
//				mList = (ArrayList<Address>) object;
//				if (!isDetached()) {
//					mInfoHandler.removeMessages(MSG_LIST);
//					mInfoHandler.sendEmptyMessage(MSG_LIST);
//					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
//					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
//				}
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
		case R.id.info_change:
			startActivity(new Intent(mActivity,ChangePassActivity.class));
			break;
		case R.id.info_nick:
			startActivity(new Intent(mActivity,ChangeNickActivity.class));
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
//        startReqTask(PcenterInfoFragment.this);
	}

}
