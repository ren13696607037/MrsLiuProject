package com.techfly.liutaitai.model.pcenter.fragment;

import java.io.Console;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.BalanceHistoryActivity;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class RechargeFragment extends CommonFragment implements OnClickListener{
	private RechargeActivity mActivity;
	private User mUser;
	private TextView mPrice;
	private RelativeLayout mAlipay;
	private RelativeLayout mWeixin;
	private CheckBox mCbAlipay;
	private CheckBox mCbWeixin;
	private Button mBtn;
	private Balance mBalance;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (RechargeActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        mBalance = (Balance) mActivity.getIntent().getSerializableExtra(IntentBundleKey.BALANCE_PRICE);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        onInitView(view);
    }
    private void onInitView(View view){
    	setTitleText(R.string.recharge_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mPrice = (TextView) view.findViewById(R.id.recharge_price);
    	mAlipay = (RelativeLayout) view.findViewById(R.id.recharge_alipay);
    	mWeixin = (RelativeLayout) view.findViewById(R.id.recharge_weixin);
    	mCbAlipay = (CheckBox) view.findViewById(R.id.recharge_cb_alipay);
    	mCbWeixin = (CheckBox) view.findViewById(R.id.recharge_cb_weixin);
    	mBtn = (Button) view.findViewById(R.id.recharge_btn);
    	
    	mBtn.setOnClickListener(this);
    	mAlipay.setOnClickListener(this);
    	mWeixin.setOnClickListener(this);
    	
    	
    	mPrice.setText(mBalance.getmPrice());
    }

	@Override
	public void requestData() {

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.recharge_alipay:
			mCbAlipay.setChecked(true);
			mCbWeixin.setChecked(false);
			break;
		case R.id.recharge_weixin:
			mCbAlipay.setChecked(true);
			mCbWeixin.setChecked(false);
			break;
		case R.id.recharge_btn:
			
			break;

		default:
			break;
		}
	}

}
