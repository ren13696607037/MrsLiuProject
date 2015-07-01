package com.hylsmart.yihui.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.mall.bean.Category;

public class CategoryInfoRightListAdapter extends BaseAdapter {
	private List<Category> mDatas;
	private Context context;

	public CategoryInfoRightListAdapter(List<Category> mDatas, Context context) {
		this.mDatas = mDatas;
		this.context = context;
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
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_category_info_right_list, null);
			mHolder = new ViewHolder();
			mHolder.textView = (TextView) convertView.findViewById(R.id.category_name);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
//		mHolder.textView.setText(mDatas.get(position).getName());
		return convertView;
	}

	public void refreshData(List<Category> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView textView;
	}
}
