package com.techfly.liutaitai.model.pcenter.fragment;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeAddFragment extends CommonFragment {
    private ChangeAddressActivity mActivity;
    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtAddress;
    private Button mButton;
    private String mExtra;
    private AddressManage mItem;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(ChangeAddressActivity) activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExtra=mActivity.getIntent().getStringExtra(IntentBundleKey.CHANGEADD_ID);
        mItem=(AddressManage) mActivity.getIntent().getSerializableExtra(IntentBundleKey.CHANGEADD_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changeadd, container, false);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        mEtAddress=(EditText) view.findViewById(R.id.changeadd_add);
        mEtName=(EditText) view.findViewById(R.id.changeadd_name);
        mEtPhone=(EditText) view.findViewById(R.id.changeadd_phone);
        mButton=(Button) view.findViewById(R.id.changeadd_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
            	String name=mEtName.getText().toString();
            	String phone=mEtPhone.getText().toString();
            	String address=mEtAddress.getText().toString();
            	if(mEtAddress.length()==0||mEtName.length()==0||mEtPhone.length()==0){
            		SmartToast.makeText(mActivity, R.string.input_error, Toast.LENGTH_SHORT).show();
            	}else{
            		if(!Utility.isPhone(phone)){
            			SmartToast.makeText(mActivity, R.string.phone_error, Toast.LENGTH_SHORT).show();
            			mEtPhone.setText("");
            		}else{
            			//TODO
                    }
            	}
            }
        });
        if(!TextUtils.isEmpty(mExtra)){
        	setTitleText(mExtra);
        }else{
//        	setTitleText(R.string.changeadd_title);
//        	mEtAddress.setText(mItem.getmAddress());
//        	mEtName.setText(mItem.getmName());
//        	mEtPhone.setText(mItem.getmPhone());
        }
    }
    @Override
    public void requestData() {

    }

}
