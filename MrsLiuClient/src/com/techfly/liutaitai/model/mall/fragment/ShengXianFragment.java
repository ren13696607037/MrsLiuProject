package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.ServiceCategoryParser;
import com.techfly.liutaitai.bizz.parser.ShengxianServiceParser;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.activities.ProductInfoActivity;
import com.techfly.liutaitai.model.mall.adapter.PopUpAdapter;
import com.techfly.liutaitai.model.mall.adapter.PopUpAdapter2;
import com.techfly.liutaitai.model.mall.adapter.ShengXianServiceAdapter;
import com.techfly.liutaitai.model.mall.adapter.ShengXianServiceAdapter.AddShopCarCallBack;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.SortRule;
import com.techfly.liutaitai.model.mall.parser.ShopCartParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.GridViewForScrollView;
import com.techfly.liutaitai.util.view.PullToRefreshLayout;
import com.techfly.liutaitai.util.view.PullToRefreshLayout.OnRefreshListener;

public class ShengXianFragment extends CommonFragment implements OnClickListener,AddShopCarCallBack, OnRefreshListener{
    private TextView mSortTv1;
    private TextView mSortTv2;
    private View mFlagImg1;
    private int mPage = 1;
    private PopupWindow mSortPop1;
    private ShengXianServiceAdapter mAdapter;
    private PopupWindow mSortPop2;
    private GridViewForScrollView mPopList1;
    private ListView mPopList2;
    private int type;
    private String mSortId="0";
    private PopUpAdapter2 mPopAdapter1;
    private PopUpAdapter mPopAdapter2;
    
