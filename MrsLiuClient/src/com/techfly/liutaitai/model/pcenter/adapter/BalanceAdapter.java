package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.model.pcenter.fragment.MyBalanceFragment;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.IntentBundleKey;

public class BalanceAdapter extends BaseAdapter {
	private Context mContext;
	private MyBalanceFragment mFragment;
	private ArrayList<Balance> mList;
	public BalanceAdapter(MyBalanceFragment context, ArrayList<Balance> list){
		this.mFragment = context;
		this.mList = list;
	}
	public void updateList(ArrayList<Balance> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mList != null ? mList.get(arg0) : new Balance();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.item_balance, null);
			holder.mPrice = (TextView) arg1.findViewById(R.id.ibalance_name);
			holder.mSend = (TextView) arg1.findViewById(R.id.ibalance_send);
			holder.mTime = (TextView) arg1.findViewById(R.id.ibalance_time);
			holder.mButton = (Button) arg1.findViewById(R.id.ibalance_btn);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		holder.mPrice.setText(mList.get(arg0).getmPrice());
		holder.mSend.setText(mList.get(arg0).getmSend());
		holder.mTime.setText(mList.get(arg0).getmTime());
		holder.mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mFragment.getActivity(), RechargeActivity.class);
				intent.putExtra(IntentBundleKey.BALANCE_PRICE, mList.get(arg0));
				mFragment.startActivity(intent);
			}
		});
		return arg1;
	}
	
	class ViewHolder{
		private TextView mPrice;
		private TextView mSend;
		private TextView mTime;
		private Button mButton;
	}
}
