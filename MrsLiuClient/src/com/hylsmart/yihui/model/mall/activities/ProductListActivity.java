package com.hylsmart.yihui.model.mall.activities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.util.AppLog;

public class ProductListActivity extends TabActivity{
    private TabHost mTabHost;
    public static final String TAB_SALE = "tab_sale";// 销量
    public static final String TAB_PRICE = "tab_price";// 价格
    public static final String TAB_SHELVES = "tab_shelves";// 时间
    public Context mContext;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_product_list);
        initTabHost();
    }

    private void initTabHost() {
        mTabHost = getTabHost();
        mTabHost.setup();
        mTabHost.setup(this.getLocalActivityManager());
        mTabHost.setOnTabChangedListener(onTabChangeListener);
        Intent intent = new Intent(this, ProSaleListActivity.class);
        
       
        mTabHost.addTab(mTabHost
                .newTabSpec(TAB_SALE)
                .setIndicator(
                        getMenuItem(
                                getString(R.string.pro_sale_tab)))
                .setContent(intent));
        mTabHost.addTab(mTabHost
                .newTabSpec(TAB_PRICE)
                .setIndicator(
                        getMenuItem(
                                getString(R.string.pro_price_tab)))
                .setContent(new Intent(this,ProPriceListActivity.class)));
   
        
        mTabHost.addTab(mTabHost
                .newTabSpec(TAB_SHELVES)
                .setIndicator(
                        getMenuItem(
                                getString(R.string.pro_shelves_tab)))
                .setContent(new Intent(this, ProShelvesListActivity.class)));
     
    }

    private OnTabChangeListener onTabChangeListener = new OnTabChangeListener() {

        @Override
        public void onTabChanged(String tabId) {
      
            if (tabId.equals(TAB_SALE)) {
//                ImageView iv = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.icon);
//                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.name);
//                tv.setText("XXXXsssss");
                AppLog.Logd("switch to TAB_SALE");
            } else if (tabId.equals(TAB_PRICE)) {
//                ImageView iv = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.icon);
//                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.name);
//                tv.setText("XXXXsssss");
                AppLog.Logd("switch to TAB_PRICE ");
            } else if (tabId.equals(TAB_SHELVES)) {
//                ImageView iv = (ImageView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.icon);
//                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.name);
//                tv.setText("XXXXsssss");
                AppLog.Logd("switch to TAB_SHELVES");

            } 
        }
    };

    public View getMenuItem( String textID) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.tab_pro_item, null);
        TextView textView = (TextView) ll.findViewById(R.id.name);
        textView.setText(textID);
        return ll;
    }

    
    
    @Override
    protected void onDestroy(){
        super.onDestroy();
              
    }
    
}
