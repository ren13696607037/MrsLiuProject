package com.hylsmart.yihui.model.pcenter.adapter;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.pcenter.bean.MyOrder;
import com.hylsmart.yihui.util.ManagerListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class OrderClick implements OnClickListener{
	private Context mContext;
	private MyOrder mOrder;
	private String mString;
	public OrderClick(Context context,MyOrder order,String string){
		this.mContext=context;
		this.mOrder=order;
		this.mString=string;
	}

	@Override
	public void onClick(View v) {
		if(mContext.getString(R.string.button_cancel).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderCancelListener(mOrder);
		}else if(mContext.getString(R.string.button_deliveryed).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderLogiticsListener(mOrder);
		}else if(mContext.getString(R.string.button_pay).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
		}else if(mContext.getString(R.string.button_rate).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderRateListener(mOrder);
		}else if(mContext.getString(R.string.order_delete).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderDeleteListener(mOrder);
		}
	}

}
