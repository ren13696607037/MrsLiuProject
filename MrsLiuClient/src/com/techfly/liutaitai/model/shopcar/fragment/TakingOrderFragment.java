package com.techfly.liutaitai.model.shopcar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.bean.ConfirmOrder;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.parser.ConfrimOrderParser;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.model.shopcar.adapter.OrderProAdapter;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment;
import com.techfly.liutaitai.util.view.ListViewForScrollView;

public class TakingOrderFragment extends CreateOrderPayCommonFragment implements OnClickListener{

    private  OrderProAdapter mAdapter;
    private TextView mNickName;
    private TextView mPhone;
    private TextView mAddress;
    private EditText mEt;
    private TextView mTotalPriceTv;
    private TextView mDeleverFeeTv;
    private TextView mActualPriceTv;
    private TextView mCommitOrderTv;
    private  boolean mIsFromShopCar = true;
    private TextView mProductNameTv;
    private TextView mProPriceTv;
    private TextView mProCountTv;
    private ImageView mProImg;
    private RelativeLayout mProductInfoLayout;
    private ListViewForScrollView mListView;
    private AddressManage mAddressManage;
    private Product mProduct;
    private RelativeLayout mWholeLayout;
    private boolean mIsFirstTakingOrder;
    private RelativeLayout mAddressRelativeLayout;
    private int type;
    protected boolean mIsUseVoucher;
    protected float mDeliverFee;
    private String mVoucherId="0";
    @Override
    public void requestData() {
       
       
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mIsFromShopCar = activity.getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
        type = activity.getIntent().getIntExtra(IntentBundleKey.TYPE, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onConfirmOrder();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	if(resultCode==Constant.ORDER_ADDRESS_SUCCESS||resultCode==Constant.ORDER_CITY_SUCCESS){
    		if(data!=null&&data.getSerializableExtra(IntentBundleKey.ADDRESS_EXTRA)!=null){
                mAddressManage = (AddressManage) data.getSerializableExtra(IntentBundleKey.ADDRESS_EXTRA);
                onDisplay();
            }
//    	}
            
    }
   
    private void onDisplay() {
        mWholeLayout.setVisibility(View.VISIBLE);
        if(mAddressManage!=null&&!TextUtils.isEmpty(mAddressManage.getmDetail()) && !mAddressManage.getmDetail().equals("null")){
            mIsFirstTakingOrder = false;
            mAddressRelativeLayout.setVisibility(View.VISIBLE);
            mPhone.setText(mAddressManage.getmPhone());
            mAddress.setText(mAddressManage.getmCity()+mAddressManage.getmDetail());
            mNickName.setText(mAddressManage.getmName());
        }else{
            mIsFirstTakingOrder = true;
            mAddressRelativeLayout.setVisibility(View.GONE);
        }
        if(mIsFromShopCar){
            mProductInfoLayout.setVisibility(View.GONE);
            mAdapter = new OrderProAdapter(getActivity(), ShopCar.getShopCar().getCheckShopproductList());
            mListView.setAdapter(mAdapter);
            mTotalPriceTv.setText("￥"+ShopCar.getShopCar().getTotalPrice()+"");
            mActualPriceTv.setText("￥"+ShopCar.getShopCar().getTotalPrice()+"");
        }else{
          mProductInfoLayout.setVisibility(View.VISIBLE);
          mProduct = (Product) getActivity().getIntent().getSerializableExtra(IntentBundleKey.PRODUCT);
         
          mProductNameTv.setText(mProduct.getmName());
          mProPriceTv.setText("￥"+mProduct.getmPrice()+"");
          mProCountTv.setText("x"+mProduct.getmAmount()+"");
          if(!TextUtils.isEmpty(mProduct.getmImg())&&!mProduct.getmImg().equals("null")){
              ImageLoader.getInstance().displayImage(mProduct.getmImg(), mProImg,ImageLoaderUtil.mHallIconLoaderOptions);
          }
          float totalPrice =(mProduct.getmPrice()*mProduct.getmAmount());
          long l1 = Math.round(totalPrice * 100); // 四舍五入
          totalPrice = (float) (l1 / 100.00); // 注意：使用 100.0 而不是 100
          mTotalPriceTv.setText("￥"+    totalPrice+"");
          mActualPriceTv.setText("￥"+totalPrice+"");
        }
        mDeleverFeeTv.setText("￥"+mDeliverFee);
        
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        mNickName = (TextView) view.findViewById(R.id.person_info);
        mPhone = (TextView) view.findViewById(R.id.phone);
        mAddress = (TextView) view.findViewById(R.id.address);
        mEt = (EditText) view.findViewById(R.id.note);
        mTotalPriceTv = (TextView) view.findViewById(R.id.total_price);
        mDeleverFeeTv = (TextView) view.findViewById(R.id.deliver_price);
        mActualPriceTv = (TextView) view.findViewById(R.id.actual_price);
        mCommitOrderTv = (TextView) view.findViewById(R.id.commit_order);
        mCommitOrderTv.setOnClickListener(this);
        mProductNameTv = (TextView) view.findViewById(R.id.product_name);
        mProPriceTv = (TextView) view.findViewById(R.id.product_price);
        mProCountTv = (TextView) view.findViewById(R.id.product_count);
        mProImg = (ImageView) view.findViewById(R.id.product_icon);
        mProductInfoLayout = (RelativeLayout) view.findViewById(R.id.product_info);
        mListView = (ListViewForScrollView) view.findViewById(R.id.listview);
        mWholeLayout = (RelativeLayout) view.findViewById(R.id.layout);
        mWholeLayout.setVisibility(View.GONE);
        mAddressRelativeLayout = (RelativeLayout) view.findViewById(R.id.address_info);
        
        mAddressRelativeLayout.setOnClickListener(this);
    }

    private void initTitleView(){
        setTitleText(R.string.confirm_order);
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taking_order,
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
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
        case R.id.commit_order:
            if(mIsFirstTakingOrder){
                UIHelper.toAddressAddActivity(this);// 跳到地址增加页面
           }else{
               if(mIsFromShopCar){
                   StringBuffer buffer = new StringBuffer();
                   for(Product product: ShopCar.getShopCar().getCheckShopproductList()){
                       buffer.append(product.getmName()+" ");
                   }
                   onCommitOrder(Constant.PRODUCT_TYPE_ENTITY ,Constant.PAY_TYPE_CREATE,ShopCar.getShopCar().getTotalPrice()+"",buffer.toString());   
               }else{
                       float totalPrice =(float) (mProduct.getmPrice()*mProduct.getmAmount());
                       long   l1   =   Math.round(totalPrice*100);   //四舍五入   
                       totalPrice   =   (float) (l1/100.00);               //注意：使用   100.0   而不是   100   
                       onCommitOrder(Constant.PRODUCT_TYPE_ENTITY ,Constant.PAY_TYPE_CREATE,totalPrice+"",mProduct.getmName());   
               }
           }
            break;
        case R.id.address_info:
            UIHelper.toAddressManageActivity(this);// 跳到地址管理页面
            break;
        default:
            break;
        }
    }
   
    private void onConfirmOrder(){
        mLoadHandler.removeMessages(Constant.NET_LOAD);
        mLoadHandler.sendEmptyMessage(Constant.NET_LOAD);
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.CONFIRM_ORDER_REQUEST);
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
        url.setmGetParamPrefix("type")
                .setmGetParamValues(type+"");
        url.setmGetParamPrefix("total")
        .setmGetParamValues(ShopCar.getShopCar().getTotalPrice()+"");
        param.setmParserClassName(ConfrimOrderParser.class.getName());
        param.setmHttpURL(url);
        RequestManager.getRequestData(getActivity(), creatConfirmReqSuccessListener(), createErrorListener(), param);
    }
    private Response.Listener<Object> creatConfirmReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object result) {
                AppLog.Logd(result.toString());
                AppLog.Loge(" data success to load" + result.toString());
                if(getActivity()!=null&&!isDetached()){
                    mWholeLayout.setVisibility(View.VISIBLE);
                   ResultInfo info = (ResultInfo) result;
                   if(info.getmCode()==0){
                       mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                       mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                       ConfirmOrder confirm = (ConfirmOrder) info.getObject();
                       if(!TextUtils.isEmpty(confirm.getAddress())){
                           mAddressManage = confirm.getmAddressManage();
                       }else{
                           mAddressManage =null;
                       }
                       mIsUseVoucher = confirm.ismIsUseVoucher();
                       mDeliverFee = confirm.getmDeliverFee();
                       onDisplay();
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
    public String onEncapleOrderInfo(HttpURL url) {
        if(mIsFromShopCar){
            for(Product pro : ShopCar.getShopCar().getCheckShopproductList()){
                url.setmGetParamPrefix("ids");
                url.setmGetParamValues(pro.getmId());
            }
            url.setmGetParamPrefix("total");
            url.setmGetParamValues(ShopCar.getShopCar().getTotalPrice()+"");
        }else{
            url.setmGetParamPrefix("ids");
            url.setmGetParamValues(mProduct.getmId());
            
            float totalPrice =(mProduct.getmPrice()*mProduct.getmAmount());
            long l1 = Math.round(totalPrice * 100); // 四舍五入
            totalPrice = (float) (l1 / 100.00); // 注意：使用 100.0 而不是 100
            url.setmGetParamPrefix("total");
            url.setmGetParamValues( totalPrice+"");
            
        }
        url.setmGetParamPrefix("addressId");
        url.setmGetParamValues(mAddressManage.getmId());
        
        
        url.setmGetParamPrefix("freight");
        url.setmGetParamValues("0");
        
        url.setmGetParamPrefix("voucherId");
        if(!mIsUseVoucher){
            url.setmGetParamValues("0");
        }else{
            url.setmGetParamValues(mVoucherId);
        }
        
        url.setmGetParamPrefix("comment");
        if(TextUtils.isEmpty(mEt.getText().toString())){
            url.setmGetParamValues("");
        }else{
            url.setmGetParamValues(mEt.getText().toString());
        }
        
        url.setmGetParamPrefix("type");
        url.setmGetParamValues(type+"");
    
        
        return "success";
        
    
    }

    

    @Override
    public void onOrderCreateSuccess(String orderId, String money, String proName) {
       
        TakingOrderActivity activity =(TakingOrderActivity) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString(IntentBundleKey.ORDER_ID, orderId);
        bundle.putString(IntentBundleKey.ORDER_MONEY, money);
        bundle.putString(IntentBundleKey.ORDER_PRODUCT, proName);
        activity.showOrderCreateFragment(bundle);
    }

    
    
}
