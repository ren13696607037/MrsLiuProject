package com.hylsmart.yihui.model.pcenter.fragment;


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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.model.pcenter.activities.ChangeAddressActivity;
import com.hylsmart.yihui.model.pcenter.activities.CityListActivity;
import com.hylsmart.yihui.model.pcenter.activities.ProvinceActivity;
import com.hylsmart.yihui.model.pcenter.bean.Address;
import com.hylsmart.yihui.model.pcenter.bean.Area;
import com.hylsmart.yihui.model.pcenter.bean.City;
import com.hylsmart.yihui.model.pcenter.bean.Province;
import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.Utility;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class ChangeAddressFragment extends CommonFragment implements OnClickListener{
	private ChangeAddressActivity mActivity;
	private RelativeLayout mRlArea;
	private TextView mTvArea;
	private EditText mEtDetail;
	private EditText mEtName;
	private EditText mEtPhone;
	private EditText mEtZipcode;
	private Button mButton;
	private Area mArea=null;
	private City mCity=null;
	private Province mProvince=null;
	private int mExtra;
	private final int MSG_CHANGE=0x101;
	private User mUser;
	private Address mAddress=null;
	private Address mAddBundle=new Address();
	public Handler mChangeHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE:
				ResultInfo info=(ResultInfo) msg.obj;
				if(info.getmCode()==0){
					if(mAddress!=null){
						showSmartToast(R.string.change_success, Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.address_success, Toast.LENGTH_SHORT);
					}
					if(mExtra==Constant.ORDER_ADDRESS_INTENT){
						Intent intent=new Intent();
						mAddBundle.setmId(info.getmData());
						intent.putExtra(IntentBundleKey.ADDRESS_VALUES, mAddBundle);
						mActivity.setResult(mExtra+1,intent);
					}else{
						mActivity.setResult(Constant.ADDRESS_SUCCESS);
					}
					mActivity.finish();
				}else{
					String message=info.getmMessage();
					if(message!=null&&!TextUtils.isEmpty(message)&&!"null".equals(message)){
						showSmartToast(message, Toast.LENGTH_SHORT);
					}else{
						if(mAddress!=null){
							showSmartToast(R.string.change_error, Toast.LENGTH_SHORT);
						}else{
							showSmartToast(R.string.address_error, Toast.LENGTH_SHORT);
						}
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
        mActivity=(ChangeAddressActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
        mExtra=mActivity.getIntent().getIntExtra(IntentBundleKey.ADDRESS_EXTRA, -1);
        mAddress=(Address) mActivity.getIntent().getSerializableExtra(IntentBundleKey.ADDRESS);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changeadd, container, false);
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
    	
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mRlArea=(RelativeLayout) view.findViewById(R.id.address_area);
    	mTvArea=(TextView) view.findViewById(R.id.area_text);
    	mEtDetail=(EditText) view.findViewById(R.id.address_detail);
    	mEtName=(EditText) view.findViewById(R.id.address_name);
    	mEtPhone=(EditText) view.findViewById(R.id.address_phone);
    	mEtZipcode=(EditText) view.findViewById(R.id.address_zipcode);
    	mButton=(Button) view.findViewById(R.id.address_btn);
    	
    	mRlArea.setOnClickListener(this);
    	mTvArea.setOnClickListener(this);
    	mButton.setOnClickListener(this);
    	if(mAddress!=null){
    		setTitleText(R.string.add_change_title);
    		mTvArea.setText(mAddress.getmAddress());
    		mEtDetail.setText(mAddress.getmDetail());
    		mEtName.setText(mAddress.getmName());
    		mEtPhone.setText(mAddress.getmPhone());
    		if(mAddress.getmZipcode()!=null&&!TextUtils.isEmpty(mAddress.getmZipcode())&&!"null".equals(mAddress.getmZipcode())){
    			mEtZipcode.setText(mAddress.getmZipcode());
    		}
    	}else{
    		setTitleText(R.string.add_title);
    	}
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        if(mAddress!=null){
        	url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CHANGE_ADDRESS_URL);
        	url.setmGetParamPrefix(JsonKey.AddressKey.ADDRESSID).setmGetParamValues(mAddress.getmId());
        	url.setmGetParamPrefix(JsonKey.AddressKey.AREAID).setmGetParamValues(mAddress.getmAddressId());
        	url.setmGetParamPrefix(JsonKey.AddressKey.ISDEFAULT).setmGetParamValues(mAddress.ismIsSelect()+"");
        }else{
        	url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ADD_ADDRESS_URL);
        	url.setmGetParamPrefix(JsonKey.AddressKey.AREAID).setmGetParamValues(mCity.getmId());
        }
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(mUser.getmId());
        url.setmGetParamPrefix(JsonKey.AddressKey.NAME).setmGetParamValues(mEtName.getText().toString());
        url.setmGetParamPrefix(JsonKey.AddressKey.MOBILE).setmGetParamValues(mEtPhone.getText().toString());
        url.setmGetParamPrefix(JsonKey.AddressKey.ADDRESS).setmGetParamValues(mEtDetail.getText().toString());
        url.setmGetParamPrefix(JsonKey.AddressKey.ZIPCODE).setmGetParamValues(mEtZipcode.getText().toString());
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
                	mAddBundle.setmAddress(mTvArea.getText().toString());
                	mAddBundle.setmDetail(mEtDetail.getText().toString());
                	mAddBundle.setmName(mEtName.getText().toString());
                	mAddBundle.setmPhone(mEtPhone.getText().toString());
                	Message msg=new Message();
                	msg.what=MSG_CHANGE;
                	msg.obj=object;
                	mChangeHandler.removeMessages(MSG_CHANGE);
                	mChangeHandler.sendMessage(msg);
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
		case R.id.address_area: case R.id.area_text:
			Intent intent=new Intent(mActivity,ProvinceActivity.class);
			startActivityForResult(intent, Constant.PRO_INTENT);
			break;
		case R.id.address_btn:
			toChange();
			break;

		default:
			break;
		}
	}
	private void toChange(){
		if(mEtDetail.length()==0||mEtName.length()==0||mEtPhone.length()==0){
			showSmartToast(R.string.input_error, Toast.LENGTH_SHORT);
		}else{
				if((mArea==null||mCity==null)&&mAddress==null){
					showSmartToast(R.string.area_error, Toast.LENGTH_SHORT);
				}else{
				if(Utility.isPhone(mEtPhone.getText().toString())){
					startReqTask(ChangeAddressFragment.this);
				}else{
					showSmartToast(R.string.phone_error, Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Constant.PRO_SUCCESS){
			mArea=(Area) data.getSerializableExtra(IntentBundleKey.AREA);
			mCity=(City) data.getSerializableExtra(IntentBundleKey.CITY);
			mProvince=(Province) data.getSerializableExtra(IntentBundleKey.PROVINCE);
			if(mAddress!=null){
				mAddress.setmAddressId(mCity.getmId());
			}
			mTvArea.setText(mProvince.getmName()+"/"+mArea.getmName()+"/"+mCity.getmName());
		}
	}

}
