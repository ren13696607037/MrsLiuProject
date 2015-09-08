package com.techfly.liutaitai.util.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.ImageCacheUtil;

public class RollViewPager extends ViewPager{

	public RollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		viewPagerTask = new ViewPagerTask();
		myOnTouchListener = new MyOnTouchListener();
	}

	public RollViewPager(Context context) {
		super(context);
		this.context = context;

		viewPagerTask = new ViewPagerTask();
		myOnTouchListener = new MyOnTouchListener();
	}

	private String TAG = "RollViewPager";
	private Context context;
	private int currentItem;
	private ArrayList<String> uriList;// 图片地址
	private ArrayList<View> dots;// 点的位置不固定，所以需要让调用者传入
	private TextView title;// 用于显示每个图片的标题
	private String[] titles;
	private int[] resImageIds;
	private int dot_focus_resId;// 获取焦点的图片id
	private int dot_normal_resId;// 未获取焦点的图片id
	private OnPagerClickCallback onPagerClickCallback;
	private boolean isShowResImage = false;
	MyOnTouchListener myOnTouchListener;
	ViewPagerTask viewPagerTask;

	private long start = 0;

	public class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				getParent().requestDisallowInterceptTouchEvent(true);
				start = System.currentTimeMillis();
				handler.removeCallbacksAndMessages(null);
				
				break;
			case MotionEvent.ACTION_MOVE:
				
				getParent().requestDisallowInterceptTouchEvent(true);
				handler.removeCallbacks(viewPagerTask);
				break;
			case MotionEvent.ACTION_CANCEL:
				getParent().requestDisallowInterceptTouchEvent(true);
				startRoll();
				break;
			case MotionEvent.ACTION_UP:
				getParent().requestDisallowInterceptTouchEvent(true);
				long duration = System.currentTimeMillis() - start;
				if (duration <= 400) {
					if (onPagerClickCallback != null)
						onPagerClickCallback.onPagerClick(currentItem);
				}
				startRoll();
				break;
			case MotionEvent.ACTION_SCROLL:
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			}
			return true;
		}
	}

	public class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			currentItem = (currentItem + 1)
					% (isShowResImage ? resImageIds.length : uriList.size());
			handler.obtainMessage().sendToTarget();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setCurrentItem(currentItem);
			startRoll();
		}
	};

	public void setDot(ArrayList<View> dots, int dot_focus_resId,
			int dot_normal_resId) {
		this.dots = dots;
		this.dot_focus_resId = dot_focus_resId;
		this.dot_normal_resId = dot_normal_resId;
	}

	public void setPagerCallback(OnPagerClickCallback onPagerClickCallback) {
		this.onPagerClickCallback = onPagerClickCallback;
	}

	/**
	 * 设置网络图片的url集合，也可以是本地图片的uri
	 * 图片uriList集合，可以是网络地址，如：http://www.ssss.cn/3.jpg，也可以是本地的uri,如：
	 * assest://image/3.jpg，uriList和resImageIds只需传入一个
	 * 
	 * @param uriList
	 */
	public void setUriList(ArrayList<String> uriList) {
		isShowResImage = false;
		this.uriList = uriList;
	}

	/**
	 * 设置res图片的id 图片uriList集合，可以是网络地址，如：http://www.ssss.cn/3.jpg，也可以是本地的uri,如：
	 * assest://image/3.jpg，uriList和resImageIds只需传入一个
	 * 
	 * @param resImageIds
	 */
	public void setResImageIds(int[] resImageIds) {
		isShowResImage = true;
		this.resImageIds = resImageIds;
	}

	/**
	 * 标题相关
	 * 
	 * @param title
	 *            用于显示标题的TextView
	 * @param titles
	 *            标题数组
	 */
	public void setTitle(TextView title, String[] titles) {
		this.title = title;
		this.titles = titles;
		if (title != null && titles != null && titles.length > 0)
			title.setText(titles[0]);// 默认显示第一张的标题
	}

	private boolean hasSetAdapter = false;
	private final int SWITCH_DURATION = 3500;
	
	public void resetCurrentItem(){
		currentItem = 0;
	}
	
	public void removeCallback(){
		if(handler!=null){
			handler.removeCallbacks(viewPagerTask);
		}
	}
	
	public void setHasSetAdapter(boolean b){
		hasSetAdapter = b;
	}

	/**
	 * 开始滚动
	 */
	public void startRoll() {
		if (!hasSetAdapter) {
			hasSetAdapter = true;
			this.setOnPageChangeListener(new MyOnPageChangeListener());
			ViewPagerAdapter mAdapter = new ViewPagerAdapter();
			this.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		}
		handler.postDelayed(viewPagerTask, SWITCH_DURATION);
	}
	
	public void startLoad(){
		if (!hasSetAdapter) {
			hasSetAdapter = true;
			this.setOnPageChangeListener(new MyOnPageChangeListener());
			ViewPagerAdapter mAdapter = new ViewPagerAdapter();
			this.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {
		int oldPosition = 0;

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			if (title != null)
				title.setText(titles[position]);
			if (dots != null && dots.size() > 0) {
				dots.get(position).setBackgroundResource(dot_focus_resId);
				dots.get(oldPosition).setBackgroundResource(dot_normal_resId);
			}
			oldPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	/**
	 * 自带设配器,需要回调类来处理page的点击事件
	 * 
	 * @author dance
	 * 
	 */
	class ViewPagerAdapter extends PagerAdapter {
		

		@Override
		public int getCount() {
			return isShowResImage ? resImageIds.length : uriList.size();
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			View view = View.inflate(context, R.layout.viewpager_item, null);
			((ViewPager) container).addView(view);
			view.setOnTouchListener(myOnTouchListener);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			if (isShowResImage) {
				imageView.setImageResource(resImageIds[position]);
			} else {
				ImageLoader.getInstance().displayImage(uriList.get(position),
						imageView, ImageCacheUtil.OPTIONS.default_options);
				
			}
			return view;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// 将ImageView从ViewPager移除
			((ViewPager) arg0).removeView((View) arg2);
		}

		
	}

	@Override
	protected void onDetachedFromWindow() {
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}

	/**
	 * 处理page点击的回调接口
	 * 
	 * @author dance
	 * 
	 */
	public interface OnPagerClickCallback {
		public abstract void onPagerClick(int position);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {//本控件的点击事件自己解决，告诉父控件不要干扰我。。
		
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(ev);
	}



	
	
}
