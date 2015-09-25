package com.techfly.liutaitai.scale;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DragViewPager extends ViewPager {

    private OnPageChangeListener mRealOnPageChangeListener;

    public DragViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mRealOnPageChangeListener = listener;
        super.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                PagerAdapter adapter = getAdapter();
                if (adapter != null && adapter instanceof OnPageChangeListener) {
                    ((OnPageChangeListener) adapter)
                            .onPageScrollStateChanged(arg0);
                }
                mRealOnPageChangeListener.onPageScrollStateChanged(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                PagerAdapter adapter = getAdapter();
                if (adapter != null && adapter instanceof OnPageChangeListener) {
                    ((OnPageChangeListener) adapter).onPageScrolled(arg0, arg1,
                            arg2);
                }
                mRealOnPageChangeListener.onPageScrolled(arg0, arg1, arg2);
            }

            @Override
            public void onPageSelected(int arg0) {
                PagerAdapter adapter = getAdapter();
                if (adapter != null && adapter instanceof OnPageChangeListener) {
                    ((OnPageChangeListener) adapter).onPageSelected(arg0);
                }
                mRealOnPageChangeListener.onPageSelected(arg0);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * String action; switch (event.getAction()) { case
         * MotionEvent.ACTION_DOWN: action = "ACTION_DOWN"; break; case
         * MotionEvent.ACTION_MOVE: action = "ACTION_MOVE"; break; case
         * MotionEvent.ACTION_UP: action = "ACTION_UP"; break; case
         * MotionEvent.ACTION_CANCEL: action = "ACTION_CANCEL"; break; default:
         * action = String.valueOf(event.getAction()); break; } Log.e(TAG,
         * "onTouchEvent>>" + action);
         */
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        /*
         * String action; switch (event.getAction()) { case
         * MotionEvent.ACTION_DOWN: action = "ACTION_DOWN"; break; case
         * MotionEvent.ACTION_MOVE: action = "ACTION_MOVE"; break; case
         * MotionEvent.ACTION_UP: action = "ACTION_UP"; break; case
         * MotionEvent.ACTION_CANCEL: action = "ACTION_CANCEL"; break; default:
         * action = String.valueOf(event.getAction()); break; } Log.e(TAG,
         * "dispatchTouchEvent>>" + action);
         */
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        /*
         * String action; switch (event.getAction()) { case
         * MotionEvent.ACTION_DOWN: action = "ACTION_DOWN"; break; case
         * MotionEvent.ACTION_MOVE: action = "ACTION_MOVE"; break; case
         * MotionEvent.ACTION_UP: action = "ACTION_UP"; break; case
         * MotionEvent.ACTION_CANCEL: action = "ACTION_CANCEL"; break; default:
         * action = String.valueOf(event.getAction()); break; } Log.e(TAG,
         * "onInterceptTouchEvent>>" + action);
         */
        try {
            result = super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return result;
    }

}
