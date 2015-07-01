package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.AddressManageParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.adapter.AddressAdapter;
import com.techfly.liutaitai.model.pcenter.bean.Address;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.AddressListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;

public class AddressManageFragment extends CommonFragment implements
		OnItemClickListener, AddressListener {
	private AddressManageActivity mActivity;
	private XListView mListView;
	private ArrayList<Address> mList = new ArrayList<Address>();
	private AddressAdapter mAdapter;
	private boolean mIsDefault = false;
	private final int MSG_LIST = 0x101;
	private final int MSG_DEFAULT = 0x102;
	private final int MSG_DELETE = 0x103;
	private LinearLayout mNewBtn;// 新增收货地址
	private TextView mTvNoData;
	private Address mAddress;
	private String mAddressId;
	private boolean isMulChoice = false;
	private ArrayList<String> mIDs=new ArrayList<String>();
	private int mExtra;
	public Handler mAddressHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				if (mList.size() > 0) {
					mListView.setVisibility(View.VISIBLE);
					mTvNoData.setVisibility(View.INVISIBLE);
					mAdapter.updateList(mList);
				} else {
					setNoData();
				}
				break;
			case MSG_DEFAULT:
				ResultInfo info = (ResultInfo) msg.obj;
				if (info.getmCode() == 0) {
					mIsDefault = false;
					showSmartToast(R.string.default_success, Toast.LENGTH_SHORT);
					startReqTask(AddressManageFragment.this);
				} else {
					String message = info.getmMessage();
					if (message != null && !TextUtils.isEmpty(message)
							&& !"null".equals(message)) {
						showSmartToast(message, Toast.LENGTH_SHORT);
					} else {
						showSmartToast(R.string.default_error,
								Toast.LENGTH_SHORT);
					}
				}
				break;
			case MSG_DELETE:
				ResultInfo info1 = (ResultInfo) msg.obj;
				if (info1.getmCode() == 0) {
					mIsDefault = false;
					mIDs.clear();
					showSmartToast(R.string.delete_success, Toast.LENGTH_SHORT);
					startReqTask(AddressManageFragment.this);
				} else {
					String message = info1.getmMessage();
					if (message != null && !TextUtils.isEmpty(message)
							&& !"null".equals(message)) {
						showSmartToast(message, Toast.LENGTH_SHORT);
					} else {
						showSmartToast(R.string.delete_error,
								Toast.LENGTH_SHORT);
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
		mActivity = (AddressManageActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManagerListener.newManagerListener().onRegisterAddressListener(this);
		mIsDefault = false;
		startReqTask(AddressManageFragment.this);
		mExtra=mActivity.getIntent().getIntExtra(IntentBundleKey.ADDRESS_EXTRA, -1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_address, container,
				false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManagerListener.newManagerListener().onUnRegisterAddressListener(this);
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
		setTitleText(R.string.pcenter_address);
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		setRightText(R.string.address_edit, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				if (mActivity.getString(R.string.address_edit).equals(
						textView.getText().toString())) {
					if(mList!=null&&mList.size()>0){
						isMulChoice = true;
						setRightText(R.string.address_delete);
						ManagerListener.newManagerListener().notifyAddressListener(
								isMulChoice);
					}
					
				} else {
					isMulChoice = false;
					setRightText(R.string.address_edit);
					ManagerListener.newManagerListener()
							.notifyAddressListener(isMulChoice);
					if (mIDs.size() > 0) {
						startReqTask(AddressManageFragment.this);
					} 
				}

			}
		});

		mListView = (XListView) view.findViewById(R.id.address_list);
		mNewBtn = (LinearLayout) view.findViewById(R.id.address_new);
		mTvNoData = (TextView) view.findViewById(R.id.address_no_content);

		mNewBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,
						ChangeAddressActivity.class);
				intent.putExtra(IntentBundleKey.ADDRESS_EXTRA, Constant.ADDRESS_INTENT);
				startActivityForResult(intent, Constant.ADDRESS_INTENT);
			}
		});
		mAdapter = new AddressAdapter(this, mList, isMulChoice,mExtra);
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setOnItemClickListener(this);
	
	}

	@Override
	public void requestData() {
		if (mIsDefault) {
			RequestParam param = new RequestParam();
			HttpURL url = new HttpURL();
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.ADDRESS_DEFAULT_URL);
			url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL)
					.setmGetParamValues(
							SharePreferenceUtils.getInstance(mActivity)
									.getUser().getmId());
			url.setmGetParamPrefix(JsonKey.AddressKey.ADDRESSID)
					.setmGetParamValues(mAddressId);
			param.setmHttpURL(url);
			param.setmParserClassName(CommonParser.class.getName());
			RequestManager
					.getRequestData(mActivity, createReqSuccessListener(),
							createReqErrorListener(), param);

		} else if(mIDs.size()>0){
			RequestParam param = new RequestParam();
			HttpURL url = new HttpURL();
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.ADDRESS_DELETE_URL);
			url.setmGetParamPrefix(JsonKey.AddressKey.ADDRESSPKS)
					.setmGetParamValues(getID(mIDs));
			param.setmHttpURL(url);
			param.setPostRequestMethod();
			param.setmParserClassName(CommonParser.class.getName());
			RequestManager
					.getRequestData(mActivity, createReqSuccessListener(),
							createReqErrorListener(), param);
		}else {
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
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mList = (ArrayList<Address>) object;
				if (!isDetached()) {
					mAddressHandler.removeMessages(MSG_LIST);
					mAddressHandler.sendEmptyMessage(MSG_LIST);
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

	private void setNoData() {
		mListView.setVisibility(View.INVISIBLE);
		mTvNoData.setVisibility(View.VISIBLE);
		mTvNoData.setText(R.string.address_no_content);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.ADDRESS_SUCCESS) {
			mIsDefault = false;
			startReqTask(AddressManageFragment.this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mAddress = (Address) parent.getAdapter().getItem(position);
		if (isMulChoice) {
			CheckBox checkBox = (CheckBox) mListView.getChildAt(position)
					.findViewById(R.id.iaddress_delete);
			if (checkBox.isChecked()) {
				if(mIDs.contains(mAddress.getmId())){
					mIDs.remove(mAddress.getmId());
				}
				checkBox.setChecked(false);
			} else {
				if(!mIDs.contains(mAddress.getmId())){
					mIDs.add(mAddress.getmId());
				}
				checkBox.setChecked(true);
			}
		}else{
			if(mExtra==-1){
				Intent intent = new Intent(mActivity,
						ChangeAddressActivity.class);
				intent.putExtra(IntentBundleKey.ADDRESS, mAddress);
				startActivityForResult(intent, Constant.ADDRESS_INTENT);
			}else{
				Intent intent=new Intent();
				intent.putExtra(IntentBundleKey.ADDRESS_VALUES, mAddress);
				mActivity.setResult(mExtra+1, intent);
				mActivity.finish();
			}
		}
	}

	private Response.Listener<Object> createReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (!isDetached()) {
					if(mIDs.size()>0){
						Message msg = new Message();
						msg.what = MSG_DELETE;
						msg.obj = object;
						mAddressHandler.removeMessages(MSG_DELETE);
						mAddressHandler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = MSG_DEFAULT;
						msg.obj = object;
						mAddressHandler.removeMessages(MSG_DEFAULT);
						mAddressHandler.sendMessage(msg);
					}
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
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
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}

	@Override
	public void onDeleteListener(boolean b) {
		for (int i = 0; i < mList.size(); i++) {
			mAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
		}
		mAdapter = new AddressAdapter(this, mList, b,mExtra);
		mListView.setAdapter(mAdapter);
	}
	private String getID(ArrayList<String> list){
		String string="";
		if(list!=null){
			int size=list.size();
			for(int i=0;i<size;i++){
				if(i==size-1){
					string+=list.get(i);
				}else{
					string+=list.get(i)+",";
				}
			}
		}
		return string;
	}

	@Override
	public void onSelectListener(int type,String id) {
		if (isMulChoice) {
			if (type==0) {
				if(mIDs.contains(id)){
					mIDs.remove(id);
				}
			} else {
				if(!mIDs.contains(id)){
					mIDs.add(id);
				}
			}
			AppLog.Loge("xll", getID(mIDs));
		}
	}

	@Override
	public void onDefaultListener(boolean b,String id) {
		mAddressId=id;
		if (!b&& !isMulChoice) {
			mIsDefault = true;
			startReqTask(AddressManageFragment.this);
		}
	}

}
