package com.techfly.liutaitai.model.pcenter.fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.AddressManageParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.Constant;


import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeAddFragment extends CommonFragment {
    private ChangeAddressActivity mActivity;
    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtAddress;
    private Button mButton;
    private String mExtra;
    private AddressManage mAddressManage=null;
	private ResultInfo mInfo;
	private User mUser;
	private final int MSG_DATA=0x101;
	public Handler mChangeHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DATA:
				if(mInfo.getmCode()==0){
            		if(mAddressManage==null){
            			showSmartToast(R.string.address_success, Toast.LENGTH_SHORT);
            		}else{
//            			showSmartToast(R.string.addchange_success, Toast.LENGTH_SHORT);
            		}
//            		mActivity.setResult(Constant.CHANGE_ADD_SUCCESS);
                	mActivity.finish();
				}else{
					showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
				}
				break;

			default:
				break;
			}
		}
		
	};
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(ChangeAddressActivity) activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        mExtra=mActivity.getIntent().getStringExtra(IntentBundleKey.CHANGEADD_ID);
        mAddressManage=(AddressManage) mActivity.getIntent().getSerializableExtra(IntentBundleKey.CHANGEADD_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changeadd, container, false);
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
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        mEtAddress=(EditText) view.findViewById(R.id.changeadd_add);
        mEtName=(EditText) view.findViewById(R.id.changeadd_name);
        mEtPhone=(EditText) view.findViewById(R.id.changeadd_phone);
        mButton=(Button) view.findViewById(R.id.changeadd_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
            	String name=mEtName.getText().toString();
            	String phone=mEtPhone.getText().toString();
            	String address=mEtAddress.getText().toString();
            	if(mEtAddress.length()==0||mEtName.length()==0||mEtPhone.length()==0){
            		SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
            	}else{
            		if(!Utility.isPhone(phone)){
            			SmartToast.makeText(mActivity, R.string.phone_error, Toast.LENGTH_SHORT).show();
            			mEtPhone.setText("");
            		}else{
            			//TODO
                    }
            	}
            }
        });
        if(!TextUtils.isEmpty(mExtra)){
        	setTitleText(mExtra);
        }else{
//        	setTitleText(R.string.changeadd_title);
//        	mEtAddress.setText(mItem.getmAddress());
//        	mEtName.setText(mItem.getmName());
//        	mEtPhone.setText(mItem.getmPhone());
        }
    }
    @Override
    public void requestData() {
    	RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.ADDRESS_URL);
			param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setmHttpURL(url);
			param.setmParserClassName(AddressManageParser.class.getName());
			RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mInfo=(ResultInfo) object;
                if(!isDetached()){
                	mChangeHandler.removeMessages(MSG_DATA);
                	mChangeHandler.sendEmptyMessage(MSG_DATA);
                	mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                if(!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
       };
    }

}
