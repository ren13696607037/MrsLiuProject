package com.techfly.liutaitai.model.order.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.util.ImageLoaderUtil;

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
		
		holder.mName.setText(mList.get(position).getmServiceName());
		holder.mPrice.setText("￥"+mList.get(position).getmServicePrice());
		holder.mTime.setText(mList.get(position).getmServiceTime());
		setType(mList.get(position).getmServiceType(), holder.mType, mList.get(position).getmServicePerson(), holder.mTech);
		setState(mList.get(position).getmServiceStatus(), holder.mState, holder.mButton, holder.mButton2);
		ImageLoader.getInstance().displayImage(mList.get(position).getmServiceIcon(), holder.mImageView , ImageLoaderUtil.mOrderServiceIconLoaderOptions);
		holder.mButton.setOnClickListener(new ServiceClick(mContext, holder.mButton.getText().toString(), mList.get(position)));
//		holder.mButton2.setOnClickListener(new ServiceClick(mContext, holder.mButton2.getText().toString(), mList.get(position)));
		holder.mButton2.setOnClickListener(new ServiceClick(mContext, "去评价", mList.get(position)));
		return convertView;
	}
	private void setType(String type, TextView textView, String string, TextView textView2){
		if("0".equals(type)){
			textView.setText(R.string.apply_text1);
			textView.setBackgroundResource(R.drawable.order_service_manicure_bg);
			textView2.setText(mContext.getString(R.string.order_service_text1, string));
		}else if("1".equals(type)){
			textView.setText(R.string.apply_text3);
			textView.setBackgroundResource(R.drawable.order_service_eyelash_bg);
			textView2.setText(mContext.getString(R.string.order_service_text3, string));
		}
	}
	private void setState(String state, TextView textView, Button button, Button button2){
		button.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		if("0".equals(state)){
			textView.setText(R.string.order_service_state);
			button.setText(R.string.order_service_btn1);
			button2.setText(R.string.order_service_btn);
		}else if("1".equals(state)){
			textView.setText(R.string.order_service_state1);
			button.setText(R.string.order_service_btn2);
			button2.setVisibility(View.INVISIBLE);
		}else if("2".equals(state)){
			textView.setText(R.string.order_service_state2);
			button.setText(R.string.order_service_btn3);
			button2.setVisibility(View.INVISIBLE);
		}else if("3".equals(state) || "4".equals(state)){
			textView.setText(R.string.order_service_state3);
			button.setText(R.string.order_service_btn3);
			button2.setVisibility(View.INVISIBLE);
		}else if("5".equals(state)){
			textView.setText(R.string.order_service_state4);
			button.setText(R.string.order_service_btn5);
			button2.setText(R.string.order_service_btn4);
		}else if("6".equals(state)){
			textView.setText(R.string.order_service_state5);
			button.setText(R.string.order_service_btn);
			button2.setText(R.string.order_service_btn4);
		}else if("-1".equals(state)){
			textView.setText(R.string.order_service_state6);
			button.setText(R.string.order_service_btn);
			button2.setText(R.string.order_service_btn4);
		}
	}
	
	class ViewHolder{
		private TextView mType;
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
