package com.techfly.liutaitai.model.mall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;

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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order_basket, null);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}


	static class ViewHolder {
		
	}
}
