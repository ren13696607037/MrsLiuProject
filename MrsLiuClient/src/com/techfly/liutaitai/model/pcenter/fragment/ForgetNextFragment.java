package com.techfly.liutaitai.model.pcenter.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.ForgetNextActivity;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ForgetNextFragment extends CommonFragment{
	private ForgetNextActivity mActivity;
	private CheckBox mCbNewSee;
	private CheckBox mCbOldSee;
	private EditText mEtNewPass;
	private EditText mEtOldPass;
	private Button mButton;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(ForgetNextActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgetnext, container, false);
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
    	setTitleText(R.string.forget_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mEtNewPass=(EditText) view.findViewById(R.id.next_et_new);
    	mEtOldPass=(EditText) view.findViewById(R.id.next_et_old);
    	mCbNewSee=(CheckBox) view.findViewById(R.id.next_see1);
    	mCbOldSee=(CheckBox) view.findViewById(R.id.next_see);
    	mButton=(Button) view.findViewById(R.id.next_btn);
    	mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEtNewPass.length()==0||mEtOldPass.length()==0){
					SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT);
				}else{
					startReqTask(ForgetNextFragment.this);
				}
			}
		});
    	
    	mCbOldSee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mEtOldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					mEtOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				mEtOldPass.setSelection(mEtOldPass.length());
			}
		});
    	mCbNewSee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mEtNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					mEtNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				mEtNewPass.setSelection(mEtNewPass.length());
			}
		});
    	
    }
	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PASS_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(mActivity).getUser().getmId());
        url.setmGetParamPrefix(JsonKey.UserKey.OPASS).setmGetParamValues(mEtOldPass.getText().toString());
        url.setmGetParamPrefix(JsonKey.UserKey.PASS).setmGetParamValues(mEtNewPass.getText().toString());
        param.setmHttpURL(url);
        param.setPostRequestMethod();
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
       
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ResultInfo info=(ResultInfo) object;
                if(!isDetached()){
                	if(info.getmCode()==0){
                		SmartToast.makeText(mActivity, R.string.pass_success, Toast.LENGTH_SHORT).show();
                		mActivity.finish();
                	}else{
                		if(info.getmMessage()==null||"null".equals(info.getmMessage())){
                			SmartToast.makeText(mActivity, R.string.pass_error, Toast.LENGTH_SHORT).show();
                		}else{
                			SmartToast.makeText(mActivity, info.getmMessage(), Toast.LENGTH_SHORT).show();
                		}
                	}
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
