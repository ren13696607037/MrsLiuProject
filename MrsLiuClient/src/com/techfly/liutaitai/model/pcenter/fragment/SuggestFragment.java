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
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.SuggestActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class SuggestFragment extends CommonFragment implements OnClickListener{
	private SuggestActivity mActivity;
	private EditText mEtContent;//反馈建议
	private Button mButton;
	private User mUser;
	private final int MSG_SUGGEST=0x101;
	public Handler mSuggestHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUGGEST:
				ResultInfo info=(ResultInfo)msg.obj;
				if(info.getmCode()==0){
					showSmartToast(R.string.submit_success, Toast.LENGTH_SHORT);
					mActivity.finish();
				}else {
					if(info.getmMessage()!=null&&!TextUtils.isEmpty(info.getmMessage())){
						showSmartToast(R.string.submit_fail, Toast.LENGTH_SHORT);
					}else{
						showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
					}
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
        mActivity=(SuggestActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);
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
    	setTitleText(R.string.setting_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mEtContent=(EditText) view.findViewById(R.id.suggest_content);
    	mButton=(Button) view.findViewById(R.id.suggest_btn);
    	
    	mButton.setOnClickListener(this);
    	
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SUGGEST_URL);
        url.setmGetParamPrefix(JsonKey.SuggestKey.CONTENT).setmGetParamValues(mEtContent.getText().toString());
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
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
                if(!isDetached()){
                	Message message=new Message();
                	message.what=MSG_SUGGEST;
                	message.obj=object;
                	mSuggestHandler.sendMessage(message);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suggest_btn:
			toSuggest();
			break;
		default:
			break;
		}
	}
	private void toSuggest(){
		if(mEtContent.length()>0){
			if(mUser==null){
				showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
			}else{
				startReqTask(SuggestFragment.this);
			}
		}else{
			showSmartToast(R.string.input_error, Toast.LENGTH_SHORT);
		}
	}

}
