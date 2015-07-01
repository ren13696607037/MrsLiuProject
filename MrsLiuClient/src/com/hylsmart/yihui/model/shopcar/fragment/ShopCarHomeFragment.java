package com.hylsmart.yihui.model.shopcar.fragment;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.bizz.shopcar.OnShopCarLisManager;
import com.hylsmart.yihui.bizz.shopcar.OnShopCarListener;
import com.hylsmart.yihui.bizz.shopcar.ShopCar;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.model.shopcar.activities.TakingOrderActivity;
import com.hylsmart.yihui.model.shopcar.adapter.ShopCarAdapter;
import com.hylsmart.yihui.model.shopcar.adapter.ShopCarAdapter.EditCallBack;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AlertDialogUtils;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.RequestParamConfig;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class ShopCarHomeFragment extends CommonFragment implements OnClickListener,OnShopCarListener
,EditCallBack,OnCheckedChangeListener{
    private ShopCarAdapter mAdapter;
    private ListView mListView;
    private CheckBox mCheckBox;
    private CheckBox mCheckBox2;
    private TextView mTotalPriceTv;
    private TextView mConfirmTv;
    private TextView mDelAllTv;
    private List<Product> mProList;
    private boolean mIsHomeCart = true;
    private RelativeLayout mEditRelativeLayout;
    private RelativeLayout mFinishRelativeLayout;
    private TextView mCollectTv;
    private Dialog mDialog;
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mIsHomeCart = getActivity().getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        OnShopCarLisManager.getShopCarLisManager().onRegisterShopCarListener(this);
    }

    @Override
    public void onDestroy() {
        onChangeProAllNotEdit();
        OnShopCarLisManager.getShopCarLisManager().onUnRegisterShopCarListener(this);
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
        mListView = (ListView) view.findViewById(R.id.listView);
        mCheckBox = (CheckBox) view.findViewById(R.id.product_all_checkbox);
        mCheckBox2 = (CheckBox) view.findViewById(R.id.product_all_checkbox2);
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckBox2.setOnCheckedChangeListener(this);
        mTotalPriceTv = (TextView) view.findViewById(R.id.money_total);
        mConfirmTv = (TextView) view.findViewById(R.id.confirm_btn);
        mConfirmTv.setOnClickListener(this);
        mCollectTv = (TextView) view.findViewById(R.id.collect);
        mCollectTv.setOnClickListener(this);
        mDelAllTv = (TextView) view.findViewById(R.id.del_all);
        mDelAllTv.setOnClickListener(this);
        mAdapter = new ShopCarAdapter(getActivity(), null,this);
        mListView.setAdapter(mAdapter);
        mEditRelativeLayout = (RelativeLayout) view.findViewById(R.id.editable_area);
        mFinishRelativeLayout = (RelativeLayout) view.findViewById(R.id.finish_area);
        onUpdateData();
    }
    
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        onUpdateData();
    }

    private void onUpdateData(){
        mProList = ShopCar.getShopCar().getShopproductList();
        if(mProList==null || mProList.size()==0){
            onDisplayNoData();
        }else{
            onDispalyData();
        }
       
    }
    
    private void onDisplayNoData() {
        mListView.setVisibility(View.GONE);
        mCheckBox.setClickable(false);
        mCheckBox2.setClickable(false);
        mConfirmTv.setClickable(false);
        mDelAllTv.setClickable(false);
        mTotalPriceTv.setText("￥0.00");
    }
    
    private void onDispalyData(){
        mListView.setVisibility(View.VISIBLE);
        mAdapter.updateListView(mProList);
        mCheckBox.setClickable(true);
        mCheckBox2.setClickable(true);
        mConfirmTv.setClickable(true);
        mDelAllTv.setClickable(true);
        mDelAllTv.setText(getString(R.string.address_delete)+"("+ShopCar.getShopCar().getShopCheckedAmountSum()+")");
        if(ShopCar.getShopCar().getShopAmountSum()>0){
            setTitleText(getString(R.string.home_shopcar_tab)+"("+ShopCar.getShopCar().getShopAmountSum()+")");
        }else{
            setTitleText(getString(R.string.home_shopcar_tab));
        }
        mConfirmTv.setText(getString(R.string.taking_order_txt)+"("+ShopCar.getShopCar().getShopCheckedAmountSum()+")");
       
        mTotalPriceTv.setText("￥"+ShopCar.getShopCar().getTotalPrice());
        if(ShopCar.getShopCar().getShopCheckedAmountSum()==ShopCar.getShopCar().getShopAmountSum()){
            mCheckBox.setChecked(true);
            mCheckBox2.setChecked(true);
        }else{
            mCheckBox.setChecked(false);
            mCheckBox2.setChecked(false);
        }
        mConfirmTv.setText(getString(R.string.taking_order_txt)+"("+ShopCar.getShopCar().getShopCheckedAmountSum()+")");
    }

    private void initTitleView(){
       if(mIsHomeCart){
          setLeftHeadIcon(-1);
      }else{
          setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
      }
       setTitleText(getString(R.string.home_shopcar_tab)+"("+ShopCar.getShopCar().getShopAmountSum()+")");
       setRightText(R.string.editable_all,new OnClickListener() {
        
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub 
           TextView tv = (TextView) view;
           if(tv.getText().toString().equals(getString(R.string.editable_all))){
               setRightText(getString(R.string.finish));
               mFinishRelativeLayout.setVisibility(View.GONE);
               mEditRelativeLayout.setVisibility(View.VISIBLE);
               onChangeProAllEdit();
            
           }else{
               setRightText(getString(R.string.editable_all));
               mFinishRelativeLayout.setVisibility(View.VISIBLE);
               mEditRelativeLayout.setVisibility(View.GONE);
               onChangeProAllNotEdit();
           }
      
        }
    });
    }
    /**
     * 改变产品所有都不可以编辑
     */
    protected void onChangeProAllNotEdit() {
        if(ShopCar.getShopCar().getShopproductList()!=null){
            for(Product product:ShopCar.getShopCar().getShopproductList()){
                product.setmEditable(false);
           }
            mAdapter.notifyDataSetChanged();
            
        }
     
    }
   /**
    * 改变产品所有都可以编辑
    */
    protected void onChangeProAllEdit() {
        if(ShopCar.getShopCar().getShopproductList()!=null){
            for(Product product:ShopCar.getShopCar().getShopproductList()){
                product.setmEditable(true);
          }
          mAdapter.notifyDataSetChanged();
        }
    }
    
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopcar_home,
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

        super.onDetach();
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
        case R.id.confirm_btn:
            if(ShopCar.getShopCar().getShopCheckedAmountSum()>0){
                Intent intent = new Intent(getActivity(),TakingOrderActivity.class);
                startActivity(intent);
            }else{
                showSmartToast("请选择要结算的商品", Toast.LENGTH_LONG);
            }
         
            break;
        case R.id.del_all:
            if(ShopCar.getShopCar().getShopCheckedAmountSum()>0){
             mDialog =   AlertDialogUtils.displayMyAlertChoice(getActivity(), R.string.cancel, R.string.product_shop_del_confirm_message, R.string.confirm,
                        new OnClickListener() {
                            
                            @Override
                            public void onClick(View arg0) {
                                ShopCar.getShopCar().delCartCheckedItem();
                                mDialog.dismiss();
                                
                            }
                        }
                        , R.string.cancel,  new OnClickListener() {
                            
                            @Override
                            public void onClick(View arg0) {
                                mDialog.dismiss();
                                
                            }
                        });
             mDialog.show();
               
            }else{
                showSmartToast("请选择要从购物车中移除的商品", Toast.LENGTH_LONG);
            }
           
            break;
        case R.id.collect:
            onCollect();
            break;
        
        default:
            break;
        }
    }
    /**
     * 请求收藏购物车中选中的商品
     */
    private void onCollect(){
        StringBuffer buffer = new StringBuffer();
        for(Product product:mProList){
            if(product.ismIsCheck()){
                buffer.append(product.getmId()+",");
            }
        }
        if(!TextUtils.isEmpty(buffer.toString())){
            String ids = buffer.toString();
            ids = ids.substring(0, buffer.length()-1);
            RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.GOODS_COLLECT);
            url.setmGetParamPrefix(RequestParamConfig.PRINCIPAL).setmGetParamValues("5149");
            url.setmGetParamPrefix(RequestParamConfig.COMMODITY_ID).setmGetParamValues(ids+"");
            param.setmParserClassName(CommonParser.class.getName());
            param.setmHttpURL(url);
            RequestManager.getRequestData(getActivity(), creatReqSuccessListener(), createErrorListener(), param);  
        }else{
            showSmartToast("请选择要收藏的商品", Toast.LENGTH_LONG);
        }
      
    }
   
    private Response.Listener<Object> creatReqSuccessListener() {
        return new Listener<Object>() {

            @Override
            public void onResponse(Object result) {
                AppLog.Logd(result.toString());
                AppLog.Loge(" data success to load" + result.toString());
                if(getActivity()!=null&&!isDetached()){
                  if(result instanceof ResultInfo){
                      ResultInfo rInfo =(ResultInfo) result;
                      if(rInfo.getmCode()==Constant.RESULT_CODE){
                          showSmartToast("收藏成功", Toast.LENGTH_LONG);
                      }
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
    /**
     * onCheck all data
     * @param isCheck
     */
    private void onCheckAll(boolean isCheck) {
        mProList = ShopCar.getShopCar().getShopproductList();
        for(Product pro:mProList){
            pro.setmIsCheck(isCheck);
        }
        onUpdateData();
    }

    @Override
    public void notify(Bundle bundle) {
        onUpdateData();
    }

    @Override
    public void onEdit() {
        setRightText(getString(R.string.finish));
        mFinishRelativeLayout.setVisibility(View.GONE);
        mEditRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
       int id = compoundButton.getId();
       switch (id) {
    case R.id.product_all_checkbox:
        AppLog.Logd("Fly", "ischeck1"+isCheck);
        mCheckBox2.setChecked(isCheck);
        mCheckBox.setChecked(isCheck);
        onCheckAll(isCheck);
        break;
    case R.id.product_all_checkbox2:
        AppLog.Logd("Fly", "ischeck2");
        mCheckBox2.setChecked(isCheck);
        mCheckBox.setChecked(isCheck);
        onCheckAll(isCheck);
        break;
    default:
        break;
    }
    }
    
}
