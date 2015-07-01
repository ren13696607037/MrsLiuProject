package com.hylsmart.yihui.model.pcenter.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.parser.RegisterParser;
import com.hylsmart.yihui.bizz.shopcar.OnShopCarLisManager;
import com.hylsmart.yihui.model.pcenter.activities.RegisterActivity;
import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.SmartToast;
import com.hylsmart.yihui.util.Utility;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class RegisterFragment extends CommonFragment implements OnClickListener ,OnCheckedChangeListener{
	private RegisterActivity mActivity;
	private EditText mPhone;
	private EditText mCode;
	private TextView mTvCode;
	private EditText mPass;
	private EditText mRepass;
	private CheckBox mCheckBox;
	private CheckBox mCbPassSee;
	private CheckBox mCbRepassSee;
	private TextView mTextView;
	private Button mButton;
	private int MSG_TOTAL_TIME;
    private final int MSG_UPDATE_TIME = 500;
    private ProgressBar mProgressBar;
    private int mProgress;
    private String mToken;
    private boolean mIsCode = true;
    private User mUser = new User();
    private String mPhoneString;
    private String mCodeString;
    private String mPassString;
    private String mRepassString;
    private Handler mRegisterHandler=new Handler(){
    	@Override
        public void handleMessage(Message msg) {
    	if (mUser != null) {
        	if(mUser.getmId()!=null){
            	mUser.setmPass(mPass.getText().toString());
            		SmartToast.makeText(mActivity, R.string.register_success, Toast.LENGTH_SHORT).show();
            		SharePreferenceUtils.getInstance(mActivity).saveUser(mUser);
            		OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null); 
                    mActivity.setResult(Constant.REGISTER_SUCCESS);
                    mActivity.finish();
            }else if (mUser.getmId()== null && mUser.getmPhone()== null) {
                if(!TextUtils.isEmpty(mUser.getmMessage()) && !"null".equals(mUser.getmMessage())){
                	SmartToast.makeText(mActivity, mUser.getmMessage(), Toast.LENGTH_SHORT).show();
                }else{
                	SmartToast.makeText(mActivity, R.string.register_error, Toast.LENGTH_SHORT).show();
                }
                mCode.setText("");
                mPass.setText("");
                mRepass.setText("");
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
                    mTvCode.setText(R.string.register_resend);
                    mTvCode.setEnabled(true);
                } else {
                    mTvCode.setText(R.string.register_code);
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
        mActivity=(RegisterActivity) activity;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.welcome_reg);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mPass=(EditText) view.findViewById(R.id.register_pass);
    	mPhone=(EditText) view.findViewById(R.id.register_phone);
    	mButton=(Button) view.findViewById(R.id.register_btn);
    	mTvCode=(TextView) view.findViewById(R.id.register_code);
    	mCode=(EditText) view.findViewById(R.id.register_code_et);
    	mProgressBar=(ProgressBar) view.findViewById(R.id.code_progress);
    	mRepass=(EditText) view.findViewById(R.id.register_repass);
    	mCheckBox=(CheckBox) view.findViewById(R.id.register_check);
    	mTextView=(TextView) view.findViewById(R.id.register_text);
    	mCbPassSee=(CheckBox) view.findViewById(R.id.register_pass_see);
    	mCbRepassSee=(CheckBox) view.findViewById(R.id.register_pass_resee);
    	
    	mButton.setOnClickListener(this);
    	mTvCode.setOnClickListener(this);
    	mCbPassSee.setOnCheckedChangeListener(this);
    	mCbRepassSee.setOnCheckedChangeListener(this);
    	mCheckBox.setOnCheckedChangeListener(this);
    	
    	SpannableString ss = new SpannableString(mActivity.getString(R.string.register_text3));  
        //用颜色标记文本
        ss.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_blue)), 6, 10,  
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_blue)), 11, 15,  
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setText(ss);
        setButton();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

	@Override
	public void requestData() {
		if(mIsCode){
			RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SMSCODE_URL);
            url.setmGetParamPrefix(JsonKey.UserKey.MOBILE).setmGetParamValues(mPhone.getText().toString());
            param.setmHttpURL(url);
            param.setmParserClassName(CommonParser.class.getName());
            RequestManager.getRequestData(mActivity, createReqSuccessListener(), createReqErrorListener(), param);
		}else{
			RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.REGISTER_URL);
            url.setmGetParamPrefix(JsonKey.UserKey.MOBILE).setmGetParamValues(mPhone.getText().toString()).setmGetParamPrefix(JsonKey.UserKey.PASS)
                    .setmGetParamValues(mPass.getText().toString());
            param.setPostRequestMethod();
            param.setmHttpURL(url);
            param.setmParserClassName(RegisterParser.class.getName());
            RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
		}
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mUser = (User) object;
                if (!isDetached()) {
                    timeHandler.sendEmptyMessage(2);
                    mRegisterHandler.removeMessages(0);
                    mRegisterHandler.sendEmptyMessage(0);
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
//                showSmartToast(R.string.register_error, Toast.LENGTH_SHORT);
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
                ResultInfo info=(ResultInfo) object;
                showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
                if(!isDetached()){
                	mCode.setText("");
                	mPass.setText("");
                	mRepass.setText("");
                	mToken=info.getmData();
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
		case R.id.register_code:
			mIsCode = true;
			getCode();
			break;

		default:
			break;
		}
	}
	
	private void getCode(){
		if(mPhone.length()!=0){
			if(Utility.isPhone(mPhone.getText().toString())){
				 mTvCode.setEnabled(false);
	                MSG_TOTAL_TIME = 60;
	                Toast.makeText(mActivity, "短信已发送！", Toast.LENGTH_SHORT).show();
	                Message message = new Message();
	                message.what = MSG_UPDATE_TIME;
	                timeHandler.sendMessage(message);
	                showProgress();
	                requestData();
	                mCode.requestFocus();
			}else{
				SmartToast.makeText(mActivity, R.string.phone_error, Toast.LENGTH_SHORT).show();
			}
		}else{
			SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void toRegister(){
		if(mPhone.length()==0||mPass.length()==0||mRepass.length()==0){
			SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
		}else{
			if (mPass.length() > 5&&mRepass.length()>5) {
				if(mPass.getText().toString().equals(mRepass.getText().toString())){
					startReqTask(this);
				}else{
					showSmartToast(R.string.register_repass_error, Toast.LENGTH_SHORT);
				}
            } else {
                showSmartToast(R.string.register_pass_error, Toast.LENGTH_SHORT);
                mPass.setText("");
                mRepass.setText("");
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.register_pass_resee:
			if(isChecked){
				mRepass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}else{
				mRepass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			mRepass.setSelection(mRepass.length());
			break;
		case R.id.register_pass_see:
			if(isChecked){
				mPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}else{
				mPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			mPass.setSelection(mPass.length());
			break;
		case R.id.register_check:
			if(isChecked){
				mButton.setEnabled(true);
			}else{
				mButton.setEnabled(false);
			}
			break;

		default:
			break;
		}
	}
	private void setButton(){
		mPhone.addTextChangedListener(new MyWatcher(mPhoneString));
		mCode.addTextChangedListener(new MyWatcher(mCodeString));
		mPass.addTextChangedListener(new MyWatcher(mPassString));
		mRepass.addTextChangedListener(new MyWatcher(mRepassString));
	}
	private class MyWatcher implements TextWatcher{
		private String mString;
		
		public MyWatcher(String string){
			this.mString=string;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			mString= s.toString();
			if(mString.length()>0){
				mButton.setEnabled(true);
				mButton.setBackgroundResource(R.drawable.login_button_blue);
			}else{
				mButton.setEnabled(false);
				mButton.setBackgroundResource(R.drawable.login_button_gray);
			}
		}
		
	}

}
