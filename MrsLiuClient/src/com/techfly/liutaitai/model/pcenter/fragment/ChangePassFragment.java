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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.ChangePassActivity;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.MD5;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ChangePassFragment extends CommonFragment {
	private ChangePassActivity mActivity;
	private EditText mOldPass;
	private EditText mNewPass;
	private EditText mRePass;
	private Button mButton;
	private final int MSG_CHANGE=0x101;
	private ResultInfo mInfo;
	public Handler mChangePass=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE:
				if(mInfo.getmCode() == 10){
					showSmartToast(R.string.pass_success, Toast.LENGTH_SHORT);
					mActivity.finish();
				}else{
					if(mInfo.getmMessage() != null && !"".equals(mInfo.getmMessage()) && !TextUtils.isEmpty(mInfo.getmMessage())){
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.pass_error, Toast.LENGTH_SHORT);
					}
					mNewPass.setText("");
	                mOldPass.setText("");
	                mRePass.setText("");
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
        mActivity = (ChangePassActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass,
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
    	setTitleText(R.string.pinfo_pass);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mOldPass = (EditText) view.findViewById(R.id.changepass_old);
    	mNewPass = (EditText) view.findViewById(R.id.changepass_new);
    	mRePass = (EditText) view.findViewById(R.id.changepass_repass);
    	mButton = (Button) view.findViewById(R.id.changepass_btn);
    	mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mNewPass.length()==0||mOldPass.length()==0||mRePass.length()==0){
					SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
				}else{
					if (mNewPass.length() > 5 && mNewPass.length() < 13 && mOldPass.length()>5 && mOldPass.length() < 13 && mRePass.length()>5 && mRePass.length() < 13 ) {
						if(mNewPass.getText().toString().equals(mRePass.getText().toString())){
							startReqTask(ChangePassFragment.this);
						}else{
							showSmartToast(R.string.register_repass_error, Toast.LENGTH_SHORT);
						}
		            } else {
		                showSmartToast(R.string.register_pass_error, Toast.LENGTH_SHORT);
		                mNewPass.setText("");
		                mOldPass.setText("");
		                mRePass.setText("");
		            }
				}
			}
		});
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PASS_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.MOBILE).setmGetParamValues(SharePreferenceUtils.getInstance(mActivity).getUser().getmPhone()+"");
        url.setmGetParamPrefix(JsonKey.UserKey.PASS).setmGetParamValues(MD5.getDigest(mOldPass.getText().toString()));
        url.setmGetParamPrefix(JsonKey.UserKey.NPASS).setmGetParamValues(MD5.getDigest(mNewPass.getText().toString()));
        param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
}
private Response.Listener<Object> createMyReqSuccessListener() {
    return new Listener<Object>() {
        @Override
        public void onResponse(Object object) {
            AppLog.Logd(object.toString());
            mInfo = (ResultInfo) object;
            if (!isDetached()) {
                mChangePass.removeMessages(MSG_CHANGE);
                mChangePass.sendEmptyMessage(MSG_CHANGE);
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
            if (isDetached()) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
            }
        }
    };
}

}
