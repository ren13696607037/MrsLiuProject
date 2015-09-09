package com.techfly.liutaitai.model.pcenter.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.TechOrderDetailParser;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.order.activities.ServiceDetailActivity;
import com.techfly.liutaitai.model.order.parser.ServiceDetailParser;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class TechOrderDetailFragment extends CommonFragment implements OnClickListener{
	private ServiceDetailActivity mActivity;
	private TextView mNo;
	private TextView mTime;
	private TextView mName;
	private TextView mPhone;
	private TextView mAddress;
	private TextView mServiceTime;
	private TextView mVoucher;
	private TextView mTotal;
	private ImageView mImageView;
	private TextView mPrice;
	private TextView mProName;
	private TextView mState;
	private Button mButton;
	private Button mButton2;
	private TechOrder mOrder;
	private String mId;
	private ImageView mIvPhone;
	private ImageView mIvAddress;
	private User mUser;
	private final int MSG_DATA = 0x101;
	private Handler mServiceDetailHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == MSG_DATA){
				setData();
			}
		};
	};
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ServiceDetailActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = mActivity.getIntent().getStringExtra(IntentBundleKey.ORDER_SERVICE);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(TechOrderDetailFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_service_detail, container, false);
        return view;
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	setTitleText(R.string.service_detail_title);
    	
    	mAddress = (TextView) view.findViewById(R.id.tsd_address);
    	mButton = (Button) view.findViewById(R.id.tsd_btn);
    	mButton2 = (Button) view.findViewById(R.id.tsd_btn2);
    	mImageView = (ImageView) view.findViewById(R.id.tsd_img);
    	mName = (TextView) view.findViewById(R.id.tsd_name);
    	mNo = (TextView) view.findViewById(R.id.tsd_no);
    	mPhone = (TextView) view.findViewById(R.id.tsd_phone);
    	mPrice = (TextView) view.findViewById(R.id.tsd_product_price);
    	mProName = (TextView) view.findViewById(R.id.tsd_product_name);
    	mTime = (TextView) view.findViewById(R.id.tsd_time);
    	mTotal = (TextView) view.findViewById(R.id.tsd_total);
    	mVoucher = (TextView) view.findViewById(R.id.tsd_voucher);
    	mServiceTime = (TextView) view.findViewById(R.id.tsd_service_time);
    	mState = (TextView) view.findViewById(R.id.tsd_state);
    	mIvAddress = (ImageView) view.findViewById(R.id.tsd_address_img);
    	mIvPhone = (ImageView) view.findViewById(R.id.tsd_phone_img);
    	
    	mIvAddress.setOnClickListener(this);
    	mIvPhone.setOnClickListener(this);
    	mButton.setOnClickListener(this);
    	mButton2.setOnClickListener(this);
    	
    }
    private void setData(){
    	mNo.setText(mActivity.getString(R.string.service_detail_text, mOrder.getmId()));
    	mAddress.setText(mActivity.getString(R.string.service_detail_text4, mOrder.getmCustomerAddress()));
    	ImageLoader.getInstance().displayImage(mOrder.getmServiceIcon(), mImageView, ImageLoaderUtil.mOrderServiceIconLoaderOptions);
    	mName.setText(mActivity.getString(R.string.service_detail_text2, mOrder.getmCustomerName()));
    	mPhone.setText(mActivity.getString(R.string.service_detail_text3, mOrder.getmCustomerPhone()));
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_DETAIL_URL);
        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mId);
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
        param.setmParserClassName(TechOrderDetailParser.class.getName());
        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
            	mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                AppLog.Logd(object.toString());
                if(object instanceof Service){
                	mOrder = (TechOrder) object;
                    if(!isDetached()){
                    	mServiceDetailHandler.removeMessages(MSG_DATA);
                    	mServiceDetailHandler.sendEmptyMessage(MSG_DATA);
                    }
                }else{
                	showSmartToast((String)object, Toast.LENGTH_SHORT);
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                AppLog.Loge(" data failed to load"+error.getMessage());
            }
       };
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
