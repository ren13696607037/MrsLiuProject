package com.hylsmart.yihui.model.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.home.bean.Banner;
import com.hylsmart.yihui.model.home.bean.SecKillItem;
import com.hylsmart.yihui.model.mall.fragment.ProductInfoFragment;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.ImageLoaderUtil;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.Utility;
import com.hylsmart.yihui.util.view.ViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SecKillAdapter extends PagerAdapter {

	private Context mContext;
	private ArrayList<SecKillItem> mdata;
	private int mStatus;
	private ImageLoader mLoader;
	private int mScreenWidth;
	
	public SecKillAdapter(Context context,ArrayList<SecKillItem> data,int status){
		mContext = context;
		mdata = data;
		mStatus = status;
		mLoader = ImageLoader.getInstance();
		mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
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
		final SecKillItem mdataItem = mdata.get(position);
		LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_seckill_pager_layout, container,false);
		ViewPager.LayoutParams layoutParams = (com.hylsmart.yihui.util.view.ViewPager.LayoutParams) itemLayout.getLayoutParams();
		layoutParams.height = (int) (Constant.SCREEN_WIDTH * 0.42);
		itemLayout.setLayoutParams(layoutParams);
		itemLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(mStatus == 1){
					Toast.makeText(mContext, R.string.seckill_not_start, Toast.LENGTH_SHORT).show();
				}else if(mStatus == 3){
					Toast.makeText(mContext, R.string.seckill_over, Toast.LENGTH_SHORT).show();
				}else if(mStatus == 2){
					if(mdataItem.ismEnabled()){
						UIHelper.toProductInfoActivity(mContext, mdataItem.getmId(),ProductInfoFragment.FLAG_SECKILL);
					}else{
						Toast.makeText(mContext, R.string.seckill_over, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		ImageView mSeckillImage = (ImageView) itemLayout.findViewById(R.id.seckill_image);
		LayoutParams imageParams = (LayoutParams) mSeckillImage.getLayoutParams();
		imageParams.height = (int) (mScreenWidth*0.31);
		mSeckillImage.setLayoutParams(imageParams);
		mLoader.displayImage(mdata.get(position).getmImage(), mSeckillImage, ImageLoaderUtil.mHallIconLoaderOptions);
		
		TextView mSeckillName = (TextView) itemLayout.findViewById(R.id.seckill_name);
		mSeckillName.setText(mdata.get(position).getmName());
		
		TextView mSeckillPrice = (TextView) itemLayout.findViewById(R.id.seckill_price);
		mSeckillPrice.setText(String.format(mContext.getResources().getString(R.string.goods_price), String.valueOf(mdata.get(position).getmPrice())));
		
		TextView mSecKillDiscount = (TextView) itemLayout.findViewById(R.id.seckill_discount_text);
		String discount = String.valueOf(mdata.get(position).getmPrice()/mdata.get(position).getmOldPrice());
		String discontStr = discount.substring(0, discount.indexOf(".", 0)+2);
		mSecKillDiscount.setText(String.format(mContext.getResources().getString(R.string.discount), discontStr));
		
		container.addView(itemLayout);
		return itemLayout;
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

	@Override
	public float getPageWidth(int position) {
		if(position == 0 || position == getCount()-1){
			return 0.9f;
		}
		return 0.8f;
	}

	
}
