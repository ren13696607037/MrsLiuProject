package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techfly.liutaitai.MainActivity;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.PagerAdapter;
import com.techfly.liutaitai.util.view.ScrollTabView;
import com.techfly.liutaitai.util.view.ScrollTabsAdapter;
import com.techfly.liutaitai.util.view.TabAdapter;

public class MyOrderFragment extends CommonFragment {
	private int mPush;
	private ViewPager mViewPager;
	private ScrollTabView mTabView;
	private TabAdapter mTabAdapter;
	private int mPosition;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPush = getActivity().getIntent().getIntExtra(IntentBundleKey.PUSH, -1);
        mPosition = getActivity().getIntent().getIntExtra(IntentBundleKey.ORDER_ID, 0);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
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
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.myorder_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG,new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mPush!=-1){
					if(!Utility.getActivityName(getActivity())){
						getActivity().finish();
					}else{
						Bundle bundle=new Bundle();
						bundle.putInt(IntentBundleKey.PUSH, mPush);
						Utility.go2Activity(getActivity(),MainActivity.class,bundle);
					}
				}else{
					getActivity().finish();
				}
			}
		});
    	mViewPager=(ViewPager) view.findViewById(R.id.order_viewpager);
    	mTabView=(ScrollTabView) view.findViewById(R.id.order_tab_container);
    	onInitTabConfig();
    	onInitViewPager();
    }
    private void onInitTabConfig(){
    	mTabAdapter=new ScrollTabsAdapter(getActivity(), 4);
    	mTabAdapter.add(getActivity().getString(R.string.order_all));
    	mTabAdapter.add(getActivity().getString(R.string.service_text));
    	mTabAdapter.add(getActivity().getString(R.string.service_text1));
    	mTabAdapter.add(getActivity().getString(R.string.service_text2));
    	mTabView.setAdapter(mTabAdapter);
    }
    private void onInitViewPager(){
    	PagerAdapter pagerAdapter=new PagerAdapter(
				getActivity().getSupportFragmentManager());
    	pagerAdapter.addFragment(new MyOrderAllFragment());
    	pagerAdapter.addFragment(new MyOrderPayFragment());//带接单
    	pagerAdapter.addFragment(new MyOrderDeliveryFragment());//带服务
    	pagerAdapter.addFragment(new MyOrderRateFragment());//进行中
    	mViewPager.setAdapter(pagerAdapter);
    	mTabView.setViewPager(mViewPager);
    	if(mPosition != 0){
    		mTabView.selectedTab(mPosition);
    		mViewPager.setCurrentItem(mPosition);
    	}
    }
	@Override
	public void requestData() {
		
	}

}
