package com.techfly.liutaitai.model.order.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.order.activities.RateActivity;
import com.techfly.liutaitai.model.order.activities.ServiceDetailActivity;
import com.techfly.liutaitai.model.order.adapter.ServiceAdapter;
import com.techfly.liutaitai.model.order.parser.ServiceOrderParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.ServiceClickListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class ServiceOrderFragment extends CommonFragment implements IXListViewListener, ServiceClickListener{
	private XListView mListView;
	private ArrayList<Service> mList=new ArrayList<Service>();
	private ServiceAdapter mAdapter;
	private TextView mTextView;
	private final int MSG_LIST=0x101;
	private int mPage = 1;
	private int mSize = 10;
	private User mUser;
	private int mType = 0;
	private Service mService;
	public Handler mOrderHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				mListView.setVisibility(View.VISIBLE);
				mTextView.setVisibility(View.GONE);
				if (mList.size() == 0) {
					setNoData();
				}
				mAdapter.updateList(mList);
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
        ManagerListener.newManagerListener().onRegisterServiceClickListener(this);
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
    	setTitleText(R.string.order_appointment_title);
    	mListView=(XListView) view.findViewById(R.id.all_list);
    	mTextView=(TextView) view.findViewById(R.id.all_text);
    	mAdapter=new ServiceAdapter(getActivity(), mList);
    	mListView.setAdapter(mAdapter);
    	mListView.setPullLoadEnable(false);
    	mListView.setPullRefreshEnable(true);
    	mListView.setXListViewListener(this);
    	
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Service service=(Service) parent.getAdapter().getItem(position);
				Intent intent=new Intent(getActivity(),ServiceDetailActivity.class);
				intent.putExtra(IntentBundleKey.ORDER_SERVICE, service.getmId());
				startActivity(intent);
			}
		});
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
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SERVICE_LIST_URL);
			url.setmGetParamPrefix(JsonKey.BalanceKey.PAGE)
			.setmGetParamValues(mPage + "")
			;
			url.setmGetParamPrefix(JsonKey.BalanceKey.SIZE).setmGetParamValues(mSize + "");	
			param.setmParserClassName(ServiceOrderParser.class.getName());
		}
		param.setmIsLogin(true);
//		param.setmId(mUser.getmId());
//		param.setmToken(mUser.getmToken());
		param.setmId("1");
		param.setmToken("440a07c991c4bbae3bcd52746e6a9d32");
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}
   
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ArrayList<Service> list=(ArrayList<Service>) object;
                mList.clear();
                mList.addAll(list);
                if(!isDetached()){
                	mOrderHandler.removeMessages(MSG_LIST);
                	mOrderHandler.sendEmptyMessage(MSG_LIST);
                	mListView.stopLoadMore();
                	mListView.stopRefresh();
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
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                mListView.stopLoadMore();
            	mListView.stopRefresh();
                AppLog.Loge(" data failed to load"+error.getMessage());
            }
       };
    }
	private void setNoData() {
		mListView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(R.string.app_name);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mUser=SharePreferenceUtils.getInstance(getActivity()).getUser();
        if(mUser!=null){
        	startReqTask(ServiceOrderFragment.this);
        }else{
        	mListView.setVisibility(View.GONE);
    		mTextView.setVisibility(View.VISIBLE);
    		mTextView.setText(R.string.login_toast);
        }
	}

	@Override
	public void onRefresh() {
		mOrderHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				startReqTask(ServiceOrderFragment.this);
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		
	}

	@Override
	public void onServiceDeleteListener(Service service) {
		mType = 1;
		mService = service;
	}

	@Override
	public void onServicePayListener(Service service) {
		mType = 2;
		
	}

	@Override
	public void onServiceCancelListener(Service service) {
		mType = 3;
		mService = service;
	}

	@Override
	public void onServiceAgainListener(Service service) {
		mType = 4;
		
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
        	startReqTask(ServiceOrderFragment.this);
        }else{
        	mListView.setVisibility(View.GONE);
    		mTextView.setVisibility(View.VISIBLE);
    		mTextView.setText(R.string.service_no_content);
        }
	}


}
