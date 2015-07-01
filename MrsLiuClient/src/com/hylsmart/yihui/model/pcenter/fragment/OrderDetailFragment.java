package com.hylsmart.yihui.model.pcenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.parser.OrderDetailParser;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.model.mall.fragment.ProductInfoFragment;
import com.hylsmart.yihui.model.pcenter.activities.OrderDetailActivity;
import com.hylsmart.yihui.model.pcenter.activities.SearchLogisticsActivity;
import com.hylsmart.yihui.model.pcenter.adapter.OrderClick;
import com.hylsmart.yihui.model.pcenter.adapter.OrderProductAdapter;
import com.hylsmart.yihui.model.pcenter.bean.MyOrder;
import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.ManagerListener;
import com.hylsmart.yihui.util.ManagerListener.OrderCancelListener;
import com.hylsmart.yihui.util.ManagerListener.OrderDeleteListener;
import com.hylsmart.yihui.util.ManagerListener.OrderLogiticsListener;
import com.hylsmart.yihui.util.ManagerListener.OrderPayListener;
import com.hylsmart.yihui.util.ManagerListener.OrderRateListener;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.fragment.OrderPayFragment;
import com.hylsmart.yihui.util.view.ListViewForScrollView;

public class OrderDetailFragment extends OrderPayFragment implements OrderCancelListener,OrderDeleteListener,OrderLogiticsListener,OrderPayListener,OrderRateListener,OnItemClickListener{
	private OrderDetailActivity mActivity;
	private ListViewForScrollView mListView;
	private MyOrder mOrder;
	private String mOrderId;
	private OrderProductAdapter mAdapter;
	private TextView mTvSn;
	private TextView mTvState;
	private TextView mTvName;
	private TextView mTvPhone;
	private TextView mTvAddress;
	private TextView mTvPay;
	private TextView mTvNote;
	private TextView mTvPrice;
	private TextView mTvFree;
	private TextView mTvTotal;
	private Button mButton;
	private Button mButton2;
	private User mUser;
	private boolean isDelete=false;
	private boolean isCancel=false;
	private final int MSG_DETAIL=0x101;
	private final int MSG_DLELTE=0x102;
	private final int MSG_CANCEL=0x103;
	private ResultInfo mInfo;
	public Handler mDetailHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DLELTE:
				if(mInfo.getmCode()==0){
					showSmartToast(R.string.delete_success, Toast.LENGTH_SHORT);
					mActivity.setResult(Constant.DETAIL_SUCCESS);
					mActivity.finish();
				}else{
					if(mInfo.getmMessage()!=null&&!TextUtils.isEmpty(mInfo.getmMessage())&&!"null".equals(mInfo.getmMessage())){
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.delete_error, Toast.LENGTH_SHORT);
					}
				}
				break;
			case MSG_DETAIL:
				setView();
				break;
			case MSG_CANCEL:
				if(mInfo.getmCode()==0){
					showSmartToast(R.string.cancel_success, Toast.LENGTH_SHORT);
					mActivity.setResult(Constant.DETAIL_SUCCESS);
					mActivity.finish();
				}else{
					if(mInfo.getmMessage()!=null&&!TextUtils.isEmpty(mInfo.getmMessage())&&!"null".equals(mInfo.getmMessage())){
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.cancel_error, Toast.LENGTH_SHORT);
					}
				}
				break;

			default:
				break;
			}
			
		}
		
	};

    @Override
    public void requestData() {
    	if(isDelete||isCancel){
    		RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            if(isDelete){
            	url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_DELETE_URL);
            }else{
            	url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_CANCEL_URL);
            }
            url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
            url.setmGetParamPrefix(JsonKey.MyOrderKey.ID).setmGetParamValues(mOrder.getmId());
//            url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("6533");
            param.setmHttpURL(url);
            param.setmParserClassName(CommonParser.class.getName());
            RequestManager.getRequestData(getActivity(), createReqSuccessListener(), createReqErrorListener(), param);
    	}else{
    		RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.MYORDER_LIST_URL+"/"+mOrderId);
            url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(mUser.getmId());
