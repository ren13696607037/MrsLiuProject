package com.techfly.liutaitai.model.mall;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.techfly.liutaitai.R;

public class OrderBasketClick implements OnClickListener {

	private Context mContext;

	public OrderBasketClick(Context mContext) {
		super();
		this.mContext = mContext;
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
		case R.id.order_basket_item_list:

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
