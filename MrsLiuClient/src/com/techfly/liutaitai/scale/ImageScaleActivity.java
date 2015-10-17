package com.techfly.liutaitai.scale;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.scale.resource.BaseImageResource;
import com.techfly.liutaitai.scale.resource.ImageResourceFactory;
import com.techfly.liutaitai.scale.resource.ResourceType;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ImageScaleActivity extends FragmentActivity implements OnPhotoTapListener{

	private DragViewPager mViewPager;
	private View mMaskLayer;
	private Gallery mGallery;
	private BaseImageResource mResource;
	private int mIndex;
	private boolean isSingle = false;
	private ArrayList<String> mListUrls = new ArrayList<String>();
	private RelativeLayout mFootView;
	private ArrayList<GalleryData> mList;
	private boolean mDel;
	private ArrayList<ImageEntity> mItems;
	private PhotoPagerAdapter adapter;
	private GalleryAdapter galleryAdapter;
   
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mList = intent.getParcelableArrayListExtra(IntentBundleKey.PICTURE_TITLE);
		mListUrls = intent.getStringArrayListExtra(IntentBundleKey.PICTURE_URL);
		mIndex = intent.getExtras().getInt(IntentBundleKey.PICTURE_INDEX);
		mDel = intent.getExtras().getBoolean(IntentBundleKey.PICTURE_DEL);
		mItems = intent.getExtras().getParcelableArrayList(IntentBundleKey.PICTURE_ENTITY);
		showView(mListUrls);
	}

	private void showView(List<?> data) {
		ResourceType type = ResourceType.values()[ResourceType.RES_HTTP.ordinal()];
		mResource = ImageResourceFactory.getInstance(this).getResource(type, data);
		mGallery = (Gallery) findViewById(R.id.gallery_bottom);
		mMaskLayer = findViewById(R.id.pager_mask_layer);
		mFootView = (RelativeLayout) findViewById(R.id.pager_footer);
		RelativeLayout.LayoutParams params = (LayoutParams) mFootView.getLayoutParams();
		params.height = Constant.SCREEN_HEIGHT / 5;
		Log.d("height", params.height + "");
		mFootView.setLayoutParams(params);
		mViewPager = (DragViewPager) findViewById(R.id.pager);
		galleryAdapter = new GalleryAdapter(this, mList, mViewPager);
		if (!isSingle && mList != null) {
			mFootView.setVisibility(View.VISIBLE);
			mGallery.getLayoutParams().height = Constant.SCREEN_HEIGHT / 5;
			mGallery.setAdapter(galleryAdapter);
			galleryAdapter.setPictureDel(mDel);
			mIndex = (mIndex==data.size()?mIndex-1:mIndex);
			mGallery.setSelection(mIndex);
			mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
					mViewPager.setCurrentItem(position);
					galleryAdapter.notifyDataSetChanged();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		} else {
			mFootView.setVisibility(View.GONE);
		}
		mMaskLayer.setVisibility(View.VISIBLE);
		adapter = new PhotoPagerAdapter(getSupportFragmentManager(), mResource);
		adapter.setOnPhotoTapListener(this);
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setCurrentItem(mIndex<data.size()-1?mIndex:data.size()-1);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mGallery.setSelection(arg0);
				mIndex = arg0;
				galleryAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	@Override
	public void onPhotoTap(View view, float x, float y) {
	    finish();
		if (mMaskLayer.isShown()) {
			mMaskLayer.setVisibility(ViewPager.INVISIBLE);
			mMaskLayer.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		} else {
			mMaskLayer.setVisibility(View.VISIBLE);
			mMaskLayer.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	
}
