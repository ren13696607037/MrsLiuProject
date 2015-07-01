package com.hylsmart.yihui.util.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.alipay.IPayment;
import com.hylsmart.yihui.bizz.alipay.Keys;
import com.hylsmart.yihui.bizz.alipay.PayOrder;
import com.hylsmart.yihui.bizz.alipay.PaymentImpl;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.shopcar.OnShopCarLisManager;
import com.hylsmart.yihui.bizz.shopcar.ShopCar;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.net.ResultCode;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.RequestParamConfig;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.SmartToast;
import com.hylsmart.yihui.util.UIHelper;

public  abstract class CreateOrderPayCommonFragment extends CommonFragment {

    protected ProgressDialog mProgressDialog;// 进度条
    private String mOrderId;
    private String mPayMoney;
    private String mProductName;
    private int mPayType;
    private int mProductType;
    protected boolean mIsFromShopCar;
   
    public interface PayCallBack{
        void onPaySuccess();
    }
    protected void   showDialog(){
            mProgressDialog =ProgressDialog.show(getActivity(), 
                    getString(R.string.order_commit_title), getString(R.string.order_commit_message), false, false);
          
     }
    protected void   showDeleteDialog(){
        mProgressDialog =ProgressDialog.show(getActivity(), 
                getString(R.string.order_delete_title), getString(R.string.order_delete_message), false, false);
      
 }
    
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mIsFromShopCar = activity.getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
    }

    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }

    public abstract String onEncapleOrderInfo();
    
    public abstract void onOrderCreateSuccess(String orderId,String money,String proName);
    
    protected void onCommitOrder(int productType,int payType,String price,String productName) {
           AppLog.Logd("Fly", "Taking Order request data");
           mPayType = payType;
           mPayMoney = price;
           mProductName = productName;
           mProductType = productType;
           if(TextUtils.isEmpty(onEncapleOrderInfo())){
               showSmartToast("请开发者组装订单信息 在 onEncapleOrderInfo 方法中", Toast.LENGTH_LONG);
               return;
           }
           if(mPayType !=Constant.PAY_TYPE_CREATE){
               onPay();
               return;
           }
           showDialog();
           RequestParam param = new RequestParam();
           param.setPostRequestMethod();
           HttpURL url = new HttpURL();
           if(productType==Constant.PRODUCT_TYPE_VIRTUAL){
               url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.ORDER_COMMIT_REQUEST_URL);
           }else{
               url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.ORDER_COMMIT_REQUEST_URL);
           }
         
           url.setmGetParamPrefix(RequestParamConfig.GOODS);
           url.setmGetParamValues(onEncapleOrderInfo());
           param.setmHttpURL(url);
           param.setmParserClassName(CommonParser.class.getName());
           RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
        
    }
    
    
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd("Fly", "object===="+object.toString());
                if(getActivity()==null || isDetached()){
                    return;
                }
                if(null!=mProgressDialog){
                    mProgressDialog.dismiss();
                }
                if(object instanceof ResultInfo){
                 ResultInfo info =  (ResultInfo) object;
                    if(info.getmCode()==ResultCode.CODE_0){
                         mOrderId = info.getmData();
                         if(mIsFromShopCar){
                             ShopCar.getShopCar().getShopproductList().removeAll(ShopCar.getShopCar().getCheckShopproductList());
                             OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null);
                         }
                        onOrderCreateSuccess(mOrderId,mPayMoney,mProductName);
                     }else{
                         showSmartToast(info.getmMessage()+"", Toast.LENGTH_LONG);
                     }
               }
                }
        };
    }
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(getActivity()==null || isDetached()){
                    return;
                }
                if(null!=mProgressDialog){
                    mProgressDialog.dismiss();
                }
            
            }
        };
    }
    private void onPay() {
         IPayment pay = new PaymentImpl();
         PayOrder order = new PayOrder();
         order.setmAccountId(Keys.DEFAULT_SELLER);
         order.setmMerchantId(Keys.DEFAULT_PARTNER);
         if(mPayType ==Constant.PAY_TYPE_CREATE){
               order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_CALLBACK_URL);
         }else{
               order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_ORDER_CALLBACK_URL);
         }
         order.setmOrderNo(mOrderId);
         order.setmProductPrice(mPayMoney);
         order.setmProductName(mProductName);
         String username = SharePreferenceUtils.getInstance(getActivity()).getUser().getmNick();
         order.setmProductDesc(username+"购买"+mProductName);
         pay.onPay(getActivity(), order,new IPayment.PayCallBack() {
             @Override
             public void onFail(String message) {
                 if(getActivity()!=null && !isDetached()){
                     SmartToast.showText(getActivity(), message);
                 }
             }
             
             @Override
             public void onError(String message) {
                 // TODO Auto-generated method stub
                 if(getActivity()!=null && !isDetached()){
                     SmartToast.showText(getActivity(), message);
                 }
             }
             
             @Override
             public void onComplete() {
                 // TODO Auto-generated method stub
                 if(getActivity()!=null && !isDetached()){
                     SmartToast.showText(getActivity(), R.string.alipay_success_promt);
                 }
                 if(mProductType==Constant.PRODUCT_TYPE_VIRTUAL){
                     return;
                 }else{
                      getActivity().finish();
                 }
               
                 
             }
             
             @Override
             public void onCancel() {
                 // TODO Auto-generated method stub
                 if(getActivity()!=null && !isDetached()){
                     SmartToast.showText(getActivity(), R.string.alipay_cancel_promt);
                 }
             }
         });
        
    }
    
     protected void onPay(String orderId,String payMoney,String productName,final PayCallBack callback) {
        IPayment pay = new PaymentImpl();
        PayOrder order = new PayOrder();
        order.setmAccountId(Keys.DEFAULT_SELLER);
        order.setmMerchantId(Keys.DEFAULT_PARTNER);
        order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_ORDER_CALLBACK_URL);
        order.setmOrderNo(orderId);
        order.setmProductPrice(payMoney);
        order.setmProductName(productName);
        String username = SharePreferenceUtils.getInstance(getActivity()).getUser().getmNick();
        order.setmProductDesc(username+"购买"+productName);
        pay.onPay(getActivity(), order,new IPayment.PayCallBack() {
            @Override
            public void onFail(String message) {
                if(getActivity()!=null && !isDetached()){
                    SmartToast.showText(getActivity(), message);
                }
            }
            
            @Override
            public void onError(String message) {
                // TODO Auto-generated method stub
                if(getActivity()!=null && !isDetached()){
                    SmartToast.showText(getActivity(), message);
                }
               
            }
            
            @Override
            public void onComplete() {
                // TODO Auto-generated method stub
                if(getActivity()!=null && !isDetached()){
                    SmartToast.showText(getActivity(), R.string.alipay_success_promt);
                }
                if(callback!=null){
                    callback.onPaySuccess();
                }
            }
            
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                if(getActivity()!=null && !isDetached()){
                    SmartToast.showText(getActivity(), R.string.alipay_cancel_promt);
                }
            }
        });
        
    }
    
}
