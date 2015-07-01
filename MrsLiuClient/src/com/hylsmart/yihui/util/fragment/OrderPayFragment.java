package com.hylsmart.yihui.util.fragment;

import android.app.Activity;
import android.app.ProgressDialog;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bizz.alipay.IPayment;
import com.hylsmart.yihui.bizz.alipay.Keys;
import com.hylsmart.yihui.bizz.alipay.PayOrder;
import com.hylsmart.yihui.bizz.alipay.PaymentImpl;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.SmartToast;

public class OrderPayFragment extends CommonFragment {
    protected ProgressDialog mProgressDialog;// 进度条
    private String mOrderId;
    private String mPayMoney;
    private String mProductName;
    private int mProductType;
    private PayCallBack mPayCallBack;
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
    }

    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }

  
    
    protected void onCommitOrder(int productType,String orderId,String price,String productName,PayCallBack payCallBack) {
           AppLog.Logd("Fly", "Taking Order request data");
           mPayMoney = price;
           mProductName = productName;
           mProductType = productType;
           mPayCallBack =payCallBack;
           mOrderId = orderId;
           onPay();
    }
    
    
    
    private void onPay() {
         IPayment pay = new PaymentImpl();
         PayOrder order = new PayOrder();
         order.setmAccountId(Keys.DEFAULT_SELLER);
         order.setmMerchantId(Keys.DEFAULT_PARTNER);
         order.setmNotifyUrl(Constant.YIHUIMALL_BASE_URL+Constant.ALIPAY_CALLBACK_URL);
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
                 mPayCallBack.onPaySuccess();
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
