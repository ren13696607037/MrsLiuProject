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
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bizz.parser.CityListParser;
import com.hylsmart.yihui.bizz.parser.CityListsParser;
import com.hylsmart.yihui.bizz.parser.ProvinceListParser;
import com.hylsmart.yihui.model.pcenter.activities.CityListActivity;
import com.hylsmart.yihui.model.pcenter.activities.ProvinceActivity;
import com.hylsmart.yihui.model.pcenter.adapter.CityAdapter;
import com.hylsmart.yihui.model.pcenter.adapter.ProvinceAdapter;
import com.hylsmart.yihui.model.pcenter.bean.Area;
import com.hylsmart.yihui.model.pcenter.bean.City;
import com.hylsmart.yihui.model.pcenter.bean.Province;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.ManagerListener;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class ProvinceFragment extends CommonFragment {
	private ProvinceActivity mActivity;
	private ListView mListView;
	private ArrayList<Province> mList=new ArrayList<Province>();
	private ProvinceAdapter mAdapter;
	private final int MSG_LIST=0x101;
	public Handler mProvinceHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
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
        mActivity=(ProvinceActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startReqTask(ProvinceFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
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
    	mListView=(ListView) view.findViewById(R.id.province_list);
    	mAdapter=new ProvinceAdapter(mActivity, mList);
    	mListView.setAdapter(mAdapter);
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Province province=(Province) parent.getAdapter().getItem(position);
				Intent intent=new Intent(mActivity,CityListActivity.class);
				intent.putExtra(IntentBundleKey.PROVINCE, province);
				startActivityForResult(intent, Constant.CITY_INTENT);
			}
		});
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PROVINCE_URL);
        param.setmHttpURL(url);
        param.setmParserClassName(ProvinceListParser.class.getName());
        RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
       
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mList=(ArrayList<Province>) object;
                if(!isDetached()){
                	mProvinceHandler.removeMessages(MSG_LIST);
                	mProvinceHandler.sendEmptyMessage(MSG_LIST);
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
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode==Constant.CITY_SUCCESS){
    		mActivity.setResult(Constant.PRO_SUCCESS, data);
    		mActivity.finish();
    	}
    }
    

}
