package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Voucher;

public class VoucherAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Voucher> mList;
	public VoucherAdapter(Context context, ArrayList<Voucher> list){
		this.mContext = context;
		this.mList = list;
	}
	public void updateList(ArrayList<Voucher> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mList != null ? mList.get(position) : new Voucher();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_voucher, null);
			holder.mNeed = (TextView) convertView.findViewById(R.id.ivoucher_text);
			holder.mPrice = (TextView) convertView.findViewById(R.id.ivoucher_price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mNeed.setText(mContext.getString(R.string.voucher_text5, mList.get(position).getmNeed()));
		holder.mPrice.setText(mList.get(position).getmPrice());
		return convertView;
	}
	
	class ViewHolder {
		private TextView mPrice;
		private TextView mNeed;
	}

}
