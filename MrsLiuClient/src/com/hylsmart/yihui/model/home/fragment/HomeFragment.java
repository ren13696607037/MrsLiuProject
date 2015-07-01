package com.hylsmart.yihui.model.home.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.home.adapter.BannerAdapter;
import com.hylsmart.yihui.model.home.adapter.CommonAdapter;
import com.hylsmart.yihui.model.home.adapter.HomeCategoryListAdapter;
import com.hylsmart.yihui.model.home.adapter.SecKillAdapter;
import com.hylsmart.yihui.model.home.adapter.ViewHolder;
import com.hylsmart.yihui.model.home.bean.Advertisement;
import com.hylsmart.yihui.model.home.bean.Banner;
import com.hylsmart.yihui.model.home.bean.Category;
import com.hylsmart.yihui.model.home.bean.ContactInfo;
import com.hylsmart.yihui.model.home.bean.HomeBean;
import com.hylsmart.yihui.model.home.bean.HotGood;
import com.hylsmart.yihui.model.home.bean.ProductGroupBuy;
import com.hylsmart.yihui.model.home.bean.SecKillItem;
import com.hylsmart.yihui.model.home.parser.HomeBeanParser;
import com.hylsmart.yihui.model.mall.fragment.ProductInfoFragment;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.ImageLoaderUtil;
import com.hylsmart.yihui.util.SharePreferenceUtils;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.Utility;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.GridViewForScrollView;
import com.hylsmart.yihui.util.view.ListViewForScrollView;
import com.hylsmart.yihui.util.view.LoadingLayout;
import com.hylsmart.yihui.util.view.ViewPager;
import com.hylsmart.yihui.util.view.ViewPagerWrapper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeFragment extends CommonFragment implements View.OnClickListener{

	private TextView mSearchText;
	private ViewPagerWrapper mViewPagerWrapper;
	private GridViewForScrollView mGridView;
	private ListView mListView;
	private ImageView mAdImage;
	private ImageView mMoreTuanGouImg;
	private TextView mRefreshText;
	private LoadingLayout mLoadingLayout;
	private ScrollView mScrollView;
	private LinearLayout mSeckillLayout;
	private ViewPager mSeckillPager;
	private TextView mHourText;
	private TextView mMinuteText;
	private TextView mSecondText;
	private LinearLayout mHotGoodsLayout;
	private RelativeLayout mHotGoodsOneLayout;
	private RelativeLayout mHotGoodsTwoLayout;
	private RelativeLayout mHotGoodsThreeLayout;
	private ImageView mHotGoodsImageOne;
	private TextView mHotGoodsDescribeOne;
	private TextView mHotGoodsNameOne;
	private TextView mHotGoodsPriceOne;
	private ImageView mHotGoodsImageTwo;
	private TextView mHotGoodsDescribeTwo;
	private TextView mHotGoodsNameTwo;
	private TextView mHotGoodsPriceTwo;
	private ImageView mHotGoodsImageThree;
	private TextView mHotGoodsDescribeThree;
	private TextView mHotGoodsNameThree;
	private TextView mHotGoodsPriceThree;
	
	private ArrayList<Banner> mdataBanner = new ArrayList<Banner>();
	private ArrayList<ProductGroupBuy> mdataGrid = new ArrayList<ProductGroupBuy>();
	private ArrayList<Category> mdataListview = new ArrayList<Category>();
	private Advertisement mdataAd;
	private ContactInfo mdataInfo;
	private ArrayList<SecKillItem> mdataSecKill = new ArrayList<SecKillItem>();
	private ArrayList<HotGood> mdataHotGoods = new ArrayList<HotGood>();
	
	private CommonAdapter<ProductGroupBuy> mGridAdapter;
	private HomeCategoryListAdapter mListViewAdapter;
	private BannerAdapter mBannerAdapter;
	private SecKillAdapter mSecKillAdapter;
	
    private ImageLoader mLoader;
    private SharePreferenceUtils mSPUtils;
    private int mScreenWidth;
    
    private long mTime = 10000;
    private int mHours;
    private int mMinutes;
    private int mSeconds;
    private Timer mTimer;
    private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(Utility.getHours(mTime)<10){
				mHourText.setText("0"+Utility.getHours(mTime));
			}else{
				mHourText.setText(String.valueOf(Utility.getHours(mTime)));
			}
			
			if(Utility.getMinutes(mTime)<10){
				mMinuteText.setText("0"+Utility.getMinutes(mTime));
			}else{
				mMinuteText.setText(String.valueOf(Utility.getMinutes(mTime)));
			}
			
			if(Utility.getSeconds(mTime)<10){
				mSecondText.setText("0"+Utility.getSeconds(mTime));
			}else{
				mSecondText.setText(String.valueOf(Utility.getSeconds(mTime)));
			}
			
		}
    	
    };
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoader = ImageLoader.getInstance();
		mSPUtils = SharePreferenceUtils.getInstance(getActivity());
		
		startReqTask(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mSearchText = (TextView) view.findViewById(R.id.search_text_home);
		mSearchText.setOnClickListener(this);
		
		mScrollView = (ScrollView) view.findViewById(R.id.main_scrollview);
		mScrollView.setVisibility(View.GONE);
		
		//Banner广告栏
		mViewPagerWrapper = (ViewPagerWrapper) view.findViewById(R.id.vpWrapper);
		
		//易起团
		initGroupByView(view);
		
		//限时秒杀
		initSecKillView(view);
		
		//热卖商品
		initHotGoods(view);
		
		//首页广告条
		initAdvertise(view);
		
		//分类楼层
		initCategoryFloor(view);
		
		
		
		
		mLoadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
		
		mRefreshText = (TextView) view.findViewById(R.id.refresh_text);
		mRefreshText.setOnClickListener(this);
		
		
		
	}
	
	//装填易起团界面
	private void initGroupByView(View view){
		mMoreTuanGouImg = (ImageView) view.findViewById(R.id.more_group_buy);
		mMoreTuanGouImg.setOnClickListener(this);
		
		mGridView = (GridViewForScrollView) view.findViewById(R.id.grid_group_buy);
		LinearLayout.LayoutParams gridParams = (android.widget.LinearLayout.LayoutParams) mGridView.getLayoutParams();
		gridParams.height = (int) (Constant.SCREEN_WIDTH*0.54);
		mGridView.setLayoutParams(gridParams);
		mGridAdapter = new CommonAdapter<ProductGroupBuy>(getActivity(), mdataGrid, R.layout.item_grid_group_buy) {

			@Override
			public void convert(final ViewHolder holder, ProductGroupBuy item, int position) {
				holder.setText(R.id.name_group_buy, item.getmCommodityName());
				holder.setText(R.id.current_price_group_buy, getString(R.string.goods_price, String.valueOf(item.getmPrice())));
				holder.setText(R.id.old_price_group_buy, getString(R.string.goods_price, String.valueOf(item.getmOldPrice())));
				holder.setText(R.id.buy_num_group_buy, getString(R.string.sale_sum, item.getmSaleNum()));
				
				final ImageView imageProduct = holder.getView(R.id.imag_group_buy);
				//Utility.resizeViewHeight(holder.getConvertView(), imageProduct, 0.5f);
				LayoutParams layoutParams = (LayoutParams) imageProduct.getLayoutParams();
				layoutParams.width = (int) (Constant.SCREEN_WIDTH*0.4);
				layoutParams.height = layoutParams.width;
				imageProduct.setLayoutParams(layoutParams);
				
				mLoader.displayImage(item.getmImage(), imageProduct, ImageLoaderUtil.mHallIconLoaderOptions);
			}
		};
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> convertView, View parent, int positon, long arg3) {
				ProductGroupBuy product = mGridAdapter.getItem(positon);
				//UIHelper.toTakingOrderActivity(getActivity(), product.getmCommodityName(), String.valueOf(product.getmPrice()), "");
				UIHelper.toProductInfoActivity(getActivity(), product.getmId(),ProductInfoFragment.FLAG_TUAN_GOU);
			}
		});
	}

	//装填限时秒杀模块
	private void initSecKillView(View view) {
		mSeckillLayout = (LinearLayout) view.findViewById(R.id.sec_kill_layout);
		
		mHourText = (TextView) view.findViewById(R.id.hour_text);
		mMinuteText= (TextView) view.findViewById(R.id.minute_text);
		mSecondText = (TextView) view.findViewById(R.id.second_text);
		
		mSeckillPager = (ViewPager) mSeckillLayout.findViewById(R.id.seckill_pager);
		
	}
	
	//装填首页广告条
	private void initAdvertise(View view){
		mAdImage = (ImageView) view.findViewById(R.id.advertise);
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		int height = (int) (mScreenWidth * 0.11);
		mAdImage.getLayoutParams().width = mScreenWidth;
		mAdImage.getLayoutParams().height = height;
		mAdImage.setLayoutParams(mAdImage.getLayoutParams());
		mAdImage.setOnClickListener(this);
	}
	
	//装填热卖商品
	private void initHotGoods(View view){
		mHotGoodsLayout = (LinearLayout) view.findViewById(R.id.hot_goods_layout);
		//LinearLayout.LayoutParams hotGoodsLayoutParams = (android.widget.LinearLayout.LayoutParams) mHotGoodsLayout.getLayoutParams();
		//hotGoodsLayoutParams.width = (int) (mScreenWidth*0.52);
		//mHotGoodsLayout.setLayoutParams(hotGoodsLayoutParams);
		mHotGoodsOneLayout = (RelativeLayout) mHotGoodsLayout.findViewById(R.id.hot_goods_one_layout);
		mHotGoodsTwoLayout = (RelativeLayout) mHotGoodsLayout.findViewById(R.id.hot_goods_two_layout);
		mHotGoodsThreeLayout = (RelativeLayout) mHotGoodsLayout.findViewById(R.id.hot_goods_three_layout);
		
		mHotGoodsImageOne = (ImageView) mHotGoodsLayout.findViewById(R.id.hot_goods_image_one);
		mHotGoodsDescribeOne = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_describe_one);
		mHotGoodsNameOne = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_name_one);
		mHotGoodsPriceOne = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_price_one);
		
		mHotGoodsImageTwo = (ImageView) mHotGoodsLayout.findViewById(R.id.hot_goods_image_two);
		mHotGoodsDescribeTwo = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_describe_two);
		mHotGoodsNameTwo = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_name_two);
		mHotGoodsPriceTwo = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_price_two);
		
		mHotGoodsImageThree = (ImageView) mHotGoodsLayout.findViewById(R.id.hot_goods_image_three);
		/*LayoutParams imageThreeParams = (LayoutParams) mHotGoodsImageThree.getLayoutParams();
		imageThreeParams.height = imageThreeParams.width;
		imageThreeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mHotGoodsImageThree.setLayoutParams(imageThreeParams);*/
		mHotGoodsDescribeThree = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_describe_three);
		mHotGoodsNameThree = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_name_three);
		mHotGoodsPriceThree = (TextView) mHotGoodsLayout.findViewById(R.id.hot_goods_price_three);
	}
	
	//装填分类楼层
	private void initCategoryFloor(View view){
		mListView = (ListView) view.findViewById(R.id.listview_home_category);
		mListViewAdapter = new HomeCategoryListAdapter(getActivity(), mdataListview, R.layout.item_list_home_category);
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(null);
		/*mListView.setOnTouchListener(new OnTouchListener() {
           

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
            }
				return false;
			}
    });*/
	}

	@Override
	public void requestData() {
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.SYSTEM_URL);
		
		RequestParam param = new RequestParam();
		param.setmHttpURL(url);
		param.setmParserClassName(HomeBeanParser.class.getName());
		
		RequestManager.getRequestData(getActivity(), creatSuccessListener(), creatErrorListener(), param);
	}

	private Response.Listener<Object> creatSuccessListener(){
		return new Response.Listener<Object>() {

			@Override
			public void onResponse(Object obj) {
				if(getActivity() != null && !isDetached()){
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					
					
					
					mRefreshText.setVisibility(View.GONE);
					mScrollView.setVisibility(View.VISIBLE);
					HomeBean data = (HomeBean) obj;
					SharePreferenceUtils.getInstance(getActivity()).saveShareInfo(data.getmShareUrl(), data.getmShareContent());
					mTime = Long.parseLong(data.getmSecKill().getmStartTime());
					mHandler.sendEmptyMessage(0);
					
					mdataBanner.addAll(data.getmCommodityBanner());
					mBannerAdapter = new BannerAdapter(getActivity(), mdataBanner);
					mViewPagerWrapper.setAdapter(mBannerAdapter);
					
					if(data.getmTuanGouCard().size() <= 2){
						mdataGrid.addAll(data.getmTuanGouCard());
					}else{
						mdataGrid.addAll(data.getmTuanGouCard().subList(0, 2));
					}
					mGridAdapter.notifyDataSetChanged();
					
					mdataListview.addAll(data.getmCommodityCard());
					mListViewAdapter.notifyDataSetChanged();
					Utility.setListViewHeightBasedOnChildren(mListView);
					
					mdataAd = data.getmAdvertising();
					mLoader.displayImage(mdataAd.getmImage(), mAdImage, ImageLoaderUtil.mHallIconLoaderOptions);
					
					mdataInfo = new ContactInfo();
					mdataInfo.setmQQ(data.getmQQ());
					mdataInfo.setmWeixin(data.getmWeixin());
					mSPUtils.saveContactInfo(mdataInfo);
					AppLog.Loge(" qq,weixin" + mSPUtils.getContactInfo().getmWeixin());
					
					mdataSecKill.addAll(data.getmSecKill().getmItems());
					mSecKillAdapter = new SecKillAdapter(getActivity(),mdataSecKill,data.getmSecKill().getmStatus());
					mSeckillPager.setAdapter(mSecKillAdapter);
					
					mdataHotGoods = data.getmCommodityHot();
					if(mdataHotGoods != null && mdataHotGoods.size() >= 3){
						mLoader.displayImage(mdataHotGoods.get(0).getmImage(), mHotGoodsImageOne, ImageLoaderUtil.mHallIconLoaderOptions);
						mHotGoodsDescribeOne.setText(mdataHotGoods.get(0).getmDescription());
						mHotGoodsNameOne.setText(mdataHotGoods.get(0).getmCommodityName());
						mHotGoodsPriceOne.setText(String.format(getResources().getString(R.string.goods_price), String.valueOf(mdataHotGoods.get(0).getmPrice())));
						mHotGoodsOneLayout.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View view) {
								UIHelper.toProductInfoActivity(getActivity(), mdataHotGoods.get(0).getmCommodityId(),ProductInfoFragment.FLAG_NORMAL);
							}
						});
						
						mLoader.displayImage(mdataHotGoods.get(1).getmImage(), mHotGoodsImageTwo, ImageLoaderUtil.mHallIconLoaderOptions);
						mHotGoodsDescribeTwo.setText(mdataHotGoods.get(1).getmDescription());
						mHotGoodsNameTwo.setText(mdataHotGoods.get(1).getmCommodityName());
						mHotGoodsPriceTwo.setText(String.format(getResources().getString(R.string.goods_price), String.valueOf(mdataHotGoods.get(1).getmPrice())));
						mHotGoodsTwoLayout.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View view) {
								UIHelper.toProductInfoActivity(getActivity(), mdataHotGoods.get(1).getmCommodityId(),ProductInfoFragment.FLAG_NORMAL);
							}
						});
						
						mLoader.displayImage(mdataHotGoods.get(2).getmImage(), mHotGoodsImageThree, ImageLoaderUtil.mHallIconLoaderOptions);
						mHotGoodsDescribeThree.setText(mdataHotGoods.get(2).getmDescription());
						mHotGoodsNameThree.setText(mdataHotGoods.get(2).getmCommodityName());
						mHotGoodsPriceThree.setText(String.format(getResources().getString(R.string.price), String.valueOf(mdataHotGoods.get(2).getmPrice())));
						mHotGoodsThreeLayout.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View view) {
								UIHelper.toProductInfoActivity(getActivity(), mdataHotGoods.get(2).getmCommodityId(),ProductInfoFragment.FLAG_NORMAL);
							}
						});
					}
					
					if(mTimer == null){
						mTimer = new Timer();
						TimerTask timerTask = new TimerTask(){

							@Override
							public void run() {
								if(mTime>0){
									mTime = mTime-1;
								}
								mHandler.sendEmptyMessage(0);
							}};
							mTimer.schedule(timerTask, 0, 1000);
					}
					
				}
			}
		};
	}

	private Response.ErrorListener creatErrorListener(){
		return new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_FAILURE);
					mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
					
					mRefreshText.setVisibility(View.VISIBLE);
					mScrollView.setVisibility(View.GONE);
					mLoadingLayout.setVisibility(View.GONE);
				}
			}};
	}
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.search_text_home:
			UIHelper.toSearchActivity(getActivity(),0);
			break;
		case R.id.advertise:
			UIHelper.toProductInfoActivity(getActivity(), mdataAd.getmGoodsId(),ProductInfoFragment.FLAG_NORMAL);
			break;
		case R.id.more_group_buy:
		    UIHelper.toTuangouInfoActivity(getActivity());
		    break;
		case R.id.refresh_text:
		    startReqTask(HomeFragment.this);
		    break;
		default:
			break;
		}
	}

}
