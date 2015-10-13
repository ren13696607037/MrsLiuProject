package com.techfly.liutaitai;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.LocationParser;
import com.techfly.liutaitai.model.home.activities.HomeActivity;
import com.techfly.liutaitai.model.mall.activities.OrderBastketActivity;
import com.techfly.liutaitai.model.order.activities.OrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.PCenterHomeActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.service.LocationService;
import com.techfly.liutaitai.update.UpdateMgr;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.ManagerListener.StartServiceListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends TabActivity implements TabSwitchCallBack ,StartServiceListener{
    private TabHost mTabHost;
    public static final String TAB_HOME = "tab_home";
    public static final String TAB_SHOP_CAR = "tab_shop_car";
    public static final String TAB_MALL = "tab_mall";
    public static final String TAB_PCENTER = "tab_pcenter";
    public Context mContext;
    private LocationReceiver mReceiver;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ManagerListener.newManagerListener().onRegisterStartServiceListener(this);
        setContentView(R.layout.activity_main);
//        onInitShopCart();
        TabSwitchListener.getTabSwitchLisManager().onRegisterTabSwitchListener(
                this);
        initTabHost();
        UpdateMgr.getInstance(mContext).checkUpdateInfo(null, false);
        ManagerListener.newManagerListener().notifyStartServiceListener();
        UmengUpdateAgent.update(this);
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
                .setContent(new Intent(this, OrderBastketActivity.class)));
        mTabHost.addTab(mTabHost
                .newTabSpec(TAB_SHOP_CAR)
                .setIndicator(
                        getMenuItem(R.drawable.ic_tab_shopcar,
                                getString(R.string.home_shopcar_tab)))
                .setContent(new Intent(this, OrderActivity.class)));
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
    protected void onDestroy() {
        super.onDestroy();
        TabSwitchListener.getTabSwitchLisManager()
                .onUnRegisterTabSwitchListener(this);
        ManagerListener.newManagerListener().onUnRegisterStartServiceListener(this);
        if(mReceiver != null){
        	//注销服务
        	  unregisterReceiver(mReceiver);
        	  //结束服务，如果想让服务一直运行就注销此句
        	  stopService(new Intent(this, LocationService.class));
        }
    }
    public class LocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			   Bundle bundle=intent.getExtras();      
			   String lon=bundle.getString("lon");    
			   String lat=bundle.getString("lat"); 
			   mUser = SharePreferenceUtils.getInstance(mContext).getUser();
			   if(!"".equals(lat)&&!"".equals(lon)&& mUser != null){
				   getUpdateLoc(lon, lat);
			   }else if(mUser == null){
				 //注销服务
				  	  unregisterReceiver(mReceiver);
				  	  //结束服务，如果想让服务一直运行就注销此句
				  	  stopService(new Intent(mContext, LocationService.class));
			   }
		}
    	
    }
    
    private void getUpdateLoc(String lon,String lat){
    	RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.LocationUrl);
        url.setmGetParamPrefix("lng").setmGetParamValues(lon);
        url.setmGetParamPrefix("lat").setmGetParamValues(lat);
        param.setmIsLogin(true);
        param.setmId(mUser.getmId());
        param.setmToken(mUser.getmToken());
        param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(LocationParser.class.getName());
        RequestManager.getRequestData(this, createMyReqSuccessListener(), createMyReqErrorListener(), param);
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ResultInfo resultInfo=(ResultInfo) object;
                AppLog.Loge("xll", "location request is -=-=-=-"+resultInfo.toString());
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
	public void onStartService() {
		mUser = SharePreferenceUtils.getInstance(mContext).getUser();
		if(mUser != null && "2".equals(mUser.getmType())){
			//启动服务
	        startService(new Intent(MainActivity.this, LocationService.class));
	        //注册广播
	        mReceiver=new LocationReceiver();
	        IntentFilter filter=new IntentFilter();
	        filter.addAction("com.techfly.liutaitai.LocationService");
	        registerReceiver(mReceiver, filter);
		}
	}

}
