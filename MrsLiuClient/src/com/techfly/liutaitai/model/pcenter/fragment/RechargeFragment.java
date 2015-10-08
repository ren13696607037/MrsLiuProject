package com.techfly.liutaitai.model.pcenter.fragment;


import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.paymanage.PayImplFactory;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.bizz.wenxin.MD5;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.mall.parser.ShopCartParser;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

public class RechargeFragment extends CommonFragment implements OnClickListener{
	private RechargeActivity mActivity;
	private User mUser;
	private TextView mPrice;
	private RelativeLayout mAlipay;
	private RelativeLayout mWeixin;
	private ImageView mCbAlipay;
	private ImageView mCbWeixin;
	private Button mBtn;
	private Balance mBalance;
	private int mType = Constant.PAY_ALIPAY;
    protected String orderNo;

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (RechargeActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        mBalance = (Balance) mActivity.getIntent().getSerializableExtra(IntentBundleKey.BALANCE_PRICE);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.recharge_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mPrice = (TextView) view.findViewById(R.id.recharge_price);
    	mAlipay = (RelativeLayout) view.findViewById(R.id.recharge_alipay);
    	mWeixin = (RelativeLayout) view.findViewById(R.id.recharge_weixin);
    	mCbAlipay = (ImageView) view.findViewById(R.id.recharge_cb_alipay);
    	mCbWeixin = (ImageView) view.findViewById(R.id.recharge_cb_weixin);
    	mBtn = (Button) view.findViewById(R.id.recharge_btn);
    	
    	mBtn.setOnClickListener(this);
    	mAlipay.setOnClickListener(this);
    	mWeixin.setOnClickListener(this);
    	
    	
    	mPrice.setText(mBalance.getmPrice());
    	
    }

    public void chargeCallBack(){
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
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
        
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
                + "common/payData");
        url.setmGetParamPrefix("id");
        url.setmGetParamValues(orderNo);
        url.setmGetParamPrefix("type");
        if(mType == Constant.PAY_ALIPAY){
            url.setmGetParamValues("0");
        }else{
            url.setmGetParamValues("1");
        }
       
        param.setmHttpURL(url);
//        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(getActivity(), creatMySuccessListener(),
                creatErrorListener(), param);//
    }
    private Response.Listener<Object> creatMySuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
//                ResultInfo result = (ResultInfo) obj;
//                if(result.getmCode()==0){
//                    AppLog.Logd("Fly", "result.getmData()==="+result.getmData());
                    showSmartToast("充值成功", Toast.LENGTH_SHORT);
                    getActivity().setResult(Constant.BALANCE_SUCCESS);
                    getActivity().finish();
//                }
            }
        };
    }

	@Override
	public void requestData() {
	    
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
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
        
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
                + Constant.RECHARGE_REQUEST_URL);
        url.setmGetParamPrefix("vid");
        url.setmGetParamValues(mBalance.getmId());
        url.setmGetParamPrefix("type");
        if(mType == Constant.PAY_ALIPAY){
            url.setmGetParamValues("0");
        }else{
            url.setmGetParamValues("1");
        }
        url.setmGetParamPrefix("money");
        url.setmGetParamValues(mBalance.getmPrice());
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(getActivity(), creatSuccessListener(),
                creatErrorListener(), param);//
    }
    private Response.Listener<Object> creatSuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                ResultInfo result = (ResultInfo) obj;
                if(result.getmCode()==0){
                    AppLog.Logd("Fly", "result.getmData()==="+result.getmData());
                    orderNo = result.getmData();
                    PayImplFactory.getInstance().getPayImpl(mType).onPay(getActivity(),   orderNo, mBalance.getmPrice(), "充值", new PayCallBack() {
                        
                        @Override
                        public void onPaySuccess() {
                            chargeCallBack();
                        }
                    }); 
                }
            }
        };
    }

    private Response.ErrorListener creatErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());

            }
        };
    }

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.recharge_alipay:
			mType = Constant.PAY_ALIPAY;
			mCbAlipay.setImageResource(R.drawable.address_default);
			mCbWeixin.setImageResource(R.drawable.address_undefault);
			break;
		case R.id.recharge_weixin:
			mType = Constant.PAY_WENXIN;
			mCbWeixin.setImageResource(R.drawable.address_default);
			mCbAlipay.setImageResource(R.drawable.address_undefault);
			break;
		case R.id.recharge_btn:
		    AppLog.Logd("Fly","mBalance.getmId()==="+mBalance.getmId());
		    AppLog.Logd("Fly","mBalance.getmPrice()==="+mBalance.getmPrice());
		    mBalance.setmPrice("1");
			requestData();
			break;

		default:
			break;
		}
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(resultCode == Constant.BALANCE_SUCCESS){
	        chargeCallBack();
	    }
    }

    private String genOutTradNo() {
	       Random random = new Random();
	       return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	   }
}
