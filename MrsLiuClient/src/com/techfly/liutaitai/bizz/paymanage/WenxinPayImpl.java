package com.techfly.liutaitai.bizz.paymanage;

import android.content.Context;

import com.techfly.liutaitai.bizz.alipay.IPayment;
import com.techfly.liutaitai.bizz.alipay.Keys;
import com.techfly.liutaitai.bizz.alipay.PayOrder;
import com.techfly.liutaitai.bizz.wenxin.WeixinPayImpl;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

public class WenxinPayImpl implements PayInterface{

    @Override
    public void onPay(Context context, String orderId, String payMoney,
            String productName, PayCallBack callback) {
        IPayment pay = new WeixinPayImpl();
        PayOrder order = new PayOrder();
        order.setmAccountId(Keys.DEFAULT_SELLER);
        order.setmMerchantId(Keys.DEFAULT_PARTNER);
        order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_ORDER_CALLBACK_URL);
        order.setmOrderNo(orderId);
        order.setmProductPrice(payMoney);
        order.setmProductName(productName);
        String username = SharePreferenceUtils.getInstance(context).getUser().getmNick();
        order.setmProductDesc(username+"购买"+productName);
        pay.onPay(context, order,new IPayment.PayCallBack() {
            @Override
            public void onFail(String message) {
                
            }
            
            @Override
            public void onError(String message) {
                 
               
            }
            
            @Override
            public void onComplete() {
                
            }
            @Override
            public void onCancel() {
             
            }
        });
        
    }

}
