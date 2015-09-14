package com.techfly.liutaitai.model.mall;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.fragment.OrderInfoFragment;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.UIHelper;

public class OrderInfoClick implements OnClickListener {

	private OrderInfoFragment mFragment;
	private String mId;
	private MyOrder mOrder;

	public OrderInfoClick(OrderInfoFragment mFragment, String mId,
			MyOrder mOrder) {
		super();
		this.mFragment = mFragment;
		this.mId = mId;
		this.mOrder = mOrder;
	}

	@Override
	public void onClick(View v) {
		TextView tv = (TextView) v;

		if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_1))) {
			// 付款
			SmartToast.makeText(mFragment.getActivity(), "去付钱了骚年",
					Toast.LENGTH_SHORT).show();
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_2))) {
			contactService();// 联系客服
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_4))) {
			// 申请售后
			UIHelper.toAfterActivity(mFragment, mId);
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_5))) {
			// 删除订单
			deleteOrder();
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_0))) {
			cancelOrder();// 取消订单
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_3))) {
			// 去评价

			UIHelper.toOrderEvaActivity(mFragment, mOrder);
		}

	}

	private void deleteOrder() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.DELETE_ORDER);
		url.setmGetParamPrefix(Constant.ORDER_INFO_ID).setmGetParamValues(mId);
		param.setmHttpURL(url);
		param.setmIsLogin(true);
		param.setmToken(SharePreferenceUtils
				.getInstance(mFragment.getActivity()).getUser().getmToken());
		param.setmId(SharePreferenceUtils.getInstance(mFragment.getActivity())
				.getUser().getmId());
		param.setPostRequestMethod();
		param.setmParserClassName(CommonParser.class.getName());
		RequestManager.getRequestData(mFragment.getActivity(),
				createDeleteReqSuccessListener(), createMyReqErrorListener(),
				param);

	}

	private Response.Listener<Object> createDeleteReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (mFragment.getActivity() == null || mFragment.isDetached()) {
					return;
				}

				if (object instanceof ResultInfo) {
					ResultInfo ri = (ResultInfo) object;
					if (ri != null) {
						if (ri.getmCode() == 0) {
							SmartToast.makeText(mFragment.getActivity(),
									"操作成功", Toast.LENGTH_SHORT).show();
							mFragment.getActivity().finish();
						} else {
							SmartToast.makeText(mFragment.getActivity(),
									"操作失败", Toast.LENGTH_SHORT).show();
						}
					}

				}

			}

		};
	}

	private void contactService() {
		final String phone = mFragment.getString(R.string.service_phone);
		AlertDialog.Builder builder = new Builder(mFragment.getActivity());
		builder.setMessage("确认拨打电话  " + phone + "  ？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phone));
						mFragment.startActivity(intent);
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();

	}

	private void cancelOrder() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CANCEL_ORDER);
		url.setmGetParamPrefix(Constant.ORDER_INFO_ID).setmGetParamValues(mId);
		param.setmHttpURL(url);
		param.setmIsLogin(true);
		param.setmToken(SharePreferenceUtils
				.getInstance(mFragment.getActivity()).getUser().getmToken());
		param.setmId(SharePreferenceUtils.getInstance(mFragment.getActivity())
				.getUser().getmId());
		param.setPostRequestMethod();
		param.setmParserClassName(CommonParser.class.getName());
		RequestManager
				.getRequestData(mFragment.getActivity(),
						createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (mFragment.getActivity() == null || mFragment.isDetached()) {
					return;
				}

				if (object instanceof ResultInfo) {
					ResultInfo ri = (ResultInfo) object;
					if (ri != null) {
						if (ri.getmCode() == 0) {
							SmartToast.makeText(mFragment.getActivity(),
									"操作成功", Toast.LENGTH_SHORT).show();
							// 之后的一些操作,例如刷新列表
							mFragment.refreshData();
						} else {
							SmartToast.makeText(mFragment.getActivity(),
									"操作失败", Toast.LENGTH_SHORT).show();
						}
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
				SmartToast.makeText(mFragment.getActivity(),
						R.string.loading_fail, Toast.LENGTH_SHORT).show();
			}
		};
	}

}
