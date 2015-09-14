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
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.OrderPayFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyOrderAllFragment extends OrderPayFragment implements OnItemClickListener,IXListViewListener,OrderCancelListener,OrderDeleteListener,OrderLogiticsListener,OrderPayListener,OrderRateListener{
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<TechOrder> mList=new ArrayList<TechOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private final int MSG_DLELTE=0x102;
	private final int MSG_CANCEL=0x103;
	private int mPage=1;
	private int mSize=10;
	private boolean isCancel=false;
	private boolean isDelete=false;
	private boolean isRefresh=true;
	private ResultInfo mInfo;
	private TechOrder mOrder;
	private User mUser;
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
        mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
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
	        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_LIST_URL);
	        url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("0");
	        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
	        url.setmGetParamPrefix(JsonKey.VoucherKey.PAGE).setmGetParamValues(mPage+"");
	        param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setPostRequestMethod();
	        param.setmHttpURL(url);
	        param.setmParserClassName(TechOrderParser.class.getName());
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
	public void onOrderRateListener(TechOrder order) {
		Intent intent=new Intent(getActivity(),RateActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order);
		startActivity(intent);
	}

	@Override
	public void onOrderPayListener(final TechOrder order) {
		onCommitOrder(Constant.PRODUCT_TYPE_ENTITY,order.getmId(), order.getmServicePrice(), order.getmServiceName(),new PayCallBack() {
            
            @Override
            public void onPaySuccess() {
                mList.clear();mPage=0;
                startReqTask(MyOrderAllFragment.this);
                UIHelper.toOrdeFinishActivity(MyOrderAllFragment.this, order.getmId(),false);
            }
        });
	}

	@Override
	public void onOrderLogiticsListener(TechOrder order) {
		Intent intent=new Intent(getActivity(),SearchLogisticsActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order);
		startActivity(intent);
	}

	@Override
	public void onOrderDeleteListener(TechOrder order) {
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
	public void onOrderCancelListener(TechOrder order) {
		mOrder=order;
		isCancel=true;
		isRefresh=false;
		isDelete=false;
		startReqTask(MyOrderAllFragment.this);
	}

}
