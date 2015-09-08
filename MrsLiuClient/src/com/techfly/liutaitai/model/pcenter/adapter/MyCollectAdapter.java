package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.MyCollect;

public class MyCollectAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<MyCollect> mList;
	public MyCollectAdapter(Context context,ArrayList<MyCollect> list){
		this.mContext=context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null?mList.get(position):new MyCollect();
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_mycollect, null);
			holder.mImageView=(ImageView) convertView.findViewById(R.id.icollect_img);
			holder.mTvPrice=(TextView) convertView.findViewById(R.id.icollect_price);
			holder.mTvTitle=(TextView) convertView.findViewById(R.id.icollect_title);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.mImageView);
		holder.mTvPrice.setText(mContext.getString(R.string.collect_price,mList.get(position).getmPrice()));
		holder.mTvTitle.setText(mList.get(position).getmTitle());
		return convertView;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mTvTitle;
		private TextView mTvPrice;
	}

}
