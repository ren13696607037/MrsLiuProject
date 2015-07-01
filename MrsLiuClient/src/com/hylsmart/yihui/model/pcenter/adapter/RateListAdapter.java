package com.hylsmart.yihui.model.pcenter.adapter;

import java.util.ArrayList;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.pcenter.bean.Rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RateListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Rate> mList;
	public RateListAdapter(Context context,ArrayList<Rate> list){
		this.mContext=context;
		this.mList=list;
	}
	public void updateList(ArrayList<Rate> list){
		this.mList=list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null?mList.get(position):new Rate();
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_ratelist, null);
			holder.mTvContent=(TextView) convertView.findViewById(R.id.iratelist_content);
			holder.mTvName=(TextView) convertView.findViewById(R.id.iratelist_name);
			holder.mTvTime=(TextView) convertView.findViewById(R.id.iratelist_time);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mTvContent.setText(mList.get(position).getmContent());
		holder.mTvTime.setText(mList.get(position).getmTime());
		holder.mTvName.setText(mList.get(position).getmName());
		return convertView;
	}
	class ViewHolder{
		private TextView mTvName;
		private TextView mTvTime;
		private TextView mTvContent;
	}

}
