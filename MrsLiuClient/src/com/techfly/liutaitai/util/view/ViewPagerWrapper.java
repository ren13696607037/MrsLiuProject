package com.techfly.liutaitai.util.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.Utility;

public class ViewPagerWrapper extends LinearLayout {

	private View mView;
	private ViewPager mPager;

	private LinearLayout mDotsLayout;
	private int mInterval = 5000;
	private boolean mAutoPlay = true;
	private boolean mShowDots = true;
	private boolean mHeightChangable = true;
	// private Float mScale = 0.55f;

	private int mCount = 0;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mCount++;
			mPager.setCurrentItem(mCount % mPager.getAdapter().getCount());
		}

	};
	private ImageView mCurrentDotView;
	private Timer timer;

	public ViewPagerWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
		mView = LayoutInflater.from(context).inflate(R.layout.common_viewpager, null);
		mPager = (ViewPager) mView.findViewById(R.id.viewpager);
		Utility.resizeViewHeight(mPager, mView, 0.4f);
		mDotsLayout = (LinearLayout) mView.findViewById(R.id.pager_dots);
		if (mShowDots) {
			mDotsLayout.setVisibility(View.VISIBLE);
		} else {
			mDotsLayout.setVisibility(View.GONE);
		}
		addView(mView);
	}

	public ViewPager getmPager() {
		return mPager;
	}

	public void setAdapter(final PagerAdapter adapter) {
		if (adapter == null)
			return;
		mPager.setAdapter(adapter);
		/*
		 * if(mHeightChangable == true){
		 * mView.getViewTreeObserver().addOnGlobalLayoutListener( new
		 * OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() { int width =
		 * mPager.getWidth(); int height = (int) (width * 0.55f);
		 * LinearLayout.LayoutParams params = new
		 * LinearLayout.LayoutParams(width, height);
		 * mView.setLayoutParams(params);
		 * mView.getViewTreeObserver().removeGlobalOnLayoutListener(this); } });
		 * }
		 */
		mDotsLayout.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			ImageView dotView = new ImageView(getContext());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			int marginLeft = (int) (5 * getContext().getResources().getDisplayMetrics().density);
			params.setMargins(marginLeft, 0, 0, 0);
			dotView.setLayoutParams(params);
			dotView.setImageResource(R.drawable.ic_dot_unselected);
			mDotsLayout.addView(dotView);
		}
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (mCurrentDotView != null) {
					mCurrentDotView.setImageResource(R.drawable.ic_dot_unselected);
				}
				mCount = position;
				ImageView dotView = (ImageView) mDotsLayout.getChildAt(position);
				dotView.setImageResource(R.drawable.ic_dot_selected);
				mCurrentDotView = dotView;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		if (timer == null) {
			timer = new Timer();
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					if (mAutoPlay) {
						mHandler.sendEmptyMessage(0);
					}
				}
			};
			timer.schedule(timerTask, 0, mInterval);
		}
	}

	public void setInterval(int interval) {
		mInterval = interval;
	}

	public void setAutoPlay(boolean isAutoPlay) {
		mAutoPlay = isAutoPlay;
	}

	public void showDots(boolean show) {
		mShowDots = show;
	}

	public void setHeightChangable(boolean isChangable) {
		mHeightChangable = isChangable;
	}

	/*
	 * public Float getmScale() { return mScale; }
	 * 
	 * public void setmScale(Float mScale) { this.mScale = mScale; }
	 */

}
