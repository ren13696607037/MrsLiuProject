package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.GroupBean;
import com.techfly.liutaitai.model.mall.adapter.GroupListAdapter;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.LoadingLayout;
import com.techfly.liutaitai.util.view.XListView;

public class GroupListFragment extends CommonFragment {

	private Activity mActivity;
	private LoadingLayout mLoadingLayout;
	private XListView mListView;
	private GroupListAdapter mAadapter;
	private List<GroupBean> mDatas;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initView(view);
//		requestData();
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mLoadingLayout = (LoadingLayout)view.findViewById(R.id.loading_layout);
		mListView = (XListView)view.findViewById(R.id.xlistview);
		mDatas = new ArrayList<GroupBean>();
		GroupBean group = new GroupBean();
		group.setMarketPrice("9999999");
		group.setName("��Ͳϴ�»�");
		group.setPrice("666666");
		mDatas.add(group);
		mDatas.add(group);
		mDatas.add(group);
		mDatas.add(group);
		mAadapter = new GroupListAdapter(mActivity,mDatas);
		mListView.setAdapter(mAadapter);
		mLoadingLayout.dissmissLoadingLayout();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_group_list, container, false);
	}

	@Override
	public void requestData() {
		// TODO Auto-generated method stub
		mLoadingLayout.showLoading();
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_GROUP);
		param.setmHttpURL(url);
		RequestManager.getRequestData(getActivity(), CreatReqSuccessListener(), CreateErrorListener(), param);
	}
	private Response.Listener<Object> CreatReqSuccessListener() {
		return new Listener<Object>() {

			@Override
			public void onResponse(Object result) {
				AppLog.Logd(result.toString());
				AppLog.Loge(" data success to load" + result.toString());
				mLoadingLayout.dissmissLoadingLayout();
			}
		};
	}

	private Response.ErrorListener CreateErrorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (getActivity() == null || isDetached()) {
					return;
				}
				mLoadingLayout.showMessage("��ȡ���ʧ��", R.drawable.icon_error);
			}

		};
	}

}
