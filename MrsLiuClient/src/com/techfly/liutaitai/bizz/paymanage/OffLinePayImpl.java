package com.techfly.liutaitai.bizz.paymanage;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.net.ResultCode;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

public class OffLinePayImpl implements PayInterface {
    private PayCallBack mCallback;
    @Override
    public void onPay(Context context, String orderId, String payMoney,
            String productName, PayCallBack callback) {
        mCallback = callback;
        RequestParam param = new RequestParam();
        User user = SharePreferenceUtils.getInstance(context).getUser();
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
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+"common/payData");
        url.setmGetParamPrefix("type");
        url.setmGetParamValues("3");
        url.setmGetParamPrefix("id");
        url.setmGetParamValues(orderId);
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(context, createMyReqSuccessListener(), createMyReqErrorListener(), param);
        
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd("Fly", "object===="+object.toString());
                ResultInfo resultInfo = (ResultInfo) object;
                if(resultInfo.getmCode()==ResultCode.SUCCESS){
                    if(mCallback!=null){
                        mCallback.onPaySuccess();
                    }
                }else{
                   
                }
                }
        };
    }
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            
            
            }
        };
    }
}
