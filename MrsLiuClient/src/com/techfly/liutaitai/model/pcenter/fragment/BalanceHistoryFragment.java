package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.BalanceHistoryListParser;
import com.techfly.liutaitai.model.pcenter.activities.BalanceHistoryActivity;
import com.techfly.liutaitai.model.pcenter.adapter.BalanceHistoryAdapter;
import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;

public class BalanceHistoryFragment extends CommonFragment {
	private BalanceHistoryActivity mActivity;
	private User mUser;
	private int mPage = 1;
	private int mSize = 10;
	private TextView mPrice;
	private XListView mListView;
	private ArrayList<Balance> mList = new ArrayList<Balance>();
	private BalanceHistoryAdapter mAdapter;
	private TextView mTextView;
	private final int MSG_LIST = 0x101;
	private boolean isRefresh = false;
	private Handler mBalanceHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				Balance balance = new Balance();
		    	balance.setmPrice("金额");
		    	balance.setmTime("时间");
		    	balance.setmSend("详情");
		    	mList.add(0,balance);
				mListView.setVisibility(View.VISIBLE);
				mTextView.setVisibility(View.GONE);
				if (mList.size() == 1) {
					setNoData();
				}
				mAdapter.updateList(mList);
				break;

			default:
				break;
			}
		};
	};
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BalanceHistoryActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_history,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	setTitleText(R.string.balance_text);
    	
    	mPrice = (TextView) view.findViewById(R.id.balance_history_price);
    	mListView = (XListView) view.findViewById(R.id.balance_history_list);
    	mTextView = (TextView) view.findViewById(R.id.balance_history_no_content);
    	mAdapter = new BalanceHistoryAdapter(mActivity, mList);
    	mListView.setAdapter(mAdapter);
    	
    	mPrice.setText(getString(R.string.balance_text1, mUser.getmMoney()));
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.BALANCE_HISTORY_URL);
		url.setmGetParamPrefix(JsonKey.BalanceKey.PAGE)
				.setmGetParamValues(mPage + "")
				;
		url.setmGetParamPrefix(JsonKey.BalanceKey.SIZE).setmGetParamValues(mSize + "");
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(BalanceHistoryListParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Loge("xll", "successlist-=-=-=-=-=-"+object.toString());
				ArrayList<Balance> list = (ArrayList<Balance>) object;
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
				if (!isDetached()) {
					mBalanceHandler.removeMessages(MSG_LIST);
					mBalanceHandler.sendEmptyMessage(MSG_LIST);
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
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}
	private void setNoData() {
		mListView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(R.string.balance_history_no_content);
	}

}
