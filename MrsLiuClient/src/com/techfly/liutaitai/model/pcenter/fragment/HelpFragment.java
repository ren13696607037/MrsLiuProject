package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class HelpFragment extends CommonFragment {
    private WebView mWebView;
    private TextView mTextView;
    @Override
    public void requestData() {
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.HELP_CENTER);
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
        
    }
   
    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                    if(object instanceof ResultInfo){
                        ResultInfo info = (ResultInfo) object;
                        if(info.getmCode()==0 && !TextUtils.isEmpty(info.getmData())&&!"null".equals(info.getmData())){
                            AppLog.Logd("Fly", "data"+info.getmData());
                            mWebView.loadDataWithBaseURL(null, info.getmData(), "text/html",
                                    "UTF-8", null);
                        }else{
                            mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                            mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
//                            mHelpTv.setText("内容暂无");
                        }
                    }
            }
        };
    }
    private WebViewClient getClient() {
        // TODO Auto-generated method stub
        WebViewClient mClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
           
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
            }

        };
        return mClient;
    }
    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                AppLog.Loge(" data failed to load"+error.getMessage());
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
//        startReqTask(this);
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
//        if (mWebView != null) {
//            mWebView.getSettings().setLoadWithOverviewMode(false);
//            mWebView.getSettings().setUseWideViewPort(false);
//            mWebView.getSettings().setJavaScriptEnabled(false);
//            mWebView.getSettings().setSupportZoom(false);
//            mWebView.getSettings().setBuiltInZoomControls(false);
//            mWebView.destroy();
//       }
        
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = (TextView) view.findViewById(R.id.help);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
//        mWebView = (WebView) view.findViewById(R.id.help);
//        mWebView.setWebViewClient(getClient());
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setSupportZoom(false);
//        mWebView.getSettings().setBuiltInZoomControls(false);
//        mWebView.getSettings()
//                .setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 可能的话不要超过屏幕宽度
//        mWebView.setInitialScale(120);
        initTitleView();
    }
    private void initTitleView(){
        setTitleText(R.string.pcenter_help);
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pecnter_help,
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
    
    
}
