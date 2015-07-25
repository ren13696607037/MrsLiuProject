package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.techfly.liutaitai.bizz.parser.ServiceParser;
import com.techfly.liutaitai.model.mall.adapter.PopUpAdapter;
import com.techfly.liutaitai.model.mall.adapter.ServiceAdapter;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.mall.bean.SortRule;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.GridViewForScrollView;
import com.techfly.liutaitai.util.view.PullToRefreshLayout;
import com.techfly.liutaitai.util.view.PullToRefreshLayout.OnRefreshListener;

public class ServiceListFragment extends CommonFragment implements OnClickListener, OnRefreshListener{

  
    private TextView mSortTv1;
    private TextView mSortTv2;
  
    private View mFlagImg1;
    private View mFlagImg2;
  
    private int mPage = 1;
    
    private PopupWindow mSortPop1;
    
    private ServiceAdapter mAdapter;
    
    private PopupWindow mSortPop2;
   

    private GridViewForScrollView mPopList1;
    private ListView mPopList2;
   
    private int type;
    private String mSortId;
   

    private PopUpAdapter mPopAdapter1;
    private PopUpAdapter mPopAdapter2;
    
    private List<SortRule> mSortRuleList;
    private List<SortRule> mSortRuleList2;
    private String sellerSortRuleStr = "";// 商家所属分类
    private String sellerSortRuleStr2 = "";// 商家所在地区
  
    List<Service> mList = new ArrayList<Service>();
    private PullToRefreshLayout mPull;
    private GridView mGrid;
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
        mSortPop1 = new PopupWindow(view, Constant.SCREEN_WIDTH / 3 - 60,
                LayoutParams.WRAP_CONTENT, false);
        // 需要设置一下此参数，点击外边可消失
        mPopList1 = (GridViewForScrollView) view.findViewById(R.id.gridview);
        mPopAdapter1 = new PopUpAdapter(getActivity(), mSortRuleList);
        mSortPop1.setBackgroundDrawable(new BitmapDrawable());
        mPopList1.setAdapter(mPopAdapter1);
        mPopList1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                sellerSortRuleStr = mSortRuleList.get(position).getmId();
                for(int i =0;i<mSortRuleList.size();i++ ){
                    if(i==position){
                        mSortRuleList.get(position).setmIsSelect(true);
                    }else{
                        mSortRuleList.get(position).setmIsSelect(false);
                    }
                }
                mSortId =mSortRuleList.get(position).getmId();
                mSortTv1.setText(mSortRuleList.get(position).getmName());
                mPage=1;
                mLoadHandler.sendEmptyMessageDelayed(Constant.NET_LOAD, 0);
                mPopAdapter1.notifyDataSetChanged();
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
        mSortPop2 = new PopupWindow(view, Constant.SCREEN_WIDTH / 3 - 60,
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
                sellerSortRuleStr2 = mSortRuleList2.get(position).getmId();
                mSortTv2.setText(mSortRuleList2.get(position).getmName());
                requestData();
                mLoadHandler
                        .sendEmptyMessageDelayed(Constant.NET_SUCCESS, 5000);
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
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "commodity/sort/"+type);
       
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
                    mSortRuleList = (List<SortRule>) result.getObject();
                    initPopupWindow1();
                    mSortId =  mSortRuleList.get(0).getmId();
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
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }
    
    private void getServiceList(){
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
      
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "commodity/sort/"+type+"/"+mSortId+"/"+mPage);
       
//      url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
//              JPushInterface.getRegistrationID(getActivity()));
        param.setmHttpURL(url);
        param.setmParserClassName(ServiceParser.class.getName());
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
                   mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                   ResultInfo result = (ResultInfo) object;
                   if(result.getmCode()==10){
                       List<Service> list = new ArrayList<Service>();
                       list = (List<Service>) result.getObject();
                       if(list.size()>=10){
                           mPull.setPullLoadEnable(false);
                       }else{
                           mPull.setPullLoadEnable(true);
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
      
        startReqTask(this);
        
    }

    @Override
    public void onDestroy() {
       
        super.onDestroy();
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

    private void initView(View view) {
       

        mSortTv1 = (TextView) view.findViewById(R.id.sort1);
        mSortTv2 = (TextView) view.findViewById(R.id.sort2);
    

        mFlagImg1 = view.findViewById(R.id.flag_img1);
        mFlagImg2 = view.findViewById(R.id.flag_img2);
     

        mSortTv1.setOnClickListener(this);
        mSortTv2.setOnClickListener(this);
    

        mFlagImg1.setOnClickListener(this);
        mFlagImg2.setOnClickListener(this);
      
        mPull = (PullToRefreshLayout) view.findViewById(R.id.layout_parts);
        mPull.setOnRefreshListener(this);
        
     
        mGrid = (GridView) view.findViewById(R.id.gridview_parts);
        mAdapter = new ServiceAdapter(getActivity(), mList);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

              mPage = 1;
              getServiceList();

            }
        });

    }

   

    private void initTitleView() {
      setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
      if(type==1){
          setTitleText("美甲");
      }else if(type==2){
          setTitleText("美容");
      }else if(type==3){
          setTitleText("美婕");
      }else if(type==4){
          setTitleText("化妆造型");
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.sort1:
        case R.id.flag_img1:
            if (mSortPop1 != null) {
                if (mSortPop1.isShowing()) {
                    mSortPop1.dismiss();
                } else {
                    mSortPop1.showAsDropDown(mSortTv1);
                }
            }

            break;
        case R.id.flag_img2:
        case R.id.sort2:
            if (mSortPop2 != null) {
                if (mSortPop2.isShowing()) {
                    mSortPop2.dismiss();
                } else {
                    mSortPop2.showAsDropDown(mSortTv2);
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
    
    
}
