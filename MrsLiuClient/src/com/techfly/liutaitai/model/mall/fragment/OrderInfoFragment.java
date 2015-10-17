package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.OrderInfoClick;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.parser.OrderInfoParser;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.ListViewForScrollView;

public class OrderInfoFragment extends CommonFragment {

	private TextView mTvState;
	private TextView mTvBtn1;
	private TextView mTvBtn2;

	private TextView mTvOrderNum;
	private TextView mTvTime;
	private TextView mTvCustomerName;
	private TextView mTvCustomerAddr;
	private TextView mTvTips;
	private ListViewForScrollView mListView;
	private CommonAdapter<Product> mAdapter;
	private ArrayList<Product> mDatas = new ArrayList<Product>();
	private TextView mTvOffsetValue;
	private TextView mTvPayWay;
	private TextView mTvProductCount;
	private TextView mTvTotalMoney;
	private TextView mTvDeliverFee;

	private User mUser;
	private String orderId;

	private MyOrder mOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
		orderId = getActivity().getIntent().getStringExtra(
				IntentBundleKey.ORDER_ID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_order_info, container, false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initHeader();
		initViews(view);
		startReqTask(this);
	}

	private void initHeader() {
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		setTitleText(R.string.order_info);

	}

	private void initViews(View view) {
		mTvBtn1 = (TextView) view.findViewById(R.id.order_info_tv_btn1);
		mTvBtn2 = (TextView) view.findViewById(R.id.order_info_tv_btn2);

		mTvState = (TextView) view.findViewById(R.id.order_info_tv_order_state);

		mTvOrderNum = (TextView) view
				.findViewById(R.id.order_info_tv_order_num);
		mTvTime = (TextView) view.findViewById(R.id.order_info_tv_time);
		mTvCustomerName = (TextView) view
				.findViewById(R.id.order_info_tv_customer_name);
		mTvCustomerAddr = (TextView) view
				.findViewById(R.id.order_info_tv_customer_addr);

		mTvTips = (TextView) view.findViewById(R.id.order_info_tv_tips);
		mTvOffsetValue = (TextView) view
				.findViewById(R.id.order_info_tv_offset_value);
		mTvProductCount = (TextView) view
				.findViewById(R.id.order_info_tv_product_count);
		mTvPayWay = (TextView) view.findViewById(R.id.order_info_tv_pay_way);
		mTvTotalMoney = (TextView) view
				.findViewById(R.id.order_info_tv_total_money);
		mTvDeliverFee = (TextView) view
				.findViewById(R.id.order_info_tv_deliver_fee);

		mListView = (ListViewForScrollView) view
				.findViewById(R.id.order_info_list);
		mAdapter = new CommonAdapter<Product>(getActivity(), mDatas,
				R.layout.item_order_basket_item) {

			@Override
			public void convert(ViewHolder holder, Product item, int position) {
				holder.setText(R.id.item_order_basket_item_tv_name,
						item.getmName());
				holder.setImageResource(Constant.IMG_URL + item.getmImg(),
						R.id.item_order_basket_item_iv);
				holder.setText(R.id.item_order_basket_item_tv_price,
						"￥" + item.getmPrice());
				holder.setText(R.id.item_order_basket_item_tv_unit,
						item.getmUnit());
				holder.setText(R.id.item_order_basket_item_tv_count,
						"X" + item.getmAmount());

			}
		};
		mListView.setAdapter(mAdapter);

	}

