package com.hylsmart.yihui.model.home.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mDatas;
	private int mItemLayoutId;

	public CommonAdapter(Context context, List<T> data, int layoutId) {
		this.mContext = context;
		this.mDatas = data;
		this.mItemLayoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDatas != null ? mDatas.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return mDatas != null ? mDatas.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		convert(viewHolder, getItem(position), position);
		return viewHolder.getConvertView();
	}

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.getHolder(convertView, mContext, parent,
				mItemLayoutId, position);
	}

	public void updateListView(List<T> datas) {
		mDatas = datas;
		notifyDataSetChanged();
	}

	public abstract void convert(ViewHolder holder, T item, int position);

}
