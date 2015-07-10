package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Balance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BalanceAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Balance> mList;
	public BalanceAdapter(Context context, ArrayList<Balance> list){
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
	public Object getItem(int arg0) {
		return mList != null ? mList.get(arg0) : new Balance();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.item_balance, null);
			holder.mPrice = (TextView) arg1.findViewById(R.id.ibalance_name);
			holder.mSend = (TextView) arg1.findViewById(R.id.ibalance_send);
			holder.mTime = (TextView) arg1.findViewById(R.id.ibalance_time);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		holder.mPrice.setText(mList.get(arg0).getmPrice());
		holder.mSend.setText(mList.get(arg0).getmSend());
		holder.mTime.setText(mList.get(arg0).getmTime());
		return arg1;
	}
	
	class ViewHolder{
		private TextView mPrice;
		private TextView mSend;
		private TextView mTime;
	}
}
