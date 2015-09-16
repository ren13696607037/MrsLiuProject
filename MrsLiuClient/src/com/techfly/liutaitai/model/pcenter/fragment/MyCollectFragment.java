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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.CommonProListParser;
import com.techfly.liutaitai.bizz.parser.TechOrderParser;
import com.techfly.liutaitai.model.mall.activities.ServiceInfoActivity;
import com.techfly.liutaitai.model.mall.adapter.ProItemAdapter;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.adapter.MyCollectAdapter;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.ManagerListener.CollectListener;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyCollectFragment extends CommonFragment implements IXListViewListener, CollectListener{
	private XListView mListView;
	private ArrayList<Product> mList =new ArrayList<Product>();
	private MyCollectAdapter mAdapter;
	private TextView mEmptyTv;
	private int mPage = 1;
	private int mSize = 10;
	private TextView mTextView;
	private User mUser;
	private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	mListView.setVisibility(View.VISIBLE);
        	mEmptyTv.setVisibility(View.GONE);
			if(mList.size()==0){
				setNoData();
			}
			mAdapter.updateList(mList);
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
        ManagerListener.newManagerListener().onRegisterCollectListener(this);
        startReqTask(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycollect, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerListener.newManagerListener().onUnRegisterCollectListener(this);
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
    	setTitleText(R.string.pcenter_collect);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mListView=(XListView) view.findViewById(R.id.collect_list);
    	mListView.setPullLoadEnable(false);
    	mListView.setPullRefreshEnable(true);
    	mEmptyTv = (TextView) view.findViewById(R.id.empty);
        mAdapter=new MyCollectAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setXListViewListener(this);
    	mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Product pro =  (Product) arg0.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ServiceInfoActivity.class);
				intent.putExtra(IntentBundleKey.ID, pro.getmOrderNum());
				startActivity(intent);
            }
        });
    	mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Product pro =  (Product) parent.getItemAtPosition(position);
				ManagerListener.newManagerListener().notifyCancelCollectListener(pro);
				return true;
			}
		});
    }

    private Response.Listener<Object> createCollectSuccessListener() {
        return new Listener<Object>() {

            @Override
            public void onResponse(Object result) {
                AppLog.Logd(result.toString());
                AppLog.Loge(" data success to load" + result.toString());
                if(getActivity()!=null&&!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                    ResultInfo rInfo = (ResultInfo) result;
                    if(rInfo.getmCode()==Constant.RESULT_CODE){
                        showSmartToast(R.string.collect_cancel_success, Toast.LENGTH_SHORT);
                        mList.clear();
                        startReqTask(MyCollectFragment.this);
                    }else{
                        showSmartToast(rInfo.getmMessage(), Toast.LENGTH_LONG);
                    }
                }
            }
        };
    }

    private Response.ErrorListener createCollectErrorListener() {
        return new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                if (getActivity() == null || isDetached()) {
                    return;
                }
                mLoadHandler.removeMessages(Constant.NET_FAILURE);
                mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
               
            }

        };
    }

    protected void updateCollectEdit() {
        for(Product product:mList){
            product.setmEditable(true);
        }
        mAdapter.notifyDataSetChanged();
        
    }

    @Override
    public void requestData() {
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_COLLECT);
        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
        url.setmGetParamPrefix(JsonKey.VoucherKey.PAGE).setmGetParamValues(mPage+"");
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(CommonProListParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), creatReqSuccessListener(), createErrorListener(), param);
    }
    private Response.Listener<Object> creatReqSuccessListener() {
        return new Listener<Object>() {

            @Override
            public void onResponse(Object result) {
                AppLog.Logd(result.toString());
                AppLog.Loge(" data success to load" + result.toString());
                if(getActivity()!=null&&!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                    ResultInfo rInfo = (ResultInfo) result;
                    if(rInfo.getmCode()==Constant.RESULT_CODE){
                        setRightText(getString(R.string.editable_txt));
                        if(rInfo.getObject()!=null){
                            ArrayList<Product> list =(ArrayList<Product>) rInfo.getObject();
                            mListView.stopRefresh();
                            if(list.size()==0){
                                mLoadingLayout.setVisibility(View.GONE);
                                mEmptyTv.setVisibility(View.VISIBLE);
                                setRightText("");
                            }else{
                                mLoadingLayout.setVisibility(View.VISIBLE);
                                mEmptyTv.setVisibility(View.GONE);
                            }
                            mList.addAll(list);
                            mHandler.sendEmptyMessage(0);
                        }else{
                            mList.clear();
                            requestData();
                        }
                    }else{
                        showSmartToast(rInfo.getmMessage(), Toast.LENGTH_LONG);
                    }
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener() {
        return new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                if (getActivity() == null || isDetached()) {
                    return;
                }
                mLoadHandler.removeMessages(Constant.NET_FAILURE);
                mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
               
            }

        };
    }

    @Override
    public void onRefresh() {
        mList.clear();
        requestData();
    }

    @Override
    public void onLoadMore() {
        
    }
    private void setNoData(){
		mListView.setVisibility(View.GONE);
		mEmptyTv.setVisibility(View.VISIBLE);
		mEmptyTv.setText(getResources().getString(R.string.collect_no_content));
	}

	@Override
	public void cancelCollect(Product product) {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.COLLECT_SERVICE_URL);
        url.setmGetParamPrefix(JsonKey.ServiceDetailKey.SID).setmGetParamValues(product.getmOrderNum());
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), createCollectSuccessListener(), createCollectErrorListener(), param);
	}

	@Override
	public void collect(Product product) {
		mList.clear();
		requestData();
	}
}
