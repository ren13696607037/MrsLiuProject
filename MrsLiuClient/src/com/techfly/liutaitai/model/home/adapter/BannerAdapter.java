package com.techfly.liutaitai.model.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.model.home.bean.Banner;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.UIHelper;

public class BannerAdapter extends PagerAdapter {

	private Context mContext;
	private ArrayList<Banner> mdata;
	private ImageLoader mLoader;
	
	public BannerAdapter(Context context,ArrayList<Banner> data){
		mContext = context;
		mdata = data;
		mLoader = ImageLoader.getInstance();
	}
	
	@Override
	public int getCount() {
		return mdata.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(final ViewGroup container, int position) {
		final ImageView image = new ImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		image.setLayoutParams(params);
		container.addView(image);
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int height = mContext.getResources().getDisplayMetrics().heightPixels;
		container.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		image.setScaleType(ScaleType.FIT_XY);
		mLoader.displayImage(mdata.get(position).getmImage(), image, ImageLoaderUtil.mBannerLoaderOptions);
		
		final int type = mdata.get(position).getmTargetType();
		final int targetId = mdata.get(position).getmTargetId();
		image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(type == 1){
					UIHelper.toSearchActivity(mContext, targetId);
				}else if(type == 2){
//					UIHelper.toProductInfoActivity(mContext, targetId,ProductInfoFragment.FLAG_NORMAL);
				}
			}
		});
		return image;
	}

	/*@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	 */
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		//((ViewPager)container).removeView((View)object);
	}

	
}
