package com.hylsmart.yihui.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.mall.adapter.CategoryListAdapter;
import com.hylsmart.yihui.model.mall.bean.Category;
import com.hylsmart.yihui.model.mall.bean.help.ListCatgory;
import com.hylsmart.yihui.model.mall.parser.ProductCategoryParser;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.DensityUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class CategoryListFragment extends CommonFragment {
	private Context mContext;
	// private LoadingLayout mLoadingLayout;
	private ListView mListView;
	private CategoryListAdapter mAdapter;
	private List<Category> mDatas;

	private RadioGroup mRadioGroup;
	private Fragment mFragment;
	private ArrayList<RadioButton> mRadioList;
	private ArrayList<CategoryInfoListFragment> mFragmentList;
	private View mHeaderLine;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_category_list, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initHeader(view);
		initView(view);
		// requestData();
		startReqTask(this);
	}

	private void initHeader(View view) {
		// TODO Auto-generated method stub
		setTitleText(R.string.category_title);
		setRightMoreIcon(R.drawable.search, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.toSearchActivity(getActivity(), 0);
			}
		});
		mHeaderLine = view.findViewById(R.id.header_bottom_line);
		mHeaderLine.setVisibility(View.GONE);
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		if (mRadioList == null) {
			mRadioList = new ArrayList<RadioButton>();
		}
		mRadioGroup = (RadioGroup) view
				.findViewById(R.id.category_list_radiogroup);
		mRadioGroup.setOnCheckedChangeListener(getCheckChangelistener());
	}

	private OnCheckedChangeListener getCheckChangelistener() {
		// TODO Auto-generated method stub
		OnCheckedChangeListener mListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				AppLog.Logd("Shi", "checkedId:::" + checkedId);
				changeFragment(mFragment, mFragmentList.get(checkedId));
				resetState();
				setFocusState(group, checkedId);
			}
		};
		return mListener;
	}

	protected void setFocusState(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		RadioButton r = (RadioButton) group.findViewById(checkedId);
		r.setTextColor(getResources().getColor(R.color.TextColorOrange));
		android.widget.RadioGroup.LayoutParams params = (android.widget.RadioGroup.LayoutParams) r
				.getLayoutParams();
		// params.bottomMargin = DensityUtils.dp2px(getActivity(), 0);
		// params.leftMargin = DensityUtils.dp2px(getActivity(), 0);
		// params.rightMargin = DensityUtils.dp2px(getActivity(), 0);
		// params.topMargin = DensityUtils.dp2px(getActivity(), 0);
//		params.setMargins(0, 0, 0, 0);
		if(checkedId == mRadioList.size() -1){
			params.setMargins(0, 1, 0, 1);
		}else if(checkedId == 0){
			params.setMargins(0, 0, 0, 0);
		}else{
			params.setMargins(0, 1, 0, 0);
		}
		params.gravity = Gravity.CENTER;
		r.setLayoutParams(params);
//		r.setBackgroundColor(getResources().getColor(R.color.background));
	}

	protected void resetState() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mRadioList.size(); i++) {
			mRadioList.get(i).setTextColor(
					getResources().getColor(R.color.TextColorBLACK_NORMAL));
			android.widget.RadioGroup.LayoutParams params = (android.widget.RadioGroup.LayoutParams) mRadioList
					.get(i).getLayoutParams();
			// params.bottomMargin = DensityUtils.dp2px(getActivity(), 0.5f);
			// params.leftMargin = DensityUtils.dp2px(getActivity(), 1);
			// params.rightMargin = DensityUtils.dp2px(getActivity(), 1);
			// params.topMargin = DensityUtils.dp2px(getActivity(), 0.5f);
			if(i == mRadioList.size() -1){
				params.setMargins(0, 1, 1, 1);
			}else{
				params.setMargins(0, 1, 1, 0);
			}
			params.gravity = Gravity.CENTER;
			mRadioList.get(i).setLayoutParams(params);
			mRadioList.get(i).setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
	}

	protected void changeFragment(Fragment fromFragment,
			CategoryInfoListFragment toFragment) {
		// TODO Auto-generated method stub
		if (mFragment != toFragment) {
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			hideFragment(transaction, mFragment);
			mFragment = toFragment;

			if (!toFragment.isAdded()) {
				transaction.add(R.id.fragment_category_list, toFragment);
				if (fromFragment != null) {
					transaction.hide(fromFragment);
				}
			} else {
				transaction.hide(fromFragment).show(toFragment);
			}
			transaction.commitAllowingStateLoss();

		}
	}

	private void hideFragment(FragmentTransaction transaction, Fragment fragment) {
		// TODO Auto-generated method stub
		if (fragment != null && fragment.isAdded()) {
			transaction.hide(fragment);
		}
	}

	@Override
	public void requestData() {
		// TODO Auto-generated method stub
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.PRODUCT_CATEGORY);
		param.setmHttpURL(url);
		param.setmParserClassName(ProductCategoryParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				if (object instanceof ListCatgory) {
					ListCatgory lc = (ListCatgory) object;
					if (lc != null) {
						mDatas = lc.getmArrayList();
						if (mDatas != null && mDatas.size() != 0) {
							initRadioGroup();
						} else {
							showNoData();
						}
					}

				}

			}

		};
	}

	protected void initRadioGroup() {
		// TODO Auto-generated method stub
		if (mRadioList == null) {
			mRadioList = new ArrayList<RadioButton>();
		}
		if (mFragmentList == null) {
			mFragmentList = new ArrayList<CategoryInfoListFragment>();
		}
		mFragmentList.clear();
		mRadioList.clear();
		for (int i = 0; i < mDatas.size(); i++) {
			RadioButton r = (RadioButton) LayoutInflater.from(getActivity())
					.inflate(R.layout.radiobutton_category_list, null);
			r.setId(i);
			r.setText(mDatas.get(i).getmCategoryName());
			// Drawable drawable = getActivity().getResources().getDrawable(
			// R.drawable.selector_radio);
			// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
			// drawable.getIntrinsicHeight());

			setDrawableLeft(r, mDatas.get(i).getmCategoryId());
			// r.setCompoundDrawables(drawable, null, null, null);
			// r.setText("TEXT" + i);
			if (mRadioGroup != null) {
				android.widget.RadioGroup.LayoutParams params = new android.widget.RadioGroup.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.bottomMargin = DensityUtils.dp2px(getActivity(), 0.5f);
				params.leftMargin = DensityUtils.dp2px(getActivity(), 0);
				params.rightMargin = DensityUtils.dp2px(getActivity(), 0);
				params.topMargin = DensityUtils.dp2px(getActivity(), 0.5f);
				params.gravity = Gravity.CENTER;
				mRadioGroup.addView(r, params);
			}
			if (mRadioList != null) {
				mRadioList.add(r);
			}
			CategoryInfoListFragment f = new CategoryInfoListFragment(mDatas
					.get(i).getmSubCategoryList(), mDatas.get(i)
					.getmSubCategoryBannerList());
			// CategoryInfoListFragment f = new CategoryInfoListFragment(i);
			mFragmentList.add(f);
		}
		mRadioList.get(0).setChecked(true);
	}

	private void setDrawableLeft(RadioButton r, int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 27:
			r.setButtonDrawable(R.drawable.class_1);
			break;
		case 91:
			r.setButtonDrawable(R.drawable.class_2);
			break;
		case 137:
			r.setButtonDrawable(R.drawable.class_3);
			break;
		case 244:
			r.setButtonDrawable(R.drawable.class_4);
			break;
		case 367:
			r.setButtonDrawable(R.drawable.class_5);
			break;
		case 419:
			r.setButtonDrawable(R.drawable.class_6);
			break;
		case 503:
			r.setButtonDrawable(R.drawable.class_7);
			break;
		case 573:
			r.setButtonDrawable(R.drawable.class_8);
			break;
		case 587:
			r.setButtonDrawable(R.drawable.class_9);
			break;
		case 806:
			r.setButtonDrawable(R.drawable.class_10);
			break;
		case 1064:
			r.setButtonDrawable(R.drawable.class_11);
			break;
		case 1072:
			r.setButtonDrawable(R.drawable.class_12);
			break;
		default:
			r.setButtonDrawable(R.drawable.class_2);
			break;
		}
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				mLoadHandler.removeMessages(Constant.NET_FAILURE);
				mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
				showMessage(R.string.loading_fail);
			}
		};
	}
}