    private List<SortRule> mSortRuleList= new ArrayList<SortRule>();;
    private List<SortRule> mSortRuleList2 = new ArrayList<SortRule>();;
    private String mCateID = "";// 商家所属分类
    List<Product> mList = new ArrayList<Product>();
    private PullToRefreshLayout mPull;
    private GridView mGrid;
    private TextView mShopCarNum;
    private Handler mHander = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            mPopAdapter1.updateListView(mSortRuleList);
        }
        
    };
    @Override
    public void onResume() {
        super.onResume();
        requestShopCarNum();
    }
    public void requestShopCarNum() {

        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
        int userId = 0;
        if (user != null) {
            userId = Integer.parseInt(user.getmId());
        }
        if (userId == 0) {
            mLoadHandler.removeMessages(Constant.NET_SUCCESS);
            mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
            mShopCarNum.setVisibility(View.GONE);
            return;
        }else{
            mShopCarNum.setBackgroundDrawable(getResources().getDrawable(R.drawable.shop_car_header_num_bg));
            mShopCarNum.setVisibility(View.VISIBLE);
        }
        param.setmIsLogin(true);
        param.setmId(user.getmId());
        param.setmToken(user.getmToken());

        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
                + Constant.SHOP_CARD_REQUEST_URL);
        url.setmGetParamPrefix("city");
        url.setmGetParamValues(SharePreferenceUtils.getInstance(getActivity())
                .getArea().getmId());
        url.setmGetParamPrefix("type");
        url.setmGetParamValues(type + "");
        param.setmHttpURL(url);
        param.setmParserClassName(ShopCartParser.class.getName());
        RequestManager.getRequestData(getActivity(), creatSuccessListener(),
                creatErrorListener(), param);//
    }

    private Response.Listener<Object> creatSuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                
                
                if (ShopCar.getShopCar().getShopproductList() != null) {
                    mShopCarNum.setText(ShopCar.getShopCar().getShopAmountSum()
                            + "");
                } else {
                    mShopCarNum.setText("0");
                }

            }
        };
    }

    private Response.ErrorListener creatErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());

            }
        };
    }
    private void initSortRule2(){
        SortRule rule = new SortRule();
        rule.setmId("0");
        rule.setmName("默认排序");
        
        SortRule rule1 = new SortRule();
        rule1.setmId("1");
        rule1.setmName("按销量排序");
        
        SortRule rule2 = new SortRule();
        rule2.setmId("2");
        rule2.setmName("按新品");
        
        SortRule rule3 = new SortRule();
        rule3.setmId("4");
        rule3.setmName("价格由高到低");
        
        SortRule rule4 = new SortRule();
        rule4.setmId("3");
        rule4.setmName("价格由低到高");
        
        mSortRuleList2.add(rule);
        mSortRuleList2.add(rule1);
        mSortRuleList2.add(rule2);
        mSortRuleList2.add(rule3);
        mSortRuleList2.add(rule4);
        
    }
    /**
     * 
     */
    private void initPopupWindow1() {
        // TODO Auto-generated method stub
        Utility.getScreenSize(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.service_popupwindow2, null);
        // 创建PopupWindow对象
        mSortPop1 = new PopupWindow(view, Constant.SCREEN_WIDTH,
                LayoutParams.WRAP_CONTENT, false);
        // 需要设置一下此参数，点击外边可消失
        mPopList1 = (GridViewForScrollView) view.findViewById(R.id.gridview);
        mPopAdapter1 = new PopUpAdapter2(getActivity(), mSortRuleList);
        mSortPop1.setBackgroundDrawable(new BitmapDrawable());
        mPopList1.setAdapter(mPopAdapter1);
        mPopList1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                mCateID = mSortRuleList.get(position).getmId();
                for(int i =0;i<mSortRuleList.size();i++ ){
                    if(i==position){
                        mSortRuleList.get(i).setmIsSelect(true);
                    }else{
                        mSortRuleList.get(i).setmIsSelect(false);
                    }
                }
                mSortTv1.setText(mSortRuleList.get(position).getmName());
                
                mHander.sendEmptyMessageDelayed(0, 2000);
                mPage = 1;
                mList.clear();
                getServiceList();
                mSortPop1.dismiss();
            }

        });
        // 设置点击窗口外边窗口消失
        mSortPop1.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mSortPop1.setFocusable(true);

    }

    /**
     * 
     */
    private void initPopupWindow2() {
        // TODO Auto-generated method stub
        Utility.getScreenSize(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.service_popupwindow, null);
        // 创建PopupWindow对象
        mSortPop2 = new PopupWindow(view, Constant.SCREEN_WIDTH ,
                LayoutParams.WRAP_CONTENT, false);
        // 需要设置一下此参数，点击外边可消失
        mPopList2 = (ListView) view.findViewById(R.id.listview);
        mPopAdapter2 = new PopUpAdapter(getActivity(), mSortRuleList2);
        mSortPop2.setBackgroundDrawable(new ColorDrawable(R.color.color_gray));
        mPopList2.setAdapter(mPopAdapter2);
        mPopList2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                mSortId = mSortRuleList2.get(position).getmId();
                mSortTv2.setText(mSortRuleList2.get(position).getmName());
                mPage = 1;
                mList.clear();
                getServiceList();
                mSortPop2.dismiss();
            }

        });
        // 设置点击窗口外边窗口消失
        mSortPop2.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mSortPop2.setFocusable(true);

    }

   

    @Override
    public void requestData() {

        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        if(type==1){
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "common/categories?type=3");// 生鲜请求地址组装
        }else{
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "common/categories?type=4");// 组装鲜花请求地址
        }
