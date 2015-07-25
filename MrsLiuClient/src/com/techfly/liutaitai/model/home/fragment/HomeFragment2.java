package com.techfly.liutaitai.model.home.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.home.adapter.BannerAdapter;
import com.techfly.liutaitai.model.home.bean.Banner;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CommonFragment;
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
    
    private LinearLayout mLinearLayout8;
    
  
    
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        startReqTask(this);
        mLoadHandler.sendEmptyMessageDelayed(Constant.NET_SUCCESS, 1500);// 停止加载框
        initBanner();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
  
    private void initBanner(){
        Banner banner = new Banner();
        banner.setmImage("http://app.yhmall.com:8880/apis/files/upload/2015/5/17/dianfengshan.jpg-b92f82cc-76e4-4ece-bba6-9103594a5527/dianfengshan.jpg");
        mdataBanner.add(banner);
        
        Banner banner1 = new Banner();
        banner1.setmImage("http://app.yhmall.com:8880/apis/files/upload/2015/5/17/baoqian.jpg-c5596bc1-92df-4ed3-9383-70c19063a169/baoqian.jpg");
        mdataBanner.add(banner1);
        
        Banner banner2 = new Banner();
        banner2.setmImage("http://app.yhmall.com:8880/apis/files/upload/2015/5/17/haifeisi.jpg-d987e2b7-ac3c-4c30-84e5-775d2cc17c07/haifeisi.jpg");
        mdataBanner.add(banner2);
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
        mBannerAdapter = new BannerAdapter(getActivity(), mdataBanner);
        mViewPagerWrapper.setAdapter(mBannerAdapter);
        
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
        
        mLinearLayout8 = (LinearLayout) view.findViewById(R.id. phone_service);
        mLinearLayout8.setOnClickListener(this);
        
       
    
       
        
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
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
             UIHelper.toServiceListActivity(this,4);
            break;
        case R.id.manicure_service:
            UIHelper.toServiceListActivity(this,1);
            
            break;
            
        case R.id.laundry_service:
            
            break;
        case R.id.beauty_service:
            UIHelper.toServiceListActivity(this,3);
            break;
       case R.id.   cosmetology_service:
           UIHelper.toServiceListActivity(this,2);
            break;      
       case R.id.   cleaning_service:
           
           break;   
       case R.id.   phone_service:
           
           break;   
        default:
            break;
            
        }
    }
}
