package com.techfly.liutaitai.bizz.paymanage;

import android.content.Context;

import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

public interface PayInterface {
   void  onPay(Context context,String orderId,String payMoney,String productName,final PayCallBack callback);
}
