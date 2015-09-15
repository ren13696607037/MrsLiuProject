package com.techfly.liutaitai.model.pcenter.adapter;

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
	public OrderClick(Context context,TechOrder order,String string){
		this.mContext=context;
		this.mOrder=order;
		this.mString=string;
	}

	@Override
	public void onClick(View v) {
		if(mContext.getString(R.string.tech_order_list_btn).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderCancelListener(mOrder);
		}else if(mContext.getString(R.string.tech_order_list_btn3).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderLogiticsListener(mOrder);
		}else if(mContext.getString(R.string.tech_order_list_btn2).equals(mString)){
			Utility.call(mContext, Constant.KEFU_PHONE);
//			ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
		}else if(mContext.getString(R.string.tech_order_list_btn4).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderRateListener(mOrder);
		}else if(mContext.getString(R.string.tech_order_list_btn5).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderDeleteListener(mOrder);
		}else if(mContext.getString(R.string.tech_order_list_btn1).equals(mString)){
			ManagerListener.newManagerListener().notifyOrderTakeListener(mOrder);
		}
	}

}