//            url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("94");
            param.setmHttpURL(url);
            param.setmParserClassName(OrderDetailParser.class.getName());
            RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
    	}
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mOrder=(MyOrder) object;
                if(!isDetached()){
                	mDetailHandler.removeMessages(MSG_DETAIL);
                	mDetailHandler.sendEmptyMessage(MSG_DETAIL);
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                if(!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
       };
    }
    private Response.Listener<Object> createReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mInfo=(ResultInfo) object;
                if(!isDetached()){
                	if(isCancel){
                		mDetailHandler.removeMessages(MSG_CANCEL);
                    	mDetailHandler.sendEmptyMessage(MSG_CANCEL);
                	}else{
                		mDetailHandler.removeMessages(MSG_DLELTE);
                    	mDetailHandler.sendEmptyMessage(MSG_DLELTE);
                	}
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                if(!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
       };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(OrderDetailActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
        mOrderId=mActivity.getIntent().getStringExtra(IntentBundleKey.ORDER_ID);
        startReqTask(OrderDetailFragment.this);
        ManagerListener.newManagerListener().onRegisterOrderCancelListener(this);
        ManagerListener.newManagerListener().onRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onRegisterOrderRateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerListener.newManagerListener().onUnRegisterOrderCancelListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderRateListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleView();
        initView(view);
    }
    private void initTitleView(){
        setTitleText(R.string.order_detail_title);
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    }
    private void initView(View view){
    	mListView=(ListViewForScrollView) view.findViewById(R.id.productlist);
    	mTvAddress=(TextView) view.findViewById(R.id.detail_address);
    	mTvFree=(TextView) view.findViewById(R.id.detail_free);
    	mTvName=(TextView) view.findViewById(R.id.detail_people);
    	mTvNote=(TextView) view.findViewById(R.id.detail_note);
    	mTvPay=(TextView) view.findViewById(R.id.detail_pay);
    	mTvPhone=(TextView) view.findViewById(R.id.detail_phone);
    	mTvPrice=(TextView) view.findViewById(R.id.detail_price);
    	mTvSn=(TextView) view.findViewById(R.id.detail_sn);
    	mTvState=(TextView) view.findViewById(R.id.detail_state);
    	mTvTotal=(TextView) view.findViewById(R.id.detail_total);
    	mButton=(Button) view.findViewById(R.id.detail_order_btn);
    	mButton2=(Button) view.findViewById(R.id.detail_order_btn1);
    	
    }
    private void setView(){
    	mTvAddress.setText(mOrder.getmAddress().getmAddress());
    	mTvFree.setText(mOrder.getmFree());
    	mTvName.setText(mOrder.getmAddress().getmName());
    	mTvNote.setText(getString(R.string.order_detail_note,mOrder.getmNote()));
    	setState(mOrder, mTvState, mButton, mButton2);
    	mTvPhone.setText(mOrder.getmAddress().getmPhone());
    	mTvPrice.setText(mOrder.getmTotalPrice());
    	mTvSn.setText(getString(R.string.order_sn,mOrder.getmId()));
    	mTvTotal.setText(mOrder.getmTotalPrice());
    	if(mOrder!=null){
    		mAdapter=new OrderProductAdapter(mActivity, mOrder.getmList());
    		mListView.setAdapter(mAdapter);
    		mListView.setOnItemClickListener(this);
    	}
    	mButton.setOnClickListener(new OrderClick(mActivity, mOrder, mButton.getText().toString()));
    	mButton2.setOnClickListener(new OrderClick(mActivity, mOrder, mButton2.getText().toString()));
    }
    private void setState(MyOrder order,TextView textView,Button button,Button button2){
    	int state=order.getmState();
		int rate=Integer.valueOf(order.getmNum());
		button.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		if(state==1){
			textView.setText(R.string.state_paying);
			button.setText(R.string.button_cancel);
			button2.setText(R.string.button_pay);
		}else if(state==2){
			textView.setText(R.string.state_delivery);
			button.setVisibility(View.GONE);
			button2.setVisibility(View.GONE);
		}else if(state==3){
			textView.setText(R.string.state_deliveryed);
			button.setVisibility(View.GONE);
			button2.setText(R.string.button_deliveryed);
		}else if(state==4){
			textView.setText(R.string.state_finish);
			button2.setText(R.string.order_delete);
			if(rate==0){
				button.setText(R.string.button_rate);
			}else{
				button.setVisibility(View.GONE);
			}
		}
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
	@Override
	public void onOrderRateListener(MyOrder order) {
		
	}
	@Override
	public void onOrderPayListener(final MyOrder order) {
	   StringBuffer buffer = new StringBuffer();
	   for(Product pro: order.getmList()){
	     buffer.append(pro.getmName()+",");
	   }
	   String name = buffer.toString();
	   name =name.substring(0, name.length()-1);
       onCommitOrder(Constant.PRODUCT_TYPE_ENTITY,order.getmId(), order.getmTotalPrice(),name,new PayCallBack() {
            
            @Override
            public void onPaySuccess() {
                startReqTask(OrderDetailFragment.this);
                UIHelper.toOrdeFinishActivity(OrderDetailFragment.this, order.getmId(),true);
            }
        });
	}
	@Override
	public void onOrderLogiticsListener(MyOrder order) {
		Intent intent=new Intent(getActivity(),SearchLogisticsActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order);
		startActivity(intent);
	}
	@Override
	public void onOrderDeleteListener(MyOrder order) {
		mOrder=order;
		isDelete=true;
		startReqTask(OrderDetailFragment.this);
	}
	@Override
	public void onOrderCancelListener(MyOrder order) {
		mOrder=order;
		isCancel=true;
		startReqTask(OrderDetailFragment.this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Product product=(Product) parent.getAdapter().getItem(position);
		UIHelper.toProductInfoActivity(mActivity, Integer.valueOf(product.getmId()),ProductInfoFragment.FLAG_NORMAL);
	}

}
