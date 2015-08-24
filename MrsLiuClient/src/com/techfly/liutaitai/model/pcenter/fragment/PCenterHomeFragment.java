package com.techfly.liutaitai.model.pcenter.fragment;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyApplyActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyBalanceActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyCollectActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyServiceActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyVoucherActivity;
import com.techfly.liutaitai.model.pcenter.activities.PCenterHomeActivity;
import com.techfly.liutaitai.model.pcenter.activities.PcenterInfoActivity;
import com.techfly.liutaitai.model.pcenter.activities.SettingActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.util.CircleImageView;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class PCenterHomeFragment extends CommonFragment implements OnClickListener{
	private PCenterHomeActivity mActivity;
	private RelativeLayout mWelcome;//登录注册头部
	private RelativeLayout mTop;//个人中心头部
	private RelativeLayout mBanlance;//我的余额
	private RelativeLayout mCollect;//我的收藏
	private RelativeLayout mVoucher;//我的现金券
	private RelativeLayout mAddress;//我的收货地址
	private RelativeLayout mApply;//申请成为技师
	private RelativeLayout mService;//我的服务
	private CircleImageView mHeader;
	private TextView mTvNick;
	private User mUser;
	private Button mButton;

    @Override
    public void requestData() {
        
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(PCenterHomeActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
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
        onInitView(view);
    }
    private void initTitleView(){
        setTitleText(R.string.home_pcenter_tab);
        setRightMoreIcon(R.drawable.pcenter_setting, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(mActivity,SettingActivity.class), Constant.EXIT_INTENT);
				
			}
		});
    }
    private void onInitView(View view){
    	mCollect = (RelativeLayout) view.findViewById(R.id.pcenter_collect);
    	mBanlance = (RelativeLayout) view.findViewById(R.id.pcenter_balance);
    	mTop = (RelativeLayout) view.findViewById(R.id.pcenter_top);
    	mWelcome = (RelativeLayout) view.findViewById(R.id.pcenter_welcome);
    	mTvNick=(TextView) view.findViewById(R.id.mine_name);
    	mHeader = (CircleImageView) view.findViewById(R.id.mine_img);
    	mAddress = (RelativeLayout) view.findViewById(R.id.pcenter_address);
    	mApply = (RelativeLayout) view.findViewById(R.id.pcenter_apply);
    	mService = (RelativeLayout) view.findViewById(R.id.pcenter_service);
    	mVoucher = (RelativeLayout) view.findViewById(R.id.pcenter_voucher);
    	mButton = (Button) view.findViewById(R.id.mine_btn);
    	
    	mButton.setOnClickListener(this);
    	mCollect.setOnClickListener(this);
    	mVoucher.setOnClickListener(this);
    	mWelcome.setOnClickListener(this);
    	mTop.setOnClickListener(this);
    	mAddress.setOnClickListener(this);
    	mApply.setOnClickListener(this);
    	mService.setOnClickListener(this);
    	mBanlance.setOnClickListener(this);
    	
    	mService.setVisibility(View.INVISIBLE);
    	setView();
    }
    private void setView(){
    	if(mUser==null){
    		mTop.setVisibility(View.GONE);
        	mWelcome.setVisibility(View.VISIBLE);
    	}else{
    		mTop.setVisibility(View.VISIBLE);
    		mWelcome.setVisibility(View.GONE);
    		mTvNick.setText(mUser.getmNick());
    		ImageLoader.getInstance().displayImage(mUser.getmImage(), mHeader);
    		if("1".equals(mUser.getmType())){
    			mService.setVisibility(View.VISIBLE);
    		}else if("0".equals(mUser.getmType())){
    			mService.setVisibility(View.INVISIBLE);
    		}
    	}
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcenter_home,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.pcenter_collect:
			if(mUser!=null){
				intent=new Intent(mActivity,MyCollectActivity.class);
			}else{
				showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.pcenter_address:
			if(mUser!=null){
				intent=new Intent(mActivity,AddressManageActivity.class);
			}else{
				showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.pcenter_welcome: case R.id.mine_btn:
			 Intent loginIntent=new Intent(mActivity,LoginActivity.class);
			 startActivityForResult(loginIntent, Constant.LOGIN_INTENT);
			break;
		case R.id.pcenter_top:
			intent=new Intent(mActivity,PcenterInfoActivity.class);
			break;
		case R.id.pcenter_apply:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyApplyActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_voucher:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyVoucherActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_balance:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyBalanceActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_service:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyServiceActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.EXIT_SUCCESS
				|| resultCode == Constant.LOGIN_SUCCESS
				|| resultCode == Constant.REGISTER_SUCCESS) {
			mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
			setView();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
		if(mTvNick!=null){
			setView();
		}
	}
	
}
