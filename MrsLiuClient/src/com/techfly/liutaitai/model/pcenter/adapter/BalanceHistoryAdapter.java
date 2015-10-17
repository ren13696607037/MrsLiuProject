package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Balance;

public class BalanceHistoryAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Balance> mList;
	public BalanceHistoryAdapter(Context context, ArrayList<Balance> list){
		this.mContext = context;
		this.mList = list;
	}
	public void updateList(ArrayList<Balance> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mList != null ? mList.get(position) : new Balance();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_balance_history, null);
			holder.detail = (TextView) convertView.findViewById(R.id.ibhistory_detail);
			holder.time = (TextView) convertView.findViewById(R.id.ibhistory_time);
			holder.price = (TextView) convertView.findViewById(R.id.ibhistory_price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.detail.setText(mList.get(position).getmSend());
		holder.time.setText(mList.get(position).getmTime());
		holder.price.setText(mList.get(position).getmPrice());
		return convertView;
	}
	class ViewHolder{
		private TextView detail;
		private TextView price;
		private TextView time;
	}

}
