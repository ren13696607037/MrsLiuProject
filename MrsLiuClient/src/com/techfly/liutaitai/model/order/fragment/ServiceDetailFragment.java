package com.techfly.liutaitai.model.order.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.order.activities.RateActivity;
import com.techfly.liutaitai.model.order.activities.ServiceDetailActivity;
import com.techfly.liutaitai.model.order.adapter.ServiceClick;
import com.techfly.liutaitai.model.order.parser.ServiceDetailParser;
import com.techfly.liutaitai.model.order.parser.ServiceOrderParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.ServiceClickListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ServiceDetailFragment extends CommonFragment implements ServiceClickListener{
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
	private Service mService;
	private int mType = 0;
	private String mId;
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
        startReqTask(ServiceDetailFragment.this);
        ManagerListener.newManagerListener().onRegisterServiceClickListener(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_service_detail, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerListener.newManagerListener().onUnRegisterServiceClickListener(this);
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
    	
    	mAddress = (TextView) view.findViewById(R.id.osd_address);
    	mButton = (Button) view.findViewById(R.id.osd_btn);
    	mButton2 = (Button) view.findViewById(R.id.osd_btn2);
    	mImageView = (ImageView) view.findViewById(R.id.osd_img);
    	mName = (TextView) view.findViewById(R.id.osd_name);
    	mNo = (TextView) view.findViewById(R.id.osd_no);
    	mPhone = (TextView) view.findViewById(R.id.osd_phone);
    	mPrice = (TextView) view.findViewById(R.id.osd_product_price);
    	mProName = (TextView) view.findViewById(R.id.osd_product_name);
    	mTime = (TextView) view.findViewById(R.id.osd_time);
    	mTotal = (TextView) view.findViewById(R.id.osd_total);
    	mVoucher = (TextView) view.findViewById(R.id.osd_voucher);
    	mServiceTime = (TextView) view.findViewById(R.id.osd_service_time);
    	mState = (TextView) view.findViewById(R.id.osd_state);
    	
    }
    private void setData(){
    	mNo.setText(mActivity.getString(R.string.service_detail_text, mService.getmId()));
    	mAddress.setText(mActivity.getString(R.string.service_detail_text4, mService.getmCustomerAddress()));
    	ImageLoader.getInstance().displayImage(mService.getmServiceIcon(), mImageView, ImageLoaderUtil.mOrderServiceIconLoaderOptions);
    	mName.setText(mActivity.getString(R.string.service_detail_text2, mService.getmCustomerName()));
    	mPhone.setText(mActivity.getString(R.string.service_detail_text3, mService.getmCustomerPhone()));
    	mTime.setText(mActivity.getString(R.string.order_service_text, mService.getmServiceTime()));
    	mServiceTime.setText(mActivity.getString(R.string.service_detail_text1, mService.getmServicePerson()));
    	mProName.setText(mService.getmServiceName());
    	mTotal.setText(mActivity.getString(R.string.service_detail_text9, mService.getmServicePrice()));
    	mVoucher.setText(mActivity.getString(R.string.service_detail_text7, mService.getmCash()));
    	mPrice.setText(mActivity.getString(R.string.service_detail_text5, (float)Math.round((Float.valueOf(mService.getmServicePrice())+Float.valueOf(mService.getmCash()))*100)/100));
    	setState(mService.getmServiceStatus(), mState, mButton, mButton2);
    	mButton.setOnClickListener(new ServiceClick(mActivity, mButton.getText().toString(), mService));
    	mButton2.setOnClickListener(new ServiceClick(mActivity, mButton2.getText().toString(), mService));
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
		if(mType == 1){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SERVICE_DELETE_URL);
			url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mService.getmId());
			param.setmParserClassName(CommonParser.class.getName());
		}else if(mType == 3){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SERVICE_CANCEL_URL);
			url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mService.getmId());
			param.setmParserClassName(CommonParser.class.getName());
		}else{
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_SERVICE_DETAIL_URL);
	        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mId);
	        param.setmParserClassName(ServiceDetailParser.class.getName());
		}
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
//		param.setmId("1");
//		param.setmToken("440a07c991c4bbae3bcd52746e6a9d32");
		param.setmHttpURL(url);
		param.setPostRequestMethod();
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
                	mService = (Service) object;
                    if(!isDetached()){
                    	mServiceDetailHandler.removeMessages(MSG_DATA);
                    	mServiceDetailHandler.sendEmptyMessage(MSG_DATA);
                    }
                }else if(object instanceof ResultInfo){
                	ResultInfo info = (ResultInfo) object;
            		if(mType == 1){
            			if(info.getmCode()==0){
        					showSmartToast(R.string.delete_success, Toast.LENGTH_SHORT);
        					mActivity.finish();
        					ManagerListener.newManagerListener().notifyServiceDeleteListener(mService);
        				}else{
        					if(info.getmMessage()!=null&&!TextUtils.isEmpty(info.getmMessage())&&!"null".equals(info.getmMessage())){
        						showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
        					}else{
        						showSmartToast(R.string.delete_error, Toast.LENGTH_SHORT);
        					}
        				}
            		}else if(mType == 3){
            			if(info.getmCode()==0){
        					showSmartToast(R.string.cancel_success, Toast.LENGTH_SHORT);
        					mType = 0;
        					startReqTask(ServiceDetailFragment.this);
        				}else{
        					if(info.getmMessage()!=null&&!TextUtils.isEmpty(info.getmMessage())&&!"null".equals(info.getmMessage())){
        						showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
        					}else{
        						showSmartToast(R.string.cancel_error, Toast.LENGTH_SHORT);
        					}
        				}
            		}
                }/*else{
                	showSmartToast((String)object, Toast.LENGTH_SHORT);
                }*/
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
    private void setState(String state, TextView textView, Button button, Button button2){
    	AppLog.Loge("xll", "service detail state is in");
		button.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		if("0".equals(state)){
			AppLog.Loge("xll", "service detail state is in" + state);
			textView.setText(R.string.order_service_state);
			button.setText(R.string.order_service_btn1);
			button2.setText(R.string.order_service_btn);
		}else if("1".equals(state)){
			AppLog.Loge("xll", "service detail state is in" + state);
			textView.setText(R.string.order_service_state1);
			button.setText(R.string.order_service_btn2);
			button2.setVisibility(View.INVISIBLE);
		}else if("2".equals(state)){
			AppLog.Loge("xll", "service detail state is in" + state);
			textView.setText(R.string.order_service_state2);
			button.setText(R.string.order_service_btn3);
			button2.setVisibility(View.INVISIBLE);
		}else if("3".equals(state) || "4".equals(state)){
			textView.setText(R.string.order_service_state3);
			button.setText(R.string.order_service_btn3);
			button2.setVisibility(View.INVISIBLE);
		}else if("5".equals(state)){
			AppLog.Loge("xll", "service detail state is in" + state);
			textView.setText(R.string.order_service_state4);
			button.setText(R.string.order_service_btn5);
			button2.setText(R.string.order_service_btn4);
		}else if("6".equals(state)){
			AppLog.Loge("xll", "service detail state is in" + state);
			textView.setText(R.string.order_service_state5);
			button.setText(R.string.order_service_btn);
			button2.setText(R.string.order_service_btn4);
		}else if("-1".equals(state)){
			AppLog.Loge("xll", "service detail state is in" + state);
			textView.setText(R.string.order_service_state6);
			button.setText(R.string.order_service_btn);
			button2.setText(R.string.order_service_btn4);
		}else{
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
			AppLog.Loge("xll", "service detail state is in why?????" );
		}
	}

	@Override
	public void onServiceDeleteListener(Service service) {
		mType = 1;
		mService = service;
		startReqTask(ServiceDetailFragment.this);
	}

	@Override
	public void onServicePayListener(Service service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceCancelListener(Service service) {
		mType = 3;
		mService = service;
		startReqTask(ServiceDetailFragment.this);
	}

	@Override
	public void onServiceAgainListener(Service service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceRateListener(Service service) {
		mType = 5;
		Intent intent = new Intent(getActivity(), RateActivity.class);
		intent.putExtra(IntentBundleKey.SERVICE_ID, service.getmId());
		startActivity(intent);
	}

	@Override
	public void onServiceRefreshListener() {
		if(mUser!=null){
        	startReqTask(ServiceDetailFragment.this);
        }
	}

}
