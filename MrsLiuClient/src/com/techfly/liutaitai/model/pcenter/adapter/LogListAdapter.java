package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LogListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Content> mList;
	public LogListAdapter(Context context,ArrayList<Content> list){
		this.mContext=context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null?mList.get(position):new Content();
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_searchlog, null);
			holder.mImageView=(ImageView) convertView.findViewById(R.id.ilog_img);
			holder.mTvContent=(TextView) convertView.findViewById(R.id.ilog_content);
			holder.mTvLine=(TextView) convertView.findViewById(R.id.ilog_line);
			holder.mTvLine1=(TextView) convertView.findViewById(R.id.ilog_line1);
			holder.mTvTime=(TextView) convertView.findViewById(R.id.ilog_time);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mTvLine.setVisibility(View.VISIBLE);
		holder.mTvLine1.setVisibility(View.VISIBLE);
		if(position==0){
			holder.mTvContent.setTextColor(mContext.getResources().getColor(R.color.color_blue));
			holder.mImageView.setImageResource(R.drawable.log_img);
			if(mList.size()>1){
				holder.mTvLine.setVisibility(View.INVISIBLE);
			}else{
				holder.mTvLine.setVisibility(View.INVISIBLE);
				holder.mTvLine1.setVisibility(View.INVISIBLE);
			}
		}else{
			holder.mTvContent.setTextColor(mContext.getResources().getColor(R.color.button_login1));
			holder.mImageView.setImageResource(R.drawable.log_img1);
			if(mList.size()-1==position){
				holder.mTvLine1.setVisibility(View.INVISIBLE);
			}
		}
		holder.mTvContent.setText(mList.get(position).getmMessage());
		holder.mTvTime.setText(mList.get(position).getmTime());
		return convertView;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mTvLine;
		private TextView mTvLine1;
		private TextView mTvContent;
		private TextView mTvTime;
	}

}
