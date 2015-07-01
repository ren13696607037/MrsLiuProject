package com.hylsmart.yihui.util.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hylsmart.yihui.R;
 

public class ScrollTabView extends HorizontalScrollView implements ViewPager.OnPageChangeListener{

	private TabAdapter tabAdapter;
 
	private Context mContext;
	
	private LinearLayout container;
	
	private ViewPager viewPager;
	
	public ScrollTabView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public ScrollTabView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
	public ScrollTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext=context;
		
		container=new LinearLayout(mContext);
		container.setOrientation(LinearLayout.HORIZONTAL);
		
		addView(container);
	}

	public TabAdapter getAdapter() {
		return tabAdapter;
	}

	public void setAdapter(TabAdapter tabAdapter) {
		this.tabAdapter = tabAdapter;
		
		
		initTabs();
	}
	
	public void setViewPager(ViewPager viewPager){
		this.viewPager=viewPager;
		
		viewPager.setOnPageChangeListener(this);
	}
	
	private void initTabs(){
		 for(int i=0;i<tabAdapter.getCount();i++){
			 final int position=i;
			 View tab=tabAdapter.getView(i);
			 container.addView(tab);
			 
			 
			 tab.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectedTab(position);
					viewPager.setCurrentItem(position);
				}
				 
			 });
		 }
		 
		 
		 //Ĭ��ѡ��0
		 selectedTab(0);
	}

	
	public void selectedTab(int position){
		for(int i=0;i<container.getChildCount();i++){
			LinearLayout layout=(LinearLayout) container.getChildAt(i);
			TextView textView=(TextView) layout.findViewById(R.id.text);
			container.getChildAt(i).setSelected(position==i);
			if(position==i){
				textView.setTextColor(getResources().getColor(R.color.orange_bg_home_header));
			}else{
				textView.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			}
		}
		
		int w=container.getChildAt(0).getWidth();
		
		smoothScrollTo(w*(position-1), 0);			//���ÿ���position=0,scrollTo(-x,0)�൱��scrollTo(0,0)�������ᳬ����ͼ�߽�
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		selectedTab(position);
	}


}
