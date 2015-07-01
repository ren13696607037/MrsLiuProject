package com.techfly.liutaitai.model.pcenter.fragment;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyBrowserActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyCollectActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyOrderActivity;
import com.techfly.liutaitai.model.pcenter.activities.PCenterHomeActivity;
import com.techfly.liutaitai.model.pcenter.activities.PcenterInfoActivity;
import com.techfly.liutaitai.model.pcenter.activities.SettingActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class PCenterHomeFragment extends CommonFragment implements OnClickListener{
	private PCenterHomeActivity mActivity;
	private RelativeLayout mWelcome;//登录注册头部
	private RelativeLayout mTop;//个人中心头部
	private RelativeLayout mOrder;//我的订单
	private RelativeLayout mCollect;//我的收藏夹
	private RelativeLayout mBrowser;//浏览痕迹
	private TextView mTvNick;
	private User mUser;

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
				startActivity(new Intent(mActivity,SettingActivity.class));
			}
		});
    }
    private void onInitView(View view){
    	mCollect=(RelativeLayout) view.findViewById(R.id.pcenter_collect);
    	mOrder=(RelativeLayout) view.findViewById(R.id.pcenter_order);
    	mTop=(RelativeLayout) view.findViewById(R.id.pcenter_top);
    	mWelcome=(RelativeLayout) view.findViewById(R.id.pcenter_welcome);
    	mTvNick=(TextView) view.findViewById(R.id.pcenter_name);
    	mBrowser = (RelativeLayout) view.findViewById(R.id.pcenter_browser);
    	
    	mCollect.setOnClickListener(this);
    	mOrder.setOnClickListener(this);
    	mWelcome.setOnClickListener(this);
    	mTop.setOnClickListener(this);
    	mBrowser.setOnClickListener(this);
    	setView();
    }
    private void setView(){
    	if(mUser==null){
    		mTop.setVisibility(View.INVISIBLE);
        	mWelcome.setVisibility(View.VISIBLE);
    	}else{
    		mTop.setVisibility(View.VISIBLE);
    		mWelcome.setVisibility(View.INVISIBLE);
    		mTvNick.setText(mUser.getmNick());
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
		case R.id.pcenter_order:
			if(mUser!=null){
				intent=new Intent(mActivity,MyOrderActivity.class);
			}else{
				showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.pcenter_welcome:
			 Intent loginIntent=new Intent(mActivity,LoginActivity.class);
			 startActivityForResult(loginIntent, Constant.LOGIN_INTENT);
			break;
		case R.id.pcenter_top:
			intent=new Intent(mActivity,PcenterInfoActivity.class);
			break;
		case R.id.pcenter_browser:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyBrowserActivity.class);
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
		if(resultCode==Constant.LOGIN_SUCCESS){
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
