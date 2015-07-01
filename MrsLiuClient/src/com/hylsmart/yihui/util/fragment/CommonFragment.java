package com.hylsmart.yihui.util.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.SmartToast;
import com.hylsmart.yihui.util.view.LoadingDialog.LoadingDialogListener;
import com.hylsmart.yihui.util.view.LoadingLayout;

public abstract class CommonFragment extends Fragment implements
		LoadingDialogListener {
	private TextView headerTitle;
	private ImageView headerLeftIcon;
	private ImageView headerRightIcon;
	private ImageView heederRightMore;
	private TextView shopCarTv;
	private TextView headerRightText;
    private View lineView;
	protected LinearLayout mLinearLayout;
	protected LinearLayout mDataLoadLinearLayout;
	protected LinearLayout mNoDataLinearLayout;
	protected TextView mNoDataNetTitle;
	private Activity mActivity;
	private CommonFragment mCommonFragment;
	protected Handler mLoadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case Constant.NET_SUCCESS:
				dissmissLoading();
				break;
			case Constant.NET_STATUS_NODATA:
				dissmissLoading();
				break;
			case Constant.NET_LOAD:
				showLoading();
				break;
			case Constant.NET_REFRESH:
				dissmissLoading();
				break;
			case Constant.NET_REFRESHING:
				showLoading();
				break;
			case Constant.NET_FAILURE:
				dissmissLoading();
				showSmartToast(R.string.loading_fail_server, 2000);
			default:
				break;
			}
		}

	};
	protected LoadingLayout mLoadingLayout;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
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
		onInitTitleView(view);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	private void onInitTitleView(View view) {
		headerTitle = (TextView) view.findViewById(R.id.header_title);
		headerLeftIcon = (ImageView) view.findViewById(R.id.header_left_icon);
		headerRightIcon = (ImageView) view.findViewById(R.id.header_right_icon);
		heederRightMore = (ImageView) view.findViewById(R.id.header_right_more);
		headerRightText = (TextView) view.findViewById(R.id.header_rignt_text);
		lineView = view.findViewById(R.id.line);
		shopCarTv = (TextView) view.findViewById(R.id.shop_car_num);
		mLoadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
	}

	public void dissmissLoading() {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.dissmissLoadingLayout();
	}

	public void showLoading() {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showLoading();
	}

	public void showLoading(int strId) {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showLoading(strId);
	}

	public void showLoading(String str) {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showLoading(str);
	}

	public void showNoData() {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showNoData();
	}

	public void showMessage(int strId) {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showMessage(strId);
	}

	public void showMessage(String str) {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showMessage(str);
	}

	public void showMessage(int strId, int drawableId) {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showMessage(strId, drawableId);
	}

	public void showMessage(String str, int drawableId) {
		if (mLoadingLayout == null)
			return;
		mLoadingLayout.showMessage(str, drawableId);
	}

	/**
	 * 
	 * @param stringId
	 */
	protected void setTitleText(int stringId) {
		if (null != headerTitle) {
			headerTitle.setText(stringId);
		}
	}

	/**
	 * 
	 * @param string
	 */
	protected void setTitleText(String string) {
		if (null != headerTitle) {
			headerTitle.setText(string);
		}
	}

	/**
	 * 
	 * @param string
	 */
	protected void setTitleText(String string, int drawableId) {
		if (null != headerTitle) {
			headerTitle.setText(string);
		}
	}

	/**
	 * 
	 * @param string
	 */
	protected void setTitleOnClickListener(OnClickListener onClickListener) {
		if (null != headerTitle) {
			headerTitle.setClickable(true);
			headerTitle.setOnClickListener(onClickListener);
		}
	}

	/**
	 * 
	 * @param stringId
	 */
	protected void setLeftHeadIcon(int drawableId) {
		if (null != headerLeftIcon) {
			if (drawableId == -1) {
				headerLeftIcon.setVisibility(View.GONE);
			} else {
				headerLeftIcon.setVisibility(View.VISIBLE);
				if (drawableId != Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG) {
					headerLeftIcon.setImageResource(drawableId);
				}
				headerLeftIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						mActivity.finish();
					}
				});

			}
		}

	}

	/*
	 * @param stringId
	 */
	protected void setLeftHeadIcon(int drawableId,
			OnClickListener onClickListener) {
		if (null != headerLeftIcon) {
			headerLeftIcon.setVisibility(View.VISIBLE);
			if (drawableId != Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG) {
				headerLeftIcon.setImageResource(drawableId);
			}
			headerLeftIcon.setOnClickListener(onClickListener);
		}

	}

	/**
	 * 
	 * @param drawableId
	 */
	protected void setRightHeadIcon(int drawableId, OnClickListener listener) {

		if (null != headerRightIcon) {
			headerRightIcon.setVisibility(View.VISIBLE);
			headerRightIcon.setImageResource(drawableId);
			headerRightIcon.setOnClickListener(listener);
		}
	}

	/**
	 * 
	 * @param drawableId
	 */
	public void setRightHeadIcon(int drawableId) {
		if (null != headerRightIcon) {
			headerRightIcon.setVisibility(View.VISIBLE);
			headerRightIcon.setImageResource(drawableId);
			headerRightIcon.setClickable(true);
		}
	}

	/**
	 * 
	 * @param drawableId
	 */
	public View getRightHeadView() {
		if (null != heederRightMore) {
			return heederRightMore;
		}
		return null;
	}

	/**
	 * 
	 * @param drawableId
	 */
	protected void setRightMoreIcon(int drawableId, OnClickListener listener) {
		if (null != heederRightMore) {
			heederRightMore.setVisibility(View.VISIBLE);
			heederRightMore.setImageResource(drawableId);
			heederRightMore.setOnClickListener(listener);
		}
	}

	protected void setShopCarCount(int count) {
		shopCarTv.setVisibility(View.VISIBLE);
		shopCarTv.setText(count + "");
	}

	protected void setShopCarGone() {
		shopCarTv.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @param drawableId
	 */
	public void setRightMoreIcon(int drawableId) {
		if (null != heederRightMore) {
			heederRightMore.setVisibility(View.VISIBLE);
			heederRightMore.setImageResource(drawableId);
			heederRightMore.setClickable(true);
		}
	}

	/**
	 * 
	 * @param drawableId
	 */
	public void setRightIconGone() {
		if (null != headerRightIcon) {
			headerRightIcon.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * @param string
	 */
	protected void setRightText(String string) {
	    if(TextUtils.isEmpty(string)){
	        if (null != headerRightText) {
                if(lineView!=null){
                    lineView.setVisibility(View.GONE);
                }
                headerRightText.setText(string);
            }
	    }else{
	        if (null != headerRightText) {
	            if(lineView!=null){
	                lineView.setVisibility(View.VISIBLE);
	            }
	            headerRightText.setText(string);
	        }
	    }
		
	}

	/**
	 * 
	 * @param stringId
	 */
	protected void setRightText(int stringId) {
		if (null != headerRightText) {
		    if(lineView!=null){
                lineView.setVisibility(View.VISIBLE);
            }
			headerRightText.setText(stringId);
		}
	}

	/*
	 * @param stringId
	 */
	protected void setRightText(int stringId, OnClickListener listener) {
		if (null != headerRightText) {
		    if(lineView!=null){
                lineView.setVisibility(View.VISIBLE);
            }
			headerRightText.setVisibility(View.VISIBLE);
			headerRightText.setText(stringId);
			headerRightText.setOnClickListener(listener);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

	protected void startReqTask(CommonFragment commonFragment) {
		mCommonFragment = this;
		mLoadHandler.removeMessages(Constant.NET_LOAD);
		mLoadHandler.sendEmptyMessage(Constant.NET_LOAD);
		mCommonFragment.requestData();
	}

	protected void startLoading(CommonFragment commonFragment) {
		mCommonFragment = this;
		mLoadHandler.removeMessages(Constant.NET_LOAD);
		mLoadHandler.sendEmptyMessage(Constant.NET_LOAD);
	}

	public abstract void requestData();

	@Override
	public void onDataRefresh() {
		mLoadHandler.removeMessages(Constant.NET_REFRESH);
		mLoadHandler.sendEmptyMessage(Constant.NET_REFRESH);
		startReqTask(mCommonFragment);
	}

	public void showSmartToast(String text, int duration) {
		SmartToast.makeText(this.getActivity(), text, duration).show();
	}

	public void showSmartToast(int resId, int duration)
			throws Resources.NotFoundException {
	    if(this.getActivity()!=null && !isDetached()){
	        SmartToast.makeText(this.getActivity(), resId, duration).show();
	    }
	
	}

}
