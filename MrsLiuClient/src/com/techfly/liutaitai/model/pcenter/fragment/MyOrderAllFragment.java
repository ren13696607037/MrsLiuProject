package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.OrderListParser;
import com.techfly.liutaitai.bizz.parser.TechOrderParser;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
import com.techfly.liutaitai.model.pcenter.activities.RateActivity;
import com.techfly.liutaitai.model.pcenter.activities.SearchLogisticsActivity;
import com.techfly.liutaitai.model.pcenter.adapter.MyOrderAdapter;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.OrderCancelListener;
import com.techfly.liutaitai.util.ManagerListener.OrderDeleteListener;
import com.techfly.liutaitai.util.ManagerListener.OrderLogiticsListener;
import com.techfly.liutaitai.util.ManagerListener.OrderPayListener;
import com.techfly.liutaitai.util.ManagerListener.OrderRateListener;
import com.techfly.liutaitai.util.ManagerListener.OrderTakeListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment;
import com.techfly.liutaitai.util.fragment.OrderPayFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyOrderAllFragment extends CreateOrderPayCommonFragment implements OnItemClickListener,IXListViewListener,OrderCancelListener,OrderDeleteListener,OrderLogiticsListener,OrderRateListener,OrderPayListener,OrderTakeListener{
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<TechOrder> mList=new ArrayList<TechOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private final int MSG_DLELTE=0x102;
	private final int MSG_CANCEL=0x103;
	private int mPage=1;
	private int mSize=10;
	private boolean isRefresh=true;
	private ResultInfo mInfo;
	private TechOrder mOrder;
	private User mUser;
	private int mType;
	public Handler mAllHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				if(mList.size()==0){
					setNoData();
				}
				mAdapter.updateList(mList);
				break;
			case MSG_DLELTE:
				if(mInfo.getmCode()==0){
					showSmartToast(R.string.delete_success, Toast.LENGTH_SHORT);
					startReqTask(MyOrderAllFragment.this);
				}else{
					if(mInfo.getmMessage()!=null&&!TextUtils.isEmpty(mInfo.getmMessage())&&!"null".equals(mInfo.getmMessage())){
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.delete_error, Toast.LENGTH_SHORT);
					}
				}
				break;
			case MSG_CANCEL:
				if(mInfo.getmCode()==0){
					showSmartToast(R.string.cancel_success, Toast.LENGTH_SHORT);
					startReqTask(MyOrderAllFragment.this);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
        startReqTask(MyOrderAllFragment.this);
        ManagerListener.newManagerListener().onRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onRegisterOrderRateListener(this);
        ManagerListener.newManagerListener().onRegisterOrderCancelListener(this);
        ManagerListener.newManagerListener().onRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onRegisterOrderTakeListener(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderall, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerListener.newManagerListener().onUnRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderRateListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderCancelListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderTakeListener(this);
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
    	mListView=(XListView) view.findViewById(R.id.all_list);
    	mTextView=(TextView) view.findViewById(R.id.all_text);
    	mTextView.setText(R.string.order_pay_text);
    	mAdapter=new MyOrderAdapter(getActivity(), mList);
    	mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
		if(mType == 3){
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_REMOVE_URL);
	        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
	        param.setmParserClassName(CommonParser.class.getName());
		}else if(mType == 4){
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_REFRSE_URL);
	        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
	        param.setmParserClassName(CommonParser.class.getName());
		}else if(mType == 1){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_DONE_URL);
	        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
	        //TODO 上传图片
	        param.setmParserClassName(CommonParser.class.getName());
		}else if(mType == 2){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_START_URL);
	        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
	        param.setmParserClassName(CommonParser.class.getName());
		}else if(mType == 5){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_TAKE_URL);
	        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
	        param.setmParserClassName(CommonParser.class.getName());
		}else{
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_LIST_URL);
	        url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("0");
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
	        url.setmGetParamPrefix(JsonKey.VoucherKey.PAGE).setmGetParamValues(mPage+"");
	        param.setmParserClassName(TechOrderParser.class.getName());
		}
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setPostRequestMethod();
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
    }
	private Response.Listener<Object> createCancelReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mInfo=(ResultInfo) object;
                if(!isDetached()){
                	mAllHandler.removeMessages(MSG_CANCEL);
                	mAllHandler.sendEmptyMessage(MSG_CANCEL);
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createCancelReqErrorListener() {
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
                	mAllHandler.removeMessages(MSG_DLELTE);
                	mAllHandler.sendEmptyMessage(MSG_DLELTE);
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
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                if(!isDetached()){
                	if(object instanceof ArrayList){
                		ArrayList<TechOrder> list=(ArrayList<TechOrder>) object;
                        if(isRefresh){
                        	mList.addAll(list);
                        }else{
                        	mList.clear();
                        	mList.addAll(list);
                        }
                        if (list == null || list.size() == 0) {
                        	
        				} else if (list.size() < 10) {
        					mListView.setVisibility(View.VISIBLE);
        				    mTextView.setVisibility(View.GONE);
        					mListView.setPullLoadEnable(false);
        				} else {
        					mListView.setVisibility(View.VISIBLE);
        					mTextView.setVisibility(View.GONE);
        					mListView.setPullLoadEnable(true);
        				}
        				mListView.stopLoadMore();
        				mListView.stopRefresh();
                    	mAllHandler.removeMessages(MSG_LIST);
                    	mAllHandler.sendEmptyMessage(MSG_LIST);
                	}else if(object instanceof ResultInfo){
                		ResultInfo info = (ResultInfo) object;
                		if(mType == 1){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}else if(mType == 2){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}else if(mType == 3){
                			
                		}else if(mType == 4){
                			
                		}else if(mType == 5){
                			
                		}
                	}
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
    private void setNoData(){
		mListView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(getResources().getString(R.string.order_all_text));
	}

	@Override
	public void onRefresh() {
		mAllHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPage = 1;
				mList.clear();
				requestData();
			}
		}, 0);
	}

	@Override
	public void onLoadMore() {
		mAllHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPage += 1;
				requestData();
			}
		}, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TechOrder order=(TechOrder) parent.getAdapter().getItem(position);
		Intent intent=new Intent(getActivity(),OrderDetailActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order.getmId());
		startActivityForResult(intent, Constant.DETAIL_INTENT);
	}

	@Override
	public void onOrderRateListener(TechOrder order) {//技师完成服务
		mType = 1;
		Intent intent=new Intent(getActivity(),RateActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order);
		startActivity(intent);
	}

	@Override
	public void onOrderLogiticsListener(TechOrder order) {//技师开始服务
		mType = 2;
		mOrder=order;
		isRefresh=false;
		startReqTask(MyOrderAllFragment.this);
//		Intent intent=new Intent(getActivity(),SearchLogisticsActivity.class);
//		intent.putExtra(IntentBundleKey.ORDER_ID, order);
//		startActivity(intent);
	}

	@Override
	public void onOrderDeleteListener(TechOrder order) {
		mType = 3;
		mOrder=order;
		isRefresh=false;
		startReqTask(MyOrderAllFragment.this);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Constant.DETAIL_SUCCESS){
			isRefresh=false;
			startReqTask(MyOrderAllFragment.this);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mList.size()==0){
			isRefresh=false;
			startReqTask(MyOrderAllFragment.this);
		}else{
			
		}
	}

	@Override
	public void onOrderCancelListener(TechOrder order) {
		mOrder=order;
		isRefresh=false;
		mType = 4;
		startReqTask(MyOrderAllFragment.this);
	}

	@Override
	public String onEncapleOrderInfo(HttpURL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onOrderCreateSuccess(String orderId, String money,
			String proName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOrderPayListener(TechOrder order) {//技师联系客服
		mType = 6;
		mOrder = order;
	}

	@Override
	public void onOrderTakeListener(TechOrder order) {
		mType = 5;
		mOrder = order;
		startReqTask(MyOrderAllFragment.this);
	}


}
