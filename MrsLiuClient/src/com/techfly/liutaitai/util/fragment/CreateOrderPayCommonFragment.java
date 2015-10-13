package com.techfly.liutaitai.util.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.paymanage.PayImplFactory;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.net.ResultCode;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;

public  abstract class CreateOrderPayCommonFragment extends CommonFragment {

    protected ProgressDialog mProgressDialog;// 进度条
    private String mOrderId;
    private String mPayMoney;
    private String mProductName;
    private int mProductType;
    private int mPayMethod;// 余额支付，支付宝支付，微信支付
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
        super.onAttach(activity);
        mIsFromShopCar = activity.getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
    }

    @Override
    public void requestData() {
        
        
    }

    public abstract String onEncapleOrderInfo(HttpURL url);
    
    public abstract void onOrderCreateSuccess(String orderId,String money,String proName);
    
    protected void onCommitOrder(int productType,int payType,String price,String productName) {
           AppLog.Logd("Fly", "Taking Order request data");
           mPayMoney = price;
           mProductName = productName;
           mProductType = productType;
           showDialog();
           RequestParam param = new RequestParam();
           User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
           int userId = 0;
           if (user != null) {
               userId = Integer.parseInt(user.getmId());
           }
           if (userId == 0) {
               return;
           }
           param.setmIsLogin(true);
           param.setmId(user .getmId());
           param.setmToken(user .getmToken());
//           param.setPostRequestMethod();
           HttpURL url = new HttpURL();
           if(productType==Constant.PRODUCT_TYPE_SERVICE){
               url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.SERVICE_ORDER_COMMIT_REQUEST_URL);
           }else{
               url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.ORDER_COMMIT_REQUEST_URL);
           }
           onEncapleOrderInfo(url);
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
                        if(mProductType==Constant.PRODUCT_TYPE_SERVICE){
                            mOrderId = info.getmData();
                        }else{
                            try {
                                JSONObject obj = new JSONObject(info.getmData());
                                mOrderId = obj.optString("orderNum");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                             if(mIsFromShopCar){
                                 ShopCar.getShopCar().getShopproductList().removeAll(ShopCar.getShopCar().getCheckShopproductList());
                                 OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null);
                             }
                        }
                        onOrderCreateSuccess(mOrderId,mPayMoney,mProductName);
                     }else{
                        if (mProductType==Constant.PRODUCT_TYPE_SERVICE){
                            AlertDialogUtils.displayMyAlertChoice(getActivity(), "提示", "该技师已被预约",getString(R.string.confirm), null);
                        }else{
                            showSmartToast(info.getmMessage()+"", Toast.LENGTH_LONG);
                        }
                       
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
    
    
     protected void onPay(int payMethod,String orderId,String payMoney,String productName,final PayCallBack callback) {
        mPayMethod =payMethod;
        PayImplFactory.getInstance().getPayImpl(mPayMethod).onPay(getActivity(), orderId, payMoney, productName, callback);
    }
    
}
