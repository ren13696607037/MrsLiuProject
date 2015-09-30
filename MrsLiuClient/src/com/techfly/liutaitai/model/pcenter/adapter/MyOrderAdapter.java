package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.techfly.liutaitai.model.home.activities.HomeActivity;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.view.StartTimeText;

public class MyOrderAdapter extends BaseAdapter {
	private Context mContext;
	private ViewHolder mHolder;
	private ArrayList<TechOrder> mList;
	private long mTime;
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
		if(convertView==null){
			mHolder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_myorder, null);
			mHolder.mTvNum=(TextView) convertView.findViewById(R.id.iorder_address);
			mHolder.mImageView=(ImageView) convertView.findViewById(R.id.iorder_img);
			mHolder.mTvPrice=(TextView) convertView.findViewById(R.id.iorder_price);
			mHolder.mTvTitle=(TextView) convertView.findViewById(R.id.iorder_name);
			mHolder.mTvState=(TextView) convertView.findViewById(R.id.iorder_state);
			mHolder.mButton=(Button) convertView.findViewById(R.id.iorder_btn);
			mHolder.mButton2=(Button) convertView.findViewById(R.id.iorder_btn2);
			mHolder.mTvNo = (TextView) convertView.findViewById(R.id.iorder_num);
//			holder.mView=convertView.findViewById(R.id.iorder_view);
			mHolder.mTvTime=(TextView) convertView.findViewById(R.id.iorder_time);
			mHolder.mStartTime = (StartTimeText) convertView.findViewById(R.id.iorder_time_start);
//			holder.mLayout=(RelativeLayout) convertView.findViewById(R.id.iorder_product);
			convertView.setTag(mHolder);
		}else{
			mHolder=(ViewHolder) convertView.getTag();
		}
		mHolder.mTvNo.setText(mContext.getString(R.string.service_detail_text,
				mList.get(position).getmServiceNum()));
		mHolder.mTvNum.setText(mContext.getString(R.string.tech_order_list_address,mList.get(position).getmCustomerAddress()));
		ImageLoader.getInstance().displayImage(mList.get(position).getmServiceIcon(), mHolder.mImageView,ImageLoaderUtil.mHallIconLoaderOptions);
		mHolder.mTvPrice.setText(mContext.getString(R.string.price,mList.get(position).getmServicePrice()));
		mHolder.mTvTitle.setText(mList.get(position).getmServiceName());
		if(mList.get(position).getmStartTime() != null){
			if(System.currentTimeMillis() > Utility.Date2Millis(mList.get(position).getmCustomerTime())){
				mTime = System.currentTimeMillis() - Utility.Date2Millis(mList.get(position).getmStartTime());
			}
		}
		setState(mList.get(position), mHolder.mTvState, mHolder.mButton, mHolder.mButton2,mHolder.mStartTime);
		mHolder.mButton.setOnClickListener(new OrderClick(mContext, mList.get(position), mHolder.mButton.getText().toString(),0));
		mHolder.mButton2.setOnClickListener(new OrderClick(mContext, mList.get(position), mHolder.mButton2.getText().toString(),0));
//		if(position!=mList.size()-1){
//			holder.mView.setVisibility(View.VISIBLE);
//		}
		mHolder.mTvTime.setText(mContext.getString(R.string.tech_order_list_time,mList.get(position).getmCustomerTime()));
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
		private StartTimeText mStartTime;
		private TextView mTvNo;
	}
	private void setState(TechOrder order,TextView textView,Button button,Button button2,StartTimeText time){
		time.setVisibility(View.GONE);
		int state=Integer.valueOf(order.getmServiceStatus());
		button.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		if(state==1){
			textView.setText(R.string.tech_order_list_state);
			button2.setText(R.string.tech_order_list_btn);
			button.setText(R.string.tech_order_list_btn1);
		}else if(state==2){
			textView.setText(R.string.tech_order_list_state1);
			button.setText(R.string.tech_order_list_btn3);
			button2.setText(R.string.tech_order_list_btn2);
		}else if(state==3){
			textView.setText(R.string.tech_order_list_state2);
			button2.setVisibility(View.GONE);
			button.setText(R.string.tech_order_list_btn4);
			time.setVisibility(View.VISIBLE);
			time.toStart(mTime);
		}else if(state==4 || state == 6){
			textView.setText(R.string.tech_order_list_state3);
			button.setText(R.string.tech_order_list_btn5);
			button2.setVisibility(View.GONE);
		}else if(state == 5){
			textView.setText(R.string.order_service_state4);
			button.setVisibility(View.GONE);
			button2.setVisibility(View.GONE);
		}else if(state == 0){
			textView.setText(R.string.order_service_state);
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == -1){
			textView.setText("订单已取消");
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == -2){
			textView.setText(R.string.order_service_state8);
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == 7){
			textView.setText(R.string.order_service_state7);
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == 9){
			textView.setText("用户已删除");
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else{
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}
		
	}
	public void toFinish(){
		if(mHolder != null && mHolder.mStartTime != null){
			mHolder.mStartTime.toFinishHandler();
		}
	}

}
