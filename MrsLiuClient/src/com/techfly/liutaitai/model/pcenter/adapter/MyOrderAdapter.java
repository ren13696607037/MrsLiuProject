package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.util.ImageLoaderUtil;

public class MyOrderAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<TechOrder> mList;
	public MyOrderAdapter(Context context,ArrayList<TechOrder> list){
		this.mContext=context;
		this.mList=list;
	}
	public void updateList(ArrayList<TechOrder> list){
		this.mList=list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return mList!=null?mList.get(position):new TechOrder();
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_myorder, null);
			holder.mTvNum=(TextView) convertView.findViewById(R.id.iorder_address);
			holder.mImageView=(ImageView) convertView.findViewById(R.id.iorder_img);
			holder.mTvPrice=(TextView) convertView.findViewById(R.id.iorder_price);
			holder.mTvTitle=(TextView) convertView.findViewById(R.id.iorder_name);
			holder.mTvState=(TextView) convertView.findViewById(R.id.iorder_state);
			holder.mButton=(Button) convertView.findViewById(R.id.iorder_btn);
			holder.mButton2=(Button) convertView.findViewById(R.id.iorder_btn2);
//			holder.mView=convertView.findViewById(R.id.iorder_view);
			holder.mTvTime=(TextView) convertView.findViewById(R.id.iorder_time);
//			holder.mLayout=(RelativeLayout) convertView.findViewById(R.id.iorder_product);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mTvNum.setText(mContext.getString(R.string.tech_order_list_address,mList.get(position).getmCustomerAddress()));
		ImageLoader.getInstance().displayImage(mList.get(position).getmServiceIcon(), holder.mImageView,ImageLoaderUtil.mHallIconLoaderOptions);
		holder.mTvPrice.setText(mContext.getString(R.string.price,mList.get(position).getmServicePrice()));
		holder.mTvTitle.setText(mList.get(position).getmServiceName());
		setState(mList.get(position), holder.mTvState, holder.mButton, holder.mButton2);
		holder.mButton.setOnClickListener(new OrderClick(mContext, mList.get(position), holder.mButton.getText().toString()));
		holder.mButton2.setOnClickListener(new OrderClick(mContext, mList.get(position), holder.mButton2.getText().toString()));
//		if(position!=mList.size()-1){
//			holder.mView.setVisibility(View.VISIBLE);
//		}
		holder.mTvTime.setText(mContext.getString(R.string.tech_order_list_time,mList.get(position).getmCustomerTime()));
//		holder.mLayout.setOnClickListener(new OrderClick(mContext, order, string));
		return convertView;
	}
	class ViewHolder{
		private TextView mTvNum;
		private ImageView mImageView;
		private TextView mTvTitle;
		private TextView mTvPrice;
		private TextView mTvState;
		private Button mButton;
		private Button mButton2;
		private View mView;
		private RelativeLayout mLayout;
		private TextView mTvTime;
	}
	private void setState(TechOrder order,TextView textView,Button button,Button button2){
		int state=Integer.valueOf(order.getmServiceStatus());
		button.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		if(state==1){
			textView.setText(R.string.tech_order_list_state);
			button2.setVisibility(View.GONE);
			button.setText(R.string.button_pay);
		}else if(state==2){
			textView.setText(R.string.tech_order_list_state1);
			button.setVisibility(View.GONE);
			button2.setVisibility(View.GONE);
		}else if(state==3){
			textView.setText(R.string.tech_order_list_state2);
			button2.setVisibility(View.GONE);
			button.setText(R.string.button_deliveryed);
		}else if(state==4){
			textView.setText(R.string.tech_order_list_state3);
			button.setText(R.string.button_rate);
			button2.setText(R.string.order_delete);
		}else if(state == 5 || state == 6){
			textView.setText(R.string.tech_order_list_state4);
			button.setText(R.string.button_rate);
			button2.setText(R.string.order_delete);
		}/*else if(){
			
		}*/
	}

}
