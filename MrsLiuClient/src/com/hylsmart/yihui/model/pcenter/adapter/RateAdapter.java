package com.hylsmart.yihui.model.pcenter.adapter;

import java.util.ArrayList;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.model.pcenter.bean.Rate;
import com.hylsmart.yihui.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RateAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Rate> mList;
	public RateAdapter(Context context,ArrayList<Rate> list){
		this.mContext=context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null?mList.get(position):new Product();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_rate, null);
			holder.mEditText=(EditText) convertView.findViewById(R.id.irate_content);
			holder.mImageView=(ImageView) convertView.findViewById(R.id.irate_img);
			holder.mTvDiscontent=(TextView) convertView.findViewById(R.id.irate_discontent);
			holder.mTvGreat=(TextView) convertView.findViewById(R.id.irate_great);
			holder.mTvJust=(TextView) convertView.findViewById(R.id.irate_just);
			holder.mTvPleased=(TextView) convertView.findViewById(R.id.irate_pleased);
			holder.mTvTitle=(TextView) convertView.findViewById(R.id.irate_title);
			holder.mTvUnsatisfy=(TextView) convertView.findViewById(R.id.irate_unsatisfy);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mTvTitle.setText(mList.get(position).getmProductName());
		ImageLoader.getInstance().displayImage(mList.get(position).getmProductImage(), holder.mImageView, ImageLoaderUtil.mHallIconLoaderOptions);
		holder.mTvDiscontent.setOnClickListener(new MyClick(holder, position));
		holder.mTvGreat.setOnClickListener(new MyClick(holder, position));
		holder.mTvJust.setOnClickListener(new MyClick(holder, position));
		holder.mTvPleased.setOnClickListener(new MyClick(holder, position));
		holder.mTvUnsatisfy.setOnClickListener(new MyClick(holder, position));
		mList.get(position).setmScore("100");
		
		return convertView;
	}
	
	class ViewHolder{
		private ImageView mImageView;
		private TextView mTvTitle;
		private TextView mTvGreat;//非常满意
		private TextView mTvPleased;//满意
		private TextView mTvJust;//一般
		private TextView mTvDiscontent;//不满意
		private TextView mTvUnsatisfy;//非常不满意
		private EditText mEditText;
	}
	private void setTextColor(TextView textView,boolean b){
		if(b){
			textView.setTextColor(mContext.getResources().getColor(R.color.color_blue));
		}else{
			textView.setTextColor(mContext.getResources().getColor(R.color.TextColorBLACK_NORMAL));
		}
	}
	private class MyClick implements OnClickListener{
		private ViewHolder mHolder;
		private int mPosition;
		public MyClick(ViewHolder holder,int position){
			this.mHolder=holder;
			this.mPosition=position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.irate_great:
				mList.get(mPosition).setmScore("100");
				setTextColor(mHolder.mTvDiscontent, false);
				setTextColor(mHolder.mTvUnsatisfy, false);
				setTextColor(mHolder.mTvPleased, false);
				setTextColor(mHolder.mTvJust, false);
				setTextColor(mHolder.mTvGreat, true);
				break;
			case R.id.irate_discontent:
				mList.get(mPosition).setmScore("40");
				setTextColor(mHolder.mTvDiscontent, true);
				setTextColor(mHolder.mTvUnsatisfy, false);
				setTextColor(mHolder.mTvPleased, false);
				setTextColor(mHolder.mTvJust, false);
				setTextColor(mHolder.mTvGreat, false);
				break;
			case R.id.irate_just:
				mList.get(mPosition).setmScore("60");
				setTextColor(mHolder.mTvDiscontent, false);
				setTextColor(mHolder.mTvUnsatisfy, false);
				setTextColor(mHolder.mTvPleased, false);
				setTextColor(mHolder.mTvJust, true);
				setTextColor(mHolder.mTvGreat, false);
				break;
			case R.id.irate_pleased:
				mList.get(mPosition).setmScore("80");
				setTextColor(mHolder.mTvDiscontent, false);
				setTextColor(mHolder.mTvUnsatisfy, false);
				setTextColor(mHolder.mTvPleased, true);
				setTextColor(mHolder.mTvJust, false);
				setTextColor(mHolder.mTvGreat, false);
				break;
			case R.id.irate_unsatisfy:
				mList.get(mPosition).setmScore("20");
				setTextColor(mHolder.mTvDiscontent, false);
				setTextColor(mHolder.mTvUnsatisfy, true);
				setTextColor(mHolder.mTvPleased, false);
				setTextColor(mHolder.mTvJust, false);
				setTextColor(mHolder.mTvGreat, false);
				break;

			default:
				break;
			}
			
		}
		
	}
}
