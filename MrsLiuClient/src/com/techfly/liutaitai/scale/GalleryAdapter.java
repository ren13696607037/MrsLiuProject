package com.techfly.liutaitai.scale;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techfly.liutaitai.R;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<GalleryData> mList;
	private ViewHolder mHolder;
	private DragViewPager mViewPager;
	private boolean isPictureDel = false;
	protected Dialog mAlertDialog;
	public void setPictureDel(boolean isPictureDel) {
		this.isPictureDel = isPictureDel;
	}

	/**
	 * @param context
	 * @param mListUrl
	 */
	public GalleryAdapter(Context context, ArrayList<GalleryData> list) {
		this.context = context;
		this.mList = list;
	}

	public GalleryAdapter(Context context, ArrayList<GalleryData> list, DragViewPager viewPager) {
		this.context = context;
		this.mList = list;
		this.mViewPager = viewPager;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		mHolder = null;
		int size = mList.size();
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.gallery_item1, null);
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.title_gallery);
			mHolder.mTextView2 = (TextView) convertView.findViewById(R.id.num_gallery);
			mHolder.mDel = (TextView) convertView.findViewById(R.id.delete_gallery);
			mHolder.left = (ImageView) convertView.findViewById(R.id.left_gallery);
			mHolder.right = (ImageView) convertView.findViewById(R.id.right_gallery);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTextView.setText(mList.get(position).getmTitle());
		mHolder.mTextView2.setText((position + 1) + "/" + size);
		mHolder.left.setOnClickListener(new onImageClickListener(context, mHolder.left, position, size, this));
		mHolder.right.setOnClickListener(new onImageClickListener(context, mHolder.right, position, size, this));
		if (isPictureDel&&mList.size()>1) {
			mHolder.mDel.setVisibility(View.VISIBLE);
			mHolder.mDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
				}
			});
		}else{
			mHolder.mDel.setVisibility(View.GONE);
		}
		return convertView;
	}

	public void updateText(int position) {
		mHolder.mTextView2.setText(((position + 1) + "/" + mList.size()));
		mViewPager.setCurrentItem(position);
	}

	class ViewHolder {
		TextView mTextView;
		TextView mTextView2;
		TextView mDel;
		ImageView left;
		ImageView right;
	}
	
}
