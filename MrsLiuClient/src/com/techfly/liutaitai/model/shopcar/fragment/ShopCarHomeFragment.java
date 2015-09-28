package com.techfly.liutaitai.model.shopcar.fragment;

import java.util.ArrayList;
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
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarListener;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.parser.ShopCartParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.model.shopcar.adapter.ShopCarAdapter;
import com.techfly.liutaitai.model.shopcar.adapter.ShopCarAdapter.EditCallBack;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class ShopCarHomeFragment extends CommonFragment implements OnClickListener,OnShopCarListener
,EditCallBack,OnCheckedChangeListener{
    private ShopCarAdapter mAdapter;
    private ListView mListView;
    private CheckBox mCheckBox;
    private CheckBox mCheckBox2;
    private TextView mTotalPriceTv;
    private TextView mConfirmTv;
    private TextView mDelAllTv;
    private List<Product> mProList = new ArrayList<Product>();
    private boolean mIsHomeCart = true;
    private RelativeLayout mEditRelativeLayout;
    private RelativeLayout mFinishRelativeLayout;
    private TextView mCollectTv;
    private Dialog mDialog;
    private int type;// 0 ,1 ,2 , 4分表表示 干洗,生鲜，鲜花，奢侈品
    private boolean mReqFinish;
    @Override
    public void requestData() {
       
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
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
        
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
                + Constant.SHOP_CARD_REQUEST_URL);
        url.setmGetParamPrefix("city");
        url.setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getArea().getmId());
        url.setmGetParamPrefix("type");
        url.setmGetParamValues(type+"");
        param.setmHttpURL(url);
        param.setmParserClassName(ShopCartParser.class.getName());
        RequestManager.getRequestData(getActivity(), creatSuccessListener(),
                creatErrorListener(), param);//
    }
    private Response.Listener<Object> creatSuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                mProList.clear();
                mReqFinish = true;
                if(ShopCar.getShopCar().getShopproductList()!=null &&ShopCar.getShopCar().getShopproductList().size()>0){
                    mProList.addAll(ShopCar.getShopCar().getShopproductList());
                    mAdapter = new ShopCarAdapter(getActivity(), mProList,ShopCarHomeFragment.this);
                    mListView.setAdapter(mAdapter);
                    onDispalyData();
                }else{
                    onDisplayNoData();
                }
            }
        };
    }

    private Response.ErrorListener creatErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());

            }
        };
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mIsHomeCart = getActivity().getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
        type = getActivity().getIntent().getIntExtra(IntentBundleKey.TYPE, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnShopCarLisManager.getShopCarLisManager().onRegisterShopCarListener(this);
        startReqTask(this);
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
        mEditRelativeLayout = (RelativeLayout) view.findViewById(R.id.editable_area);
        mFinishRelativeLayout = (RelativeLayout) view.findViewById(R.id.finish_area);
        mFinishRelativeLayout.setVisibility(View.VISIBLE);
        mEditRelativeLayout.setVisibility(View.GONE);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    private void onUpdateData(){
        mProList = ShopCar.getShopCar().getShopproductList();
        setTitleText(getString(R.string.shopcar)+"("+ShopCar.getShopCar().getShopAmountSum()+")");
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
        mAdapter.notifyDataSetChanged();
        mCheckBox.setClickable(true);
        mCheckBox2.setClickable(true);
        mConfirmTv.setClickable(true);
        mDelAllTv.setClickable(true);
        mDelAllTv.setText(getString(R.string.address_delete)+"("+ShopCar.getShopCar().getShopCheckedAmountSum()+")");
        if(ShopCar.getShopCar().getShopAmountSum()>0){
            setTitleText("购物车("+ShopCar.getShopCar().getShopAmountSum()+")");
        }else{
            setTitleText(getString(R.string.home_shopcar_tab));
        }
        mConfirmTv.setText(getString(R.string.taking_order_txt));
       
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
       setTitleText(getString(R.string.shopcar)+"("+ShopCar.getShopCar().getShopAmountSum()+")");
//       setRightText(R.string.editable_all,new OnClickListener() {
//        
//        @Override
//        public void onClick(View view) {
//            // TODO Auto-generated method stub 
//           TextView tv = (TextView) view;
//           if(tv.getText().toString().equals(getString(R.string.editable_all))){
//               setRightText(getString(R.string.finish));
//               mFinishRelativeLayout.setVisibility(View.GONE);
//               mEditRelativeLayout.setVisibility(View.VISIBLE);
//               onChangeProAllEdit();
//            
//           }else{
//               setRightText(getString(R.string.editable_all));
//               mFinishRelativeLayout.setVisibility(View.VISIBLE);
//               mEditRelativeLayout.setVisibility(View.GONE);
//               onChangeProAllNotEdit();
//           }
//      
//        }
//    });
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
             mDialog =   AlertDialogUtils.displayMyAlertChoice(getActivity(), R.string.delete_toshopcar_title, R.string.product_shop_del_confirm_message, R.string.confirm,
                        new OnClickListener() {
                            
                            @Override
                            public void onClick(View arg0) {
//                                ShopCar.getShopCar().delCartCheckedItem();
                                requestDeleteCart();
                                mDialog.dismiss();
                            }
                        }
                        , R.string.giveup,  new OnClickListener() {
                            
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
        if(mReqFinish){
            onUpdateData();
        }
        
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
    private  void requestDeleteCart(){
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
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
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.DELETE_TO_CART_REQUEST_URL);
        
      
    
        StringBuffer buffer = new StringBuffer();
        for(Product pro :ShopCar.getShopCar().getCheckShopproductList()){
            url.setmGetParamPrefix("ids");
            url.setmGetParamValues(pro.getmId());
//            buffer.append(pro.getmId()+"");
        }
//        String ids = buffer.toString();
//        AppLog.Logd("Fly", "ids===="+ids);
//        ids = ids.substring(0,ids.length()-1);
//        AppLog.Logd("Fly", "ids===="+ids);
//        url.setmGetParamValuse(ids);
        
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(getActivity(), creatDelSuccessListener(), creatDelErrorListener(), param);//
    }
    
    private Response.ErrorListener creatDelErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                
                showSmartToast("删除失败", Toast.LENGTH_LONG);
            }
        };
    }
    private Response.Listener<Object> creatDelSuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
               
                ResultInfo info = (ResultInfo) obj;
                if(info.getmCode()==0){
                    ShopCar.getShopCar().delCartCheckedItem();
                    OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null);
                }else{
                    showSmartToast("删除失败", Toast.LENGTH_LONG);
                }
            
            }
        };
    }
}
