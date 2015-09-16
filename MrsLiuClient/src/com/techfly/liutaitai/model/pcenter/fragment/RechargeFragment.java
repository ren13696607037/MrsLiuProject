package com.techfly.liutaitai.model.pcenter.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.paymanage.PayImplFactory;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.RechargeActivity;
import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment.PayCallBack;

public class RechargeFragment extends CommonFragment implements OnClickListener{
	private RechargeActivity mActivity;
	private User mUser;
	private TextView mPrice;
	private RelativeLayout mAlipay;
	private RelativeLayout mWeixin;
	private ImageView mCbAlipay;
	private ImageView mCbWeixin;
	private Button mBtn;
	private Balance mBalance;
	private int mType = Constant.PAY_ALIPAY;
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
    	mCbAlipay = (ImageView) view.findViewById(R.id.recharge_cb_alipay);
    	mCbWeixin = (ImageView) view.findViewById(R.id.recharge_cb_weixin);
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
			mType = Constant.PAY_ALIPAY;
			mCbAlipay.setImageResource(R.drawable.address_default);
			mCbWeixin.setImageResource(R.drawable.address_undefault);
			break;
		case R.id.recharge_weixin:
			mType = Constant.PAY_WENXIN;
			mCbWeixin.setImageResource(R.drawable.address_default);
			mCbAlipay.setImageResource(R.drawable.address_undefault);
			break;
		case R.id.recharge_btn:
			PayImplFactory.getInstance().getPayImpl(mType).onPay(getActivity(), mBalance.getmId(), mBalance.getmPrice(), "充值", new PayCallBack() {
	             
	             @Override
	             public void onPaySuccess() {
	                 showSmartToast("充值成功", Toast.LENGTH_SHORT);
	             }
	         }); 
			break;

		default:
			break;
		}
	}

}
