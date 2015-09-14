package com.techfly.liutaitai.model.pcenter.fragment;

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
import com.techfly.liutaitai.bizz.parser.OrderListParser;
import com.techfly.liutaitai.bizz.parser.TechOrderParser;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
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
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyOrderDeliveryFragment extends CommonFragment implements OnItemClickListener,IXListViewListener{
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<TechOrder> mList=new ArrayList<TechOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private int mPage=1;
	private int mSize=10;
	private User mUser;
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
        mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
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
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_LIST_URL);
        url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("2");
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
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ArrayList<TechOrder> list=(ArrayList<TechOrder>) object;
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
