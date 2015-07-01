package com.techfly.liutaitai.model.shopcar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.MainActivity;
import com.techfly.liutaitai.TabSwitchListener;

public class EmptyShopCarFragment extends CommonFragment implements OnClickListener{
    private Button mGoBtn;
    private boolean mIsHomeCart = true;
  
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mIsHomeCart  = getActivity().getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
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
        mGoBtn = (Button) view.findViewById(R.id.go_button);
        mGoBtn.setOnClickListener(this);
    }
    private void initTitleView(){
        boolean isFromCart = getActivity().getIntent().getBooleanExtra(IntentBundleKey.IS_FROM_HOME_CART, true);
        if(isFromCart){
           setLeftHeadIcon(-1);
       }else{
           setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
       }
        setTitleText(getString(R.string.home_shopcar_tab));
     
     }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty_shopcar,
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
    case R.id.go_button:
        if(mIsHomeCart){
            TabSwitchListener.getTabSwitchLisManager().onTagSwitch(MainActivity.TAB_HOME);
        }else{
            UIHelper.toHomeActivity(getActivity());
        }
      
        break;
    default:
        break;
    }
        
    }
}
