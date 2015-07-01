package com.hylsmart.yihui.model.pcenter.fragment;

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
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bizz.parser.OrderListParser;
import com.hylsmart.yihui.model.pcenter.activities.OrderDetailActivity;
import com.hylsmart.yihui.model.pcenter.adapter.MyOrderAdapter;
import com.hylsmart.yihui.model.pcenter.bean.MyOrder;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.XListView;
import com.hylsmart.yihui.util.view.XListView.IXListViewListener;

public class MyOrderDeliveryFragment extends CommonFragment implements OnItemClickListener,IXListViewListener{
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<MyOrder> mList=new ArrayList<MyOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private int mPage=0;
	private int mSize=10;
	public Handler mDeliveryHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				if(mList.size()==0){
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
        startReqTask(MyOrderDeliveryFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderdelivery, container, false);
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
    	mListView=(XListView) view.findViewById(R.id.deliver_list);
    	mTextView=(TextView) view.findViewById(R.id.delivery_text);
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
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.MYORDER_LIST_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
        url.setmGetParamPrefix(JsonKey.MyOrderKey.STATE).setmGetParamValues("1");
        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
        url.setmGetParamPrefix(JsonKey.MyOrderKey.PAGE).setmGetParamValues(mPage+"");
//        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("275");
        param.setmHttpURL(url);
        param.setmParserClassName(OrderListParser.class.getName());
        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
       
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ArrayList<MyOrder> list=(ArrayList<MyOrder>) object;
                mList.addAll(list);
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
                	mDeliveryHandler.removeMessages(MSG_LIST);
                	mDeliveryHandler.sendEmptyMessage(MSG_LIST);
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
        mTextView.setText(getResources().getString(R.string.order_delivery_text));
	}

	@Override
	public void onRefresh() {
		mDeliveryHandler.postDelayed(new Runnable() {
			
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
		mDeliveryHandler.postDelayed(new Runnable() {
			
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Constant.DETAIL_SUCCESS){
			startReqTask(MyOrderDeliveryFragment.this);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mList.size()==0){
			startReqTask(MyOrderDeliveryFragment.this);
		}
	}

}
