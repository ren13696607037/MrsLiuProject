package com.techfly.liutaitai.model.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.Utility;

public class ServiceClick implements OnClickListener{
	private Service mService;
	private String mString;
	private Context mContext;

	public ServiceClick(Context context, String string, Service service){
		this.mContext = context;
		this.mString = string;
		this.mService = service;
	}
	
	@Override
	public void onClick(View v) {
		if(mContext.getString(R.string.order_service_btn).equals(mString)){
			ManagerListener.newManagerListener().notifyServiceDeleteListener(mService);
		}else if(mContext.getString(R.string.order_service_btn1).equals(mString)){
			ManagerListener.newManagerListener().notifyServicePayListener(mService);
		}else if(mContext.getString(R.string.order_service_btn2).equals(mString)){
			ManagerListener.newManagerListener().notifyServiceCancelListener(mService);
		}else if(mContext.getString(R.string.order_service_btn3).equals(mString)){
			Utility.call(mContext, Constant.KEFU_PHONE);
		}else if(mContext.getString(R.string.order_service_btn4).equals(mString)){
			ManagerListener.newManagerListener().notifyServiceAgainListener(mService);
		}else if(mContext.getString(R.string.order_service_btn5).equals(mString)){
			ManagerListener.newManagerListener().notifyServiceRateListener(mService);
		}
	}

}
