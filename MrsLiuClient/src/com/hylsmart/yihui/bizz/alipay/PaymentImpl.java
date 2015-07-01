package com.hylsmart.yihui.bizz.alipay;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.util.AppLog;

public class PaymentImpl implements IPayment {
    private static final int RQF_PAY = 1;
    private Context mContext;
    private static final int RQF_LOGIN = 2;
    private PayCallBack mCallBack;
    @Override
  public void onPay(final Context context, PayOrder order, PayCallBack callBack) {
        try {
      mCallBack = callBack;
      mContext = context;
      String info = getNewOrderInfo(order);
      String sign = Rsa.sign(info, Keys.PRIVATE);
      sign = URLEncoder.encode(sign);
      info += "&sign=\"" + sign + "\"&" + getSignType();
      Log.i("ExternalPartner", "start pay");
      // start the pay.
      AppLog.Logd("Fly", "info = " + info);

      final String orderInfo = info;
      new Thread() {
          public void run() {
              AliPay alipay = new AliPay((Activity) context, mHandler);
              //设置为沙箱模式，不设置默认为线上环境
//              alipay.setSandBox(true);
              String result = alipay.pay(orderInfo);
              AppLog.Logd("Fly", "result = " + result);
              Message msg = new Message();
              msg.what = RQF_PAY;
              msg.obj = result;
              mHandler.sendMessage(msg);
          }
      }.start();

  } catch (Exception ex) {
      ex.printStackTrace();
      Toast.makeText(context, "失败",
              Toast.LENGTH_SHORT).show();
  }
    }    
    
  private String getNewOrderInfo(PayOrder order) {
    StringBuilder sb = new StringBuilder();
    sb.append("partner=\"");
    sb.append(Keys.DEFAULT_PARTNER);
    sb.append("\"&out_trade_no=\"");
    sb.append(order.getmOrderNo());
    sb.append("\"&subject=\"");
    sb.append(order.getmProductName());
    sb.append("\"&body=\"");
    sb.append(order.getmProductDesc());
    sb.append("\"&total_fee=\"");
    sb.append(""+order.getmProductPrice());
    sb.append("\"&notify_url=\"");

    // 网址需要做URL编码
    sb.append(URLEncoder.encode(order.getmNotifyUrl()));
    sb.append("\"&service=\"mobile.securitypay.pay");
    sb.append("\"&_input_charset=\"UTF-8");
    sb.append("\"&return_url=\"");
    sb.append(URLEncoder.encode("http://m.alipay.com"));
    sb.append("\"&payment_type=\"1");
    sb.append("\"&seller_id=\"");
    sb.append(Keys.DEFAULT_SELLER);

    // 如果show_url值为空，可不传
    // sb.append("\"&show_url=\"");
    sb.append("\"&it_b_pay=\"1m");
    sb.append("\"");

    return new String(sb);
    }

private String getSignType() {
  return "sign_type=\"RSA\"";
}
  private  Handler mHandler = new Handler() {
      public void handleMessage(android.os.Message msg) {
          Result result = new Result((String) msg.obj);

          switch (msg.what) {
          case RQF_PAY:
          case RQF_LOGIN: {
              result.parseResult();
              if(null!=mCallBack){
                  if(result.payResultCode==Result.ResultCode.PAY_SUCCESS){
                          mCallBack.onComplete();
                  }else if(result.payResultCode ==Result.ResultCode.PAY_CANCEL){
                         mCallBack.onCancel();
                  }else if(result.payResultCode == Result.ResultCode.PAY_SYSTEM_EXCEPTION){
                        mCallBack.onFail(mContext.getString(R.string.alipay_fail_promt));
                  }else{
                        mCallBack.onError(mContext.getString(R.string.alipay_fail_promt));
                  }
              }
              
          }
              break;
          default:
              break;
          }
      }
      };  
    
    
}
