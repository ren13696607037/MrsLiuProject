package com.techfly.liutaitai.model.order.adapter;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.Utility;

public class ServiceClick implements OnClickListener {
	private Service mService;
	private String mString;
	private Context mContext;
	private Dialog mDialog;
	private int mType;

	public ServiceClick(Context context, String string, Service service,
			int type) {
		this.mContext = context;
		this.mString = string;
		this.mService = service;
		this.mType = type;
	}

	@Override
	public void onClick(View v) {
		if (mContext.getString(R.string.order_service_btn).equals(mString)) {
			mDialog = new Dialog(mContext, R.style.MyDialog);
			mDialog = AlertDialogUtils.displayMyAlertChoice(mContext,
					R.string.dialog_title, R.string.dialog_message,
					R.string.confirm, new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDialog.dismiss();
							if (mType == 0) {
								ManagerListener.newManagerListener()
										.notifyServiceDeleteListener(mService);
							} else {
								ManagerListener.newManagerListener()
										.notifyServiceDetailDeleteListener(
												mService);
							}

						}
					}, R.string.dialog_cancel, null);
			mDialog.show();
		} else if (mContext.getString(R.string.order_service_btn1).equals(
				mString)) {
			if (mType == 0) {
				ManagerListener.newManagerListener().notifyServicePayListener(
						mService);
			} else {
				ManagerListener.newManagerListener()
						.notifyServiceDetailPayListener(mService);
			}

		} else if (mContext.getString(R.string.order_service_btn2).equals(
				mString)) {
			mDialog = new Dialog(mContext, R.style.MyDialog);
			mDialog = AlertDialogUtils.displayMyAlertChoice(mContext,
					R.string.dialog_title1, R.string.dialog_message2,
					R.string.confirm, new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDialog.dismiss();
							if (mType == 0) {
								ManagerListener.newManagerListener()
										.notifyServiceCancelListener(mService);
							} else {
								ManagerListener.newManagerListener()
										.notifyServiceDetailCancelListener(
												mService);
							}

						}
					}, R.string.dialog_cancel, null);
			mDialog.show();
		} else if (mContext.getString(R.string.order_service_btn3).equals(
				mString)) {
			Utility.call(mContext, Constant.KEFU_PHONE);
		} else if (mContext.getString(R.string.order_service_btn4).equals(
				mString)) {
			if (mType == 0) {
				ManagerListener.newManagerListener()
						.notifyServiceAgainListener(mService);
			} else {
				ManagerListener.newManagerListener()
						.notifyServiceDetailAgainListener(mService);
			}

		} else if (mContext.getString(R.string.order_service_btn5).equals(
				mString)) {
			if (mType == 0) {
				ManagerListener.newManagerListener().notifyServiceRateListener(
						mService);
			} else {
				ManagerListener.newManagerListener()
						.notifyServiceDetailRateListener(mService);
			}

		}
	}

}
