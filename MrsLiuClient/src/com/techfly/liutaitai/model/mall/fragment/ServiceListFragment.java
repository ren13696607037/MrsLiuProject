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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.LoginParser;
import com.techfly.liutaitai.model.mall.adapter.PopUpAdapter;
import com.techfly.liutaitai.model.mall.adapter.ServiceAdapter;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.mall.bean.SortRule;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.MD5;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class ServiceListFragment extends CommonFragment implements OnClickListener,IXListViewListener{

  
    private TextView mSortTv1;
    private TextView mSortTv2;
  
    private View mFlagImg1;
    private View mFlagImg2;
  
    private XListView mListView;
    
    private int mPage = 0;
    
    private PopupWindow mSortPop1;
    
    private ServiceAdapter mAdapter;
    
    private PopupWindow mSortPop2;
   

    private ListView mPopList1;
    private ListView mPopList2;
   

   

    private PopUpAdapter mPopAdapter1;
    private PopUpAdapter mPopAdapter2;
    
    private List<SortRule> sellerSortRuleList;
    private List<SortRule> sellerSortRuleList2;
    private String sellerSortRuleStr = "";// 商家所属分类
    private String sellerSortRuleStr2 = "";// 商家所在地区
  
    List<Service> mList = new ArrayList<Service>();

    /**
     * 
     */
    private void initPopupWindow1() {
        // TODO Auto-generated method stub
        Utility.getScreenSize(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.service_popupwindow, null);
        // 创建PopupWindow对象
        mSortPop1 = new PopupWindow(view, Constant.SCREEN_WIDTH / 3 - 60,
                LayoutParams.WRAP_CONTENT, false);
        // 需要设置一下此参数，点击外边可消失
        mPopList1 = (ListView) view.findViewById(R.id.listview);
        mPopAdapter1 = new PopUpAdapter(getActivity(), sellerSortRuleList);
        mSortPop1.setBackgroundDrawable(new BitmapDrawable());
        mPopList1.setAdapter(mPopAdapter1);
        mPopList1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                sellerSortRuleStr = sellerSortRuleList.get(position).getmId();
                mSortTv1.setText(sellerSortRuleList.get(position).getmName());
                requestData();
                mLoadHandler
                        .sendEmptyMessageDelayed(Constant.NET_SUCCESS, 5000);
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
        mPopAdapter2 = new PopUpAdapter(getActivity(), sellerSortRuleList2);
        mSortPop2.setBackgroundDrawable(new ColorDrawable(R.color.color_gray));
        mPopList2.setAdapter(mPopAdapter2);
        mPopList2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                sellerSortRuleStr2 = sellerSortRuleList2.get(position).getmId();
                mSortTv2.setText(sellerSortRuleList2.get(position).getmName());
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
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.LOGIN_URL);
       
//      url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
//              JPushInterface.getRegistrationID(getActivity()));
        param.setmHttpURL(url);
        param.setPostRequestMethod();
        param.setmParserClassName(LoginParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createMyReqSuccessListener(),
                        createMyReqErrorListener(), param);
        
    }
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
            
          
//              mUser.setPass(mPass.getText().toString());
                if (!isDetached()) {
                   
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                showSmartToast(R.string.login_error, Toast.LENGTH_SHORT);
                if (!isDetached()) {
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }
    
    
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
      
        startReqTask(this);
        mLoadHandler.sendEmptyMessageDelayed(Constant.NET_SUCCESS, 500);// 停止加载框
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
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
      

      

        mListView = (XListView) view.findViewById(R.id.xlistview);

     
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mAdapter = new ServiceAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

              

            }
        });

    }

   

    private void initTitleView() {
      
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
    public void onRefresh() {

        mPage = 1;
        requestData();

    }

    @Override
    public void onLoadMore() {

        mPage = mPage + 1;
        requestData();

    }
}
