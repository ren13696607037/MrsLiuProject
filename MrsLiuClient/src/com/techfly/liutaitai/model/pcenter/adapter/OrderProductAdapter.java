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
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.Utility;

public class OrderProductAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Product> mList;
	public OrderProductAdapter(Context context,ArrayList<Product> list){
		this.mContext=context;
		this.mList=list;
	}
	public void updateList(ArrayList<Product> list){
		this.mList=list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		return mList!=null?mList.get(arg0):new Product();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if(arg1==null){
			holder=new ViewHolder();
			arg1=LayoutInflater.from(mContext).inflate(R.layout.item_orderproduct, null);
			holder.mImageView=(ImageView) arg1.findViewById(R.id.oproduct_img);
			holder.mTvName=(TextView) arg1.findViewById(R.id.oproduct_name);
			holder.mTvPrice=(TextView) arg1.findViewById(R.id.oproduct_price);
			holder.mTvNum=(TextView) arg1.findViewById(R.id.oproduct_num);
			arg1.setTag(holder);
		}else{
			holder=(ViewHolder) arg1.getTag();
		}
		ImageLoader.getInstance().displayImage(mList.get(arg0).getmImg() ,holder.mImageView,ImageLoaderUtil.mHallIconLoaderOptions);
		Utility.setText(mContext, holder.mTvName, mList.get(arg0).getmName(), -1);
		Utility.setText(mContext, holder.mTvNum, mList.get(arg0).getmAmount()+"", R.string.collect_price);
		Utility.setText(mContext, holder.mTvPrice, mList.get(arg0).getmPrice()+"", R.string.collect_price);
		return arg1;
	}
	class ViewHolder{
		private ImageView mImageView;
		private TextView mTvName;
		private TextView mTvPrice;
		private TextView mTvNum;
	}

}
