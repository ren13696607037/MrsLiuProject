package com.techfly.liutaitai.model.mall;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.fragment.OrderInfoFragment;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
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
			ArrayList<Product> list = mOrder.getmList();
			if (list == null || list.size() == 0) {
				SmartToast.makeText(mFragment.getActivity(), "订单没有商品~",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String name = "";
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					name = name + list.get(i).getmName();
				} else {
					name = name + list.get(i).getmName() + ",";
				}

			}
			// 付款
			Intent intent = new Intent(mFragment.getActivity(),
					TakingOrderActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(IntentBundleKey.ORDER_ID, mOrder.getmNum());// 支付订单号
			
			float totalPrice = (float) (Float.parseFloat(mOrder.getmTotalPrice()) - mOrder .getmOffsetValue());
            long l1 = Math.round(totalPrice * 100); // 四舍五入
            totalPrice = (float) (l1 / 100.00); // 注意：使用 100.0 而不是 100
			;	//	
			// 例如
			// 5666995444RSFR
			bundle.putString(IntentBundleKey.ORDER_MONEY,
			        totalPrice+"");// 支付的钱
			bundle.putString(IntentBundleKey.ORDER_PRODUCT, name);// 商品名称
			intent.putExtra(IntentBundleKey.DATA, bundle);
			intent.putExtra(IntentBundleKey.IS_FROM_ORDER, true);

			mFragment.startActivity(intent);
			Constant.isShouldRefresh = true;
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
			confirmDelete();
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_0))) {
			confirmCancel();// 取消订单
		} else if (tv.getText().toString()
				.equals(mFragment.getString(R.string.order_text_3))) {
			// 去评价

			UIHelper.toOrderEvaActivity(mFragment, mOrder);
		}

	}
	
	private void confirmDelete() {
		AlertDialogUtils.displayMyAlertChoice(mFragment.getActivity(), "删除订单",
				"确定删除订单？", "确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteOrder();

					}
				}, "取消", null).show();
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
							Intent intent = new Intent();
							intent.putExtra(IntentBundleKey.DATA, true);
							mFragment.getActivity().setResult(Activity.RESULT_OK, intent);
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
	
	private void confirmCancel() {
		AlertDialogUtils.displayMyAlertChoice(mFragment.getActivity(), "删除订单",
				"确定取消订单？", "确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						cancelOrder();

					}
				}, "取消", null).show();
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
							Intent intent = new Intent();
							intent.putExtra(IntentBundleKey.DATA, true);
							mFragment.getActivity().setResult(Activity.RESULT_OK, intent);
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
