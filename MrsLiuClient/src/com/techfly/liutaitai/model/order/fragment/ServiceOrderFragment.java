package com.techfly.liutaitai.model.order.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.order.adapter.ServiceAdapter;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;

public class ServiceOrderFragment extends CommonFragment {
	private XListView mListView;
	private ArrayList<Service> mList = new ArrayList<Service>();
	private ServiceAdapter mAdapter;
	private int start = 0, requestTimes = 0;// 开始和限制条数
	private boolean reqFinish = false;
	private boolean isRefrash = false;
	private boolean isFirst = true;
	private ImageView mIvNoContent;
	private final int MSG_LIST = 0x101;
	private User mUser;
	public Handler mOrderHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				mListView.setVisibility(View.VISIBLE);
				mIvNoContent.setVisibility(View.GONE);
				if (mList.size() == 0) {
					setNoData();
				}
				mAdapter.updateList(mList);
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
//		ManagerListener.newManagerListener().onRegisterRefreshListener(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_orderall, container, false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		ManagerListener.newManagerListener().onUnRegisterRefreshListener(this);
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
		mListView = (XListView) view.findViewById(R.id.all_list);
		mIvNoContent = (ImageView) view.findViewById(R.id.all_text);
		mAdapter = new ServiceAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
//		mListView.setXListViewListener(this);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Order order = (Order) parent.getAdapter().getItem(position);
//				Intent intent = new Intent(getActivity(), MyOrderActivity.class);
//				intent.putExtra(IntentBundleKey.ORDER_ID, order.getmNo());
//				startActivity(intent);
			}
		});
	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_SERVICE_URL);
		
		param.setmHttpURL(url);
//		param.setmParserClassName(OrderParser.class.getName());
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
					onLoad();
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);

					ArrayList<Service> list = (ArrayList<Service>) object;
					if (list.size() == 0 || list == null) {
						if (isFirst) {
							isFirst = false;
							reqFinish = true;
							mListView.setPullLoadEnable(false);
							mOrderHandler.removeMessages(MSG_LIST);
							mOrderHandler.sendEmptyMessage(MSG_LIST);
							return;
						}

						SmartToast.makeText(getActivity(),
								R.string.xlistview_no_more_data,
								Toast.LENGTH_SHORT).show();
						mListView.setPullLoadEnable(false);
						reqFinish = true;
						mOrderHandler.removeMessages(MSG_LIST);
						mOrderHandler.sendEmptyMessage(MSG_LIST);
						return;
					}

					if (list.size() != 10) {
						reqFinish = true;
						mListView.setPullLoadEnable(false);
					} else {
						reqFinish = false;
						mListView.setPullLoadEnable(true);
					}

					if (isRefrash) {
						mList.clear();
						mList.addAll(list);
						start = mList.size();
						isRefrash = false;
					} else {
						mList.addAll(list);
						isRefrash = false;
						start = mList.size();
					}
					mOrderHandler.removeMessages(MSG_LIST);
					mOrderHandler.sendEmptyMessage(MSG_LIST);
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onLoad();
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				mOrderHandler.removeMessages(MSG_LIST);
				mOrderHandler.sendEmptyMessage(MSG_LIST);
				AppLog.Loge(" data failed to load" + error.getMessage());
			}
		};
	}

	private void setNoData() {
		mListView.setVisibility(View.GONE);
		mIvNoContent.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(mUser == null &&  SharePreferenceUtils.getInstance(getActivity()).getUser() != null){
			mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
			start = 0;
			isRefrash = true;
			startReqTask(ServiceOrderFragment.this);
		}else if(mUser != null){
			
		}else{
			mListView.setVisibility(View.GONE);
			mIvNoContent.setVisibility(View.VISIBLE);
		}
	}

//	@Override
//	public void onRefreshListener() {
//		if (mUser != null) {
//			startReqTask(OrderNowFragment.this);
//		} else {
//			mListView.setVisibility(View.GONE);
//			mIvNoContent.setVisibility(View.VISIBLE);
//		}
//	}
//
//	@Override
//	public void onLoadMore() {// 上拉加载更多
//
//		if (reqFinish) {
//			ShowUtils.showToast(getActivity(), R.string.xlistview_no_more_data);
//			onLoad();
//		} else {
//			isRefrash = false;
//			requestData();
//		}
//	}
//
//	@Override
//	public void onRefresh() {// 刷新，刷新的话请求第一页？
//		requestTimes = 0;
//		start = 0;
//		isRefrash = true;
//		reqFinish = false;
//		requestData();
//	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

}
