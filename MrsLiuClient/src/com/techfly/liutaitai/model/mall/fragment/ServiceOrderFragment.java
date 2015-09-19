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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.activities.JishiListActivity;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.mall.activities.ServiceTimeActivity;
import com.techfly.liutaitai.model.mall.bean.Jishi;
import com.techfly.liutaitai.model.mall.bean.ServiceInfo;
import com.techfly.liutaitai.model.mall.parser.ServiceInfoParser;
import com.techfly.liutaitai.model.pcenter.activities.MyVoucherActivity;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.DateUtils;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment;

public class ServiceOrderFragment extends CreateOrderPayCommonFragment implements
        OnClickListener {

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
    private long mSelectTimeMills = 0;
    private int mVoucherId;
    private String mOrderId;
    private double mJingdu;// 经度
    private double mWeidu;// 纬度
    private AddressManage mAddressManage;
    private Jishi mJishi;
    private boolean mIsYujia;
    private float mVoucherMoney;
    private int mType;

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

    private boolean onJudgeReq() {
        if (mAddressManage == null) {
            showSmartToast("请选择服务地点", Toast.LENGTH_LONG);
            return false;
        }
//        if (mJishi == null) {
//            showSmartToast("请选择技师", Toast.LENGTH_LONG);
//            return false;
//        }
        if (mSelectTimeMills == 0) {
            showSmartToast("请选择服务时间", Toast.LENGTH_LONG);
            return false;
        }
        if(Float.parseFloat(mInfo.getmPrice())>mVoucherMoney){
            mInfo.setmPrice((Float.parseFloat(mInfo.getmPrice())-mVoucherMoney)+"");
        }else{
            mInfo.setmPrice("0.01");
        }
        return true;
    }

   

    private Response.Listener<Object> createOrderReqSuccessListener() {

        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                ResultInfo info = (ResultInfo) object;
                mOrderId = info.getmData();
            }

        };
    }

    private Response.Listener<Object> createMyReqSuccessListener() {

        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                mInfo = (ServiceInfo) object;
                onDisplayInfo();
            }

        };
    }

    private void onDisplayInfo() {
        ImageLoader.getInstance().displayImage(mInfo.getmImg(), mImg,
                ImageLoaderUtil.mHallIconLoaderOptions);
        mNameTv.setText(mInfo.getmName());
        mPriceTv.setText("￥" + mInfo.getmPrice() + "/" + mInfo.getmUnit());
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
        mType = activity.getIntent().getIntExtra(IntentBundleKey.TYPE,0);
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (data != null) {
                mSelectTimeMills = data.getLongExtra("data", 0);
                mTimeTv.setText(DateUtils.getTime(mSelectTimeMills,
                        "yyyy-MM-dd HH:mm"));
            }
        } else if (requestCode == Constant.ORDER_CITY_INTENT) {
            if (data != null
                    && data.getSerializableExtra(IntentBundleKey.ADDRESS_EXTRA) != null) {
                mAddressManage = (AddressManage) data
                        .getSerializableExtra(IntentBundleKey.ADDRESS_EXTRA);
                mAddressTv.setText(mAddressManage.getmCity()
                        + mAddressManage.getmDetail());
            }
        } else if (requestCode == 101) {
            if (data != null) {
                mVoucherId =data.getIntExtra(IntentBundleKey.VOUCHER_EXTRA,0);
                mVoucherMoney = data.getFloatExtra(IntentBundleKey.VOUCHER_MONEY,0);
                if( mVoucherMoney!=0){
                    mVoucherTv.setText( "￥"+ mVoucherMoney+"");
                }
             
            }
       
            // voucher
        } else {
            // ji'shi
            if (data != null) {
                mJishi = (Jishi) data.getSerializableExtra("data");
                mJishiTv.setText(mJishi.getmName());
            }

        }
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
        View view = inflater.inflate(R.layout.fragment_service_order,
                container, false);
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
        if(mType==1){
            view.findViewById(R.id.yujia).setVisibility(View.GONE);
            view.findViewById(R.id.line1).setVisibility(View.GONE);
        }
       
    
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        mCheckBox.setOnClickListener(this);
        mCheckBox.setChecked(mIsYujia);
        mJishiTv = (TextView) view.findViewById(R.id.jishi);
        mJishiTv.setOnClickListener(this);

        mPhoneTv = (TextView) view.findViewById(R.id.phone);
        mPhoneTv.setText(SharePreferenceUtils.getInstance(getActivity())
                .getUser().getmPhone());
        mTimeTv = (TextView) view.findViewById(R.id.clock);
        mTimeTv.setOnClickListener(this);

        mVoucherTv = (TextView) view.findViewById(R.id.voucher);
        mVoucherTv.setOnClickListener(this);

        mButton = (Button) view.findViewById(R.id.order);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (onJudgeReq()) {
                    onCommitOrder(Constant.PRODUCT_TYPE_SERVICE, Constant.PAY_TYPE_CREATE,mInfo.getmPrice()+"",mInfo.getmName());
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.address:
            UIHelper.toAddressManageActivity(this);// 跳到地址管理页面
            break;
        case R.id.jishi:
            UIHelper.toSomeIdActivity(this, JishiListActivity.class.getName(),
                    mSelectTimeMills + "", Integer.parseInt(mInfo.getmType()));
            break;

        case R.id.clock:
            Intent intent = null;
            intent = new Intent(getActivity(), ServiceTimeActivity.class);
            this.startActivityForResult(intent, 100);

            break;
        case R.id.voucher:
            Intent intentss = null;
            intentss = new Intent(getActivity(), MyVoucherActivity.class);
            intentss.putExtra(IntentBundleKey.VOUCHER_EXTRA, 8);
            this.startActivityForResult(intentss, 101);
            break;
        case R.id.check_box:
           
            mIsYujia = !mIsYujia;
            mCheckBox.setChecked(mIsYujia);
        default:
            break;
        }
    }

    @Override
    public String onEncapleOrderInfo(HttpURL url) {
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "service/addreservation");// 生鲜请求地址组装
        url.setmGetParamPrefix("sid");
        url.setmGetParamValues(mInfo.getmId());

        url.setmGetParamPrefix("type");
        url.setmGetParamValues(mInfo.getmType());

        url.setmGetParamPrefix("time");
        url.setmGetParamValues(mSelectTimeMills + "");

        url.setmGetParamPrefix("address");
        url.setmGetParamValues(mAddressManage.getmCity() + "  "
                + mAddressManage.getmDetail());

        
        if(mJishi != null){
        	url.setmGetParamPrefix("mid");
        	url.setmGetParamValues(mJishi.getmId());
        }
        

        url.setmGetParamPrefix("mobile");
        url.setmGetParamValues(SharePreferenceUtils.getInstance(getActivity())
                .getUser().getmPhone());
        
        url.setmGetParamPrefix("clean");
        url.setmGetParamValues(mIsYujia?"1":"0");
        if(mVoucherId!=0){
            url.setmGetParamPrefix("vocher");
            url.setmGetParamValues(mVoucherId+"");
        }
        url.setmGetParamPrefix("lng");
        url.setmGetParamValues("37.1569");

        url.setmGetParamPrefix("lat");
        url.setmGetParamValues("116.2564");
        return "success";
    }

    @Override
    public void onOrderCreateSuccess(String orderId, String money,
            String proName) {
        ServiceOrderActivity activity =(ServiceOrderActivity) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString(IntentBundleKey.ORDER_ID, orderId);
        bundle.putString(IntentBundleKey.ORDER_MONEY, money);
        bundle.putString(IntentBundleKey.ORDER_PRODUCT, proName);
        activity.showOrderCreateFragment(bundle);
        
    }

}
