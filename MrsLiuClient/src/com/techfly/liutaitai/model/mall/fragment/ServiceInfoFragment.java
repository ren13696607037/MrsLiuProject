package com.techfly.liutaitai.model.mall.fragment;

import android.R.integer;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.ServiceInfo;
import com.techfly.liutaitai.model.mall.parser.ServiceInfoParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.pcenter.fragment.MyCollectFragment;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.ManagerListener.CollectListener;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ServiceInfoFragment extends CommonFragment implements CollectListener{
    private ImageView mImg;
    private TextView mNameTv;
    private TextView mPriceTv;
    private TextView mServiceContentTv;
    private TextView mDescTv;
    private Button mButton;
    private String mId;
    private ServiceInfo mInfo;
    private User mUser;
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
        mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
        ManagerListener.newManagerListener().onRegisterCollectListener(this);
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
    	ManagerListener.newManagerListener().onUnRegisterCollectListener(this);
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
        setRightMoreIcon(R.drawable.address_add, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Product product = new Product();
				product.setmId(mInfo.getmId());
				if(mInfo.isCollect()){
					ManagerListener.newManagerListener().notifyCancelCollectListener(product);
				}else{
					ManagerListener.newManagerListener().notifyCollectListener(product);
				}
			}
		});
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

	@Override
	public void cancelCollect(Product product) {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.COLLECT_SERVICE_URL);
        url.setmGetParamPrefix(JsonKey.ServiceDetailKey.SID).setmGetParamValues(product.getmId());
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), createCollectSuccessListener(0), createCollectErrorListener(), param);
	}
	private Response.Listener<Object> createCollectSuccessListener(final int type) {
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
                    	if(type == 0){
                    		showSmartToast(R.string.collect_cancel_success, Toast.LENGTH_SHORT);
                    	}else{
                    		showSmartToast(R.string.collect_success, Toast.LENGTH_SHORT);
                    	}
                    }else{
                        showSmartToast(rInfo.getmMessage(), Toast.LENGTH_LONG);
                    }
                }
            }
        };
    }

    private Response.ErrorListener createCollectErrorListener() {
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
	public void collect(Product product) {
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.COLLECT_SERVICE_URL);
        url.setmGetParamPrefix(JsonKey.ServiceDetailKey.SID).setmGetParamValues(product.getmId());
        param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setPostRequestMethod();
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), createCollectSuccessListener(1), createCollectErrorListener(), param);
	}

}
