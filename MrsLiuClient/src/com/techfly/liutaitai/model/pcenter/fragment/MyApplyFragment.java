package com.techfly.liutaitai.model.pcenter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
	private RadioGroup mGroup;
	private RadioGroup mGroup2;
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
    	
    	mGroup = (RadioGroup) view.findViewById(R.id.apply_group);
    	mGroup2 = (RadioGroup) view.findViewById(R.id.apply_group1);
    	mBeauty = (RadioButton) view.findViewById(R.id.apply_beauty);
    	mDaily = (RadioButton) view.findViewById(R.id.apply_daily);
    	mEyelash = (RadioButton) view.findViewById(R.id.apply_eyelash);
    	mMakeup = (RadioButton) view.findViewById(R.id.apply_makeup);
    	mManicure = (RadioButton) view.findViewById(R.id.apply_manicure);
    	
//    	mBeauty.setOnClickListener(this);
//    	mDaily.setOnClickListener(this);
//    	mEyelash.setOnClickListener(this);
//    	mMakeup.setOnClickListener(this);
//    	mManicure.setOnClickListener(this);
    	
    	mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				Toast.makeText(mActivity, "1 is-=-"+checkedId, Toast.LENGTH_SHORT).show();
//				switch (checkedId) {
//				case R.id.apply_beauty:
//					mBeauty.setChecked(true);
//					break;
//				case R.id.apply_manicure:
//					mManicure.setChecked(true);
//					break;
//				case R.id.apply_eyelash:
//					mEyelash.setChecked(true);
//					break;
//				default:
//					break;
//				}
				if(!isSelect){
					Toast.makeText(mActivity, "1 group is-=-"+checkedId, Toast.LENGTH_SHORT).show();
					mGroup2.clearCheck();
					isSelect = true;
				}/*else{
//					mGroup.check(checkedId);
//				}*/
			}
		});
    	mGroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				Toast.makeText(mActivity, "2 is-=-"+checkedId, Toast.LENGTH_SHORT).show();
//				switch (checkedId) {
//				case R.id.apply_makeup:
//					mMakeup.setChecked(true);
//					break;
//				case R.id.apply_daily:
//					mDaily.setChecked(true);
//					break;
//				default:
//					break;
//				}
				if(isSelect){
//					Toast.makeText(mActivity, "2 group is-=-"+checkedId, Toast.LENGTH_SHORT).show();
//					mBeauty.setChecked(false);
//					mManicure.setChecked(false);
//					mEyelash.setChecked(false);
					mGroup.clearCheck();
					isSelect = false;
				}/*else{
//					mGroup2.check(checkedId);
//				}*/
				
			}
		});
    }

	@Override
	public void requestData() {

	}

	@Override
	public void onClick(View v) {
		Toast.makeText(mActivity, "click id is" + v.getId() + isSelect, Toast.LENGTH_SHORT).show();
		switch (v.getId()) {
		case R.id.apply_beauty:
			if(!isSelect && mGroup.getCheckedRadioButtonId() != v.getId()){
				mBeauty.setChecked(true);
			}
			break;
		case R.id.apply_daily:
			if(isSelect && mGroup2.getCheckedRadioButtonId() != v.getId()){
				mDaily.setChecked(true);
			}
			break;
		case R.id.apply_eyelash:
			if(!isSelect && mGroup.getCheckedRadioButtonId() != v.getId()){
				mEyelash.setChecked(true);
			}
			break;
		case R.id.apply_makeup:
			if(isSelect && mGroup2.getCheckedRadioButtonId() != v.getId()){
				mMakeup.setChecked(true);
			}
			break;
		case R.id.apply_manicure:
			if(!isSelect && mGroup.getCheckedRadioButtonId() != v.getId()){
				mManicure.setChecked(true);
			}
			break;

		default:
			break;
		}
	}
	private void setOtherFalse(int id, RadioGroup group){
		group.check(id);
		group.clearCheck();
	}

}
