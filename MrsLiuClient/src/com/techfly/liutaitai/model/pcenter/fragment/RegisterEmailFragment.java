package com.techfly.liutaitai.model.pcenter.fragment;


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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.EmailCodeParser;
import com.techfly.liutaitai.bizz.parser.RegisterParser;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
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
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class RegisterEmailFragment extends CommonFragment implements OnClickListener ,OnCheckedChangeListener{
	private RegisterActivity mActivity;
	private AutoCompleteTextView mEmail;
	private EditText mCode;
	private ImageView mTvCode;
	private ImageButton mIbCode;
	private EditText mPass;
	private CheckBox mCheckBox;
	private CheckBox mCbPassSee;
	private TextView mTextView;
	private EditText mName;
	private Button mButton;
    private String mToken;
    private boolean mIsCode = true;
    private User mUser = new User();
    private String mPhoneString;
    private String mCodeString;
    private String mPassString;
    private AutoCompleteAdapter mEmailAdaper;
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
            }
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
        mEmailAdaper = new AutoCompleteAdapter(mActivity);
        startReqTask(RegisterEmailFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, container, false);
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
    	
    	mPass=(EditText) view.findViewById(R.id.registerem_pass);
    	mEmail=(AutoCompleteTextView) view.findViewById(R.id.register_email);
    	mName=(EditText) view.findViewById(R.id.register_username);
    	mButton=(Button) view.findViewById(R.id.registerem_btn);
    	mTvCode=(ImageView) view.findViewById(R.id.register_code_text);
    	mCode=(EditText) view.findViewById(R.id.registerem_code);
    	mCheckBox=(CheckBox) view.findViewById(R.id.registerem_check);
    	mTextView=(TextView) view.findViewById(R.id.registerem_text);
    	mCbPassSee=(CheckBox) view.findViewById(R.id.registerem_pass_resee);
    	mIbCode=(ImageButton) view.findViewById(R.id.registerem_refresh_btn);
    	
    	mButton.setOnClickListener(this);
    	mIbCode.setOnClickListener(this);
    	mCbPassSee.setOnCheckedChangeListener(this);
    	mCheckBox.setOnCheckedChangeListener(this);
    	
    	SpannableString ss = new SpannableString(mActivity.getString(R.string.register_text3));  
        //用颜色标记文本
        ss.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_blue)), 6, 10,  
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_blue)), 11, 15,  
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setText(ss);
        setButton();
        mEmail.setAdapter(mEmailAdaper);
        mEmail.addTextChangedListener(new TextWatcher() {
      	private int beforeTextIndex;

          @Override
          public void onTextChanged(CharSequence s, int start, int before,
                  int count) {
              AppLog.Logi("onTextChanged>>" + "content:" + s + " start:"
                      + start + " before:" + before + " count:" + count);
          }

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count,
                  int after) {
              beforeTextIndex = s.toString().indexOf('@');
              AppLog.Logi("beforeTextChanged>>" + "content:" + s
                      + " start:" + start + " after:" + after + " count:"
                      + count);
          }

          @Override
          public void afterTextChanged(Editable s) {
              AppLog.Logi("afterTextChanged>>" + "content:" + s);
              String input = s.toString();
              int afterTextIndex = input.indexOf('@');
              mEmailAdaper.mList.clear();
              if (input.length() > 0) {
                  if (afterTextIndex == -1) {
                      for (int i = 0; i < Constant.EMAILS.length; i++) {
                          mEmailAdaper.mList.add(input + Constant.EMAILS[i]);
                      }
                  } else if (afterTextIndex != beforeTextIndex
                          && afterTextIndex >= 1) {
                  	
                  } else if (afterTextIndex == beforeTextIndex) {

                  }
                  mButton.setEnabled(true);
    				mButton.setBackgroundResource(R.drawable.login_button_blue);
              }else{
            	  mButton.setEnabled(false);
    				mButton.setBackgroundResource(R.drawable.login_button_gray);
              }
              mEmailAdaper.notifyDataSetChanged();
              mEmail.showDropDown();
          }
		});
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
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.EMAIL_CODE_URL);
            param.setmHttpURL(url);
            param.setmParserClassName(EmailCodeParser.class.getName());
            RequestManager.getRequestData(mActivity, createReqSuccessListener(), createReqErrorListener(), param);
		}else{
			RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.REGISTER_EMAIL_URL);
            url.setmGetParamPrefix(JsonKey.UserKey.USERNAME).setmGetParamValues(mName.getText().toString()).setmGetParamPrefix(JsonKey.UserKey.EMAIL)
                    .setmGetParamValues(mEmail.getText().toString()).setmGetParamPrefix(JsonKey.UserKey.SMS)
                    .setmGetParamValues(mCode.getText().toString()).setmGetParamPrefix(JsonKey.UserKey.TOKEN).setmGetParamValues(mToken);
            url.setmGetParamPrefix(JsonKey.UserKey.PASS).setmGetParamValues(mPass.getText().toString());
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
                if(!isDetached()){
                	mCode.setText("");
                	mToken=info.getmData();
                	Message msg1 = new Message();
                    msg1.what = 2;
                    if(info.getmMessage().contains("http")){
                    	ImageLoader.getInstance().displayImage(info.getmMessage(), mTvCode);
                    }
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
		case R.id.registerem_btn:
			mIsCode = false;
			toRegister();
			break;
		case R.id.registerem_refresh_btn:
			mIsCode = true;
			requestData();
			break;

		default:
			break;
		}
	}
	
	
	private void toRegister(){
		if(mName.length()==0||mCode.length()==0||mPass.length()==0||mEmail.length()==0){
			SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
		}else{
			if (mPass.length() > 5) {
				startReqTask(this);
            } else {
                showSmartToast(R.string.register_pass_error, Toast.LENGTH_SHORT);
                mPass.setText("");
            }
		}
	}
	

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.registerem_pass_resee:
			if(isChecked){
				mPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}else{
				mPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			mPass.setSelection(mPass.length());
			break;
		case R.id.registerem_check:
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
//		mPhone.addTextChangedListener(new MyWatcher(mPhoneString));
		mCode.addTextChangedListener(new MyWatcher(mCodeString));
		mPass.addTextChangedListener(new MyWatcher(mPassString));
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
