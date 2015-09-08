package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.AddressManageParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.adapter.AddManageAdapter;
import com.techfly.liutaitai.model.pcenter.adapter.AddManageAdapter.OnDeleteListener;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
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

public class AddressManageFragment extends CommonFragment {
	private AddressManageActivity mActivity;
	private LinearLayout mButton;
	private XListView mListView;
	private ArrayList<AddressManage> mList=new ArrayList<AddressManage>();
    private AddManageAdapter mAdapter;
    private AddressManage mAddManage;
    private int mFromOrder=-1;
    private ResultInfo mInfo;
    private TextView mTextView;
    private final int MSG_LIST=0x101;
    private final int MSG_DEFAULT=0x102;
    private String mAddressId=null;
    private boolean isDefault=false;
    private int mPage = 1;
	private int mSize = 10;
    private User mUser;
    private boolean isRefresh = false;
    public Handler mAddressHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				mListView.setVisibility(View.VISIBLE);
				mTextView.setVisibility(View.GONE);
				if(mList.size()==0){
					setNoData();
				}
				mAdapter.updateList(mList);
				break;
			case MSG_DEFAULT:
				isDefault=false;
				if(mInfo.getmCode()==0){
					startReqTask(AddressManageFragment.this);
				}else{
					showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
				}
				break;

			default:
				break;
			}
		}
    	
    };
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(AddressManageActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        mFromOrder=mActivity.getIntent().getIntExtra(IntentBundleKey.ADDRESS_EXTRA, -1);
        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
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
    	setTitleText(R.string.address_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mButton=(LinearLayout) view.findViewById(R.id.address_new);
    	mListView=(XListView) view.findViewById(R.id.address_list);
    	mTextView=(TextView) view.findViewById(R.id.address_no_content);
    	mAdapter=new AddManageAdapter(mActivity, mList, AddressManageFragment.this);
    	mListView.setAdapter(mAdapter);
    	mListView.setPullLoadEnable(false);
    	mListView.setPullRefreshEnable(false);
    	
    	mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mActivity,ChangeAddressActivity.class);
                startActivityForResult(intent,Constant.ADDRESS_INTENT);
			}
		});
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mAddManage=(AddressManage) arg0.getAdapter().getItem(arg2);
				if(mFromOrder!=-1){
					mActivity.setResult(mFromOrder+1, new Intent().putExtra(IntentBundleKey.ADDRESS_EXTRA, mAddManage));
					mActivity. finish();   
				}else{
					if(!mAddManage.isDefault()){
						isDefault=true;
						mAddressId=mAddManage.getmId();
				    	startReqTask(AddressManageFragment.this);
				    }
				}
			}
		});
    	mAdapter.setListener(new OnDeleteListener() {
			
			@Override
			public void onDelete() {
				startReqTask(AddressManageFragment.this);
			}
		});
    }
	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		if(isDefault){
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.ADDRESS_DEFAULT_URL);
			url.setmGetParamPrefix(JsonKey.AddressKey.AID).setmGetParamValues(mAddressId);
			param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setmHttpURL(url);
			param.setPostRequestMethod();
			param.setmParserClassName(CommonParser.class.getName());
			RequestManager.getRequestData(mActivity, createReqSuccessListener(), createReqErrorListener(), param);
		}else{
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
					+ Constant.ADDRESS_URL);
			url.setmGetParamPrefix(JsonKey.BalanceKey.PAGE)
			.setmGetParamValues(mPage + "")
			;
			url.setmGetParamPrefix(JsonKey.BalanceKey.SIZE).setmGetParamValues(mSize + "");
			param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setmHttpURL(url);
			param.setmParserClassName(AddressManageParser.class.getName());
			RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
		}
		
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ArrayList<AddressManage> list= (ArrayList<AddressManage>) object;
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
                if(!isDetached()){
                	mAddressHandler.removeMessages(MSG_LIST);
                	mAddressHandler.sendEmptyMessage(MSG_LIST);
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
	private Response.Listener<Object> createReqSuccessListener() {
        return new Listener<Object>() {

            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mInfo=(ResultInfo) object;
                if(mFromOrder!=-1){
                    if(getActivity()!=null && !isDetached()){
                        getActivity().setResult(mFromOrder+1, new Intent().putExtra(IntentBundleKey.ADDRESS_EXTRA, mAddManage));
                        getActivity(). finish();   
                    }
                    return;
                }
                if(getActivity()!=null &&!isDetached()){
                	mAddressHandler.removeMessages(MSG_DEFAULT);
                	mAddressHandler.sendEmptyMessage(MSG_DEFAULT);
                }
            }
        };
    }

    private Response.ErrorListener createReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
            }
       };
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode==Constant.AD_MANAGE_SUCCESS){
    		startReqTask(AddressManageFragment.this);
    	}else if(resultCode==Constant.ADDRESS_SUCCESS){
    		startReqTask(AddressManageFragment.this);
    	}
    }
	private void setNoData(){
		mListView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(getResources().getString(R.string.address_no_content));
	}

}
