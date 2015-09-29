package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;

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
import com.techfly.liutaitai.model.mall.activities.ProductInfoActivity;
import com.techfly.liutaitai.model.mall.bean.Comments;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.parser.ProCommentParser;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;

public class ProductCommentFragment extends CommonFragment implements XListView.IXListViewListener {
    private CommonAdapter<Comments> mAdapter;
    private XListView mListView;
    private ArrayList<Comments> mList = new ArrayList<Comments>(); 
    private int mPage =1;// 默认请求商品的起始页
    private int type;
    private String mId;
    @Override
    public void requestData() {
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "common/reviews");
        AppLog.Logd("Fly", "type===="+type+"");
        url.setmGetParamPrefix("type").setmGetParamValues(0+"");
        url.setmGetParamPrefix("id").setmGetParamValues(mId);
        url.setmGetParamPrefix(RequestParamConfig.PAGE).setmGetParamValues(mPage+"");
        param.setmParserClassName(ProCommentParser.class.getName());
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
                        mListView.setPullRefreshEnable(true);
                        ArrayList<Comments> list =(ArrayList<Comments>) rInfo.getObject();
                        mList.addAll(list);
                        if(list.size()<Constant.DEFAULT_SIZE){
                            mListView.setPullLoadEnable(false);
                        }else{
                            mListView.setPullLoadEnable(true);
                        }
                   
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
        type =activity.getIntent().getIntExtra(IntentBundleKey.TYPE, 0);
        mId = activity.getIntent().getStringExtra(IntentBundleKey.ID);
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
        mAdapter = new CommonAdapter<Comments>(getActivity(), mList,
                R.layout.item_ratelist) {
            @Override
            public void convert(ViewHolder holder, Comments item, int position) {
                holder.setText(R.id.iratelist_name, item.getmName());
                holder.setText(R.id.iratelist_time, item.getmTime());
                holder.setText(R.id.iratelist_content,
                        item.getmContent());
                holder.setRating(R.id.iratelist__bar,Float.parseFloat(item.getmStarScore()));

            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

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
    }

    private void initTitleView(){
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText("评论列表");
      
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_listview,
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
