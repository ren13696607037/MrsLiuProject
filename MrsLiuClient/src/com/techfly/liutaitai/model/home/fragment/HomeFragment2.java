package com.techfly.liutaitai.model.home.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CityListParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.ServiceCategoryParser;
import com.techfly.liutaitai.model.home.adapter.BannerAdapter;
import com.techfly.liutaitai.model.home.bean.Banner;
import com.techfly.liutaitai.model.mall.bean.SortRule;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.activities.CitySelectActivity;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.PullToRefreshLayout;
import com.techfly.liutaitai.util.view.ViewPagerWrapper;

public class HomeFragment2 extends CommonFragment implements OnClickListener{
    private ViewPagerWrapper mViewPagerWrapper;
    private BannerAdapter mBannerAdapter;
    private ArrayList<Banner> mdataBanner = new ArrayList<Banner>();
    private LinearLayout mLinearLayout1;
    private LinearLayout mLinearLayout2;
    private LinearLayout mLinearLayout3;
    private LinearLayout mLinearLayout5;
    private LinearLayout mLinearLayout6;
    private LinearLayout mLinearLayout7;
    private TextView mCityTv;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String mLatitude;
    private String mLongitude;
    private String mLocCity;
    private boolean mIsFirst = true;
    @Override
    public void requestData() {

        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "user/imgs?city="+SharePreferenceUtils.getInstance(getActivity()).getArea().getmId());
       
//      url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
//              JPushInterface.getRegistrationID(getActivity()));
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createBannerReqSuccessListener(),
                        createMyReqErrorListener(), param);
    }
    /**
     * 
     * @return
     */
    private Response.Listener<Object> createBannerReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ResultInfo resultInfo = (ResultInfo) object;
                try {
					JSONArray array = new JSONArray( resultInfo .getmData());
					  for(int i=0 ;i<array.length();i++){
						  JSONObject  obj = array.optJSONObject(i);
						  Banner banner = new Banner();
			              banner.setmImage("http://121.43.158.189/liuTai"+obj.optString("img"));
			               banner.setmId(obj.optInt("id"));
			               mdataBanner.add(banner);
		                }
				        mBannerAdapter = new BannerAdapter(getActivity(), mdataBanner);
				        mViewPagerWrapper.setAdapter(mBannerAdapter);
				} catch (JSONException e) {
					
				}
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadHandler.sendEmptyMessageDelayed(Constant.NET_SUCCESS, 1500);// 停止加载框
        mLocationClient = new LocationClient(getActivity().getApplicationContext()); // 声明LocationClient类
        initLocationOption();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        mLocationClient.start();
    }
    private void initLocationOption() {
        // TODO Auto-generated method stub
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
//        option.setScanSpan(1000*60);// 设置发起定位请求的间隔时间为5000ms
//        option.setOpenGps(false);
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if(mIsFirst){
                mIsFirst = false;
                if (location == null) {
                    mLocCity ="合肥市";
                    return;
                } else {
                    mLatitude = String.valueOf(location.getLatitude());
                    mLongitude = String.valueOf(location.getLongitude());
                    AppLog.Logd("Fly", "mLatitude===="+mLatitude);
                    mLocCity = String.valueOf(location.getCity());
                    requestCityList();
                }
            }
          
        }

    }
    /**
     * 请求城市列表
     */
    private void requestCityList() {
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
                ArrayList<Area> list = (ArrayList<Area>) object;
                if(list.size()>0){
                    if (!TextUtils.isEmpty(mLocCity) && !mLocCity.equals("null")){
                        Area area = new Area();
                        area.setmName(mLocCity);
                        area = list.get(list.indexOf(area));
                        SharePreferenceUtils.getInstance(getActivity()).saveArea(area);
                        mCityTv.setText(mLocCity);
                    }else{
                        SharePreferenceUtils.getInstance(getActivity()).saveArea(list.get(0));
                        mCityTv.setText(list.get(0).getmName());
                    }
                    
                    requestData();
                }
              
            }
        };
    }
    

 
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());

            }
        };
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
  
  
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initTitleView();
        initView(view);
    }
    private void initTitleView(){
        setTitleText(R.string.home_tab);
       
        
    }
    private void initView(View view) {
    
        mViewPagerWrapper = (ViewPagerWrapper) view.findViewById(R.id.vpWrapper);

        
        mLinearLayout1 = (LinearLayout) view.findViewById(R.id.laundry_service);
        mLinearLayout1.setOnClickListener(this);
        
        mLinearLayout2 = (LinearLayout) view.findViewById(R.id. manicure_service);
        mLinearLayout2.setOnClickListener(this);
        
        mLinearLayout3 = (LinearLayout) view.findViewById(R.id.beauty_service);
        mLinearLayout3.setOnClickListener(this);
        
//        mLinearLayout4 = (LinearLayout) view.findViewById(R.id. cosmetology_service);
//        mLinearLayout4.setOnClickListener(this);
        
        mLinearLayout5 = (LinearLayout) view.findViewById(R.id. cosmetology_service);
        mLinearLayout5.setOnClickListener(this);
        
        mLinearLayout6 = (LinearLayout) view.findViewById(R.id. makeup_service);
        mLinearLayout6.setOnClickListener(this);
        
        mLinearLayout7 = (LinearLayout) view.findViewById(R.id. cleaning_service);
        mLinearLayout7.setOnClickListener(this);
        
        mCityTv = (TextView) view.findViewById(R.id.city);
        mCityTv .setOnClickListener(this);
    
        
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        
        case R.id.makeup_service:
             UIHelper.toShengxianListActivity(this,1);//生鲜
            break;
        case R.id.manicure_service:
            UIHelper.toGanxiListActivity(this,4);// 保养 奢侈品
            break;
            
        case R.id.laundry_service:
            UIHelper.toGanxiListActivity(this,0);// 干洗 洗衣
            break;
        case R.id.beauty_service:
            UIHelper.toServiceListActivity(this,0);// 美甲
            break;
       case R.id.   cosmetology_service:
           UIHelper.toServiceListActivity(this,1);// 美婕
            break;      
       case R.id.   cleaning_service:
           UIHelper.toShengxianListActivity(this,2);// 鲜花
           break;   
       case R.id.city:
           UIHelper.toClassActivity(this, CitySelectActivity.class.getName());
           
           break;
        default:
            break;
            
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        super.onActivityResult(requestCode, resultCode, data);
        
        if(data!=null){
            String city = data.getStringExtra(IntentBundleKey.LOCCITY);
            mCityTv.setText(city);
        }
    }
    
    
}
