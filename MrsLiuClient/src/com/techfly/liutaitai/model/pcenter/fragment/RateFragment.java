package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.OrderDetailParser;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.activities.RateActivity;
import com.techfly.liutaitai.model.pcenter.adapter.RateAdapter;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.Rate;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.ListViewForScrollView;

public class RateFragment extends CommonFragment {
	private RateActivity mActivity;
	private TextView mTextView;
	private ListViewForScrollView mListViewForScrollView;
	private Button mButton;
	private ArrayList<Rate> mList=new ArrayList<Rate>();
	private RateAdapter mAdapter;
	private MyOrder mOrder;
	private final static int MSG_RATE=0x101;
	private String mJson = null;
	private User mUser;
	public Handler mRateHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RATE:
				setView();
				break;

			default:
				break;
			}
		}
		
	};
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(RateActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrder=(MyOrder) mActivity.getIntent().getSerializableExtra(IntentBundleKey.ORDER_ID);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
        startReqTask(RateFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
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
    	setTitleText(R.string.rate);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mTextView=(TextView) view.findViewById(R.id.rate_time);
    	mListViewForScrollView=(ListViewForScrollView) view.findViewById(R.id.rate_list);
    	mButton=(Button) view.findViewById(R.id.rate_btn);
    	
    	setJsonData();
    	
    	mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toRate();
			}
		});
    	
    }
    private void toRate(){
    	RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.COMMENT_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(mUser.getmId());
//        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("2");
        url.setmGetParamPrefix(JsonKey.UserKey.COMMENT).setmGetParamValues(mJson);
        url.setmGetParamPrefix(JsonKey.MyOrderKey.ID).setmGetParamValues(mOrder.getmId());
        param.setmHttpURL(url);
        param.setPostRequestMethod();
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(mActivity, createReqSuccessListener(), createReqErrorListener(), param);
	
    }
    private Response.Listener<Object> createReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				ResultInfo info = (ResultInfo) object;
				if (getActivity() == null || isDetached()) {
					return;
				}
				if (!isDetached()) {
					if (info.getmCode() == 0) {
						SmartToast.makeText(mActivity, R.string.common_success, Toast.LENGTH_SHORT).show();
						mActivity.finish();
					} else {
						SmartToast.makeText(mActivity, info.getmMessage(), Toast.LENGTH_SHORT).show();
					}
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
    private void setView(){
    	mTextView.setText(mActivity.getString(R.string.rate_text,mOrder.getmTime()));
    	Product2Rate(mOrder);
    	mAdapter=new RateAdapter(mActivity, mList);
    	mListViewForScrollView.setAdapter(mAdapter);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.MYORDER_LIST_URL+"/"+mOrder.getmId());
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues(mUser.getmId());
//        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL).setmGetParamValues("2");
        param.setmHttpURL(url);
        param.setmParserClassName(OrderDetailParser.class.getName());
        RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
	
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mOrder=(MyOrder) object;
                if(!isDetached()){
                	mRateHandler.removeMessages(MSG_RATE);
                	mRateHandler.sendEmptyMessage(MSG_RATE);
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
	private ArrayList<Rate> Product2Rate(MyOrder order){
		if(order!=null&&order.getmList()!=null){
			ArrayList<Product> list=order.getmList();
			int size=list.size();
			for(int i=0;i<size;i++){
				Rate rate=new Rate();
				Product product=list.get(i);
				rate.setmProductId(product.getmId());
				rate.setmProductImage(product.getmImg());
				rate.setmProductName(product.getmName());
				mList.add(rate);
			}
		}
		return mList;
	}
	
	private void setJsonData() {
		boolean isSet = true;
		try {
			JSONArray array = new JSONArray();
			for (int i = 0; i < mList.size(); i++) {
				Rate rate = mList.get(i);
				LinearLayout layout = (LinearLayout) mListViewForScrollView.getChildAt(i);
				EditText et = (EditText) layout.findViewById(R.id.irate_content);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("goodsId", rate.getmProductId());
				jsonObject.put("content", et.getText().toString());
				jsonObject.put("starScore", rate.getmScore());
				array.put(jsonObject);
			}
			if (isSet) {
				mJson = new JSONStringer().object().key("commentJson").value(array).endObject().toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
