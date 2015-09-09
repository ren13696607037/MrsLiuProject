package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.PagerAdapter;
import com.techfly.liutaitai.util.view.ScrollTabView;
import com.techfly.liutaitai.util.view.ScrollTabsAdapter;
import com.techfly.liutaitai.util.view.TabAdapter;

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
    	setTitleText(R.string.myorder_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
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
