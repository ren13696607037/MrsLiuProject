package com.techfly.liutaitai.model.mall.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.mall.bean.ServiceInfo;
import com.techfly.liutaitai.model.mall.parser.ServiceInfoParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ServiceInfoFragment extends CommonFragment{
    private ImageView mImg;
    private TextView mNameTv;
    private TextView mPriceTv;
    private TextView mServiceContentTv;
    private TextView mDescTv;
    private Button mButton;
    private String mId;
    private ServiceInfo mInfo;
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SERVICE_DETAIL);
        url.setmGetParamPrefix1("sid");
        url.setmGetParamValues1(String.valueOf(mId));
        if (!TextUtils.isEmpty(mId)) {
            url.setmGetParamPrefix2("principal");
            url.setmGetParamValues2(mId);
        }
        // url.setmBaseUrl("http://www.hylapp.com:10001/apis/goods/detail?pid=1533");
        param.setmHttpURL(url);
        param.setmParserClassName(ServiceInfoParser.class.getName());
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
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                mInfo   = (ServiceInfo) object;
                onDisplayInfo();
            }
            
        };
    }

   
   private void onDisplayInfo(){
       mDescTv.setText(mInfo.getmDesc());
       ImageLoader.getInstance().displayImage(mInfo.getmImg(), mImg, ImageLoaderUtil.mDetailsLoaderOptions);
       mNameTv.setText(mInfo.getmName());
       mPriceTv.setText("￥"+mInfo.getmPrice()+"/"+mInfo.getmUnit());
       mServiceContentTv.setText(mInfo.getmServiceConent());
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
        mId = activity.getIntent().getStringExtra(IntentBundleKey.ID);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDetach() {
       
        super.onDetach();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        
        startReqTask(this);
        
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_info, container, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    
    
    private void initView(View view) {
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText("服务详情");
        mDescTv = (TextView) view.findViewById(R.id.desc);
        mImg = (ImageView) view.findViewById(R.id.img);
        mNameTv = (TextView) view.findViewById(R.id.name);
        mPriceTv = (TextView) view.findViewById(R.id.price);
        mServiceContentTv = (TextView) view.findViewById(R.id.service_content);
        mButton = (Button) view.findViewById(R.id.order);
        mButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
                int userId = 0;
                if (user != null) {
                    userId = Integer.parseInt(user.getmId());
                }
                if (userId == 0) {
                    UIHelper.toLoginActivity(getActivity());
                    return;
                }
                UIHelper.toSomeIdActivity(ServiceInfoFragment.this,ServiceOrderActivity.class.getName(), mId, Integer.parseInt(mInfo.getmType()));
            }
        });
        
    }
    
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
      
        super.onSaveInstanceState(outState);
    }

}
