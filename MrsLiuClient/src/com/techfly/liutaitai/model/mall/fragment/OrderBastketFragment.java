package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.OrderBastketAdapter;
import com.techfly.liutaitai.model.mall.bean.help.ListOrder;
import com.techfly.liutaitai.model.mall.parser.OrderBasketParser;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
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
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class OrderBastketFragment extends CommonFragment implements
		IXListViewListener {

	private TextView mTvNoData;

	private XListView mListView;
	private ArrayList<MyOrder> mDatas = new ArrayList<MyOrder>();
	private OrderBastketAdapter mAdapter;
	private int start = 1;// 开始和限制条数
	private boolean reqFinish = false;
	private boolean isRefresh = false;

	private User mUser;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (mDatas.size() == 0) {
				mTvNoData.setVisibility(View.VISIBLE);
				mTvNoData.setClickable(false);
				mTvNoData.setText("没有订单~");
			} else {
				mTvNoData.setVisibility(View.GONE);
				if (msg.what == Constant.NOTIFY_LIST && mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_order_basket, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initHeader(view);
		initView(view);
		// requestData();
		startReqTask(this);
	}

	private void initHeader(View view) {
		setTitleText(R.string.category_title);
		// setRightMoreIcon(R.drawable.search, new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// UIHelper.toSearchActivity(getActivity(), 0);
		// }
		// });
	}

	private void initView(View view) {
		mTvNoData = (TextView) view.findViewById(R.id.order_basket_no_data);

		mListView = (XListView) view.findViewById(R.id.order_basket_list);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(true);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mAdapter = new OrderBastketAdapter(getActivity(), mDatas, this);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void requestData() {
		if (mUser == null) {// 未登录，先这样，过些时间在做
			SmartToast.makeText(getActivity(), R.string.error_get_user_id,
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
				+ Constant.ORDER_BASKET_LIST);
		url.setmGetParamPrefix(Constant.PAGE).setmGetParamValues(start + "");
		url.setmGetParamPrefix(Constant.SIZE).setmGetParamValues("10");
		param.setmHttpURL(url);
		param.setmIsLogin(true);
		param.setmToken(mUser.getmToken());
		param.setmId(mUser.getmId());
		param.setPostRequestMethod();
		param.setmParserClassName(OrderBasketParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (getActivity() == null || isDetached()) {
					return;
				}
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				onLoad();
				if (object instanceof ListOrder) {
					ListOrder lc = (ListOrder) object;
					if (lc != null) {
						ArrayList<MyOrder> list = lc.getmArrayList();
						if (list.size() == 0 || list == null) {

							reqFinish = true;
							mListView.setPullLoadEnable(false);
							return;
						}

						if (list.size() != 10) {
							reqFinish = true;
							mListView.setPullLoadEnable(false);
						} else {
							reqFinish = false;
							mListView.setPullLoadEnable(true);
						}

						if (isRefresh) {
							mDatas.clear();
							mDatas.addAll(list);
							isRefresh = false;
						} else {
							mDatas.addAll(list);
							isRefresh = false;
						}
						start++;

						mHandler.removeMessages(Constant.NOTIFY_LIST);
						mHandler.sendEmptyMessage(Constant.NOTIFY_LIST);
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
				mLoadHandler.removeMessages(Constant.NET_FAILURE);
				mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
				onLoad();
				showMessage(R.string.loading_fail);
			}
		};
	}

	@Override
	public void onRefresh() {
		start = 1;
		isRefresh = true;
		reqFinish = false;
		requestData();
	}

	@Override
	public void onLoadMore() {
		if (reqFinish) {
			SmartToast.makeText(getActivity(), R.string.xlistview_no_more_data,
					Toast.LENGTH_SHORT).show();
			onLoad();
		} else {
			isRefresh = false;
			requestData();
		}

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

}
