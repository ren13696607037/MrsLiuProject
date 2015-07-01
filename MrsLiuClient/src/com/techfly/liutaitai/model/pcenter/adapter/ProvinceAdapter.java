package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Province;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProvinceAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Province> mList;
	public ProvinceAdapter(Context context,ArrayList<Province> list){
		this.mContext=context;
		this.mList=list;
	}
	public void updateList(ArrayList<Province> list){
		this.mList=list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null?mList.get(position):new Province();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_group, null);
			holder.mTextView=(TextView) convertView.findViewById(R.id.group_text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mTextView.setText(mList.get(position).getmName());
		return convertView;
	}
	class ViewHolder{
		private TextView mTextView;
	}

	

}
