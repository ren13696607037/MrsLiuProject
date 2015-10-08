package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.LoginParser;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.RegisterActivity;
import com.techfly.liutaitai.model.pcenter.activities.UserActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.MD5;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class LoginFragment extends CommonFragment implements OnClickListener {
	private LoginActivity mActivity;
	private User mUser = new User();
	private final int MSG_LOGIN = 0x102;
	private LinearLayout mLayout;
	private TextView mTvForget;
	private EditText mPhone;
	private EditText mPass;
	private Button mButton;
	private Button mRegister;
	private TextView mTextView;
	private int mExtra = 0;

	private Handler loginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOGIN:
				if (mUser.getmPhone() != null) {
					if(mExtra == 0){
						Toast.makeText(getActivity(), R.string.login_success,
								Toast.LENGTH_SHORT).show();
					}
					SharePreferenceUtils.getInstance(mActivity).saveUser(
							mUser);
					mActivity.setResult(Constant.LOGIN_SUCCESS);
					mActivity.finish();
				} else {
					if (!TextUtils.isEmpty(mUser.getmMessage())
							&& !"null".equals(mUser.getmMessage())) {
						Toast.makeText(getActivity(), mUser.getmMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), R.string.login_error,
								Toast.LENGTH_SHORT).show();
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
		mActivity = (LoginActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	private void onInitView(View view) {
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		setTitleText(R.string.welcome_login);
		mPass = (EditText) view.findViewById(R.id.login_pass);
		mPhone = (EditText) view.findViewById(R.id.login_phone);
		mButton = (Button) view.findViewById(R.id.login_button);
		mRegister = (Button) view.findViewById(R.id.register_button);
//		mLayout = (LinearLayout) view.findViewById(R.id.login_about);
		mTvForget = (TextView) view.findViewById(R.id.login_forget);
		mTextView = (TextView) view.findViewById(R.id.login_user);
		
		
		mButton.setOnClickListener(this);
		mRegister.setOnClickListener(this);
//		mLayout.setOnClickListener(this);
		mTvForget.setOnClickListener(this);
		mTextView.setOnClickListener(this);
	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.LOGIN_URL);
		url.setmGetParamPrefix(JsonKey.UserKey.NAME)
				.setmGetParamValues(mPhone.getText().toString())
				.setmGetParamPrefix(JsonKey.UserKey.PASS)
				.setmGetParamValues(MD5.getDigest(mPass.getText().toString()));
		url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
				JPushInterface.getRegistrationID(mActivity.getApplicationContext()));
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(LoginParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mUser = (User) object;
				AppLog.Loge("xll", mUser.toString());
//				mUser.setPass(mPass.getText().toString());
				if (!isDetached()) {
					loginHandler.removeMessages(MSG_LOGIN);
					loginHandler.sendEmptyMessage(MSG_LOGIN);
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
				showSmartToast(R.string.login_error, Toast.LENGTH_SHORT);
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
		case R.id.login_forget:
			Intent intent = new Intent(getActivity(), RegisterActivity.class);
			intent.putExtra(IntentBundleKey.TYPE, Constant.FORGET_INTENT);
			startActivityForResult(intent, Constant.FORGET_INTENT);
			break;
		case R.id.login_button:
			if (mPhone.length() != 0 && mPass.length() != 0) {
				if (Utility.isPhone(mPhone.getText().toString())) {
					startReqTask(LoginFragment.this);
				} else {
					SmartToast.makeText(getActivity(), R.string.phone_error,
							Toast.LENGTH_SHORT).show();
					mPhone.setText("");
					mPass.setText("");
				}
			} else {
				SmartToast.makeText(getActivity(), R.string.input_error,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.register_button:
			startActivityForResult(new Intent(getActivity(), RegisterActivity.class), Constant.REGISTER_INTENT);
			break;
		case R.id.login_user:
			startActivity(new Intent(getActivity(), UserActivity.class));
			break;

		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mExtra = 1;
		if(resultCode == Constant.REGISTER_SUCCESS){
			mPass.setText(data.getStringExtra(IntentBundleKey.USER_PASS));
			mPhone.setText(data.getStringExtra(IntentBundleKey.USER_NAME));
			startReqTask(LoginFragment.this);
		}else if(resultCode == Constant.FORGET_SUCCESS){
			mPass.setText(data.getStringExtra(IntentBundleKey.USER_PASS));
			mPhone.setText(data.getStringExtra(IntentBundleKey.USER_NAME));
			startReqTask(LoginFragment.this);
		}
	}

}
