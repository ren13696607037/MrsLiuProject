package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.RateListParser;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.activities.RateListActivity;
import com.techfly.liutaitai.model.pcenter.adapter.RateListAdapter;
import com.techfly.liutaitai.model.pcenter.bean.Rate;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class RateListFragment extends CommonFragment implements IXListViewListener{
	private RateListActivity mActivity;
	private RatingBar mBar;
	private TextView mTextView;
	private TextView mTvContent;
	private XListView mListView;
	private ArrayList<Rate> mList=new ArrayList<Rate>();
	private RateListAdapter mAdapter;
	private Product mProduct;
	private int mPage=0;
	private int mSize=10;
	private final int MSG_LIST=0x101;
	public Handler mRateListHandler=new Handler(){

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
        mActivity=(RateListActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProduct=(Product) mActivity.getIntent().getSerializableExtra(IntentBundleKey.PRODUCT_ID);
        startReqTask(RateListFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ratelist, container, false);
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
    	mBar=(RatingBar) view.findViewById(R.id.list_bar);
    	mTextView=(TextView) view.findViewById(R.id.list_text);
    	mTvContent=(TextView) view.findViewById(R.id.rate_no_content);
    	mListView=(XListView) view.findViewById(R.id.list_rate);
    	
    	mListView.setXListViewListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
    	
    	mBar.setFocusable(false);
    	mBar.setEnabled(false);
    	mBar.setRating(mProduct.getmCommentRating()/20);
    	mTextView.setText(mActivity.getString(R.string.list_text,mProduct.getmCommentReputably()));
    	setTitleText(mActivity.getString(R.string.rate_title)+"  ("+mProduct.getmCommentCount()+")  ");
    	mAdapter=new RateListAdapter(mActivity, mList);
    	mListView.setAdapter(mAdapter);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.COMMENT_URL);
        url.setmGetParamPrefix(JsonKey.AdvertisementKey.GOODSID).setmGetParamValues(mProduct.getmId());
        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
        url.setmGetParamPrefix(JsonKey.MyOrderKey.PAGE).setmGetParamValues(mPage+"");
        param.setmHttpURL(url);
        param.setmParserClassName(RateListParser.class.getName());
        RequestManager.getRequestData(mActivity, createReqSuccessListener(), createReqErrorListener(), param);
	
	}
	 private Response.Listener<Object> createReqSuccessListener() {
			return new Listener<Object>() {
				@Override
				public void onResponse(Object object) {
					AppLog.Logd(object.toString());
					ArrayList<Rate> list=(ArrayList<Rate>) object;
	                mList.clear();
	                mList.addAll(list);
	                if (list == null || list.size() == 0) {
	                	
					} else if (list.size() < 10) {
						mListView.setVisibility(View.VISIBLE);
					    mTvContent.setVisibility(View.GONE);
						mListView.setPullLoadEnable(false);
					} else {
						mListView.setVisibility(View.VISIBLE);
						mTvContent.setVisibility(View.GONE);
						mListView.setPullLoadEnable(true);
					}
					mListView.stopLoadMore();
					mListView.stopRefresh();
					if (getActivity() == null || isDetached()) {
						return;
					}
					if (!isDetached()) {
						mRateListHandler.removeMessages(MSG_LIST);
						mRateListHandler.sendEmptyMessage(MSG_LIST);
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
					if (getActivity() == null || isDetached()) {
						return;
					}
					AppLog.Loge(" data failed to load" + error.getMessage());
					if (!isDetached()) {
						mLoadHandler.removeMessages(Constant.NET_STATUS_NODATA);
						mLoadHandler.sendEmptyMessage(Constant.NET_STATUS_NODATA);
					}
				}
			};
		}
		private void setNoData(){
			mListView.setVisibility(View.GONE);
	        mTvContent.setVisibility(View.VISIBLE);
	        mTvContent.setText(getResources().getString(R.string.rate_no_text));
		}

		@Override
		public void onRefresh() {
			mRateListHandler.postDelayed(new Runnable() {
				
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
			mRateListHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mPage += 1;
					requestData();
				}
			}, 0);
		}

}
