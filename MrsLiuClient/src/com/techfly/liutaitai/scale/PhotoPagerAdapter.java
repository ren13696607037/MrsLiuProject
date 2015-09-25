package com.techfly.liutaitai.scale;

import java.util.WeakHashMap;

import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewGroup;

import com.techfly.liutaitai.scale.resource.BaseImageResource;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter implements
        OnPageChangeListener {

    private BaseImageResource mResource;
    private OnPhotoTapListener mTapListener;
    private WeakHashMap<Integer, ImageScaleFragment> mFragmentMap;
	

    private static final String TAG = PhotoPagerAdapter.class.getSimpleName();

    public PhotoPagerAdapter(FragmentManager mgr, BaseImageResource resource) {
        super(mgr);
        this.mResource = resource;
        this.mFragmentMap = new WeakHashMap<Integer, ImageScaleFragment>();
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mTapListener = listener;
    }

    @Override
    public int getCount() {
        return mResource.size();
    }

    @Override
    public Fragment getItem(int position) {
        ImageScaleFragment fragment = ImageScaleFragment.newInstance(mResource,
                mTapListener, position);
        mFragmentMap.put(position, fragment);
        return fragment;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.finishUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        Log.i(TAG, "setPrimaryItem>>" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        if (position > 0) {
            ImageScaleFragment preFragment = mFragmentMap.get(position - 1);
            if (preFragment != null) {
                preFragment.restore();
            }
        }
        if (position < mResource.size() - 1) {
            ImageScaleFragment postFragment = mFragmentMap.get(position + 1);
            if (postFragment != null) {
                postFragment.restore();
            }
        }
    }

}
