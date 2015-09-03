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
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.VoucherParser;
import com.techfly.liutaitai.model.pcenter.activities.MyVoucherActivity;
import com.techfly.liutaitai.model.pcenter.adapter.VoucherAdapter;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.pcenter.bean.Voucher;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyVoucherFragment extends CommonFragment implements IXListViewListener{
	private MyVoucherActivity mActivity;
	private User mUser;
	private XListView mListView;
	private int mPage = 1;
	private int mSize = 10;
	private VoucherAdapter mAdapter;
	private TextView mHint;
	private boolean isRefresh = false;
	private ArrayList<Voucher> mList = new ArrayList<Voucher>();
	private final int MSG_LIST = 0x101;
	private Handler mVoucherHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				mListView.setVisibility(View.VISIBLE);
				mHint.setVisibility(View.GONE);
				if (mList.size() == 0) {
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
        mActivity = (MyVoucherActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher,
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
    	setTitleText(R.string.pcenter_voucher);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mListView = (XListView) view.findViewById(R.id.voucher_list);
    	mHint = (TextView) view.findViewById(R.id.voucher_hint);
    	
    	
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.VOUCHER_URL);
		url.setmGetParamPrefix(JsonKey.BalanceKey.PAGE)
		.setmGetParamValues(mPage + "")
		;
		url.setmGetParamPrefix(JsonKey.BalanceKey.SIZE).setmGetParamValues(mSize + "");
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(VoucherParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				ArrayList<Voucher> list= (ArrayList<Voucher>) object;
				if(isRefresh){
					mList.addAll(list);
				}else{
					mList.clear();
					mList.addAll(list);
				}
				if (list == null || list.size() == 0) {

				} else if (list.size() < 10) {
					mListView.setVisibility(View.VISIBLE);
					mHint.setVisibility(View.GONE);
					mListView.setPullLoadEnable(false);
				} else {
					mListView.setVisibility(View.VISIBLE);
					mHint.setVisibility(View.GONE);
					mListView.setPullLoadEnable(true);
				}
				mListView.stopLoadMore();
				mListView.stopRefresh();
				AppLog.Loge("xll", object.toString());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					mVoucherHandler.removeMessages(MSG_LIST);
                    mVoucherHandler.sendEmptyMessage(MSG_LIST);
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
		mHint.setVisibility(View.VISIBLE);
		mHint.setText(R.string.voucher_hint);
	}
	@Override
	public void onRefresh() {
		mVoucherHandler.postDelayed(new Runnable() {

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
		mVoucherHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				isRefresh = true;
				mPage += mSize;
				requestData();
			}
		}, 0);
	}

}