//      url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
//              JPushInterface.getRegistrationID(getActivity()));
        param.setmHttpURL(url);
        param.setmParserClassName(ServiceCategoryParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createMyReqSuccessListener(),
                        createMyReqErrorListener(), param);
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                if (!isDetached()) {
                    ResultInfo result = (ResultInfo) object;
                    mSortRuleList.clear();
                    mSortRuleList = (List<SortRule>) result.getObject();
                    SortRule rule = new SortRule();
                    rule.setmId("0");
                    rule.setmIsSelect(true);
                    rule.setmName("全部分类");
                    mSortRuleList.add(0, rule);
                    initPopupWindow1();
                    if(mSortRuleList!=null&&mSortRuleList.size()>0){
                        mSortId =  mSortRuleList.get(0).getmId(); 
                    }
                    getServiceList();
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                showSmartToast(R.string.loading_fail_server, Toast.LENGTH_SHORT);
                if (!isDetached()) {
                    mPull.refreshFinish(PullToRefreshLayout.FAIL);
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }
    
    private void getServiceList(){
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_LIST);
//      url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
//              JPushInterface.getRegistrationID(getActivity()));
        String areaId =SharePreferenceUtils.getInstance(getActivity()).getArea().getmId();
        url.setmGetParamPrefix("type").setmGetParamValues(type+"");
        url.setmGetParamPrefix("city").setmGetParamValues(areaId);
        url.setmGetParamPrefix(RequestParamConfig.PAGE).setmGetParamValues(mPage+"");
        url.setmGetParamPrefix("size").setmGetParamValues("10");
        url.setmGetParamPrefix(RequestParamConfig.SORT).setmGetParamValues(mSortId  +"");
        if(!TextUtils.isEmpty(mCateID)){
            url.setmGetParamPrefix("cate").setmGetParamValues(mCateID);
        }
        param.setmHttpURL(url);
        param.setmParserClassName(ShengxianServiceParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createServiceReqSuccessListener(),
                        createMyReqErrorListener(), param);
    }
    private Response.Listener<Object> createServiceReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                if (getActivity()!=null&&!isDetached()) {
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessageDelayed(Constant.NET_SUCCESS,0);
                    mPull.refreshFinish(PullToRefreshLayout.SUCCEED);
                    ResultInfo result = (ResultInfo) object;
                   if(result.getmCode()==0){
                       requestShopCarNum();
                       List<Product> list = new ArrayList<Product>();
                       list = (List<Product>) result.getObject();
                       if(list.size()>=10){
                           mPull.setPullRefreshEnable(true);
                           mPull.setPullLoadEnable(true);
                       }else{
                           mPull.setPullRefreshEnable(true);
                           mPull.setPullLoadEnable(false);
                       }
                       mList.addAll(list);
                       mAdapter.notifyDataSetChanged();
                   }else{
                       showSmartToast(result.getmMessage(), Toast.LENGTH_LONG);
                   }
                
                   
                }
            }
        };
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        type =activity.getIntent().getIntExtra("type", 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      
        super.onCreate(savedInstanceState);
        initSortRule2();
        startReqTask(this);
        
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initTitleView();
        initView(view);
    }

    private void initView(View view) {
       
        mShopCarNum = (TextView) view.findViewById(R.id.shop_car_num);
        mSortTv1 = (TextView) view.findViewById(R.id.sort1);
        mSortTv2 = (TextView) view.findViewById(R.id.sort2);
        mFlagImg1 = view.findViewById(R.id.flag_img1);
        mSortTv1.setOnClickListener(this);
        mSortTv2.setOnClickListener(this);
        mFlagImg1.setOnClickListener(this);
        mPull = (PullToRefreshLayout) view.findViewById(R.id.layout_parts);
        mPull.setOnRefreshListener(this);
        mGrid = (GridView) view.findViewById(R.id.gridview_parts);
        mAdapter = new ShengXianServiceAdapter(getActivity(), mList,this);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Product pro = (Product) arg0.getItemAtPosition(arg2);
                Intent intent = null;
                intent = new Intent(getActivity(),
                        ProductInfoActivity.class);
                intent.putExtra(IntentBundleKey.ID, pro.getmId());
                intent.putExtra(IntentBundleKey.TYPE, type);
                intent.putExtra(IntentBundleKey.IMAGE_PATH, pro.getmImg());
                startActivityForResult(intent, 0);
            }
        });
        initPopupWindow2();
    }

   

    private void initTitleView() {
      setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
      if(type==1){
          setTitleText("生鲜");
          }else if(type==2){
          setTitleText("鲜花");
      }
      setRightHeadIcon(R.drawable.shop_car_header, new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            if (authLogin()) {
                UIHelper.toShopCarActivity(getActivity(), type);
            } else {
                UIHelper.toLoginActivity(getActivity());
            }
            
        }
    });
    }
    private boolean authLogin() {
        User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
        if (user != null && !TextUtils.isEmpty(user.getmId())) {
            int id = Integer.parseInt(user.getmId());
            if (id > 0) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
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
        super.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.sort1:
        case R.id.flag_img1:
            if (mSortPop1 != null) {
                if (mSortPop1.isShowing()) {
                    mSortPop1.dismiss();
                } else {
                    mSortPop1.showAsDropDown(mSortTv1,0,10);
                }
            }

            break;
        case R.id.sort2:
            if (mSortPop2 != null) {
                if (mSortPop2.isShowing()) {
                    mSortPop2.dismiss();
                } else {
                    mSortPop2.showAsDropDown(mSortTv2,0,10);
                }
            }
            break;
      
        default:
            break;
        }
    }

    

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPage = 1;
        mList.clear();
        getServiceList();
        
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPage = mPage + 1;
        getServiceList();
        
    }
    @Override
    public void onSuccess() {
        requestShopCarNum();
    }
}
