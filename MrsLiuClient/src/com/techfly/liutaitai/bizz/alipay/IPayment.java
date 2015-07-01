package com.techfly.liutaitai.bizz.alipay;

import android.content.Context;


public interface IPayment {
    interface PayCallBack{
        void onCancel();
        void onComplete();
        void onFail(String message);
        void onError(String message);
        
    }
   void onPay(Context context ,PayOrder order ,PayCallBack payCallBack);
   
}
