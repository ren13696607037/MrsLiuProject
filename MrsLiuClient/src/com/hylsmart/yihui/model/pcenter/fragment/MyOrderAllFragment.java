package com.hylsmart.yihui.model.pcenter.fragment;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.parser.OrderListParser;
import com.hylsmart.yihui.model.pcenter.activities.OrderDetailActivity;
import com.hylsmart.yihui.model.pcenter.activities.RateActivity;
import com.hylsmart.yihui.model.pcenter.activities.SearchLogisticsActivity;
import com.hylsmart.yihui.model.pcenter.adapter.MyOrderAdapter;
import com.hylsmart.yihui.model.pcenter.bean.MyOrder;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.ManagerListener;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.ManagerListener.OrderCancelListener;
import com.hylsmart.yihui.util.ManagerListener.OrderDeleteListener;
import com.hylsmart.yihui.util.ManagerListener.OrderLogiticsListener;
import com.hylsmart.yihui.util.ManagerListener.OrderPayListener;
import com.hylsmart.yihui.util.ManagerListener.OrderRateListener;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.fragment.OrderPayFragment;
import com.hylsmart.yihui.util.view.XListView;
import com.hylsmart.yihui.util.view.XListView.IXListViewListener;

public class MyOrderAllFragment extends OrderPayFragment implements OnItemClickListener,IXListViewListener,OrderCancelListener,OrderDeleteListener,OrderLogiticsListener,OrderPayListener,OrderRateListener{
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<MyOrder> mList=new ArrayList<MyOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private final int MSG_DLELTE=0x102;
	private final int MSG_CANCEL=0x103;
	private int mPage=0;
	private int mSize=10;
	private boolean isCancel=false;
	private boolean isDelete=false;
	private boolean isRefresh=true;
	private ResultInfo mInfo;
	private MyOrder mOrder;
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
					isDelete=false;
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
					isCancel=false;
					isDelete=false;
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
        startReqTask(MyOrderAllFragment.this);
        ManagerListener.newManagerListener().onRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onRegisterOrderRateListener(this);
        ManagerListener.newManagerListener().onRegisterOrderCancelListener(this);
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
        ManagerListener.newManagerListener().onUnRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderRateListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderCancelListener(this);
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
		if(isDelete){
			RequestParam param = new RequestParam();
	        HttpURL url = new HttpURL();
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_DELETE_URL);
	        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.ID).setmGetParamValues(mOrder.getmId());
//	        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("6533");
	        param.setmHttpURL(url);
	        param.setmParserClassName(CommonParser.class.getName());
	        RequestManager.getRequestData(getActivity(), createReqSuccessListener(), createReqErrorListener(), param);
		}else if(isCancel){
			RequestParam param = new RequestParam();
	        HttpURL url = new HttpURL();
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_CANCEL_URL);
	        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.ID).setmGetParamValues(mOrder.getmId());
	        param.setmHttpURL(url);
	        param.setmParserClassName(CommonParser.class.getName());
	        RequestManager.getRequestData(getActivity(), createCancelReqSuccessListener(), createCancelReqErrorListener(), param);
	       
		}else{
			RequestParam param = new RequestParam();
	        HttpURL url = new HttpURL();
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.MYORDER_LIST_URL);
	        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.STATE).setmGetParamValues("3");
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.PAGE).setmGetParamValues(mPage+"");
//	        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("2");
	        param.setmHttpURL(url);
	        param.setmParserClassName(OrderListParser.class.getName());
	        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
		}
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
                ArrayList<MyOrder> list=(ArrayList<MyOrder>) object;
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
                if(!isDetached()){
                	mAllHandler.removeMessages(MSG_LIST);
                	mAllHandler.sendEmptyMessage(MSG_LIST);
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
				mPage = 0;
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
		MyOrder order=(MyOrder) parent.getAdapter().getItem(position);
		Intent intent=new Intent(getActivity(),OrderDetailActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order.getmId());
		startActivityForResult(intent, Constant.DETAIL_INTENT);
	}

	@Override
	public void onOrderRateListener(MyOrder order) {
		Intent intent=new Intent(getActivity(),RateActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order);
		startActivity(intent);
	}

	@Override
	public void onOrderPayListener(final MyOrder order) {
		onCommitOrder(Constant.PRODUCT_TYPE_ENTITY,order.getmId(), order.getmPrice(), order.getmTitle(),new PayCallBack() {
            
            @Override
            public void onPaySuccess() {
                mList.clear();mPage=0;
                startReqTask(MyOrderAllFragment.this);
                UIHelper.toOrdeFinishActivity(MyOrderAllFragment.this, order.getmId(),false);
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
		isRefresh=false;
		startReqTask(MyOrderAllFragment.this);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Constant.DETAIL_SUCCESS){
			isRefresh=false;
			isDelete=false;
			startReqTask(MyOrderAllFragment.this);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mList.size()==0){
			isRefresh=false;
			isDelete=false;
			startReqTask(MyOrderAllFragment.this);
		}else{
			
		}
	}

	@Override
	public void onOrderCancelListener(MyOrder order) {
		mOrder=order;
		isCancel=true;
		isRefresh=false;
		isDelete=false;
		startReqTask(MyOrderAllFragment.this);
	}

}
