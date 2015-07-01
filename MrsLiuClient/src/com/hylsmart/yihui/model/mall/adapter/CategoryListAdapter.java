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

public class CategoryListAdapter extends BaseAdapter {
	private Context context;
	private List<Category> mItems;

	public CategoryListAdapter(Context context, List<Category> mItems) {
		this.context = context;
		this.mItems = mItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems == null ? 0 : mItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItems == null ? null : mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_category_list, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.category_name);
			viewHolder.info = (TextView) convertView.findViewById(R.id.category_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.name.setText(mItems.get(position).getName());
//		StringBuilder sb = new StringBuilder();
//		for (Category mCategory : mItems.get(position).getmChildCategory()) {
//			sb.append(mCategory.getName()).append("|");
//		}
//		viewHolder.info.setText(sb.toString());
		return convertView;
	}

	public void refreshView(List<Category> mItems) {
		this.mItems = mItems;
		this.notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView name;
		TextView info;
	}
}
