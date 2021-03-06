package com.techfly.liutaitai.model.pcenter.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.Utility;

public class OrderClick implements OnClickListener {
	private Context mContext;
	private TechOrder mOrder;
	private String mString;
	private int mIndex;
	private Dialog mDialog;

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
//			if(System.currentTimeMillis() - Utility.Date2Millis(mOrder.getmStartTime()) > (Integer.valueOf(mOrder.getmMinutes())*60*1000)){
				if (mIndex == 0) {
					ManagerListener.newManagerListener().notifyOrderRateListener(
							mOrder);
				} else if (mIndex == 1) {
					ManagerListener.newManagerListener()
							.notifyDetailOrderRateListener(mOrder);
				}
//			}else{
//				Toast.makeText(mContext, "尚在服务时间~~！", Toast.LENGTH_LONG).show();
//			}
		} else if (mContext.getString(R.string.tech_order_list_btn5).equals(
				mString)) {
			mDialog = new Dialog(mContext, R.style.MyDialog);
        	mDialog = AlertDialogUtils.displayMyAlertChoice(mContext, R.string.dialog_title, R.string.dialog_message1, R.string.confirm, new View.OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    mDialog.dismiss();
                    if (mIndex == 0) {
        				ManagerListener.newManagerListener().notifyOrderDeleteListener(
        						mOrder);
        			} else if (mIndex == 1) {
        				ManagerListener.newManagerListener()
        						.notifyDetailOrderDeleteListener(mOrder);
        			}
                }
            }, R.string.dialog_cancel, null);
        	mDialog.show();
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
