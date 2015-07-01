package com.techfly.liutaitai.model.shopcar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class UnLoginShopCarFragment extends CommonFragment implements OnClickListener{
   private Button mLoginBtn;
    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
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
       mLoginBtn = (Button) view.findViewById(R.id.login_button);
       mLoginBtn.setOnClickListener(this);
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
        View view = inflater.inflate(R.layout.fragment_unlogin_shopcar,
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
    case R.id.login_button:
         onLauncherLogin();
        break;
    
    default:
        break;
    }
        
    }
    private void onLauncherLogin() {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
    }
}
