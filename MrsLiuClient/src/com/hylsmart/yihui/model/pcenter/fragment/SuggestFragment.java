package com.hylsmart.yihui.model.pcenter.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.model.home.bean.ContactInfo;
import com.hylsmart.yihui.model.pcenter.activities.SuggestActivity;
import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class SuggestFragment extends CommonFragment implements OnClickListener{
	private SuggestActivity mActivity;
	private EditText mEtContent;//反馈建议
	private EditText mEtContact;//联系方式
	private TextView mTvQQ;//官方QQ
	private TextView mTvQQCopy;//复制QQ
	private TextView mTvWX;//官方微信
	private TextView mTvWXCopy;//复制微信
	
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
    	mEtContact=(EditText) view.findViewById(R.id.suggest_contact);
    	mTvQQ=(TextView) view.findViewById(R.id.suggest_qq);
    	mTvQQCopy=(TextView) view.findViewById(R.id.suggest_qq_copy);
    	mTvWX=(TextView) view.findViewById(R.id.suggest_wx);
    	mTvWXCopy=(TextView) view.findViewById(R.id.suggest_wx_copy);
    	
    	mButton.setOnClickListener(this);
    	mTvQQCopy.setOnClickListener(this);
    	mTvWXCopy.setOnClickListener(this);
    	
    	ContactInfo contactInfo=SharePreferenceUtils.getInstance(mActivity).getContactInfo();
    	if(contactInfo!=null){
    		mTvQQ.setText(contactInfo.getmQQ());
    		mTvWX.setText(contactInfo.getmWeixin());
    	}
    	
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SUGGEST_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(mUser.getmId())
        .setmGetParamPrefix(JsonKey.SuggestKey.CONTACT).setmGetParamValues(mEtContact.getText().toString());
        url.setmGetParamPrefix(JsonKey.SuggestKey.CONTENT).setmGetParamValues(mEtContent.getText().toString());
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
		case R.id.suggest_qq_copy:
			toCopy(mTvQQ.getText().toString());
			break;
		case R.id.suggest_wx_copy:
			toCopy(mTvWX.getText().toString());
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
	private void toCopy(String string){
		ClipboardManager cmb=(ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE); 
		AppLog.Loge("xll", string);
		ClipData clip = ClipData.newPlainText("simple text",string);
		cmb.setPrimaryClip(clip);
	}

}
