package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.adapter.JishiAdapter;
import com.techfly.liutaitai.model.mall.bean.Jishi;
import com.techfly.liutaitai.model.mall.bean.ServiceInfo;
import com.techfly.liutaitai.model.mall.parser.JiShiParser;
import com.techfly.liutaitai.model.mall.parser.ServiceInfoParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.net.ResultCode;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class JiShiListFragment extends CommonFragment implements OnClickListener,IXListViewListener{
    private JishiAdapter mAdapter;
    private List<Jishi> mList = new ArrayList<Jishi>();
    private XListView mListView;
    private boolean mIsRefresh;
    private boolean mIsLoadAll;
   
    private int mPage = 1;;
    private int type;
    private String time;
    
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "service/masters");
        User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
        int userId = 0;
        if (user != null) {
            userId = Integer.parseInt(user.getmId());
        }
        if (userId == 0) {
            return;
        }
        param.setmIsLogin(true);
        param.setmId(user .getmId());
        param.setmToken(user .getmToken());
        url.setmGetParamPrefix("type");
        url.setmGetParamValues(String.valueOf(type));
      
        url.setmGetParamPrefix("time");
        url.setmGetParamValues(time);
        
        // url.setmBaseUrl("http://www.hylapp.com:10001/apis/goods/detail?pid=1533");
        param.setmHttpURL(url);
        param.setmParserClassName(JiShiParser.class.getName());
        // param.setmParserClassName(ProductInfoParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createMyReqSuccessListener(),
                        createMyReqErrorListener(), param);

    }

    private Response.Listener<Object> createMyReqSuccessListener() {
        
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ResultInfo resultInfo = (ResultInfo) object;
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                if (getActivity() != null && !isDetached()) {
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessageDelayed(
                            Constant.NET_SUCCESS, 0);// 停止加载框
                    if (resultInfo != null
                            && resultInfo.getmCode() == ResultCode.SUCCESS) {
                        List<Jishi> list = (List<Jishi>) resultInfo
                                .getObject();
                        mListView.stopLoadMore();
                        mListView.stopRefresh();
                        if (list.size() == 0) {
                            if (mList.size() == 0) {
                                // 没有收藏
                                onDispalyEmpty();
                                mIsLoadAll = false;
                                mIsRefresh = true;
                            } else {
                                // 没有更多收藏
                                onDispalyData();
                                showSmartToast("", Toast.LENGTH_LONG);
                                mIsLoadAll = false;
                                mIsRefresh = true;
                            }
                        } else {
                            onDispalyData();
                            if (list.size() < 10) {
                                mIsLoadAll = false;
                                mIsRefresh = true;
                                // 没有更多收藏
                            } else {
                                // 有更多收藏
                                mIsLoadAll = true;
                                mIsRefresh = true;
                            }
                            mList.addAll(list);
                        }
                        mListView.setPullLoadEnable(mIsLoadAll);
                        mListView.setPullRefreshEnable(mIsRefresh);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (mList.size() == 0) {
                            // 没有筛选的职位
                            onDispalyEmpty();
                        }else{
                            showSmartToast(resultInfo.getmMessage(),
                                    Toast.LENGTH_LONG);
                        }
                      
                    }
                }
            }
            
        };
    }

   
  
    
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                mLoadHandler.removeMessages(Constant.NET_FAILURE);
                mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
                showMessage(R.string.loading_fail);
            }
        };
    }
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        type = activity.getIntent().getIntExtra(IntentBundleKey.TYPE, 0);
        time = activity.getIntent().getStringExtra(IntentBundleKey.ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        super.onViewCreated(view, savedInstanceState);
        initTitleView();
        initView(view);
    }

    private void initTitleView() {
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText("选择技师");
    }

    private void initView(View view) {
        mListView = (XListView) view.findViewById(R.id.listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mAdapter = new JishiAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

               

            }
        });
     

    }

    private void onDispalyData() {
        mListView.setVisibility(View.VISIBLE);
    
    }

    private void onDispalyEmpty() {
        mListView.setVisibility(View.GONE);
   
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jishi_list,
                container, false);
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
        default:
            break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onRefresh() {
        mList.clear();
        mPage = 1;
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        requestData();
    }

    @Override
    public void onLoadMore() {
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mPage = mPage + 1;
        requestData();

    }

}
