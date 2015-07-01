package com.hylsmart.yihui.model.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonProListParser;
import com.hylsmart.yihui.dao.DataBaseInfo;
import com.hylsmart.yihui.dao.DataHelper;
import com.hylsmart.yihui.dao.Persistence;
import com.hylsmart.yihui.dao.SearchHistoryDbAdapter;
import com.hylsmart.yihui.model.home.adapter.CommonAdapter;
import com.hylsmart.yihui.model.home.adapter.ViewHolder;
import com.hylsmart.yihui.model.home.bean.SearchHistory;
import com.hylsmart.yihui.model.mall.adapter.ProItemAdapter;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.model.mall.fragment.ProductInfoFragment;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.RequestParamConfig;
import com.hylsmart.yihui.util.UIHelper;
import com.hylsmart.yihui.util.fragment.CommonFragment;
import com.hylsmart.yihui.util.view.XListView;

public class SearchFragment extends CommonFragment implements
		View.OnClickListener, XListView.IXListViewListener {

	private EditText mSearchEdit;
	private ImageView mSearchBtn;
	private ImageView mBackArrow;
	private TextView mSaleNumBtn;
	private TextView mPriceBtn;
	private TextView mStoreTimeBtn;
	private View mSaleNumDivider;
	private View mPriceDivider;
	private View mStoreTimeDivider;
	private ListView mHistoryListview;
	private TextView mClearText;
	private XListView mProductListview;
	private LinearLayout mProductListLayout;
	private LinearLayout mHintLayout;
	private TextView mHintText;

	private ArrayList<Product> mdata = new ArrayList<Product>();
	private ProItemAdapter mProductListAdapter;

	private int fid;
	private int order = 1;
	private int sort = 0;
	private String name = "";
	private int page = 0;
	private int size = 10;

	private DataHelper mDataHelper;
	private SearchHistoryDbAdapter mHistoryDbAdapter;
	private ArrayList<Persistence> mHistoryList = new ArrayList<Persistence>();
	private HistoryAdapter mHistoryListviewAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fid = getActivity().getIntent().getIntExtra(
				JsonKey.ProductGroupBuyKey.ID, 0);

		mDataHelper = new DataHelper();
		mHistoryDbAdapter = new SearchHistoryDbAdapter(getActivity());
		mDataHelper.setmAdapter(mHistoryDbAdapter);
		mHistoryList = (ArrayList<Persistence>) mDataHelper.getDataList();

		if(fid != 0){
			startReqTask(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_search, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onInitView(view);
	}

	private void onInitView(View view) {
		mBackArrow = (ImageView) view.findViewById(R.id.back_arrow);
		mBackArrow.setOnClickListener(this);

		mSaleNumBtn = (TextView) view.findViewById(R.id.search_sale_num_btn);
		mSaleNumBtn.setOnClickListener(this);

		mPriceBtn = (TextView) view.findViewById(R.id.search_price_btn);
		mPriceBtn.setOnClickListener(this);

		mStoreTimeBtn = (TextView) view
				.findViewById(R.id.search_store_time_btn);
		mStoreTimeBtn.setOnClickListener(this);

		mSaleNumDivider = view.findViewById(R.id.search_sale_num_divider);
		mSaleNumDivider.setBackgroundColor(getResources().getColor(
				R.color.orange_bg_home_header));
		mPriceDivider = view.findViewById(R.id.search_price_divider);
		mStoreTimeDivider = view.findViewById(R.id.search_store_time_divider);

		mSearchEdit = (EditText) view.findViewById(R.id.search_edit);
		mSearchEdit.setOnClickListener(this);

		mSearchBtn = (ImageView) view.findViewById(R.id.search_btn);
		mSearchBtn.setOnClickListener(this);

		mProductListLayout = (LinearLayout) view
				.findViewById(R.id.search_product_list_layout);
		mProductListLayout.setVisibility(View.GONE);
		mProductListview = (XListView) view
				.findViewById(R.id.search_product_listview);
		mProductListAdapter = new ProItemAdapter(getActivity(), mdata,
				Constant.TUANGOU_PRO_ITEM_STYLE);
		mProductListview.setAdapter(mProductListAdapter);
		mProductListview.setXListViewListener(this);
		mProductListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertview, int position,
					long arg3) {
				Product item = (Product) mProductListAdapter.getItem(position-1);
				UIHelper.toProductInfoActivity(getActivity(), Integer.parseInt(item.getmId()),ProductInfoFragment.FLAG_NORMAL);
			}
		});

		mHistoryListview = (ListView) view
				.findViewById(R.id.search_history_listview);
		if(fid == 0){
			mHistoryListview.setVisibility(View.VISIBLE);
		}
		
		/*LinearLayout clearLayout = new LinearLayout(getActivity());
		AbsListView.LayoutParams Parentparams = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		clearLayout.setLayoutParams(Parentparams);
		clearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		clearLayout.setPadding(200, 20, 200, 0);*/
		
		/*mClearText = new TextView(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		mClearText.setLayoutParams(params);
		mClearText.setPadding(100, 20, 100, 20);
		mClearText.setBackgroundColor(getResources().getColor(R.color.white_bg_home));
		mClearText.setTextColor(getResources().getColor(
				R.color.TextColorBLACK_NORMAL));
		mClearText.setTextSize(16);
		mClearText.setGravity(Gravity.CENTER);
		
		mClearText.setText(getResources().getString(
				R.string.clear_search_history));*/
		LinearLayout mClearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.footview_list_history, null);
		mClearText = (TextView) mClearLayout.findViewById(R.id.footer_text);
		mHistoryListview.addFooterView(mClearLayout);
		mClearText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Persistence p : mHistoryList) {
					SearchHistory history = (SearchHistory) p;
					mDataHelper.deleteData(String.valueOf(history.getId()));
				}
				mHistoryList.clear();
				mHistoryListviewAdapter.notifyDataSetChanged();
				//mHistoryListview.setAdapter(mHistoryListviewAdapter);
				mHistoryListview.setVisibility(View.GONE);
			}
		});
		
		mHistoryListviewAdapter = new HistoryAdapter(mHistoryList);
		mHistoryListview.setAdapter(mHistoryListviewAdapter);
		mHistoryListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position != mHistoryListviewAdapter.getCount()){
					Persistence p = (Persistence) parent.getAdapter().getItem(position);
					SearchHistory history = (SearchHistory) p;
					mSearchEdit.setText(history.getName());
					resetParam();
					name = history.getName();
					startReqTask(SearchFragment.this);
				}
				
			}
		});
		
		mHintText = (TextView) view.findViewById(R.id.hint_text);
		mHintLayout = (LinearLayout) view.findViewById(R.id.hint_layout);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
				+ Constant.SEARCH_PRODUCT_URL);
		url.setmGetParamPrefix(RequestParamConfig.FID).setmGetParamValues(
				String.valueOf(fid));
		url.setmGetParamPrefix(RequestParamConfig.PAGE).setmGetParamValues(
				page + "");
		url.setmGetParamPrefix(RequestParamConfig.SORT).setmGetParamValues(
				sort + "");
		url.setmGetParamPrefix(RequestParamConfig.ORDER).setmGetParamValues(
				order + "");
		url.setmGetParamPrefix(RequestParamConfig.NAME)
				.setmGetParamValues(name);
		param.setmParserClassName(CommonProListParser.class.getName());
		param.setmHttpURL(url);
		RequestManager.getRequestData(getActivity(), creatReqSuccessListener(),
				createErrorListener(), param);
	}

	private Response.Listener<Object> creatReqSuccessListener() {
		return new Listener<Object>() {

			@Override
			public void onResponse(Object result) {
				AppLog.Logd(result.toString());
				AppLog.Loge(" data success to load" + result.toString());

				if (getActivity() != null && !isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					
					if(name != ""){
						SearchHistory history = new SearchHistory();
		            	 history.setName(name);
		            	 if(mHistoryDbAdapter.getDataList(DataBaseInfo.History.COLUMN_NAME_PRO_NAME+ "=?" , new String[]{name}).size() != 0) {
		            		 SearchHistory repeatHistory = (SearchHistory) mHistoryDbAdapter.getDataList(DataBaseInfo.History.COLUMN_NAME_PRO_NAME+ "=?" , new String[]{name}).get(0);
		            		 mDataHelper.deleteData(String.valueOf(repeatHistory.getId()));
		            	 }
		            	 mDataHelper.addData(history);
					}
					

					mHistoryListview.setVisibility(View.GONE);
					ResultInfo rInfo = (ResultInfo) result;
					if (rInfo.getmCode() == Constant.RESULT_CODE) {
						ArrayList<Product> list = (ArrayList<Product>) rInfo
								.getObject();
						if(list.size()==0||list==null){
							mHintText.setVisibility(View.VISIBLE);
							mHintLayout.setVisibility(View.VISIBLE);
						}else{
							mHintText.setVisibility(View.GONE);
							mHintLayout.setVisibility(View.GONE);
							mProductListLayout.setVisibility(View.VISIBLE);
						}
						
						if (page == 0) {
							mdata.clear();
						}
						mdata.addAll(list);
						mProductListAdapter.updateList(mdata);

						
						mProductListview.stopRefresh();
						mProductListview.stopLoadMore();
						if (list.size() < Constant.DEFAULT_SIZE) {
							mProductListview.setPullLoadEnable(false);
						} else {
							mProductListview.setPullLoadEnable(true);
						}

					} else {
						showSmartToast(rInfo.getmMessage() + "",
								Toast.LENGTH_LONG);
					}

				}

			}
		};
	}

	private Response.ErrorListener createErrorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (getActivity() == null || isDetached()) {
					return;
				}
				mLoadHandler.removeMessages(Constant.NET_FAILURE);
				mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);

				mProductListview.stopRefresh();
				mProductListview.stopLoadMore();
			}

		};
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_arrow:
			getActivity().finish();
			break;
		case R.id.search_sale_num_btn:
			//mSaleNumDivider.setBackgroundColor(getResources().getColor(
					//R.color.orange_bg_home_header));
			mSaleNumDivider.setBackgroundDrawable(getResources().getDrawable(R.drawable.divider_search_tab_selected));
			//mSaleNumBtn.setCompoundDrawables(null, null, null, getResources().getDrawable(R.drawable.line_bottom_search));
			mPriceDivider.setBackgroundColor(getResources().getColor(
					R.color.gray_bg_divider_search));
			mStoreTimeDivider.setBackgroundColor(getResources().getColor(
					R.color.gray_bg_divider_search));
			
			mSaleNumBtn.setTextColor(getResources().getColor(R.color.orange_bg_home_header));
			mPriceBtn.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			mStoreTimeBtn.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			
			order = Constant.PRODUCT_SALE_REQ;
			
			
			onRefresh();
			break;
		case R.id.search_price_btn:
			mSaleNumDivider.setBackgroundColor(getResources().getColor(
					R.color.gray_bg_divider_search));
			mPriceDivider.setBackgroundColor(getResources().getColor(
					R.color.orange_bg_home_header));
			mStoreTimeDivider.setBackgroundColor(getResources().getColor(
					R.color.gray_bg_divider_search));
			
			mSaleNumBtn.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			mPriceBtn.setTextColor(getResources().getColor(R.color.orange_bg_home_header));
			mStoreTimeBtn.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			
			order = Constant.PRODUCT_PRICE_REQ;

			if (sort == 0) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.arrow_price_up);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mPriceBtn.setCompoundDrawables(null, null, drawable, null);
				sort = 1;
			} else {
				Drawable drawable = getResources().getDrawable(
						R.drawable.arrow_price_down);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mPriceBtn.setCompoundDrawables(null, null, drawable, null);
				sort = 0;
			}
			onRefresh();
			break;
		case R.id.search_store_time_btn:
			mSaleNumDivider.setBackgroundColor(getResources().getColor(
					R.color.gray_bg_divider_search));
			mPriceDivider.setBackgroundColor(getResources().getColor(
					R.color.gray_bg_divider_search));
			mStoreTimeDivider.setBackgroundColor(getResources().getColor(
					R.color.orange_bg_home_header));
			
			mSaleNumBtn.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			mPriceBtn.setTextColor(getResources().getColor(R.color.TextColorBLACK_NORMAL));
			mStoreTimeBtn.setTextColor(getResources().getColor(R.color.orange_bg_home_header));
			
			order = Constant.PRODUCT_SHELVES_REQ;
			onRefresh();
			break;
		case R.id.search_edit:
			mHistoryList.clear();
			mHistoryList.addAll(mDataHelper.getDataList() );
			AppLog.Logd(mDataHelper.getDataList().size() + "条");
			mHistoryListviewAdapter.notifyDataSetChanged();
			mProductListLayout.setVisibility(View.GONE);
			mHistoryListview.setVisibility(View.VISIBLE);
			mHintText.setVisibility(View.GONE);
			mHintLayout.setVisibility(View.GONE);
			break;
		case R.id.search_btn:
			String searchContent = mSearchEdit.getText().toString();
			if (TextUtils.isEmpty(searchContent)) {
				Toast.makeText(getActivity(), R.string.search_wanted_goods,
						Toast.LENGTH_LONG).show();
			} else {
				resetParam();
				name = searchContent;
				startReqTask(this);
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		page = 0;
		requestData();
	}

	@Override
	public void onLoadMore() {
		page = page + 1;
		requestData();
	}

	private void resetParam() {
		fid = 0;
		order = 1;
		sort = 0;
		name = "";
		page = 0;
		size = 10;
	}

	private class HistoryAdapter extends CommonAdapter<Persistence> {

		public HistoryAdapter(List<Persistence> data) {
			super(getActivity(), data, R.layout.list_item_history_list);
		}

		@Override
		public void convert(ViewHolder holder, Persistence item, int position) {
			final SearchHistory data = (SearchHistory) item;
			holder.setText(R.id.history_text, data.getName());
			ImageView imag = holder.getView(R.id.clear_history_imag);
			imag.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mDataHelper.deleteData(String.valueOf(data.getId()));
					mHistoryList.clear();
					mHistoryList.addAll(mDataHelper.getDataList());
					notifyDataSetChanged();
					if (mHistoryList.size() == 0) {
						mHistoryListview.setVisibility(View.GONE);
					} else {
						mHistoryListview.setAdapter(new HistoryAdapter(
								mHistoryList));
					}

				}
			});
		}

	}

}
