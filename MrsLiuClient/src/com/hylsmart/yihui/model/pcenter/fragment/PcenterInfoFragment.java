package com.hylsmart.yihui.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.AddressManageParser;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.shopcar.OnShopCarLisManager;
import com.hylsmart.yihui.model.pcenter.activities.AddressManageActivity;
import com.hylsmart.yihui.model.pcenter.activities.ForgetNextActivity;
import com.hylsmart.yihui.model.pcenter.activities.PcenterInfoActivity;
import com.hylsmart.yihui.model.pcenter.bean.Address;
import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AlertDialogUtils;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class PcenterInfoFragment extends CommonFragment implements OnClickListener{
	private PcenterInfoActivity mActivity;
	private TextView mTvPhone;
	private TextView mTvAddress;
	private TextView mTvPass;
	private RelativeLayout mRlAddress;
	private Button mButton;
	private Dialog mDialog;
	private User mUser;
	private ArrayList<Address> mList = new ArrayList<Address>();
	private final int MSG_LIST = 0x101;
	public Handler mInfoHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				int size=mList.size();
				Address addr=null;
				for(int i=0;i<size;i++){
					Address address =mList.get(i);
					if(address.ismIsSelect()){
						addr=address;
					}
				}
				if(addr!=null){
					mTvAddress.setText(addr.getmAddress());
				}else{
					if(mList.size()>0){
						mTvAddress.setText(mList.get(0).getmAddress());
					}else{
						mTvAddress.setText("无");
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
        mActivity=(PcenterInfoActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(PcenterInfoFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcenterinfo, container, false);
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
    	setTitleText(R.string.pinfo_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mButton=(Button) view.findViewById(R.id.info_btn);
    	mTvAddress=(TextView) view.findViewById(R.id.info_address_text);
    	mTvPhone=(TextView) view.findViewById(R.id.info_phone);
    	mRlAddress=(RelativeLayout) view.findViewById(R.id.info_address);
    	mTvPass=(TextView) view.findViewById(R.id.info_pass);
    	
    	mButton.setOnClickListener(this);
    	mRlAddress.setOnClickListener(this);
    	mTvPass.setOnClickListener(this);
    	if(mUser.getmPhone()!=null&&!TextUtils.isEmpty(mUser.getmPhone())&&!"null".equals(mUser.getmPhone())){
    		mTvPhone.setText(mUser.getmPhone());
    	}else{
    		mTvPhone.setText("无");
    	}
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ADDRESS_URL);
		url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL)
				.setmGetParamValues(
						SharePreferenceUtils.getInstance(mActivity)
								.getUser().getmId());
		param.setmHttpURL(url);
		param.setmParserClassName(AddressManageParser.class.getName());
		RequestManager.getRequestData(mActivity,
				createMyReqSuccessListener(), createMyReqErrorListener(),
				param);
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mList = (ArrayList<Address>) object;
				if (!isDetached()) {
					mInfoHandler.removeMessages(MSG_LIST);
					mInfoHandler.sendEmptyMessage(MSG_LIST);
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
		case R.id.info_btn:
			mDialog= new Dialog(mActivity, R.style.MyDialog);
        	mDialog=AlertDialogUtils.displayMyAlertChoice(mActivity, R.string.pinfo_exit, R.string.pinfo_exit, R.string.confirm, new View.OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
//                    exit();
                	SharePreferenceUtils.getInstance(mActivity).clearUser();
                    OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null); 
                    mActivity.finish();
                    mDialog.dismiss();
                }
            }, R.string.cancel, null);
        	mDialog.show();
			break;
		case R.id.info_address:
			startActivity(new Intent(mActivity,AddressManageActivity.class));
			break;
		case R.id.info_pass:
			startActivity(new Intent(mActivity,ForgetNextActivity.class));
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(PcenterInfoFragment.this);
	}
	private void exit(){
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.LOGOUT_URL);
		url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL)
				.setmGetParamValues(
						SharePreferenceUtils.getInstance(mActivity)
								.getUser().getmId());
		param.setmHttpURL(url);
		param.setmParserClassName(CommonParser.class.getName());
		RequestManager.getRequestData(mActivity,
				createReqSuccessListener(), createReqErrorListener(),
				param);
	}
	private Response.Listener<Object> createReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				ResultInfo info=(ResultInfo) object;
				if (!isDetached()) {
					if(info.getmCode()==0){
						SharePreferenceUtils.getInstance(mActivity).clearUser();
	                    OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null); 
	                    mActivity.finish();
	                    mDialog.dismiss();
					}else{
						showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
					}
				}
			}
		};
	}

	private Response.ErrorListener createReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
				}
			}
		};
	}

}
