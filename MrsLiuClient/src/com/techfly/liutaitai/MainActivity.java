package com.techfly.liutaitai;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.home.activities.HomeActivity;
import com.techfly.liutaitai.model.mall.activities.CategoryListActivity;
import com.techfly.liutaitai.model.order.activities.OrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.PCenterHomeActivity;
import com.techfly.liutaitai.model.shopcar.activities.ShopCarHomeActivity;
import com.techfly.liutaitai.update.UpdateMgr;
import com.techfly.liutaitai.util.AppLog;

public class MainActivity extends TabActivity implements TabSwitchCallBack {
	private TabHost mTabHost;
	public static final String TAB_HOME = "tab_home";
	public static final String TAB_SHOP_CAR = "tab_shop_car";
	public static final String TAB_MALL = "tab_mall";
	public static final String TAB_PCENTER = "tab_pcenter";
	public Context mContext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		ShopCar.getShopCar().getDbShopProList(mContext);
		TabSwitchListener.getTabSwitchLisManager().onRegisterTabSwitchListener(this);
		initTabHost();
		UpdateMgr.getInstance(mContext).checkUpdateInfo(null, false);
	}

	private void initTabHost() {
		mTabHost = getTabHost();
		mTabHost.setup();
		mTabHost.setup(this.getLocalActivityManager());
		mTabHost.setOnTabChangedListener(onTabChangeListener);
		Intent intent = new Intent(this, HomeActivity.class);
		mTabHost.addTab(mTabHost
				.newTabSpec(TAB_HOME)
				.setIndicator(
						getMenuItem(R.drawable.ic_tab_home,
								getString(R.string.home_tab)))
				.setContent(intent));
		mTabHost.addTab(mTabHost
				.newTabSpec(TAB_MALL)
				.setIndicator(
						getMenuItem(R.drawable.ic_tab_mall,
								getString(R.string.home_mall_tab)))
				.setContent(new Intent(this, CategoryListActivity.class)));
		mTabHost.addTab(mTabHost
				.newTabSpec(TAB_SHOP_CAR)
				.setIndicator(
						getMenuItem(R.drawable.ic_tab_shopcar,
								getString(R.string.home_shopcar_tab)))
				.setContent(new Intent(this, ShopCarHomeActivity.class)));
		mTabHost.addTab(mTabHost
				.newTabSpec(TAB_PCENTER)
				.setIndicator(
						getMenuItem(R.drawable.ic_tab_pcenter,
								getString(R.string.home_pcenter_tab)))
				.setContent(new Intent(this, PCenterHomeActivity.class)));
	}

	private OnTabChangeListener onTabChangeListener = new OnTabChangeListener() {

		@Override
		public void onTabChanged(String tabId) {
			if (tabId.equals(TAB_HOME)) {
				AppLog.Logd("switch to TAB_MANGOZONE initMangoAreaTabs");

			} else if (tabId.equals(TAB_SHOP_CAR)) {
				AppLog.Logd("switch to TAB_MANGOBOARD initBoardTabs ");
			} else if (tabId.equals(TAB_MALL)) {
				AppLog.Logd("switch to TAB_MANGOREMMEND initRecommendTab");

			} else if (tabId.equals(TAB_PCENTER)) {
				AppLog.Logd("switch to TAB_MANGOCATE initCateTabs");

			}
		}
	};

	public View getMenuItem(int imgID, String textID) {
		LinearLayout ll = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.tab_home_item, null);
		ImageView imgView = (ImageView) ll.findViewById(R.id.icon);
		// imgView.setBackgroundResource(imgID);
		imgView.setImageDrawable(getResources().getDrawable(imgID));
		TextView textView = (TextView) ll.findViewById(R.id.name);
		textView.setText(textID);
		return ll;
	}

    @Override
    public void switchTab(String tag) {
        mTabHost.setCurrentTabByTag(tag);
    }
	
    @Override
    protected void onDestroy(){
        super.onDestroy();
        TabSwitchListener.getTabSwitchLisManager().onUnRegisterTabSwitchListener(this);
              
    }
	

}
