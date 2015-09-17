package com.techfly.liutaitai.model.pcenter.adapter;

import android.R.integer;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.Utility;

public class OrderClick implements OnClickListener{
	private Context mContext;
	private TechOrder mOrder;
	private String mString;
	private int mIndex;
	public OrderClick(Context context,TechOrder order,String string,int index){
		this.mContext=context;
		this.mOrder=order;
		this.mString=string;
		this.mIndex = index;
	}

	@Override
	public void onClick(View v) {
		if(mContext.getString(R.string.tech_order_list_btn).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderCancelListener(mOrder,mIndex);
		}else if(mContext.getString(R.string.tech_order_list_btn3).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderLogiticsListener(mOrder,mIndex);
		}else if(mContext.getString(R.string.tech_order_list_btn2).equals(mString)){
			Utility.call(mContext, Constant.KEFU_PHONE);
//			ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
		}else if(mContext.getString(R.string.tech_order_list_btn4).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderRateListener(mOrder,mIndex);
		}else if(mContext.getString(R.string.tech_order_list_btn5).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderDeleteListener(mOrder,mIndex);
		}else if(mContext.getString(R.string.tech_order_list_btn1).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderTakeListener(mOrder,mIndex);
		}
	}

}
