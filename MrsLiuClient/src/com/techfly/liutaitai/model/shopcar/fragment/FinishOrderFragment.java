package com.techfly.liutaitai.model.shopcar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.activities.MyOrderActivity;
import com.techfly.liutaitai.model.mall.activities.MyYuYueActivity;
import com.techfly.liutaitai.model.mall.activities.OrderBastketActivity;
import com.techfly.liutaitai.model.order.activities.OrderActivity;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class FinishOrderFragment extends CommonFragment implements OnClickListener{
    private TextView mGoOrderTv;
    
    
    @Override
    public void requestData() {
       
        
    }

 
  
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void initView(View view) {
        mGoOrderTv = (TextView) view.findViewById(R.id.order_go_detail);
        if(getActivity() instanceof TakingOrderActivity){
            mGoOrderTv.setText(getString(R.string.order_basket_read));
        }else{
            mGoOrderTv.setText(getString(R.string.order_read));
        }
        mGoOrderTv.setOnClickListener(this);
    }
    private void initTitleView(){
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText(getString(R.string.order_status_title));
     
     }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_finish,
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
    case R.id.order_go_detail:
        if(getActivity() instanceof TakingOrderActivity){
          Intent intent = new Intent(getActivity(),MyOrderActivity.class);
          intent.putExtra(IntentBundleKey.IS_FROM_ORDER, true);
          startActivity(intent);
          getActivity().finish();
        }else{
            Intent intent = new Intent(getActivity(),MyYuYueActivity.class);
            intent.putExtra(IntentBundleKey.IS_FROM_ORDER, true);
            startActivity(intent);
            getActivity().finish();
        }
      
        break;
    default:
        break;
    }
        
    }

   
}
