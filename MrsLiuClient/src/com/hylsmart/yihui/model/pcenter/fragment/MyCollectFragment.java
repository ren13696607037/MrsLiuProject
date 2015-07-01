package com.hylsmart.yihui.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.parser.CommonProListParser;
import com.hylsmart.yihui.model.mall.adapter.ProItemAdapter;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.model.mall.fragment.ProductInfoFragment;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.RequestParamConfig;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.XListView;
import com.hylsmart.yihui.util.view.XListView.IXListViewListener;

public class MyCollectFragment extends CommonFragment implements IXListViewListener{
	private XListView mListView;
	private ArrayList<Product> mList =new ArrayList<Product>();
	private ProItemAdapter mAdapter;
	private TextView mEmptyTv;
	private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
          mAdapter.updateListView(mList);
        }
	};
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startReqTask(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycollect, container, false);
        return view;
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
                "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.pcenter_collect);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	setRightText(R.string.editable_txt,new OnClickListener() {
    	        
    	        @Override
    	        public void onClick(View view) {
    	            // TODO Auto-generated method stub 
    	           TextView tv = (TextView) view;
    	           if(tv.getText().toString().equals(getString(R.string.editable_txt))){
    	               setRightText(getString(R.string.address_delete));
    	               updateCollectEdit();
    	           }else{
    	             
    	               deleteChoosePro();
    	           }
    	      
    	        }
    	    });
    	mListView=(XListView) view.findViewById(R.id.collect_list);
    	mListView.setPullLoadEnable(false);
    	mListView.setPullRefreshEnable(true);
    	mEmptyTv = (TextView) view.findViewById(R.id.empty);
        mAdapter=new ProItemAdapter(getActivity(), mList,Constant.COLLECT_PRO_ITEM_STYLE);
        mListView.setAdapter(mAdapter);
        mListView.setXListViewListener(this);
    	mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                Product pro =  (Product) arg0.getItemAtPosition(position);
                UIHelper.toProductInfoActivity(getActivity(), Integer.parseInt(pro.getmId()),ProductInfoFragment.FLAG_NORMAL);
            }
        });
    }

    protected void deleteChoosePro() {
        StringBuffer buffer = new StringBuffer();
        for(Product product:mList){
            if(product.ismIsCheck()){
                buffer.append(product.getmId()+",");
            }
        }
        if(!TextUtils.isEmpty(buffer.toString())){
            String ids = buffer.toString();
            ids = ids.substring(0, buffer.length()-1);
            RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_CANCEL_COLLECT );
//            url.setmGetParamPrefix(RequestParamConfig.PRINCIPAL).setmGetParamValues("5149");
            url.setmGetParamPrefix(RequestParamConfig.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
            url.setmGetParamPrefix(RequestParamConfig.COMMODITY_ID ).setmGetParamValues(ids);
            param.setmParserClassName(CommonParser.class.getName());
            param.setmHttpURL(url);
            RequestManager.getRequestData(getActivity(), creatReqSuccessListener(), createErrorListener(), param);
        }else{
          showSmartToast("请选择要取消收藏的商品", Toast.LENGTH_LONG);
        }
        
        
    }

    protected void updateCollectEdit() {
        for(Product product:mList){
            product.setmEditable(true);
        }
        mAdapter.notifyDataSetChanged();
        
    }

    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_COLLECT);
//        url.setmGetParamPrefix(RequestParamConfig.PRINCIPAL).setmGetParamValues("5149");
        url.setmGetParamPrefix(RequestParamConfig.PRINCIPAL).setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getUser().getmId());
        param.setmParserClassName(CommonProListParser.class.getName());
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
                        setRightText(getString(R.string.editable_txt));
                        if(rInfo.getObject()!=null){
                            ArrayList<Product> list =(ArrayList<Product>) rInfo.getObject();
                            mListView.stopRefresh();
                            if(list.size()==0){
                                mLoadingLayout.setVisibility(View.GONE);
                                mEmptyTv.setVisibility(View.VISIBLE);
                                setRightText("");
                            }else{
                                mLoadingLayout.setVisibility(View.VISIBLE);
                                mEmptyTv.setVisibility(View.GONE);
                            }
                            mList.addAll(list);
                            mHandler.sendEmptyMessage(0);
                        }else{
                            mList.clear();
                            requestData();
                        }
                    }else{
                        showSmartToast(rInfo.getmMessage(), Toast.LENGTH_LONG);
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
    public void onRefresh() {
        mList.clear();
        requestData();
        
      
        
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }
}
