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
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class FinishOrderFragment extends CommonFragment implements OnClickListener{
    private TextView mGoOrderTv;
    
    
    @Override
    public void requestData() {
       
        
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
        mGoOrderTv = (TextView) view.findViewById(R.id.order_go_detail);
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
            TakingOrderActivity ac = (TakingOrderActivity) getActivity();
            final Bundle bundle = ac.getBundleInfo();
            UIHelper.toOrderDetailActivity(FinishOrderFragment.this, bundle.getString(IntentBundleKey.ORDER_ID));;
        }else{
            if(!getActivity().getIntent().getBooleanExtra(IntentBundleKey.ORDER_DETAIL, false)){
                UIHelper.toOrderDetailActivity(FinishOrderFragment.this, getActivity().getIntent().getStringExtra(IntentBundleKey.ORDER_ID));;
            }else{
                getActivity().finish();
            }
           
        }
      
        break;
    default:
        break;
    }
        
    }

   
}
