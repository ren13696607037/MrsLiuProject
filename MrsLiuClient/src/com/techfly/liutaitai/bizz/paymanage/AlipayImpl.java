package com.techfly.liutaitai.bizz.paymanage;

import android.content.Context;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.alipay.IPayment;
import com.techfly.liutaitai.bizz.alipay.Keys;
import com.techfly.liutaitai.bizz.alipay.PayOrder;
import com.techfly.liutaitai.bizz.alipay.PaymentImpl;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

public class AlipayImpl implements PayInterface{

    @Override
    public void onPay(final Context context, String orderId, String payMoney,
            String productName, final PayCallBack callback) {
        IPayment pay = new PaymentImpl();
        PayOrder order = new PayOrder();
        order.setmAccountId(Keys.DEFAULT_SELLER);
        order.setmMerchantId(Keys.DEFAULT_PARTNER);
        if(productName.equals("充值")){
            order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_RECHARGER_CALLBACK_URL);
        }else{
            order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_ORDER_CALLBACK_URL);
        }
        order.setmOrderNo(orderId);
        order.setmProductPrice(payMoney);
        order.setmProductName(productName);
        String username = SharePreferenceUtils.getInstance(context).getUser().getmNick();
        order.setmProductDesc(username+"购买"+productName);
        pay.onPay(context, order,new IPayment.PayCallBack() {
            @Override
            public void onFail(String message) {
                    SmartToast.showText(context, message);
            }
            
            @Override
            public void onError(String message) {
                    SmartToast.showText(context, message);
               
            }
            
            @Override
            public void onComplete() {
//                SmartToast.showText(context, R.string.alipay_success_promt);
                if(callback!=null){
                    callback.onPaySuccess();
                }
            }
            @Override
            public void onCancel() {
                    SmartToast.showText(context, R.string.alipay_cancel_promt);
            }
        });
        
        
    }

}
