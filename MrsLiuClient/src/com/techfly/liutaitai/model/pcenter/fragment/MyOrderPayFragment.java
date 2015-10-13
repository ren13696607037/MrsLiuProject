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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.TechOrderParser;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
import com.techfly.liutaitai.model.pcenter.adapter.MyOrderAdapter;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.OrderCancelListener;
import com.techfly.liutaitai.util.ManagerListener.OrderPayListener;
import com.techfly.liutaitai.util.ManagerListener.OrderTakeListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.OrderPayFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyOrderPayFragment extends OrderPayFragment implements
		OnItemClickListener, IXListViewListener, OrderCancelListener,
		OrderTakeListener, OrderPayListener {
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<TechOrder> mList = new ArrayList<TechOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST = 0x101;
	private final int MSG_CANCEL = 0x102;
	private int mPage = 1;
	private int mSize = 10;
	private TechOrder mOrder;
	private int mType;
	private boolean isRefresh = true;
	private ResultInfo mInfo;
	private User mUser;
	private View mView;
	public Handler mPayHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				if (mList.size() == 0) {
					setNoData();
				}
				mAdapter.updateList(mList);
				break;
			case MSG_CANCEL:
				if (mInfo.getmCode() == 0) {
					showSmartToast(R.string.cancel_success, Toast.LENGTH_SHORT);
					startReqTask(MyOrderPayFragment.this);
				} else {
					if (mInfo.getmMessage() != null
							&& !TextUtils.isEmpty(mInfo.getmMessage())
							&& !"null".equals(mInfo.getmMessage())) {
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					} else {
						showSmartToast(R.string.cancel_error,
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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
		startReqTask(MyOrderPayFragment.this);
		ManagerListener.newManagerListener()
				.onRegisterOrderCancelListener(this);
		ManagerListener.newManagerListener().onRegisterOrderTakeListener(this);
		ManagerListener.newManagerListener().onRegisterOrderPayListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_orderpay, container,
				false);
		return mView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManagerListener.newManagerListener().onUnRegisterOrderCancelListener(
				this);
		ManagerListener.newManagerListener()
				.onUnRegisterOrderTakeListener(this);
		ManagerListener.newManagerListener().onUnRegisterOrderPayListener(this);
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
		mListView = (XListView) view.findViewById(R.id.pay_list);
		mTextView = (TextView) view.findViewById(R.id.pay_text);
		mTextView.setText(R.string.order_pay_text);
		mAdapter = new MyOrderAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
	}

	@Override
	public void requestData() {
		if (mUser != null) {
			RequestParam param = new RequestParam();
			HttpURL url = new HttpURL();
			if (mType == 3) {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_REMOVE_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mOrder.getmId());
				param.setmParserClassName(CommonParser.class.getName());
			} else if (mType == 5) {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_TAKE_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mOrder.getmId());
				param.setmParserClassName(CommonParser.class.getName());
			} else {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_LIST_URL);
				url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE)
						.setmGetParamValues("1");
				url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE)
						.setmGetParamValues(mSize + "");
				url.setmGetParamPrefix(JsonKey.VoucherKey.PAGE)
						.setmGetParamValues(mPage + "");
				param.setmParserClassName(TechOrderParser.class.getName());
			}
			param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setPostRequestMethod();
			param.setmHttpURL(url);
			RequestManager.getRequestData(getActivity(),
					createMyReqSuccessListener(), createMyReqErrorListener(),
					param);
		} else {
			showSmartToast(R.string.login_toast, Toast.LENGTH_SHORT);
			mLoadHandler.removeMessages(Constant.NET_SUCCESS);
			mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
		}
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (!isDetached()) {
					if (object instanceof ArrayList) {
						ArrayList<TechOrder> list = (ArrayList<TechOrder>) object;
						if (isRefresh) {
							mList.addAll(list);
						} else {
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
						mListView.stopLoadMore();
						mListView.stopRefresh();
						mPayHandler.removeMessages(MSG_LIST);
						mPayHandler.sendEmptyMessage(MSG_LIST);
					} else if (object instanceof ResultInfo) {
						ResultInfo info = (ResultInfo) object;
						if (mType == 3) {
							if (info.getmCode() == 0) {
								ManagerListener.newManagerListener()
										.notifyOrderPayListener(mOrder);
							}
						} else if (mType == 5) {
							if (info.getmCode() == 0) {
								ManagerListener.newManagerListener()
										.notifyOrderPayListener(mOrder);
							}
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
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}

	private void setNoData() {
		mListView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(getResources().getString(R.string.order_pay_text));
	}

	@Override
	public void onRefresh() {
		mPayHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mType = 0;
				isRefresh = false;
				mPage = 1;
				mList.clear();
				mListView.setPullLoadEnable(false);
				requestData();
			}
		}, 0);
	}

	@Override
	public void onLoadMore() {
		mPayHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mType = 0;
				isRefresh = true;
				mPage += 1;
				requestData();
			}
		}, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TechOrder order = (TechOrder) parent.getAdapter().getItem(position);
		Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order.getmId());
		startActivityForResult(intent, Constant.DETAIL_INTENT);
	}

	@Override
	public void onOrderCancelListener(TechOrder order) {
		mType = 3;
		mOrder = order;
		startReqTask(MyOrderPayFragment.this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.DETAIL_SUCCESS) {
			isRefresh = false;
			mType = 0;
			startReqTask(MyOrderPayFragment.this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mList.size() == 0) {
			isRefresh = false;
			mType = 0;
			startReqTask(MyOrderPayFragment.this);
		}
	}

	@Override
	public void onOrderTakeListener(TechOrder order) {
		mType = 5;
		mOrder = order;
		isRefresh = false;
		startReqTask(MyOrderPayFragment.this);
	}

	@Override
	public void onOrderPayListener(TechOrder order) {
		mType = 0;
		mOrder = order;
		isRefresh = false;
		startReqTask(MyOrderPayFragment.this);
	}

}
