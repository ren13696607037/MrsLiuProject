package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.OrderBastketAdapter;
import com.techfly.liutaitai.model.mall.parser.ProductCategoryParser;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;

public class OrderBastketFragment extends CommonFragment {
	
	private XListView mListView;
	private ArrayList<MyOrder> mDatas = new ArrayList<MyOrder>();
	private OrderBastketAdapter mDapter;
	private int start = 0;// 开始和限制条数
	private boolean reqFinish = false;
	private boolean isRefresh = false;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
//		setRightMoreIcon(R.drawable.search, new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				UIHelper.toSearchActivity(getActivity(), 0);
//			}
//		});
	}

	private void initView(View view) {

	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_CATEGORY);
		param.setmHttpURL(url);
		param.setmParserClassName(ProductCategoryParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);

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
				showMessage(R.string.loading_fail);
			}
		};
	}
}
