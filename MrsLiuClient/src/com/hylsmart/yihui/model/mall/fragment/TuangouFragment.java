package com.hylsmart.yihui.model.mall.fragment;

import java.util.ArrayList;

import android.app.Activity;
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
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.ProTuangouParser;
import com.hylsmart.yihui.model.mall.adapter.ProItemAdapter;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.XListView;

public class TuangouFragment extends CommonFragment implements XListView.IXListViewListener{
    private ProItemAdapter mAdapter;
    private XListView mListView;
    private ArrayList<Product> mList = new ArrayList<Product>(); 
    private int mPage =0;// 默认请求商品的起始页
    
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_TUANGOU);
        param.setmParserClassName(ProTuangouParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), creatReqSuccessListener(), createErrorListener(), param);
    }
    private Response.Listener<Object> creatReqSuccessListener() {
        return new Listener<Object>() {

            @Override
            public void onResponse(Object result) {
                AppLog.Logd(result.toString());
                AppLog.Loge(" data success to load" + result.toString());
                if(getActivity()!=null&&!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                    ResultInfo rInfo = (ResultInfo) result;
                    if(rInfo.getmCode()==Constant.RESULT_CODE){
                        ArrayList<Product> list =(ArrayList<Product>) rInfo.getObject();
                        mList.addAll(list);
                        mListView.setPullRefreshEnable(false);
                        if(list.size()<Constant.DEFAULT_SIZE){
                            mListView.setPullLoadEnable(false);
                        }else{
                            mListView.setPullLoadEnable(true);
                        }
                        mAdapter.updateList(mList);
                    }else{
                        showSmartToast(rInfo.getmMessage()+"", Toast.LENGTH_LONG);
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
                mLoadHandler.removeMessages(Constant.NET_FAILURE);
                mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
               
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
        // TODO Auto-generated method stub
        mListView=(XListView) view.findViewById(R.id.listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mAdapter=new ProItemAdapter(getActivity(), mList,Constant.TUANGOU_PRO_ITEM_STYLE);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Product pro = (Product) arg0.getItemAtPosition(position);
                AppLog.Logd("Fly", "id"+pro.getmId());
                UIHelper.toProductInfoActivity(getActivity(), Integer.parseInt(pro.getmId()),ProductInfoFragment.FLAG_TUAN_GOU);
            }
        });
    }

    private void initTitleView(){
        setTitleText(R.string.tuangou_title_txt);
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
     
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuangou,
                container, false);
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
    public void onRefresh() {
        mPage =0;
        mList.clear();
        requestData();
        
    }
    @Override
    public void onLoadMore() {
      mPage = mPage+1;
      requestData();
        
    }
    
    
}
