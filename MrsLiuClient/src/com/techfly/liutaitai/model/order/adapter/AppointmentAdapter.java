package com.techfly.liutaitai.model.order.adapter;

import java.util.ArrayList;

import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.Service;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AppointmentAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Product> mList;
	public AppointmentAdapter(Context context, ArrayList<Product> list){
		this.mContext = context;
		this.mList = list;
	}
	public void updateList(ArrayList<Product> list){
		this.mList = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mList != null ? mList.get(position) : new Product();
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
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	class ViewHolder{
		
	}

}
