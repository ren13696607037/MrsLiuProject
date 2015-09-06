package com.techfly.liutaitai.model.pcenter.fragment;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.LoginParser;
import com.techfly.liutaitai.model.pcenter.activities.ChangeNickActivity;
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
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ChangeNickFragment extends CommonFragment implements OnClickListener{
	private ChangeNickActivity mActivity;
	private EditText mNick;
	private RelativeLayout mDelete;
	private Button mBtn;
	private User mUser;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ChangeNickActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_nickname,
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
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	setTitleText(R.string.change_nick_title);
    	
    	mNick = (EditText) view.findViewById(R.id.change_nick);
    	mDelete = (RelativeLayout) view.findViewById(R.id.change_nick_delete);
    	mBtn = (Button) view.findViewById(R.id.change_nick_button);
    	
    	mBtn.setOnClickListener(this);
    	mDelete.setOnClickListener(this);
    }

	@Override
	public void requestData() {
//		RequestParams params = new RequestParams();
//		params.addHeader("enctype", "multipart/form-data");
//		params.addHeader("lt-token", mUser.getmToken());
//		params.addHeader("lt-id", mUser.getmId());
//		if(mUser != null){
//			AppLog.Loge("xll", mUser.getmToken());
//			AppLog.Loge("xll", mUser.getmId());
//		}
//		params.addBodyParameter(JsonKey.UserKey.NICK, mNick.getText().toString());
//		final HttpUtils http = new HttpUtils();
//		http.send(HttpRequest.HttpMethod.POST, Constant.YIHUIMALL_BASE_URL
//				+ Constant.CHANGE_INFO_URL, params,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException exception, String arg1) {
//						AppLog.Logd("Fly",
//								"exception===" + exception.getMessage());
//						if (!isDetached()) {
//							mLoadHandler.removeMessages(Constant.NET_SUCCESS);
//							mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
//							SmartToast.makeText(getActivity(),
//									getString(R.string.life_helper_send_fail),
//									Toast.LENGTH_LONG).show();
//						}
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> info) {
//						AppLog.Logd("Fly", "info===" + info.result);
//						if (!isDetached()) {
//							mLoadHandler.removeMessages(Constant.NET_SUCCESS);
//							mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
//							// TODO修改成功后的操作
//							JSONObject obj;
//							try {
//								obj = new JSONObject(info.result);
//								if (obj != null) {
//									if (obj.optInt(JsonKey.CODE) == 0) {
//										mUser.setmNick(mNick.getText().toString());
//										SmartToast
//												.makeText(
//														getActivity(),
//														getString(R.string.life_helper_send_success),
//														Toast.LENGTH_LONG)
//												.show();
//										mActivity.finish();
//									} else {
//										SmartToast.makeText(getActivity(),
//												obj.optString(JsonKey.MESSAGE),
//												Toast.LENGTH_LONG).show();
//									}
//								}
//								SharePreferenceUtils.getInstance(getActivity())
//										.saveUser(mUser);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CHANGE_NICK_URL);
		url.setmGetParamPrefix(JsonKey.UserKey.NICK)
				.setmGetParamValues(mNick.getText().toString())
				;
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		AppLog.Loge("xll", mUser.getmToken());
		AppLog.Loge("xll", mUser.getmId());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(CommonParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				ResultInfo info = (ResultInfo) object;
				AppLog.Loge("xll", info.toString());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					if(info.getmCode() == 0){
						mUser.setmNick(mNick.getText().toString());
						SmartToast
								.makeText(
										getActivity(),
										getString(R.string.life_helper_send_success),
										Toast.LENGTH_LONG)
								.show();
						mActivity.finish();
					}else{
						SmartToast.makeText(getActivity(),
								info.getmMessage(),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
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
		case R.id.change_nick_button:
			if(mNick.length()>0){
				startReqTask(ChangeNickFragment.this);
			}else{
				showSmartToast(R.string.input_error, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.change_nick_delete:
			mNick.setText("");
			break;

		default:
			break;
		}
	}

}
