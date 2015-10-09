package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.GanxiServiceParser;
import com.techfly.liutaitai.model.mall.activities.GanxiProductInfoActivity;
import com.techfly.liutaitai.model.mall.adapter.GanxiServiceAdapter;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.PullToRefreshLayout;
import com.techfly.liutaitai.util.view.PullToRefreshLayout.OnRefreshListener;
import com.techfly.liutaitai.util.view.PullableGridView;

public class GanxiFragment extends CommonFragment implements OnRefreshListener {

    private GanxiServiceAdapter mAdapter;
    private PullableGridView mListView;
    private PullToRefreshLayout mPull;
    private ArrayList<Product> mList = new ArrayList<Product>(); 
    private int mPage =1;// 默认请求商品的起始页
    private int mSort = 0;
    private int type;
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_LIST);
        url.setmGetParamPrefix("type").setmGetParamValues(type+"");
        url.setmGetParamPrefix("city").setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getArea().getmId());
        url.setmGetParamPrefix(RequestParamConfig.PAGE).setmGetParamValues(mPage+"");
        param.setmParserClassName(GanxiServiceParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), creatReqSuccessListener(), createErrorListener(), param);
    }
    private Response.Listener<Object> creatReqSuccessListener() {
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

    private Response.ErrorListener createErrorListener() {
        return new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                if (getActivity() == null || isDetached()) {
                    return;
                }
                mPull.refreshFinish(PullToRefreshLayout.FAIL);
                mLoadHandler.removeMessages(Constant.NET_FAILURE);
                mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
               
            }

        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        type =activity.getIntent().getIntExtra(IntentBundleKey.TYPE, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startReqTask(this);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
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
    private void initView(View view) {
        // TODO Auto-generated method stub
        mListView=(PullableGridView) view.findViewById(R.id.gridview_parts);
        mPull = (PullToRefreshLayout) view.findViewById(R.id.layout_parts);
        mPull.setOnRefreshListener(this);
        mAdapter=new GanxiServiceAdapter(getActivity(), mList,Constant.TUANGOU_PRO_ITEM_STYLE);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Product pro = (Product) arg0.getItemAtPosition(arg2);
                Intent intent = null;
                intent = new Intent(getActivity(),
                        GanxiProductInfoActivity.class);
                intent.putExtra(IntentBundleKey.ID, pro.getmId());
                intent.putExtra(IntentBundleKey.TYPE, type);
                intent.putExtra(IntentBundleKey.IMAGE_PATH, pro.getmImg());
                startActivityForResult(intent, 0);
            }
        });
    }

    private void initTitleView(){
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        if(type==0){
            setTitleText("干洗");
        }else if(type==1){
            setTitleText("生鲜");
        }else if(type==2){
            setTitleText("鲜花");
        }else if(type==4){
            setTitleText("奢侈品");
        }
     
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ganxishe_listview,
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
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPage =0;
        mList.clear();
        requestData();
        
    }
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPage = mPage+1;
        requestData();
        
    }
    }

