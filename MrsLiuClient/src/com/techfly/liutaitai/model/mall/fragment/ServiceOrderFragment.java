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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.ServiceInfo;
import com.techfly.liutaitai.model.mall.parser.ServiceInfoParser;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ServiceOrderFragment extends CommonFragment implements OnClickListener{

    private ImageView mImg;
    private TextView mNameTv;
    private TextView mPriceTv;
    private CheckBox mCheckBox;
    private TextView mAddressTv;
    private TextView mTimeTv;
    private TextView mPhoneTv;
    private TextView mVoucherTv;
    private TextView mJishiTv;
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
             
            }
            
        };
    }

   
   private void onDisplayInfo(){
      
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
        View view = inflater.inflate(R.layout.fragment_service_order, container, false);
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
        mImg = (ImageView) view.findViewById(R.id.img);
        mNameTv = (TextView) view.findViewById(R.id.name);
        mPriceTv = (TextView) view.findViewById(R.id.price);
        
        mAddressTv = (TextView) view.findViewById(R.id.address);
        mAddressTv.setOnClickListener(this);
        
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        mCheckBox.setOnClickListener(this);
        
        mJishiTv = (TextView) view.findViewById(R.id.jishi);
        mJishiTv.setOnClickListener(this);
        
        mPhoneTv = (TextView) view.findViewById(R.id.phone);
        
        mTimeTv = (TextView) view.findViewById(R.id.clock);
        mTimeTv.setOnClickListener(this);
        
        mVoucherTv = (TextView) view.findViewById(R.id.voucher);
        mVoucherTv.setOnClickListener(this);
        
        mButton = (Button) view.findViewById(R.id.order);
        mButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
               
          
            }
        });
        
    }
    
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
      
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }


}
