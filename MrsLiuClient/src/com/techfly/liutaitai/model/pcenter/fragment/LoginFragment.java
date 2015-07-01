package com.techfly.liutaitai.model.pcenter.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.LoginParser;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.RegisterActivity;
import com.techfly.liutaitai.model.pcenter.adapter.AutoCompleteAdapter;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class LoginFragment extends CommonFragment implements OnClickListener{
	
	private LoginActivity mActivity;
	private EditText mPhone;
	private EditText mPass;
	private TextView mForget;
	private CheckBox mCbSee;
	private Button mLogin;
//	private AutoCompleteTextView mEmail;
	private AutoCompleteAdapter mEmailAdaper;
	private User mUser=new User();
    private final int MSG_LOGIN=0x102;
    private Handler mLoginHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOGIN:
				 if(mUser.getmPhone()!=null){
	                    Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_SHORT).show();
	                    SharePreferenceUtils.getInstance(mActivity).saveUser(mUser);
	                    OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null); 
	                    mActivity.setResult(Constant.LOGIN_SUCCESS);
	                    mActivity.finish();
	                }else{
	                    if(!TextUtils.isEmpty(mUser.getmMessage())&&!"null".equals(mUser.getmMessage())){
	                        Toast.makeText(mActivity, mUser.getmMessage(), Toast.LENGTH_SHORT).show();
	                    }else{
	                        Toast.makeText(mActivity, R.string.login_error, Toast.LENGTH_SHORT).show();
	                    }
	                    mPass.setText("");
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
        mActivity=(LoginActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmailAdaper = new AutoCompleteAdapter(mActivity);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
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
    	setTitleText(R.string.welcome_login);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	setRightText(R.string.welcome_reg, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mActivity,RegisterActivity.class);
				startActivityForResult(intent, Constant.REGISTER_INTENT);
			}
		});
    	
    	mForget=(TextView) view.findViewById(R.id.login_forget);
    	mLogin=(Button) view.findViewById(R.id.login_button);
    	mPass=(EditText) view.findViewById(R.id.login_pass);
    	mPhone=(EditText) view.findViewById(R.id.login_phone);
    	mCbSee=(CheckBox) view.findViewById(R.id.login_pass_see);
//    	mPhone=(EditText) view.findViewById(R.id.login_name);
    	
    	mForget.setOnClickListener(this);
    	mLogin.setOnClickListener(this);
    	mCbSee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					mPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				mPass.setSelection(mPass.length());
			}
		});
//    	mEmail.setAdapter(mEmailAdaper);
//        mEmail.addTextChangedListener(new TextWatcher() {
//      	private int beforeTextIndex;
//
//          @Override
//          public void onTextChanged(CharSequence s, int start, int before,
//                  int count) {
//              AppLog.Logi("onTextChanged>>" + "content:" + s + " start:"
//                      + start + " before:" + before + " count:" + count);
//          }
//
//          @Override
//          public void beforeTextChanged(CharSequence s, int start, int count,
//                  int after) {
//              beforeTextIndex = s.toString().indexOf('@');
//              AppLog.Logi("beforeTextChanged>>" + "content:" + s
//                      + " start:" + start + " after:" + after + " count:"
//                      + count);
//          }
//
//          @Override
//          public void afterTextChanged(Editable s) {
//              AppLog.Logi("afterTextChanged>>" + "content:" + s);
//              String input = s.toString();
//              int afterTextIndex = input.indexOf('@');
//              mEmailAdaper.mList.clear();
//              if (input.length() > 0) {
//                  if (afterTextIndex == -1) {
//                      for (int i = 0; i < Constant.EMAILS.length; i++) {
//                          mEmailAdaper.mList.add(input + Constant.EMAILS[i]);
//                      }
//                  } else if (afterTextIndex != beforeTextIndex
//                          && afterTextIndex >= 1) {
//                  	
//                  } else if (afterTextIndex == beforeTextIndex) {
//
//                  }
//              }
//              mEmailAdaper.notifyDataSetChanged();
//              mEmail.showDropDown();
//          }
//		});
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
     
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.LOGIN_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.MOBILE).setmGetParamValues(mPhone.getText().toString())
           .setmGetParamPrefix(JsonKey.UserKey.PASS).setmGetParamValues(mPass.getText().toString());
     
        param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(LoginParser.class.getName());
        RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
       
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mUser=(User) object;
                if(!isDetached()){
                	mLoginHandler.removeMessages(MSG_LOGIN);
                	mLoginHandler.sendEmptyMessage(MSG_LOGIN);
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
                showSmartToast(R.string.login_error, Toast.LENGTH_SHORT);
                if(!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
       };
    }

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.login_forget:
//			intent=new Intent(mActivity,ForgetFirstActivity.class);
//			startActivityForResult(intent, Constant.FORGET_INTENT);
			showSmartToast(R.string.app_toast, Toast.LENGTH_SHORT);
			break;
		case R.id.login_button:
			toLogin();
			break;

		default:
			break;
		}
		
	}
	private void toLogin(){
		if(mPass.length()==0||mPhone.length()==0){
			SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
		}else{

//			if(!Utility.isPhone(mPhone.getText().toString())){
//				SmartToast.makeText(mActivity, R.string.phone_error, Toast.LENGTH_SHORT).show();
//			}else{
			startReqTask(LoginFragment.this);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Constant.REGISTER_SUCCESS:
			mActivity.setResult(Constant.LOGIN_SUCCESS);
			mActivity.finish();
			break;
		case Constant.FORGET_SUCCESS:
			mActivity.setResult(Constant.LOGIN_SUCCESS);
			mActivity.finish();
			break;
		default:
			break;
		}
	}

}
