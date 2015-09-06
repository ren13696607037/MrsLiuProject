package com.techfly.liutaitai.model.pcenter.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.MyApplyActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class MyApplyFragment extends CommonFragment implements OnClickListener{
	private MyApplyActivity mActivity;
	private CheckBox mManicure;
	private CheckBox mEyelash;
	private ImageView mImageView;
	private ImageView mImageView2;
	private Button mButton;
	private boolean isSelect;
	private User mUser;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MyApplyActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
//        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myapply,
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
    	setTitleText(R.string.pcenter_apply);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mEyelash = (CheckBox) view.findViewById(R.id.apply_eyelash);
    	mManicure = (CheckBox) view.findViewById(R.id.apply_manicure);
    	
    	mEyelash.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					
				}else{
					
				}
			}
		});
    	
    	mImageView = (ImageView) view.findViewById(R.id.apply_img);
    	mImageView2 = (ImageView) view.findViewById(R.id.apply_img1);
    	mButton = (Button) view.findViewById(R.id.apply_btn);
    	
    	mButton.setOnClickListener(this);
    	mImageView.setOnClickListener(this);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
				+ Constant.APPLY_URL);
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
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
		case R.id.apply_btn:
			break;
		case R.id.apply_img:
			break;
		case R.id.apply_img1:
			break;

		default:
			break;
		}
	}

}
