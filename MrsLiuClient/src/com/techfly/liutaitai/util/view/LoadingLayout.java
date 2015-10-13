package com.techfly.liutaitai.util.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.ShowUtils;

/**
 * 
 * @ClassName: LoadingLayout
 * @Description: TODO(loading��)
 * @author cr
 * @date 2014-12-18 ����3:40:25
 * 
 */
public class LoadingLayout extends FrameLayout {
	private static final String TAG_PROGRESS = "ProgressLayout.TAG_PROGRESS";
	private View mLoadingLayout;
	private ProgressBar mProgress;
	private TextView mLoadingText;
	private Context mContext;
	private List<View> mContentViews = new ArrayList<View>();

	public LoadingLayout(Context context) {
		super(context);
		initView(context);

	}

	public LoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	/**
	 * 
	 * @Title: initView
	 * @Description: TODO(init the view)
	 * @param @param context �趨�ļ�
	 * @return void ��������
	 */
	private void initView(Context context) {
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
		view.setTag(TAG_PROGRESS);
		mLoadingLayout = (View) view.findViewById(R.id.loadingLayout);
		mProgress = (ProgressBar) view.findViewById(R.id.loadingLayoutProgress);
		mLoadingText = (TextView) view.findViewById(R.id.loadingLayoutText);
		measure(0, 0);
		addView(view, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);

		if (child.getTag() == null || (!child.getTag().equals(TAG_PROGRESS))) {
			mContentViews.add(child);
		}
	}

	public void showLoading() {
		mLoadingText.setText(R.string.loading_text);
		loading();
	}

	public void showLoading(String str) {
		mLoadingText.setText(str);
		loading();
	}

	public void showLoading(int strId) {
		mLoadingText.setText(strId);
		loading();

	}

	private void loading() {
		mLoadingLayout.setVisibility(View.VISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		mLoadingText.setVisibility(View.VISIBLE);
		setContentVisibility(false);
	}

	public void showNoData() {
		mLoadingText.setText(R.string.loading_fail_nodata);
		setTextDrawableTop(mLoadingText, R.drawable.icon_error);
		mProgress.setVisibility(View.GONE);
		mLoadingText.setVisibility(View.VISIBLE);
		setContentVisibility(false);
	}

	public void showMessage(String text) {
		mLoadingText.setText(text);
		setTextDrawableTop(mLoadingText, R.drawable.icon_error);
		message();
	}

	public void showMessage(int strId) {
		mLoadingText.setText(strId);
		setTextDrawableTop(mLoadingText, R.drawable.icon_error);
		message();
	}

	public void showMessage(String text, int drawableId) {
		mLoadingText.setText(Html.fromHtml(text));
		setTextDrawableTop(mLoadingText, drawableId);
		message();
	}

	public void showMessage(int strId, int drawableId) {
		mLoadingText.setText(strId);
		setTextDrawableTop(mLoadingText, drawableId);
		message();
	}

	private void message() {
		mProgress.setVisibility(View.GONE);
		mLoadingText.setVisibility(View.VISIBLE);
	}

	/***
	 * 
	 * @Title: dissmissLoadingLayout
	 * @Description: TODO(�ر�loading�Ĳ���)
	 * @param �趨�ļ�
	 * @author wl
	 * @return void ��������
	 * @date 2014-12-12 ����3:39:30
	 */
	public void dissmissLoadingLayout() {
		mLoadingLayout.setVisibility(View.GONE);
	
  	    setContentVisibility(true);
	}

	/**
	 * 
	 * @Title: setTextDrawableTop
	 * @Description: TODO(����TextView��DrawableTop)
	 * @param drawableId
	 *            ��Դ�ļ�
	 * @param textview
	 *            Ҫ���õ�textview
	 * @return void ��������
	 * @date 2014-11-28 ����3:41:21
	 */
	protected void setTextDrawableTop(TextView textView, int drawableId) {
		Drawable drawable = getContext().getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		textView.setCompoundDrawables(null, drawable, null, null);
		textView.setCompoundDrawablePadding(ShowUtils.dip2px(mContext, 10));
	}

	private void setContentVisibility(boolean visible) {
		for (View v : mContentViews) {
			if (!Collections.<Integer> emptyList().contains(v.getId())) {
				v.setVisibility(visible ? View.VISIBLE : View.GONE);
			}
		}
	}

}
