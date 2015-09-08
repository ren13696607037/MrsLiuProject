package com.techfly.liutaitai.util.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.techfly.liutaitai.MainActivity;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.CityListParser;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.activities.CitySelectActivity;
import com.techfly.liutaitai.util.adapter.CitySortAdapter;
import com.techfly.liutaitai.util.view.CharacterParser;
import com.techfly.liutaitai.util.view.PinyinComparator;
import com.techfly.liutaitai.util.view.SearchView;
import com.techfly.liutaitai.util.view.SideBar;
import com.techfly.liutaitai.util.view.SideBar.OnTouchingLetterChangedListener;
import com.techfly.liutaitai.util.view.SortModel;

public class CitySelectFragment extends CommonFragment {
    private ListView mSortListView;
    private SideBar mSideBar;
    private TextView mDialog;
    private CitySortAdapter mAdapter;
    private SearchView mClearEditText;
    private CharacterParser mCharacterParser;
    private List<SortModel> mSortedCityList;
    private PinyinComparator mPinyinComparator;
    private List<Area> mCityList = new ArrayList<Area>();
    public final static int MESSAGE_CITY_REQUEST_FINISH =101;
    private CitySelectActivity mActivity;
    public LocationClient mLocationClient = null;
    private String mLatitude;
    private String mLongitude;
    private String mLocCity;
    private TextView mLocCityTv;
    private boolean mIsFromMainActivity = false;
    public BDLocationListener myListener = new MyLocationListener();
    private Handler mHandler = new Handler(){
        
        @Override
        public void handleMessage(Message msg) {
           int what =  msg.what;
           switch (what) {
           case MESSAGE_CITY_REQUEST_FINISH:
               mSortedCityList = filledData(mCityList);
               Collections.sort(mSortedCityList, mPinyinComparator);
               mAdapter.updateListView(mSortedCityList);
               mLocCityTv.setText(mLocCity);
               mLocCityTv.setEnabled(true);
            break;
        default:
            break;
        }
        }
        
    };
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Utility.getScreenSize(activity);
        mActivity=(CitySelectActivity)activity;
        mIsFromMainActivity = mActivity.getIntent().getBooleanExtra(IntentBundleKey.REDIRECT_TYPE, false);
  
    }
    
    public void onBackPressed(){
    	if(SharePreferenceUtils.getInstance(mActivity).isFirst()){
    	    getActivity().setResult(-101);
    	}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        initLocationOption();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
        startReqTask(this) ;
    }
    private void initLocationOption() {
        // TODO Auto-generated method stub
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
//        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }
    private  class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null){
                return ;
            }else{
                 mLatitude = String.valueOf(location.getLatitude());
                 mLongitude =String.valueOf(location.getLongitude());
                 mLocCity = String.valueOf(location.getCity());
                if(!TextUtils.isEmpty(mLocCity)&&!mLocCity.equals("null")){
                    //
                	AppLog.Logd("Fly", "  mLocCity ==="+  mLocCity );
                	if(mCityList!=null&&mCityList.size()>0){
                		mLocCityTv.setText(mLocCity);
                	}
                	
                	mLocCityTv.setOnClickListener(new View.OnClickListener() {
               			
               			@Override
               			public void onClick(View view) {
               			   Area area = new Area();
                           area.setmName(mLocCity);
                           if(mCityList.contains(area)){
                            area = mCityList.get(mCityList.indexOf(area));
                            SharePreferenceUtils.getInstance(getActivity()).saveArea(area);
                            SharePreferenceUtils.getInstance(getActivity()).saveIsFirst(false);
                           }
                           else{
                               showSmartToast(R.string.select_city_location_disable, Toast.LENGTH_LONG);
                           }
               			 if(mIsFromMainActivity){
                             Intent intent =   getActivity().getIntent().putExtra(IntentBundleKey.LOCCITY,mLocCity);
                             getActivity().setResult(mActivity.RESULT_OK,intent );
                             getActivity(). finish();
                         }else{
                             if(!TextUtils.isEmpty(mLocCity)){
                                 Intent intent = new Intent(getActivity(),MainActivity.class);
                                 startActivity(intent);
                                 getActivity(). finish();  
                             }else{
                                 showSmartToast("定位中，请稍等！", Toast.LENGTH_LONG);
                             }
                             
                         }
               			}
               		});
                }else{
                	mLocCityTv.setText("定位失败" );
                	mLocCityTv.setEnabled(false);
                }
             
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_select, container, false);
        return view;
    }
    


    @Override
    public void onDestroy() {
        if(null!=mCityList){
            mCityList.clear();
            mCityList =null;
        }
        if(mLocationClient!=null){
        	mLocationClient.stop();
        	mLocationClient.unRegisterLocationListener(myListener);
         
        }
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
        onInitTitleView();
    }
 
    private void onInitTitleView() {
       if(SharePreferenceUtils.getInstance(getActivity()).isFirst()){
    	      setLeftHeadIcon(-1);
       }
       setTitleText(R.string.select_city_title_text);
    }

    private void onInitView(View view) {
        // init view for  imageView = findViewById()
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator();
        mSideBar = (SideBar) view.findViewById(R.id.sidrbar);
        mDialog = (TextView) view.findViewById(R.id.dialog);
        mLocCityTv = (TextView) view.findViewById(R.id.location_city);
        mLocCityTv.setEnabled(false);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mSortListView.setSelection(position);
                }
            }
        });
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        mSortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        mSortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                SortModel model =  (SortModel) mAdapter.getItem(position);
                Area area = new Area();
                area.setmId( model.getId()+"");
                area.setmName(model.getName());
                SharePreferenceUtils.getInstance(getActivity()).saveArea(area);
                if(mIsFromMainActivity){
                    Intent intent =   getActivity().getIntent().putExtra(IntentBundleKey.LOCCITY, ((SortModel) mAdapter.getItem(position)).getName());
                    getActivity().setResult(mActivity.RESULT_OK,intent );
                    getActivity(). finish();  
                }else{
                    if(!TextUtils.isEmpty(mLocCity)){
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity(). finish();  
                    }else{
                        showSmartToast("定位中，请稍等！", Toast.LENGTH_LONG);
                    }
                    
                }
            	 
            }
        });
        mSortedCityList = filledData(mCityList);
        Collections.sort(mSortedCityList, mPinyinComparator);
        mAdapter = new CitySortAdapter(getActivity(), mSortedCityList);
        mSortListView.setAdapter(mAdapter);

        mClearEditText = (SearchView) view.findViewById(R.id.filter_edit);
  
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
   
   
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
    private List<SortModel> filledData(List<Area> cityList) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < cityList.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setId(Integer.parseInt(cityList.get(i).getmId()));
            sortModel.setName(cityList.get(i).getmName());
            String pinyin = mCharacterParser.getSelling(cityList.get(i).getmName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mSortedCityList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : mSortedCityList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || mCharacterParser.getSelling(name).startsWith(
                                filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        Collections.sort(filterDateList, mPinyinComparator);
        mAdapter.updateListView(filterDateList);
    }
    
    /**
     * 请求城市名称
     */
    public void requestData(){
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL+Constant.CITY_REQUEST_LIST_URL);
        param.setmHttpURL(url);
        param.setmParserClassName(CityListParser.class.getName());
        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
    }
    
    /**
     * 
     * @return
     */
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                    mCityList = (List<Area>) object;
                    mHandler.removeMessages(MESSAGE_CITY_REQUEST_FINISH);
                    mHandler.sendEmptyMessage(MESSAGE_CITY_REQUEST_FINISH);
                    AppLog.Logd(mCityList.size()+"");
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
        };
    }
    

 
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                mLoadHandler.removeMessages(Constant.NET_STATUS_NODATA);
                mLoadHandler.sendEmptyMessage(Constant.NET_STATUS_NODATA);
            }
        };
    }

    
}
