package com.techfly.liutaitai.bizz.wenxin;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.alipay.IPayment;
import com.techfly.liutaitai.bizz.alipay.PayOrder;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.util.AppLog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeixinPayImpl implements IPayment{
    PayReq req;
    Map<String,String> resultunifiedorder;
    StringBuffer sb;
    IWXAPI msgApi;
    private Context mContext;
    private PayOrder mPayOrder;
    private ProgressDialog dialog;
    @Override
    public void onPay(Context context, PayOrder order, PayCallBack payCallBack) {
        mPayOrder  = order;
        mContext = context;
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(Keys.APP_ID);
        req = new PayReq();
        sb=new StringBuffer();
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
        
    }
    /**
    生成签名
    */

   private String genPackageSign(List<NameValuePair> params) {
       StringBuilder sb = new StringBuilder();
       
       for (int i = 0; i < params.size(); i++) {
           sb.append(params.get(i).getName());
           sb.append('=');
           sb.append(params.get(i).getValue());
           sb.append('&');
       }
       sb.append("key=");
       sb.append(Keys.API_KEY);
       

       String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
       Log.e("orion",packageSign);
       AppLog.Loge("Fly", "packageSign====="+packageSign);
       return packageSign;
   }
   private String genAppSign(List<NameValuePair> params) {
       StringBuilder sb = new StringBuilder();

       for (int i = 0; i < params.size(); i++) {
           sb.append(params.get(i).getName());
           sb.append('=');
           sb.append(params.get(i).getValue());
           sb.append('&');
       }
       sb.append("key=");
       sb.append(Keys.API_KEY);

       this.sb.append("sign str\n"+sb.toString()+"\n\n");
       String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
       Log.e("orion",appSign);
       AppLog.Loge("Fly", "genAppSign=====appSign ===="+appSign );
       return appSign;
   }
   private String toXml(List<NameValuePair> params) {
       StringBuilder sb = new StringBuilder();
       sb.append("<xml>");
       for (int i = 0; i < params.size(); i++) {
           sb.append("<"+params.get(i).getName()+">");

           sb.append(params.get(i).getValue());
           
           sb.append("</"+params.get(i).getName()+">");
       }
       sb.append("</xml>");

       Log.e("orion",sb.toString());
       AppLog.Loge("Fly", " toXml====="+sb.toString());
       return sb.toString();
   }

   private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

       @Override
       protected void onPreExecute() {
           dialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_tip), mContext.getString(R.string.getting_prepayid));
           if(mContext instanceof TakingOrderActivity){
               TakingOrderActivity ac = (TakingOrderActivity) mContext;
               ac.setDialog(dialog);
           }else if(mContext instanceof RechargeActivity){
        	   RechargeActivity activity = (RechargeActivity) mContext;
        	   activity.setDialog(dialog);
           }else{
               ServiceOrderActivity ac = ( ServiceOrderActivity ) mContext;
               ac.setDialog(dialog);
           }
       }

       @Override
       protected void onPostExecute(Map<String,String> result) {
           sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
           resultunifiedorder=result;
           AppLog.Loge("Fly", " prepay_id====="+result.get("prepay_id"));
           AppLog.Loge("Fly", "  resultunifiedorder====="+ resultunifiedorder);
           genPayReq();
           sendPayReq();
       }

       @Override
       protected void onCancelled() {
           super.onCancelled();
       }

       @Override
       protected Map<String,String>  doInBackground(Void... params) {

           String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
           String entity = genProductArgs();

           Log.e("orion",entity);

           byte[] buf = Util.httpPost(url, entity);

           String content = new String(buf);
           Log.e("orion", content);
           Map<String,String> xml=decodeXml(content);

           return xml;
       }
   }



   public Map<String,String> decodeXml(String content) {

       try {
           Map<String, String> xml = new HashMap<String, String>();
           XmlPullParser parser = Xml.newPullParser();
           parser.setInput(new StringReader(content));
           int event = parser.getEventType();
           while (event != XmlPullParser.END_DOCUMENT) {

               String nodeName=parser.getName();
               switch (event) {
                   case XmlPullParser.START_DOCUMENT:

                       break;
                   case XmlPullParser.START_TAG:

                       if("xml".equals(nodeName)==false){
                           //实例化student对象
                           xml.put(nodeName,parser.nextText());
                       }
                       break;
                   case XmlPullParser.END_TAG:
                       break;
               }
               event = parser.next();
           }

           return xml;
       } catch (Exception e) {
           Log.e("orion",e.toString());
       }
       return null;

   }


   private String genNonceStr() {
       Random random = new Random();
       return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
   }
   
   private long genTimeStamp() {
       return System.currentTimeMillis() / 1000;
   }
   


   private String genOutTradNo() {
       Random random = new Random();
       return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
   }
   

  //
   private String genProductArgs() {
       StringBuffer xml = new StringBuffer();

       try {
           String  nonceStr = genNonceStr();


           xml.append("</xml>");
           List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
           packageParams.add(new BasicNameValuePair("appid", Keys.APP_ID));
           packageParams.add(new BasicNameValuePair("body", mPayOrder.getmProductName()));
           packageParams.add(new BasicNameValuePair("mch_id", Keys.MCH_ID));
           packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
           packageParams.add(new BasicNameValuePair("notify_url", mPayOrder.getmNotifyUrl()));
           packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
           packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
           packageParams.add(new BasicNameValuePair("total_fee", (int)(Float.parseFloat(mPayOrder.getmProductPrice())*100)+""));
           packageParams.add(new BasicNameValuePair("trade_type", "APP"));


           String sign = genPackageSign(packageParams);
           packageParams.add(new BasicNameValuePair("sign", sign));


           String xmlstring =toXml(packageParams);
           return new String(xmlstring.toString().getBytes(), "ISO8859-1");//这句加上就可以了吧xml转码下

       } catch (Exception e) {
           Log.e("Fly", "genProductArgs fail, ex = " + e.getMessage());
           return null;
       }
       

   }
   private void genPayReq() {

       req.appId = Keys.APP_ID;
       req.partnerId = Keys.MCH_ID;
       req.prepayId = resultunifiedorder.get("prepay_id");
       req.packageValue = "Sign=WXPay";
       req.nonceStr = genNonceStr();
       req.timeStamp = String.valueOf(genTimeStamp());


       List<NameValuePair> signParams = new LinkedList<NameValuePair>();
       signParams.add(new BasicNameValuePair("appid", req.appId));
       signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
       signParams.add(new BasicNameValuePair("package", req.packageValue));
       signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
       signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
       signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
      
       req.sign = genAppSign(signParams);

       sb.append("sign\n"+req.sign+"\n\n");
       AppLog.Loge("Fly", " genPayReq====="+sb.toString());
       Log.e("orion", signParams.toString());

   }
   private void sendPayReq() {
       

       msgApi.registerApp(Keys.APP_ID);
       msgApi.sendReq(req);
//     if (dialog != null) {
//     dialog.dismiss();
// }
//       msgApi.handleIntent(new Intent(), new WXPayEntryActivity());
   }


}
