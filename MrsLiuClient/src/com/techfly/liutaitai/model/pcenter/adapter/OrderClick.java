package com.techfly.liutaitai.model.pcenter.adapter;

import android.R.integer;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.Utility;

public class OrderClick implements OnClickListener {
	private Context mContext;
	private TechOrder mOrder;
	private String mString;
	private int mIndex;

	public OrderClick(Context context, TechOrder order, String string, int index) {
		this.mContext = context;
		this.mOrder = order;
		this.mString = string;
		this.mIndex = index;
	}

	@Override
	public void onClick(View v) {
		if (mContext.getString(R.string.tech_order_list_btn).equals(mString)) {
			if (mIndex == 0) {
				ManagerListener.newManagerListener().notifyOrderCancelListener(
						mOrder);
			} else if (mIndex == 1) {
				ManagerListener.newManagerListener()
						.notifyDetailOrderCancelListener(mOrder);
			}

		} else if (mContext.getString(R.string.tech_order_list_btn3).equals(
				mString)) {
			if(System.currentTimeMillis() >= Utility.Date2Millis(mOrder.getmCustomerTime())){
				if (mIndex == 0) {
					ManagerListener.newManagerListener()
							.notifyOrderLogiticsListener(mOrder);
				} else if (mIndex == 1) {
					ManagerListener.newManagerListener()
							.notifyDetailOrderLogiticsListener(mOrder);
				}
			}else{
				Toast.makeText(mContext, "未到开始时间，无法开始！", Toast.LENGTH_LONG).show();
			}
		} else if (mContext.getString(R.string.tech_order_list_btn2).equals(
				mString)) {
			Utility.call(mContext, Constant.KEFU_PHONE);
			// ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
		} else if (mContext.getString(R.string.tech_order_list_btn4).equals(
				mString)) {
			if(System.currentTimeMillis() - Utility.Date2Millis(mOrder.getmStartTime()) > (Integer.valueOf(mOrder.getmMinutes())*60*1000)){
				if (mIndex == 0) {
					ManagerListener.newManagerListener().notifyOrderRateListener(
							mOrder);
				} else if (mIndex == 1) {
					ManagerListener.newManagerListener()
							.notifyDetailOrderRateListener(mOrder);
				}
			}else{
				Toast.makeText(mContext, "尚在服务时间~~！", Toast.LENGTH_LONG).show();
			}
		} else if (mContext.getString(R.string.tech_order_list_btn5).equals(
				mString)) {
			if (mIndex == 0) {
				ManagerListener.newManagerListener().notifyOrderDeleteListener(
						mOrder);
			} else if (mIndex == 1) {
				ManagerListener.newManagerListener()
						.notifyDetailOrderDeleteListener(mOrder);
			}

		} else if (mContext.getString(R.string.tech_order_list_btn1).equals(
				mString)) {
			if (mIndex == 0) {
				ManagerListener.newManagerListener().notifyOrderTakeListener(
						mOrder);
			} else if (mIndex == 1) {
				ManagerListener.newManagerListener()
						.notifyDetailOrderTakeListener(mOrder);
			}

		}
	}

}
