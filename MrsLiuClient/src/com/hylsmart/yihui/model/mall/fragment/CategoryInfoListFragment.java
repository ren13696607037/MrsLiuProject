package com.hylsmart.yihui.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.mall.bean.CategoryInfo;
import com.hylsmart.yihui.model.mall.bean.SubCategory;
import com.hylsmart.yihui.model.mall.bean.SubCategoryBanner;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.DensityUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.adapter.CommonAdapter;
import com.hylsmart.yihui.util.adapter.ViewHolder;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.RollViewPager;
import com.hylsmart.yihui.util.view.RollViewPager.OnPagerClickCallback;

@SuppressLint("ValidFragment")
public class CategoryInfoListFragment extends CommonFragment {

	private RollViewPager mViewPager;
	private TextView mPagerTitle;
	private LinearLayout mDotLinear;
	private RelativeLayout mPagerRelative;
	private ArrayList<View> dots; // 图片标题正文的那些点
	private List<ImageView> imageViews; // 滑动的图片集合
	private String[] titles;
	private ArrayList<String> mTitles;
	public ArrayList<String> mImageUrls;
	// private ArrayList<HomePager> mPagerDatas;
	private Context mContext;

	private GridView mGridView;
	private CommonAdapter<SubCategory> mAdapter;
	private ArrayList<CategoryInfo> mDatas;
	private ArrayList<SubCategory> mSubCategoryDatas;
	private ArrayList<SubCategoryBanner> mSubCategoryBannerDatas;

	public CategoryInfoListFragment(ArrayList<SubCategory> mSubCategoryDatas,
			ArrayList<SubCategoryBanner> mSubCategoryBannerDatas) {
		super();
		this.mSubCategoryBannerDatas = mSubCategoryBannerDatas;
		this.mSubCategoryDatas = mSubCategoryDatas;
	}
    public CategoryInfoListFragment(){
    
    }
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mCategory = (Category)
		// getActivity().getIntent().getSerializableExtra(IntentBundleKey.CATEGORY);
		// startReqTask(this);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_category_info, null);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initPager(view);
		initViews(view);

	}

	private void initPager(View view) {
		// TODO Auto-generated method stub
		mPagerRelative = (RelativeLayout) view
				.findViewById(R.id.category_info_pager_relative);
		if (mSubCategoryBannerDatas == null
				|| mSubCategoryBannerDatas.size() == 0) {
			mPagerRelative.setVisibility(View.GONE);
			return;
		} else {
			mPagerRelative.setVisibility(View.VISIBLE);
		}
		LayoutParams params = (LayoutParams) mPagerRelative.getLayoutParams();
		params.height = Constant.SCREEN_WIDTH * 5/ 11;
		mPagerRelative.setLayoutParams(params);

		mDotLinear = (LinearLayout) view
				.findViewById(R.id.category_info_pager_dot_linear);
		mViewPager = (RollViewPager) view
				.findViewById(R.id.category_info_pager_viewpager);
		mViewPager.setPagerCallback(getPagerCallBack());

		if (mImageUrls == null) {
			mImageUrls = new ArrayList<String>();
		}

		if (dots == null) {
			dots = new ArrayList<View>();
		}
		mImageUrls.clear();
		dots.clear();
		mDotLinear.removeAllViews();

		for (int i = 0; i < mSubCategoryBannerDatas.size(); i++) {
			mImageUrls.add(mSubCategoryBannerDatas.get(i).getmImageUrl());
			ImageView dot = new ImageView(mContext);
			LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			p.leftMargin = 2;
			p.rightMargin = 2;
			p.gravity = Gravity.CENTER;
			dot.setLayoutParams(p);
			if (i != 0) {
				dot.setBackgroundResource(R.drawable.ic_dot_unselected);
			} else {
				dot.setBackgroundResource(R.drawable.ic_dot_selected);
			}
			mDotLinear.addView(dot);
			dots.add(dot);
		}

		mViewPager.setUriList(mImageUrls);
		mViewPager.setDot(dots, R.drawable.ic_dot_selected,
				R.drawable.ic_dot_unselected);
		if (mViewPager.getAdapter() != null) {
			mViewPager.getAdapter().notifyDataSetChanged();
		}
		mViewPager.removeCallback();
		mViewPager.resetCurrentItem();
		mViewPager.setHasSetAdapter(false);
		mViewPager.startRoll();
	}

	private OnPagerClickCallback getPagerCallBack() {
		// TODO Auto-generated method stub
		OnPagerClickCallback mCallBack = new OnPagerClickCallback() {

			@Override
			public void onPagerClick(int position) {
				// TODO Auto-generated method stub
				if (mSubCategoryBannerDatas.get(position).getmTargetType() == 1) {
					UIHelper.toSearchActivity(getActivity(),
							mSubCategoryBannerDatas.get(position)
									.getmTargetId());
				} else if (mSubCategoryBannerDatas.get(position)
						.getmTargetType() == 2) {
					UIHelper.toProductInfoActivity(getActivity(),
							mSubCategoryBannerDatas.get(position)
									.getmTargetId(),ProductInfoFragment.FLAG_NORMAL);
				}
			}
		};
		return mCallBack;
	}

	private void initViews(View view) {
		// TODO Auto-generated method stub
		if (mSubCategoryDatas == null || mSubCategoryDatas.size() == 0) {
			return;
		}

		mGridView = (GridView) view.findViewById(R.id.category_info_grid);
		mAdapter = new CommonAdapter<SubCategory>(getActivity(),
				mSubCategoryDatas, R.layout.item_category_info) {

			@Override
			public void convert(ViewHolder holder, SubCategory item,
					int position) {
				// TODO Auto-generated method stub
				View view = holder.getView(R.id.category_info_item_relative);
				int h = (Constant.SCREEN_WIDTH*7/9-DensityUtils.dp2px(getActivity(), 10))/2;
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);      
				view.setLayoutParams(lp);
				holder.setText(R.id.category_info_item_content, item.getmName());
				holder.setImageResource(item.getmImgUrl(), R.id.category_info_item_img);
			}
		};
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(getItemClickListener());
	}

	private OnItemClickListener getItemClickListener() {
		// TODO Auto-generated method stub
		OnItemClickListener mListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// UIHelper.toProductInfoActivity(getActivity(), 415);
				UIHelper.toSearchActivity(getActivity(), Integer
						.valueOf(mSubCategoryDatas.get(position).getmId()));
			}
		};
		return mListener;
	}

	@Override
	public void requestData() {

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}