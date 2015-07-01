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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bizz.parser.CityListParser;
import com.hylsmart.yihui.bizz.parser.CityListsParser;
import com.hylsmart.yihui.model.pcenter.activities.CityListActivity;
import com.hylsmart.yihui.model.pcenter.adapter.CityAdapter;
import com.hylsmart.yihui.model.pcenter.bean.Area;
import com.hylsmart.yihui.model.pcenter.bean.City;
import com.hylsmart.yihui.model.pcenter.bean.Province;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.ManagerListener;
import com.hylsmart.yihui.util.ManagerListener.CityUpdateListener;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class CitysListFragment extends CommonFragment implements CityUpdateListener,OnChildClickListener,OnGroupClickListener,OnGroupExpandListener{
	private CityListActivity mActivity;
	private ExpandableListView mListView;
	private ArrayList<Area> mList=new ArrayList<Area>();
	private CityAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private Province mProvince;
	public Handler mCityHandler=new Handler(){

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
        mActivity=(CityListActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProvince=(Province) mActivity.getIntent().getSerializableExtra(IntentBundleKey.PROVINCE);
        startReqTask(CitysListFragment.this);
        ManagerListener.newManagerListener().onRegisterCityUpdateListener(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citylist, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerListener.newManagerListener().onUnRegisterCityUpdateListener(this);
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
    	setTitleText(R.string.add_change_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mListView=(ExpandableListView) view.findViewById(R.id.city_list);
    	mAdapter=new CityAdapter(mActivity, mList);
    	mListView.setAdapter(mAdapter);
    	mListView.setOnChildClickListener(this);
    	mListView.setOnGroupClickListener(this);
    	mListView.setOnGroupExpandListener(this);
    }

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CITY_URL);
        url.setmGetParamPrefix(JsonKey.AddressKey.PROVINCE).setmGetParamValues(mProvince.getmId());
        param.setmHttpURL(url);
        param.setmParserClassName(CityListParser.class.getName());
        RequestManager.getRequestData(mActivity, createMyReqSuccessListener(), createMyReqErrorListener(), param);
       
	}
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mList=(ArrayList<Area>) object;
                if(!isDetached()){
                	mCityHandler.removeMessages(MSG_LIST);
                	mCityHandler.sendEmptyMessage(MSG_LIST);
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
    private void getChild(Area area){
    	RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CITY_URL+"/"+area.getmId());
        param.setmHttpURL(url);
        param.setmParserClassName(CityListsParser.class.getName());
        RequestManager.getRequestData(mActivity, createReqSuccessListener(area), createReqErrorListener(), param);
       
    }
    private Response.Listener<Object> createReqSuccessListener(final Area area) {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ArrayList<City> list=(ArrayList<City>) object;
                mList.get(mList.indexOf(area)).setmList(list);
                if(!isDetached()){
                	mCityHandler.removeMessages(MSG_LIST);
                	mCityHandler.sendEmptyMessage(MSG_LIST);
                }
            }
        };
    }

    private Response.ErrorListener createReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                if(!isDetached()){
                }
            }
       };
    }

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		City city=(City) mListView.getExpandableListAdapter().getChild(groupPosition, childPosition);
		Area area=(Area) mListView.getExpandableListAdapter().getGroup(groupPosition);
		Intent intent=new Intent();
		intent.putExtra(IntentBundleKey.CITY, city);
		intent.putExtra(IntentBundleKey.AREA, area);
		intent.putExtra(IntentBundleKey.PROVINCE, mProvince);
		mActivity.setResult(Constant.CITY_SUCCESS, intent);
		mActivity.finish();
		return false;
	}


	@Override
	public void onUpdateListener(Area area) {
		getChild(area);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		int size=mListView.getExpandableListAdapter().getGroupCount();
		for(int i=0;i<size;i++){
			if(groupPosition!=i){
				mListView.collapseGroup(i);
			}
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if(!parent.isGroupExpanded(groupPosition)){
			Area area=(Area) mListView.getExpandableListAdapter().getGroup(groupPosition);
			ManagerListener.newManagerListener().notifyUpdateList(area);
		}else{
			mListView.collapseGroup(groupPosition);
		}
		return false;
	}

}
