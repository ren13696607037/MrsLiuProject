package com.techfly.liutaitai.model.shopcar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment;

public class CreateOrderSucFragment extends CreateOrderPayCommonFragment implements OnClickListener{
    private TextView mPayTv;
    
    
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String onEncapleOrderInfo() {
        // TODO Auto-generated method stub
        return null;
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
        mPayTv = (TextView) view.findViewById(R.id.order_pay);
        mPayTv.setOnClickListener(this);
    }
    private void initTitleView(){
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText(getString(R.string.order_status_title));
     
     }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_create,
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
    public void onClick(View view) {
       int id = view.getId();
       switch (id) {
    case R.id.order_pay:
       TakingOrderActivity ac = (TakingOrderActivity) getActivity();
       final Bundle bundle = ac.getBundleInfo();
       String orderId = bundle.getString(IntentBundleKey.ORDER_ID, "");
       String payMoney =bundle.getString(IntentBundleKey.ORDER_MONEY, "");
       String productName =bundle.getString(IntentBundleKey.ORDER_PRODUCT, "");
       onPay(orderId, payMoney, productName, new PayCallBack() {
        
        @Override
        public void onPaySuccess() {
          TakingOrderActivity activity=  (TakingOrderActivity) getActivity();
          activity.showOrderFinishFragment(bundle);
        }
    });
      
        break;
    default:
        break;
    }
        
    }

    @Override
    public void onOrderCreateSuccess(String orderId, String money, String proName) {
        // TODO Auto-generated method stub
        
    }

   
    
}
