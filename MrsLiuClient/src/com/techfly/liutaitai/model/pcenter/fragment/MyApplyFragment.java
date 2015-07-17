package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.MyApplyActivity;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class MyApplyFragment extends CommonFragment implements OnClickListener{
	private MyApplyActivity mActivity;
	private RadioButton mManicure;
	private RadioButton mBeauty;
	private RadioButton mEyelash;
	private RadioButton mMakeup;
	private RadioButton mDaily;
	private ImageView mImageView;
	private ImageView mImageView2;
	private Button mButton;
	private boolean isSelect;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MyApplyActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myapply,
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
    	setTitleText(R.string.pcenter_apply);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	
    	mBeauty = (RadioButton) view.findViewById(R.id.apply_beauty);
    	mDaily = (RadioButton) view.findViewById(R.id.apply_daily);
    	mEyelash = (RadioButton) view.findViewById(R.id.apply_eyelash);
    	mMakeup = (RadioButton) view.findViewById(R.id.apply_makeup);
    	mManicure = (RadioButton) view.findViewById(R.id.apply_manicure);
    	
    	mBeauty.setOnClickListener(this);
    	mDaily.setOnClickListener(this);
    	mEyelash.setOnClickListener(this);
    	mMakeup.setOnClickListener(this);
    	mManicure.setOnClickListener(this);
    	
    }

	@Override
	public void requestData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_beauty:
			mBeauty.setChecked(true);
			mDaily.setChecked(false);
			mEyelash.setChecked(false);
			mMakeup.setChecked(false);
			mManicure.setChecked(false);
			break;
		case R.id.apply_daily:
			mDaily.setChecked(true);
			mBeauty.setChecked(false);
			mEyelash.setChecked(false);
			mMakeup.setChecked(false);
			mManicure.setChecked(false);
			break;
		case R.id.apply_eyelash:
			mEyelash.setChecked(true);
			mBeauty.setChecked(false);
			mDaily.setChecked(false);
			mMakeup.setChecked(false);
			mManicure.setChecked(false);
			break;
		case R.id.apply_makeup:
			mMakeup.setChecked(true);
			mBeauty.setChecked(false);
			mDaily.setChecked(false);
			mEyelash.setChecked(false);
			mManicure.setChecked(false);
			break;
		case R.id.apply_manicure:
			mManicure.setChecked(true);
			mBeauty.setChecked(false);
			mDaily.setChecked(false);
			mEyelash.setChecked(false);
			mMakeup.setChecked(false);
			break;

		default:
			break;
		}
	}

}
