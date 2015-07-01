package com.hylsmart.yihui.model.shopcar.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.BuyInfo;
import com.hylsmart.yihui.bizz.parser.AddressManageParser;
import com.hylsmart.yihui.bizz.shopcar.ShopCar;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.model.pcenter.bean.Address;
import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.model.shopcar.activities.TakingOrderActivity;
import com.hylsmart.yihui.model.shopcar.adapter.OrderProAdapter;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.ImageLoaderUtil;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.fragment.CreateOrderPayCommonFragment;
import com.hylsmart.yihui.util.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    private Address mAddressManage;
    private Product mProduct;
//    private RelativeLayout mWholeLayout;
    private boolean mIsFirstTakingOrder;
    private RelativeLayout mAddressRelativeLayout;
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        onAddressRequest();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mIsFromShopCar = activity.getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
       
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        onAddressRequest();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode==Constant.ORDER_ADDRESS_SUCCESS||resultCode==Constant.ORDER_CITY_SUCCESS){
    		if(data!=null&&data.getSerializableExtra(IntentBundleKey.ADDRESS_VALUES)!=null){
                mAddressManage = (Address) data.getSerializableExtra(IntentBundleKey.ADDRESS_VALUES);
                onDisplay();
            }
    	}
            
    }
   
    private void onDisplay() {
//        mWholeLayout.setVisibility(View.VISIBLE);
        if(mAddressManage!=null&&!TextUtils.isEmpty(mAddressManage.getmAddress()) && !mAddressManage.getmAddress().equals("null")){
            mIsFirstTakingOrder = false;
            mAddressRelativeLayout.setVisibility(View.VISIBLE);
            mPhone.setText(mAddressManage.getmPhone());
            mAddress.setText(mAddressManage.getmAddress().replace("=", "-")+"/"+mAddressManage.getmDetail());
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
        mDeleverFeeTv.setText("￥0");
        
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
//        mWholeLayout = (RelativeLayout) view.findViewById(R.id.layout);
//        mWholeLayout.setVisibility(View.GONE);
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
   
    /**
     * 地址请求
     */
    private void onAddressRequest() {
        // to do nothing
        mLoadHandler.removeMessages(Constant.NET_LOAD);
        mLoadHandler.sendEmptyMessage(Constant.NET_LOAD);
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ADDRESS_URL);
        url.setmGetParamPrefix(JsonKey.UserKey.PRINCIPAL)
                .setmGetParamValues(
                        SharePreferenceUtils.getInstance(getActivity())
                                .getUser().getmId());
        param.setmParserClassName(AddressManageParser.class.getName());
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
//                    mWholeLayout.setVisibility(View.VISIBLE);
                    mLoadingLayout.setVisibility(View.GONE);
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                    ArrayList<Address> list = (ArrayList<Address>) result;
                    if(list==null || list.size()==0){
                        mAddressManage = null;
                    }else{
                        for(Address address: list){
                            if(address.ismIsSelect()){
                                mAddressManage = address;
                            }
                        }
                        if(mAddressManage ==null){
                            mAddressManage = list.get(0);
                        }
                    }
                    onDisplay();
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
    public String onEncapleOrderInfo() {
        
        String jsonString;
        BuyInfo bInfo = new BuyInfo();
        User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
        if(user!=null && Integer.parseInt(user.getmId())!=0){
          bInfo.setUserId(Integer.parseInt(user.getmId()));
        }
        bInfo.setAddressId(Integer.parseInt(mAddressManage.getmId()));
        bInfo.setDelivery(0);
        if(!TextUtils.isEmpty(mEt.getText())){
            bInfo.setMemo(mEt.getText().toString());
        }else{
            bInfo.setMemo("");
        }
        if(mIsFromShopCar){
            bInfo.setType(0);
            bInfo.setAllPrice(ShopCar.getShopCar().getTotalPrice());
            bInfo.setCost(ShopCar.getShopCar().getTotalPrice());
            bInfo.setProductList(ShopCar.getShopCar().getCheckShopproductList());
        }else{
            if(mProduct.getmType()!=0){
                bInfo.setType(1);
            }else{
                bInfo.setType(0);
            }
            float totalPrice =(mProduct.getmPrice()*mProduct.getmAmount());
            long l1 = Math.round(totalPrice * 100); // 四舍五入
            totalPrice = (float) (l1 / 100.00); // 注意：使用 100.0 而不是 100
            bInfo.setCost(totalPrice);
            bInfo.setAllPrice(totalPrice);
            List<Product> list = new ArrayList<Product>();
            list.add(mProduct);
            bInfo.setProductList(list);
        }
      
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        jsonString = gson.toJson(bInfo);
        AppLog.Logd("Fly", "jsonString==="+jsonString);
        return jsonString;
        
    
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
