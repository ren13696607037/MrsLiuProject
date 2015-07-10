package com.techfly.liutaitai.model.order.fragment;


import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.tab.ScrollingTabView;
import com.techfly.liutaitai.util.tab.TabActionBar;
import com.techfly.liutaitai.util.tab.TabAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OrderFragment extends CommonFragment {
	private ViewPager mViewPager;
	private ScrollingTabView mTabView;
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
        View view = inflater.inflate(R.layout.fragment_order_order, container, false);
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
    	mViewPager=(ViewPager) view.findViewById(R.id.order_viewpager);
    	mTabView=(ScrollingTabView) view.findViewById(R.id.order_tab_container);
    	onInitTabConfig();
    }
    private void onInitTabConfig(){
    	TabActionBar tabsActionBar = new TabActionBar(getActivity(), mTabView);
        mTabAdapter = new TabAdapter(getActivity(), mViewPager, tabsActionBar);
//        mTabAdapter.addTab(tabsActionBar.newTab().setCustomView(LayoutInflater.from(getActivity()).inflate(R.layout.tab_order_now, null)).setmTabbgDrawableId(R.drawable.login_tab), OrderNowFragment.class, null);
//        mTabAdapter.addTab(tabsActionBar.newTab().setCustomView(LayoutInflater.from(getActivity()).inflate(R.layout.tab_order_old, null)).setmTabbgDrawableId(R.drawable.login_tab), OrderOldFragment.class, null);
    }

	@Override
	public void requestData() {

	}

}
