package com.techfly.liutaitai.model.order.adapter;

import java.util.ArrayList;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Service> mList;
	public ServiceAdapter(Context context, ArrayList<Service> list){
		this.mContext = context;
		this.mList = list;
	}
	public void updateList(ArrayList<Service> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mList != null ? mList.get(position) : new Service();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_service, null);
			holder.mButton = (Button) convertView.findViewById(R.id.ios_btn);
			holder.mButton2 = (Button) convertView.findViewById(R.id.ios_btn2);
			holder.mDate = (TextView) convertView.findViewById(R.id.ios_date);
			holder.mImageView = (ImageView) convertView.findViewById(R.id.ios_img);
			holder.mName = (TextView) convertView.findViewById(R.id.ios_name);
			holder.mPrice = (TextView) convertView.findViewById(R.id.ios_price);
			holder.mState = (TextView) convertView.findViewById(R.id.ios_state);
			holder.mTech = (TextView) convertView.findViewById(R.id.ios_tech);
			holder.mTime = (TextView) convertView.findViewById(R.id.ios_time);
			holder.mType = (TextView) convertView.findViewById(R.id.ios_type);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mType.setText(mList.get(position).getmServiceType());
		holder.mDate.setText(mList.get(position).getmCash());
		holder.mName.setText(mList.get(position).getmServiceName());
		holder.mPrice.setText(mList.get(position).getmServicePrice());
		return convertView;
	}
	
	class ViewHolder{
		private TextView mType;
		private TextView mDate;
		private TextView mTime;
		private TextView mName;
		private TextView mTech;
		private TextView mState;
		private Button mButton;
		private Button mButton2;
		private TextView mPrice;
		private ImageView mImageView;
	}

}
