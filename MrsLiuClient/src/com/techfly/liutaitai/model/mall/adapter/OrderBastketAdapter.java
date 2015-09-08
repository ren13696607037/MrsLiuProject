package com.techfly.liutaitai.model.mall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.OrderBasketClick;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.view.ListViewForScrollView;

public class OrderBastketAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MyOrder> mDatas;

	public OrderBastketAdapter(Context context, ArrayList<MyOrder> datas) {
		this.context = context;
		this.mDatas = datas;
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas == null ? null : mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_order_basket, null);
			viewHolder.mTvType = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_type);
			viewHolder.mTvTime = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_time);
			viewHolder.mListView = (ListViewForScrollView) convertView
					.findViewById(R.id.order_basket_item_list);
			viewHolder.mTvCount = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_count);
			viewHolder.mTvState = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_state);
			viewHolder.mTvTotalPrice = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_total_price);
			viewHolder.mTvBtn1 = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_bt1);
			viewHolder.mTvBtn2 = (TextView) convertView
					.findViewById(R.id.order_basket_item_tv_bt2);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		MyOrder order = mDatas.get(position);
		setState(viewHolder.mTvBtn1, viewHolder.mTvBtn2, viewHolder.mTvState,
				order.getmState());
		setType(viewHolder.mTvType, order.getmType());
		viewHolder.mTvBtn1.setOnClickListener(new OrderBasketClick());
		viewHolder.mTvBtn2.setOnClickListener(new OrderBasketClick());
		viewHolder.mTvCount.setText("总数：" + order.getmTotalCount()
				+ order.getmUnit());
		viewHolder.mTvTime.setText(order.getmTime());
		viewHolder.mTvTotalPrice.setText("合计：￥" + order.getmTotalPrice());
		CommonAdapter<Product> adapter = new CommonAdapter<Product>(context,
				order.getmList(), R.layout.item_order_basket_item) {

			@Override
			public void convert(
					com.techfly.liutaitai.util.adapter.ViewHolder holder,
					Product item, int position) {
				holder.setText(R.id.item_order_basket_item_tv_name,
						item.getmName());
				holder.setImageResource(item.getmImg(),
						R.id.item_order_basket_item_iv);
				holder.setText(R.id.item_order_basket_item_tv_price,
						"￥" + item.getmPrice());
				holder.setText(R.id.item_order_basket_item_tv_unit,
						item.getmUnit());
				holder.setText(R.id.item_order_basket_item_tv_count,
						"X" + item.getmAmount());

			}
		};
		viewHolder.mListView.setAdapter(adapter);

		return convertView;
	}

	private void setType(TextView mTvType, int type) {
		switch (type) {
		case 0:
			mTvType.setText("干洗");
			mTvType.setBackgroundResource(R.color.order_basket_bg_type_0);
			break;
		case 1:
			mTvType.setText("生鲜");
			mTvType.setBackgroundResource(R.color.order_basket_bg_type_1);
			break;
		case 2:
			mTvType.setText("鲜花");
			mTvType.setBackgroundResource(R.color.order_basket_bg_type_2);
			break;
		case 3:
			mTvType.setText("奢侈品保养");
			mTvType.setBackgroundResource(R.color.order_basket_bg_type_3);
			break;

		default:
			break;
		}

	}

	private void setState(TextView mTvBtn1, TextView mTvBtn2,
			TextView mTvState, int state) {
		mTvBtn1.setVisibility(View.INVISIBLE);
		mTvBtn2.setVisibility(View.INVISIBLE);
		switch (state) {
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
			mTvState.setText(R.string.order_state_8);
			break;
		case 9:
			mTvState.setText(R.string.order_state_9);
		case 10:
			mTvState.setText(R.string.order_state_1_);
			mTvBtn1.setVisibility(View.INVISIBLE);
			mTvBtn2.setVisibility(View.VISIBLE);
			mTvBtn2.setText(R.string.order_text_5);
			break;

		default:
			break;
		}

	}

	class ViewHolder {

		private TextView mTvType;
		private TextView mTvTime;
		private ListViewForScrollView mListView;
		private TextView mTvCount;
		private TextView mTvTotalPrice;
		private TextView mTvState;
		private TextView mTvBtn1;
		private TextView mTvBtn2;

	}
}
