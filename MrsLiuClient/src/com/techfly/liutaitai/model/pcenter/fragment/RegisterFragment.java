package com.techfly.liutaitai.model.pcenter.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.RegisterParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.MD5;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class RegisterFragment extends CommonFragment implements OnClickListener{
	private EditText mEtPhone;
    private EditText mEtCode;
    private EditText mEtPass;
    private TextView mTvCode;
    private Button mButton;
    private boolean mIsCode = true;
    private User mUser = new User();
    private int MSG_TOTAL_TIME;
    private final int MSG_UPDATE_TIME = 500;
    private ProgressBar mProgressBar;
    private int mProgress;
    private String mToken;
    private Handler mRegisterHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mUser != null) {
            	if ("0".equals(mUser.getmId()) && mUser.getmPhone() != null) {
                    if (!TextUtils.isEmpty(mUser.getmMessage()) && !"null".equals(mUser.getmMessage())) {
                        SmartToast.makeText(getActivity(), mUser.getmMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else if(!"0".equals(mUser.getmId())){
                	mUser.setmPass(mEtPass.getText().toString());
                		SmartToast.makeText(getActivity(), R.string.register_success, Toast.LENGTH_SHORT).show();
                		SharePreferenceUtils.getInstance(getActivity()).saveUser(mUser);
                        getActivity().setResult(Constant.REGISTER_SUCCESS);
                        getActivity().finish();
                }else if ("0".equals(mUser.getmId()) && mUser.getmPhone()== null) {
                    if(!TextUtils.isEmpty(mUser.getmMessage()) && !"null".equals(mUser.getmMessage())){
                    	SmartToast.makeText(getActivity(), mUser.getmMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                    	SmartToast.makeText(getActivity(), R.string.register_error, Toast.LENGTH_SHORT).show();
                    }
                    mEtCode.setText("");
                    mEtPass.setText("");
                    MSG_TOTAL_TIME=-2;
                    Message message = new Message();
                    message.what = MSG_UPDATE_TIME;
                    timeHandler.sendMessage(message);
                }
            }
        }

    };
    public Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_UPDATE_TIME:
                MSG_TOTAL_TIME--;
                if (MSG_TOTAL_TIME > 0) {
                    mTvCode.setText(MSG_TOTAL_TIME + " 秒");
                    Message message = obtainMessage();
                    message.what = MSG_UPDATE_TIME;
                    sendMessageDelayed(message, 1000);
                } else if (MSG_TOTAL_TIME == -2) {
                    mTvCode.setText(R.string.addcode_resend);
                    mTvCode.setEnabled(true);
                } else {
                    mTvCode.setText(R.string.find_code_text);
                    mTvCode.setEnabled(true);
                }
                break;
            case 1:
                if (!Thread.currentThread().isInterrupted()) {
                    mProgressBar.setProgress(mProgress);
                }
                break;
            case 2:
                mProgressBar.setVisibility(View.GONE);
                Thread.currentThread().interrupt();
                break;

            default:
                break;
            }
        }

    };
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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
    	setTitleText(R.string.welcome_reg);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mEtCode = (EditText) view.findViewById(R.id.register_code);
        mEtPass = (EditText) view.findViewById(R.id.register_pass);
        mEtPhone = (EditText) view.findViewById(R.id.register_phone);
        mButton = (Button) view.findViewById(R.id.register_btn);
        mTvCode = (TextView) view.findViewById(R.id.register_code_text);
        mButton.setOnClickListener(this);
        mTvCode.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.code_progress);
    }
	@Override
	public void requestData() {
		if (mIsCode) {
            RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SMSCODE_URL);
            url.setmGetParamPrefix(JsonKey.UserKey.MOBILE).setmGetParamValues(mEtPhone.getText().toString());
            param.setmHttpURL(url);
            param.setPostRequestMethod();
            param.setmParserClassName(CommonParser.class.getName());
            RequestManager.getRequestData(getActivity(), createReqSuccessListener(), createReqErrorListener(), param);
        } else{
            RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL +Constant.REGISTER_URL);
            url.setmGetParamPrefix(JsonKey.UserKey.MOBILE).setmGetParamValues(mEtPhone.getText().toString()).setmGetParamPrefix(JsonKey.UserKey.PASS)
                    .setmGetParamValues(MD5.getDigest(mEtPass.getText().toString()));
            param.setPostRequestMethod();
            param.setmHttpURL(url);
            param.setmParserClassName(RegisterParser.class.getName());
            RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
        } 
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
//                mUser = (User) object;
                if (!isDetached()) {
                	String code = (String) object;
                	if(code == "200"){
                		User user = new User();
                		user.setmPhone(mEtPhone.getText().toString());
                		user.setmId("1");
                		SmartToast.makeText(getActivity(), R.string.register_success, Toast.LENGTH_SHORT).show();
                		SharePreferenceUtils.getInstance(getActivity()).saveUser(mUser);
                        getActivity().setResult(Constant.REGISTER_SUCCESS);
                        getActivity().finish();
                	}else{
                		SmartToast.makeText(getActivity(), R.string.register_error, Toast.LENGTH_SHORT).show();
                	}
                    timeHandler.sendEmptyMessage(2);
//                    mRegisterHandler.removeMessages(0);
//                    mRegisterHandler.sendEmptyMessage(0);
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
                AppLog.Loge(" data failed to load" + error.getMessage());
                showSmartToast(R.string.register_error, Toast.LENGTH_SHORT);
                if (isDetached()) {
                    timeHandler.sendEmptyMessage(2);
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }
    private Response.Listener<Object> createReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mToken = (String) object;
//                ResultInfo info=(ResultInfo) object;
//                AppLog.Loge("xll", info.getmCode()+"="+info.getmData()+"="+info.getmMessage());
                if(!isDetached()){
                	mEtCode.setText("");
                	mEtPass.setText("");
//                	mToken=info.getmData();
                	Message msg1 = new Message();
                    msg1.what = 2;
                    timeHandler.sendMessage(msg1);
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                showSmartToast(R.string.register_error, Toast.LENGTH_SHORT);
                Message msg = new Message();
                msg.what = 2;
                timeHandler.sendMessage(msg);
                MSG_TOTAL_TIME=0;
                Message message = new Message();
                message.what = MSG_UPDATE_TIME;
                timeHandler.sendMessageDelayed(message,10000);
                if(!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
       };
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.register_btn:
            mIsCode = false;
            toRegister();
            break;
        case R.id.register_code_text:
            mIsCode = true;
            getCode();
            break;
        default:
            break;
        }
	}
	 private void getCode() {
	        if (mEtPhone.length() == 0) {
	            SmartToast.makeText(getActivity(), R.string.input_error, Toast.LENGTH_SHORT).show();
	        } else {
	            if (!Utility.isPhone(mEtPhone.getText().toString())) {
	                SmartToast.makeText(getActivity(), R.string.phone_error, Toast.LENGTH_SHORT).show();
	                mEtPhone.setText("");
	            } else {
	                mTvCode.setEnabled(false);
	                MSG_TOTAL_TIME = 60;
	                Toast.makeText(getActivity(), "短信已发送！", Toast.LENGTH_SHORT).show();
	                Message message = new Message();
	                message.what = MSG_UPDATE_TIME;
	                timeHandler.sendMessage(message);
	                showProgress();
	                requestData();
	                mEtCode.requestFocus();
	            }
	        }
	    }

	    private void toRegister() {
	        if (mEtPhone.length() == 0 || mEtPass.length() == 0 || mEtCode.length() == 0) {
	            SmartToast.makeText(getActivity(), R.string.input_error, Toast.LENGTH_SHORT).show();
	        } else {
	            if (mEtPass.length() > 5&&mEtPass.length()<13) {
//	            	if(mEtCode.getText().toString().equals(mToken)){
	            		startReqTask(this);
//	            	}else{
//	            		showSmartToast(R.string.register_code_error, Toast.LENGTH_SHORT);
//	            	}
	            } else {
	                showSmartToast(R.string.register_pass_error, Toast.LENGTH_SHORT);
	                mEtPass.setText("");
	            }
	        }
	    }

	    private void showProgress() {
	        mProgressBar.setVisibility(View.VISIBLE);
	        mProgressBar.setProgress(0);
	        Thread thread = new Thread(new Runnable() {

	            @Override
	            public void run() {
	                for (int i = 0; i < 100; i++) {
	                    try {
	                        mProgress = i;
	                        Thread.sleep(1000);
	                        if (i == 99) {
	                            Message msg = new Message();
	                            msg.what = 2;
	                            timeHandler.sendMessage(msg);
	                        } else {
	                            Message msg = new Message();
	                            msg.what = 1;
	                            timeHandler.sendMessage(msg);
	                        }
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        });
	        thread.start();
	    }

}
