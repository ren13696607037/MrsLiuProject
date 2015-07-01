package com.hylsmart.yihui.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.PagerAdapter;
import com.hylsmart.yihui.util.view.ScrollTabView;
import com.hylsmart.yihui.util.view.ScrollTabsAdapter;
import com.hylsmart.yihui.util.view.TabAdapter;

public class MyOrderFragment extends CommonFragment {
	private ViewPager mViewPager;
	private ScrollTabView mTabView;
	private TabAdapter mTabAdapter;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    	setTitleText(R.string.pcenter_order);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	mViewPager=(ViewPager) view.findViewById(R.id.order_viewpager);
    	mTabView=(ScrollTabView) view.findViewById(R.id.order_tab_container);
    	onInitTabConfig();
    	onInitViewPager();
    }
    private void onInitTabConfig(){
    	mTabAdapter=new ScrollTabsAdapter(getActivity());
    	mTabAdapter.add(getActivity().getString(R.string.order_all));
    	mTabAdapter.add(getActivity().getString(R.string.order_paying));
    	mTabAdapter.add(getActivity().getString(R.string.order_delivery));
    	mTabAdapter.add(getActivity().getString(R.string.order_deliveryed));
    	mTabView.setAdapter(mTabAdapter);
    }
    private void onInitViewPager(){
    	PagerAdapter pagerAdapter=new PagerAdapter(
				getActivity().getSupportFragmentManager());
    	pagerAdapter.addFragment(new MyOrderAllFragment());
    	pagerAdapter.addFragment(new MyOrderPayFragment());
    	pagerAdapter.addFragment(new MyOrderDeliveryFragment());
    	pagerAdapter.addFragment(new MyOrderRateFragment());
    	mViewPager.setAdapter(pagerAdapter);
    	mTabView.setViewPager(mViewPager);
    }
	@Override
	public void requestData() {
		
	}

}
