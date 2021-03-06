package com.techfly.liutaitai.model.order.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.order.activities.RateActivity;
import com.techfly.liutaitai.model.order.activities.ServiceDetailActivity;
import com.techfly.liutaitai.model.order.adapter.ServiceAdapter;
import com.techfly.liutaitai.model.order.parser.ServiceOrderParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.ServiceClickListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class ServiceOrderFragment extends CommonFragment implements
		IXListViewListener, ServiceClickListener {
	private XListView mListView;
	private ArrayList<Service> mList = new ArrayList<Service>();
	private ServiceAdapter mAdapter;
	private TextView mTextView;
	private final int MSG_LIST = 0x101;
	private int mPage = 1;
	private int mSize = 10;
	private User mUser;
	private int mType = 0;
	private Service mService;
	private boolean isRefresh=true;
	public Handler mOrderHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				mListView.setVisibility(View.VISIBLE);
				mTextView.setVisibility(View.GONE);
				if (mList.size() == 0) {
					setNoData();
				}else{
					mAdapter.updateList(mList);
				}
				break;

			default:
				break;
			}
		}

	};
    private boolean mIsFromOrder;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mIsFromOrder = activity.getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_ORDER, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
		ManagerListener.newManagerListener().onRegisterServiceClickListener(
				this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_service_orderl,
				container, false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManagerListener.newManagerListener().onUnRegisterServiceClickListener(
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
	    if(mIsFromOrder){
	        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
	    }
		setTitleText(R.string.order_appointment_title);
		mListView = (XListView) view.findViewById(R.id.all_list);
		mTextView = (TextView) view.findViewById(R.id.all_text);
		mAdapter = new ServiceAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Service service = (Service) parent.getAdapter().getItem(
						position);
				Intent intent = new Intent(getActivity(),
						ServiceDetailActivity.class);
				intent.putExtra(IntentBundleKey.ORDER_SERVICE, service.getmId());
				startActivity(intent);
			}
		});
	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		if (mType == 1) {
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.SERVICE_DELETE_URL);
			url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(
					mService.getmId());
			param.setmParserClassName(CommonParser.class.getName());
		} else if (mType == 3) {
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.SERVICE_CANCEL_URL);
			url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(
					mService.getmId());
			param.setmParserClassName(CommonParser.class.getName());
		} else {
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.SERVICE_LIST_URL);
			url.setmGetParamPrefix(JsonKey.BalanceKey.PAGE).setmGetParamValues(
					mPage + "");
			url.setmGetParamPrefix(JsonKey.BalanceKey.SIZE).setmGetParamValues(
					mSize + "");
			param.setmParserClassName(ServiceOrderParser.class.getName());
		}
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		// param.setmId("1");
		// param.setmToken("440a07c991c4bbae3bcd52746e6a9d32");
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (!isDetached()) {
					if (object instanceof ResultInfo) {
						ResultInfo info = (ResultInfo) object;
						if (mType == 1) {
							if (info.getmCode() == 0) {
								showSmartToast(R.string.delete_success,
										Toast.LENGTH_SHORT);
								mType = 0;
								startReqTask(ServiceOrderFragment.this);
							} else {
								if (info.getmMessage() != null
										&& !TextUtils.isEmpty(info
												.getmMessage())
										&& !"null".equals(info.getmMessage())) {
									showSmartToast(info.getmMessage(),
											Toast.LENGTH_SHORT);
								} else {
									showSmartToast(R.string.delete_error,
											Toast.LENGTH_SHORT);
								}
							}
						} else if (mType == 3) {
							if (info.getmCode() == 0) {
								showSmartToast(R.string.cancel_success,
										Toast.LENGTH_SHORT);
								mType = 0;
								startReqTask(ServiceOrderFragment.this);
							} else {
								if (info.getmMessage() != null
										&& !TextUtils.isEmpty(info
												.getmMessage())
										&& !"null".equals(info.getmMessage())) {
									showSmartToast(info.getmMessage(),
											Toast.LENGTH_SHORT);
								} else {
									showSmartToast(R.string.cancel_error,
											Toast.LENGTH_SHORT);
								}
							}
						}
					} else if (object instanceof ArrayList) {
						ArrayList<Service> list = (ArrayList<Service>) object;
                        if(isRefresh){
                        	mList.addAll(list);
                        }else{
                        	mList.clear();
                        	mList.addAll(list);
                        }
                        if (list == null || list.size() == 0) {
                        	
        				} else if (list.size() < 10) {
        					mListView.setVisibility(View.VISIBLE);
        				    mTextView.setVisibility(View.GONE);
        					mListView.setPullLoadEnable(false);
        				} else {
        					mListView.setVisibility(View.VISIBLE);
        					mTextView.setVisibility(View.GONE);
        					mListView.setPullLoadEnable(true);
        				}
						mOrderHandler.removeMessages(MSG_LIST);
						mOrderHandler.sendEmptyMessage(MSG_LIST);
						mListView.stopLoadMore();
						mListView.stopRefresh();
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
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				mListView.stopLoadMore();
				mListView.stopRefresh();
				AppLog.Loge(" data failed to load" + error.getMessage());
			}
		};
	}

	private void setNoData() {
		mListView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(R.string.app_name);
	}

	@Override
	public void onResume() {
		super.onResume();
		mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
		if (mUser != null) {
			if(mList.size() == 0){
				mList.clear();
				mPage = 1;
				startReqTask(ServiceOrderFragment.this);
			}
		} else {
			mListView.setVisibility(View.GONE);
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setText(R.string.login_toast);
		}
	}

	@Override
	public void onRefresh() {
		mOrderHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				isRefresh = false;
				mPage = 1;
				mList.clear();
				startReqTask(ServiceOrderFragment.this);
			}
		}, 0);
	}

	@Override
	public void onLoadMore() {
		mOrderHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				isRefresh = true;
				mPage += 1;
				requestData();
			}
		}, 0);
	}

	@Override
	public void onServiceDeleteListener(Service service) {
		isRefresh=false;
		mType = 1;
		mService = service;
		startReqTask(ServiceOrderFragment.this);
	}

	@Override
	public void onServicePayListener(Service service) {
		isRefresh=false;
		mType = 2;
		Intent intent = new Intent(getActivity(), ServiceOrderActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(IntentBundleKey.ORDER_ID, service.getmAliNo());// 支付订单号
																		// 例如
																		// 5666995444RSFR
		bundle.putString(IntentBundleKey.ORDER_MONEY,
				service.getmServicePrice());// 支付的钱
		bundle.putString(IntentBundleKey.ORDER_PRODUCT,
				service.getmServiceName());// 商品名称
		intent.putExtra(IntentBundleKey.DATA, bundle);
		intent.putExtra(IntentBundleKey.IS_FROM_ORDER, true);
		startActivity(intent);
	}

	@Override
	public void onServiceCancelListener(Service service) {
		isRefresh=false;
		mType = 3;
		mService = service;
		startReqTask(ServiceOrderFragment.this);
	}

	@Override
	public void onServiceAgainListener(Service service) {
		isRefresh=false;
		mType = 4;
		Intent intent = new Intent(getActivity(), ServiceOrderActivity.class);
		intent.putExtra(IntentBundleKey.ID, service.getmNum());
		startActivity(intent);
	}

	@Override
	public void onServiceRateListener(Service service) {
		isRefresh=false;
		mType = 5;
		Intent intent = new Intent(getActivity(), RateActivity.class);
		intent.putExtra(IntentBundleKey.SERVICE_ID, service.getmId());
		intent.putExtra(IntentBundleKey.TECH_ID, service.getmTechId());
		startActivityForResult(intent, Constant.RATE_INTENT);
	}

	@Override
	public void onServiceRefreshListener() {
		mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
		if (mUser != null) {
			isRefresh=true;
			mList.clear();
			mPage = 1;
			startReqTask(ServiceOrderFragment.this);
		} else {
			mListView.setVisibility(View.GONE);
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setText(R.string.service_no_content);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.RATE_SUCCESS) {
			mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
			if (mUser != null) {
				startReqTask(ServiceOrderFragment.this);
			} else {
				mListView.setVisibility(View.GONE);
				mTextView.setVisibility(View.VISIBLE);
				mTextView.setText(R.string.service_no_content);
			}
		}
	}

}
