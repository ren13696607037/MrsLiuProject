package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.OrderSearchParser;
import com.techfly.liutaitai.bizz.parser.OrderYTSearchParser;
import com.techfly.liutaitai.model.pcenter.activities.SearchLogisticsActivity;
import com.techfly.liutaitai.model.pcenter.adapter.LogListAdapter;
import com.techfly.liutaitai.model.pcenter.bean.Logistics;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.ACache;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.MD5;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class SearchLogisticsFragment extends CommonFragment {
	private ACache mCache;
	private SearchLogisticsActivity mActivity;
	private TextView mTvName;
	private TextView mTvNum;
	private MyOrder mOrder;
	private Logistics mLogistics=null;
	private ListView mListView;
	private LogListAdapter mAdapter;
	private TextView mTvText;
	private TextView mTvError;
	public Handler logHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
				setView();
		}
		
	};
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AppLog.Logd("Fly", "onAttach................");
        mActivity=(SearchLogisticsActivity) activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCache = ACache.get(mActivity);
        mOrder=(MyOrder) mActivity.getIntent().getSerializableExtra(IntentBundleKey.ORDER_ID);
        mLogistics=(Logistics) mCache.getAsObject(mOrder.getmId());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_searchlog, container, false);
       return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
        if(mLogistics!=null){
        	setView();
        }else{
        	if(mOrder.getmExpNo()!=null&&!TextUtils.isEmpty(mOrder.getmExpNo())&&!"null".equals(mOrder.getmExpNo())){
        		startReqTask(SearchLogisticsFragment.this);
        	}else{
        		setNoData();
        	}
        }
    }
    private void onInitView(View view){
    	setTitleText(R.string.log_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mTvName=(TextView) view.findViewById(R.id.log_name);
    	mTvNum=(TextView) view.findViewById(R.id.log_num);
    	mListView=(ListView) view.findViewById(R.id.log_list);
    	mTvText=(TextView) view.findViewById(R.id.log_pay);
    	mTvError=(TextView) view.findViewById(R.id.log_no_content);
    	
    }
    private void setView(){
    	setData();
    	if(mLogistics!=null&&mLogistics.getmList()!=null){
    		mAdapter=new LogListAdapter(mActivity, mLogistics.getmList());
    	}else{
    		mTvError.setVisibility(View.VISIBLE);
    		mTvError.setText(R.string.log_text2);
    	}
    	mListView.setAdapter(mAdapter);
    	
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
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		String no=mOrder.getmExpNo();
//		String no="718641764096";//中通测试帐号
//		String no="500005261644";//圆通测试帐号
		
		String shortName=mOrder.getmExpShortName();
		String name =mOrder.getmExpCom();
		if("ZTO".equals(shortName) || "中通快递".equals(name) ){
			url.setmBaseUrl(Constant.ZTO_URL+no);
			param.setmParserClassName(OrderSearchParser.class.getName());
		}else if("YTO".equals(shortName)||"YT".equals(shortName) ||"圆通速递".equals(name)  ){
//			url.setmBaseUrl("http://www.hylapp.com:10001/apis/api-transffer/yto");
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.YTO_JAVA_URL);
			String  time=Utility.getSimpDate();
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.TIMESTAMP).setmGetParamValues(time);
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.SIGN).setmGetParamValues(MD5.getDigest("lF3cA3app_key6uRGEXformatjsonmethodyto.Marketing.WaybillTracetimestamp"+time+"user_idyhmallv1.0"));
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.APPKEY).setmGetParamValues("6uRGEX");
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.FORMAT).setmGetParamValues("json");
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.METHOD).setmGetParamValues("yto.Marketing.WaybillTrace");
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.USERID).setmGetParamValues("yhmall");
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.V).setmGetParamValues("1.0");
			url.setmGetParamPrefix(JsonKey.OrderSearchKey.PARAM).setmGetParamValues("[{\"Number\":\""+no+"\"}]");
			param.setmParserClassName(OrderYTSearchParser.class.getName());
			param.setPostRequestMethod();
		}
		param.setmHttpURL(url);
		
		RequestManager
				.getRequestData(getActivity(), createSearchReqSuccessListener(),
						createSearchReqErrorListener(), param);
	}
	private Response.Listener<Object> createSearchReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (getActivity() == null || isDetached()) {
					return;
				}
				mLogistics=(Logistics) object;
				mCache.put(mOrder.getmId(), mLogistics, 60 * 30);
				if (!isDetached()) {
					logHandler.removeMessages(1);
					logHandler.sendEmptyMessage(1);
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}

	private Response.ErrorListener createSearchReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (getActivity() == null || isDetached()) {
					return;
				}
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					setView();
					mLoadHandler.removeMessages(Constant.NET_STATUS_NODATA);
					mLoadHandler.sendEmptyMessage(Constant.NET_STATUS_NODATA);
				}
			}
		};
	}
	private void setNoData(){
		mTvText.setVisibility(View.VISIBLE);
		if(mLogistics!=null&&mLogistics.getmReason()!=null&&!TextUtils.isEmpty(mLogistics.getmReason())){
			mTvName.setVisibility(View.GONE);
			mTvNum.setVisibility(View.GONE);
			mTvText.setText(mLogistics.getmReason());
			mListView.setVisibility(View.INVISIBLE);
		}else{
			mTvName.setVisibility(View.GONE);
			mTvNum.setVisibility(View.GONE);
			mTvText.setText(R.string.log_text2);
			mListView.setVisibility(View.INVISIBLE);
		}
	}
	private void setData(){
		mTvName.setText(getString(R.string.log_text1,mOrder.getmExpCom()));
    	mTvNum.setText(getString(R.string.order_sn,mOrder.getmExpNo()));
    	mTvText.setText(getString(R.string.log_text,"在线支付"));
    	mTvName.setText(Utility.setText(mActivity, mTvName.getText().toString(), 4, mTvName.getText().toString().length()));
    	mTvNum.setText(Utility.setText(mActivity, mTvNum.getText().toString(), 4, mTvNum.getText().toString().length()));
    	mTvText.setText(Utility.setText(mActivity, mTvText.getText().toString(), 5, mTvText.getText().toString().length()));
	}

}
