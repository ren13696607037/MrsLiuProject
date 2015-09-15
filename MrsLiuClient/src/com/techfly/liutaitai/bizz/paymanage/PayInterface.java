package com.techfly.liutaitai.bizz.paymanage;

import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

import android.content.Context;

public interface PayInterface {
   void  onPay(Context context,String orderId,String payMoney,String productName,final PayCallBack callback);
}
