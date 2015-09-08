package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CityListParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.CityUpdateListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.ShowDialog;

public class ChangeAddFragment extends CommonFragment implements CityUpdateListener{
	private ChangeAddressActivity mActivity;
	private EditText mEtName;
	private EditText mEtPhone;
	private EditText mEtAddress;
	private Button mButton;
	private String mExtra;
	private AddressManage mAddressManage = null;
	private ResultInfo mInfo;
	private User mUser;
	private final int MSG_DATA = 0x101;
	private final int MSG_CITY = 0x102;
	private String mCity;
	private ArrayList<Area> mCitys = new ArrayList<Area>();
	private TextView mTvCity;
	private ShowDialog mDialog;
	public Handler mChangeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DATA:
				if (mInfo.getmCode() == 0) {
					if (mAddressManage == null) {
						showSmartToast(R.string.address_success,
								Toast.LENGTH_SHORT);
					} else {
						// showSmartToast(R.string.addchange_success,
						// Toast.LENGTH_SHORT);
					}
					// mActivity.setResult(Constant.CHANGE_ADD_SUCCESS);
					mActivity.finish();
				} else {
					showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
				}
				break;
			case MSG_CITY:

				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (ChangeAddressActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManagerListener.newManagerListener().onRegisterCityUpdateListener(
				this);
		mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
		mExtra = mActivity.getIntent().getStringExtra(
				IntentBundleKey.CHANGEADD_ID);
		mAddressManage = (AddressManage) mActivity.getIntent()
				.getSerializableExtra(IntentBundleKey.CHANGEADD_ID);
		getCitys();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_changeadd, container,
				false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManagerListener.newManagerListener().onUnRegisterCityUpdateListener(
				this);
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
		mEtAddress = (EditText) view.findViewById(R.id.changeadd_add);
		mEtName = (EditText) view.findViewById(R.id.changeadd_name);
		mEtPhone = (EditText) view.findViewById(R.id.changeadd_phone);
		mTvCity = (TextView) view.findViewById(R.id.changeadd_city);
		
		mTvCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showCity();
			}
		});
		mButton = (Button) view.findViewById(R.id.changeadd_btn);
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = mEtName.getText().toString();
				String phone = mEtPhone.getText().toString();
				String address = mEtAddress.getText().toString();
				if (mEtAddress.length() == 0 || mEtName.length() == 0
						|| mEtPhone.length() == 0) {
					SmartToast.makeText(mActivity, R.string.input_error,
							Toast.LENGTH_SHORT).show();
				} else {
					if (!Utility.isPhone(phone)) {
						SmartToast.makeText(mActivity, R.string.phone_error,
								Toast.LENGTH_SHORT).show();
						mEtPhone.setText("");
					} else {
						startReqTask(ChangeAddFragment.this);
					}
				}
			}
		});
		if (mAddressManage == null) {
			setTitleText(R.string.add_title);
		} else {
			setTitleText(R.string.add_change_title);
			mEtAddress.setText(mAddressManage.getmDetail());
			mEtName.setText(mAddressManage.getmName());
			mEtPhone.setText(mAddressManage.getmPhone());
			mTvCity.setText(mAddressManage.getmCity());
		}
	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
				+ Constant.CHANGE_ADDRESS_URL);
		url.setmGetParamPrefix(JsonKey.AddressKey.NAME).setmGetParamValues(
				mEtName.getText().toString());
		url.setmGetParamPrefix(JsonKey.AddressKey.MOBILE).setmGetParamValues(
				mEtPhone.getText().toString());
		url.setmGetParamPrefix(JsonKey.AddressKey.ADDRESS).setmGetParamValues(
				mEtAddress.getText().toString());
		url.setmGetParamPrefix(JsonKey.AddressKey.CITY).setmGetParamValues(
				mCity);
		param.setmParserClassName(CommonParser.class.getName());
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		RequestManager.getRequestData(mActivity, createMyReqSuccessListener(),
				createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (!isDetached()) {
					if (object instanceof ResultInfo) {
						mInfo = (ResultInfo) object;
						mChangeHandler.removeMessages(MSG_DATA);
						mChangeHandler.sendEmptyMessage(MSG_DATA);
					} else if (object instanceof ArrayList) {
						mCitys = (ArrayList<Area>) object;
						mChangeHandler.removeMessages(MSG_CITY);
						mChangeHandler.sendEmptyMessage(MSG_CITY);
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
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}

	private void getCitys() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CITY_URL);
		param.setmHttpURL(url);
		param.setmParserClassName(CityListParser.class.getName());
		RequestManager.getRequestData(mActivity, createMyReqSuccessListener(),
				createMyReqErrorListener(), param);
	}
	private void showCity(){
		mDialog = new ShowDialog(mActivity, mCitys);
		mDialog.show();
		mDialog.setCanceledOnTouchOutside(true);
	}


	@Override
	public void onUpdateListener(Area area) {
		mTvCity.setText(area.getmName());
		mCity = area.getmId();
		mDialog.dismiss();
	}

}
