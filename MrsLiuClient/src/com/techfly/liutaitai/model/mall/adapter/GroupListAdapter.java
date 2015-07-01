package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.GroupBean;

public class GroupListAdapter extends BaseAdapter {
	private Context mContext;
	private List<GroupBean> mDatas;

	
	
	public GroupListAdapter(Context mContext, List<GroupBean> mDatas) {
		this.mContext = mContext;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas == null ? null : mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group_list, null);
			vh.mName = (TextView) convertView.findViewById(R.id.product_name);
			vh.mPrice = (TextView) convertView.findViewById(R.id.product_price);
			vh.mMarketPrice = (TextView) convertView.findViewById(R.id.product_marketprice);
			vh.mMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			vh.mImage = (ImageView) convertView.findViewById(R.id.product_image);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.mName.setText(mDatas.get(position).getName());
		vh.mMarketPrice.setText(mContext.getString(R.string.market_price,mDatas.get(position).getMarketPrice()));
		vh.mPrice.setText(mContext.getString(R.string.group_price,mDatas.get(position).getPrice()));
		return convertView;
	}

	static class ViewHolder {
		TextView mName;
		TextView mPrice;
		TextView mMarketPrice;
		ImageView mImage;
	}
}