	@Override
	public void requestData() {

		if (mUser == null || TextUtils.isEmpty(orderId)) {// 未登录，先这样，过些时间在做
			SmartToast.makeText(getActivity(), R.string.error_get_user_id,
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_INFO);
		url.setmGetParamPrefix(Constant.ORDER_INFO_ID).setmGetParamValues(
				orderId);
		param.setmHttpURL(url);
		param.setmIsLogin(true);
		param.setmToken(mUser.getmToken());
		param.setmId(mUser.getmId());
		param.setPostRequestMethod();
		param.setmParserClassName(OrderInfoParser.class.getName());
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
				if (object instanceof MyOrder) {
					mOrder = (MyOrder) object;
					if (mOrder != null) {
						initData();
					}
				}

			}

		};
	}

	protected void initData() {

		mTvBtn1.setOnClickListener(new OrderInfoClick(this, mOrder.getmId(),
				mOrder));
		mTvBtn2.setOnClickListener(new OrderInfoClick(this, mOrder.getmId(),
				mOrder));
		setState(mOrder.getmState());

		mTvCustomerName.setText("收货人：" + mOrder.getmCustomerName());
		mTvCustomerAddr.setText("收货地址：" + mOrder.getmCustomerAddr());
		mTvOffsetValue.setText("-￥" + mOrder.getmOffsetValue());
		mTvDeliverFee.setText("-￥" + mOrder.getmDeliverFee());
		mTvOrderNum.setText("订单编号：" + mOrder.getmNum());
		String unit = "份";
		if (!TextUtils.isEmpty(mOrder.getmUnit())) {
			unit = mOrder.getmUnit();
		}
		mTvProductCount.setText("共" + mOrder.getmTotalCount() + unit);
		setPayWay();
		mTvTime.setText("下单时间：" + mOrder.getmTime());
		mTvTips.setText("备注：" + mOrder.getmTips());
		mTvTotalMoney.setText("￥" + mOrder.getmTotalPrice());
		if (mAdapter != null) {
			mDatas.clear();
			mDatas.addAll(mOrder.getmList());
			mAdapter.notifyDataSetChanged();
		}
	}

	private void setPayWay() {
		String payWay = "";
		switch (mOrder.getmPayType()) {
		case 0:
			payWay = getString(R.string.recharge_text4);
			break;
		case 1:
			payWay = getString(R.string.recharge_text2);
			break;
		case 2:
			payWay = getString(R.string.recharge_text1);
			break;
		case 3:
			payWay = getString(R.string.recharge_text5);
			break;

		default:
			break;
		}

		payWay = "(" + payWay + ")";
		mTvPayWay.setText("总数" + payWay);

	}

	private void setState(int state) {
		mTvBtn1.setVisibility(View.INVISIBLE);
		mTvBtn2.setVisibility(View.INVISIBLE);
		switch (state) {
		case -2:
			mTvState.setText(R.string.order_state_2_);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_5);
			break;
		case -1:
			mTvState.setText(R.string.order_state_1_);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_5);
			break;
		case 0:
			mTvState.setText(R.string.order_state_0);
			mTvBtn1.setVisibility(View.VISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn1.setText(R.string.order_text_0);
			mTvBtn2.setText(R.string.order_text_1);
			break;
		case 1:
			mTvState.setText(R.string.order_state_1);
			mTvBtn1.setVisibility(View.VISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn1.setText(R.string.order_text_0);
			mTvBtn2.setText(R.string.order_text_2);
			break;
		case 2:
			mTvState.setText(R.string.order_state_2);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_2);
			break;
		case 3:
			mTvState.setText(R.string.order_state_3);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_2);
			break;
		case 4:
			mTvState.setText(R.string.order_state_4);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_2);
			break;
		case 5:
			mTvState.setText(R.string.order_state_5);
			mTvBtn1.setVisibility(View.VISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn1.setText(R.string.order_text_3);
			mTvBtn2.setText(R.string.order_text_4);
			break;
		case 6:
			mTvState.setText(R.string.order_state_6);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_5);
			break;
		case 7:
			mTvState.setText(R.string.order_state_7);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_2);
			break;
		case 8:
			mTvState.setText(R.string.order_state_1);
			mTvBtn1.setVisibility(View.VISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn1.setText(R.string.order_text_0);
			mTvBtn2.setText(R.string.order_text_2);
			break;
		case 9:
			mTvState.setText(R.string.order_state_9);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_5);
			break;
		case 10:
			mTvState.setText(R.string.order_state_10);
//			mTvBtn1.setVisibility(View.INVISIBLE);
//			mTvBtn2.setVisibility(View.VISIBLE);
//			mTvBtn2.setText(R.string.order_text_5);
			
			break;

		default:
			break;
		}

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

	public void refreshData() {
		startReqTask(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (Constant.isShouldRefresh) {
			startReqTask(this);
			Constant.isShouldRefresh = false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == OrderBastketFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
			if(data.getBooleanExtra(IntentBundleKey.DATA, false)){
				refreshData();
			}
		}
	}
	
	
}
