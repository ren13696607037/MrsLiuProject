package com.techfly.liutaitai.model.mall;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.UIHelper;

public class OrderBasketClick implements OnClickListener {

	private Context mContext;
	private Fragment mFragment;
	private String mId;

	public OrderBasketClick(Context mContext) {
		this.mContext = mContext;
	}

	public OrderBasketClick(Fragment fragment, String id) {
		this.mFragment = fragment;
		this.mId = id;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.order_basket_item_tv_bt1:
			clickBtn1((TextView) v);
			break;
		case R.id.order_basket_item_tv_bt2:
			clickBtn2((TextView) v);
			break;
		case R.id.order_basket_item_parent:
			UIHelper.toOrderInfoActivity(mFragment, mId);
			break;

		default:
			break;
		}

	}

	private void clickBtn2(TextView v) {

	}

	private void clickBtn1(TextView v) {

	}

}
