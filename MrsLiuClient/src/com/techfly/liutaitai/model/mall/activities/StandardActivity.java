package com.techfly.liutaitai.model.mall.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.shopcar.CallBackNullException;
import com.techfly.liutaitai.bizz.shopcar.ProductCountException;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.Standard;
import com.techfly.liutaitai.model.mall.bean.StandardClass;
import com.techfly.liutaitai.model.mall.bean.SubStandarrd;
import com.techfly.liutaitai.model.mall.fragment.ProductInfoFragment;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.DensityUtils;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.view.GridViewForScrollView;
import com.techfly.liutaitai.util.view.ProductUpdateView;
import com.techfly.liutaitai.util.view.ProductUpdateView.ShopCarCallBack;

public class StandardActivity extends Activity implements ShopCarCallBack {

	private static final int REFRESH = 0x9;
	private int mProductId = -1, mReqFlag = 1;
	private int mStock = -1;
	private String mStandard;

	private Context mContext;
	private Product mProduct;

	private RelativeLayout mParent;
	private TextView mAddShopCarTv;
	private TextView mShopNowTv;
	private LinearLayout mStandardItemLinear;
	private ImageView mImg;
	private TextView mPrice;
	private TextView mSerialNum;

	private ProductUpdateView mProductUpdateView;

	private ArrayList<StandardClass> mStandardClassList;
	private ArrayList<GridViewForScrollView> mGridList;
	// private ArrayList<Standard> mData = new ArrayList<Standard>();

	protected Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if (what == StandardActivity.REFRESH) {
				if (mGridList != null && mGridList.size() != 0) {
					for (int i = 0; i < mGridList.size(); i++) {
						@SuppressWarnings("unchecked")
						CommonAdapter<Standard> adapter = (CommonAdapter<Standard>) mGridList
								.get(i).getAdapter();
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
					}
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard);
		mContext = this;
		setWindow();
		getData();
		initViews();
	}

	private void setWindow() {
		// TODO Auto-generated method stub
		mParent = (RelativeLayout) findViewById(R.id.standard_parent_linear);
		LayoutParams params = (LayoutParams) mParent.getLayoutParams();
		int top = DensityUtils.dp2px(this, 48);
		int left = Constant.SCREEN_WIDTH / 4;
		params.setMargins(left, top, 0, 0);
		params.width = Constant.SCREEN_WIDTH * 3 / 4;
		mParent.setLayoutParams(params);
	}

	private void getData() {
		// TODO Auto-generated method stub
		Bundle b = getIntent().getExtras();
		mProduct = (Product) b.getSerializable(IntentBundleKey.PRODUCT);
		if (mProduct != null) {
			mStandardClassList = mProduct.getmStandardClassList();
			AppLog.Logd("Shi",
					"mStandardClassList:::" + mStandardClassList.size());
		}
		mReqFlag = b.getInt(IntentBundleKey.PRODUCT_REQ_FLAG);
		if (mReqFlag == ProductInfoFragment.FLAG_SECKILL) {
			mProduct.setmType(1);
		} else {
			mProduct.setmType(0);
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub

		mAddShopCarTv = (TextView) findViewById(R.id.standard_shop_car_add);
		mAddShopCarTv.setOnClickListener(getClickListener());

		mShopNowTv = (TextView) findViewById(R.id.standard_shop_now);
		mShopNowTv.setOnClickListener(getClickListener());

		mProductUpdateView = (ProductUpdateView) findViewById(R.id.standard_count_product_update_count);
		mProductUpdateView.onAttachView(mProduct);
		mStandardItemLinear = (LinearLayout) findViewById(R.id.standard_item_linear);

		mImg = (ImageView) findViewById(R.id.standard_img);
		ImageLoader.getInstance().displayImage(mProduct.getmImg(), mImg,
				ImageLoaderUtil.mHallIconLoaderOptions);
		mPrice = (TextView) findViewById(R.id.standard_price);
		mPrice.setText(getString(R.string.product_info_money)
				+ mProduct.getmPrice());
		mSerialNum = (TextView) findViewById(R.id.standard_serial_num);

		if (TextUtils.isEmpty(mProduct.getmSN())) {
			mSerialNum.setText(getString(R.string.standard_serial_num)
					+ getString(R.string.standard_serial_num_no));
		} else {
			mSerialNum.setText(getString(R.string.standard_serial_num)
					+ mProduct.getmSN());
		}

		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (mGridList == null) {
			mGridList = new ArrayList<GridViewForScrollView>();
		}
		mGridList.clear();

		for (int i = 0; i < mStandardClassList.size(); i++) {
			final StandardClass sc = mStandardClassList.get(i);
			View item = LayoutInflater.from(this).inflate(
					R.layout.standard_linear_item, null);
			// LinearLayout mItemLinear = (LinearLayout) item
			// .findViewById(R.id.standard_item_linear);
			TextView mName = (TextView) item
					.findViewById(R.id.standard_item_name);
			mName.setOnClickListener(getClickListener());
			GridViewForScrollView gv = (GridViewForScrollView) item
					.findViewById(R.id.standard_item_grid);
			final ArrayList<Standard> mData = new ArrayList<Standard>();
			if (sc != null) {
				if (sc.getmArrayList() != null) {
					mData.clear();
					mData.addAll(sc.getmArrayList());
				}
				CommonAdapter<Standard> adapter = new CommonAdapter<Standard>(
						this, mData, R.layout.item_standard_1) {

					@Override
					public void convert(ViewHolder holder, final Standard item,
							final int position) {
						// TODO Auto-generated method stub
						holder.setText(R.id.standard_item_text_1,
								item.getmValue());
						TextView v = holder.getView(R.id.standard_item_text_1);
						// AppLog.Logd("Shi", "item.isHave():::position:::"
						// + position + ":::" + item.isHave());
						v.setEnabled(item.isHave());
						v.setClickable(item.isHave());
						if (item.isHave()) {
							holder.getConvertView().setBackgroundResource(
									R.drawable.shape_collect_normal);
							v.setTextColor(Color.parseColor("#1491d2"));
							v.setBackgroundResource(R.color.color_F8);
						} else {
							holder.getConvertView().setBackgroundResource(
									R.drawable.shape_collect_pressed);
							v.setTextColor(Color.parseColor("#DFDFDF"));
							v.setBackgroundResource(R.color.background);
							item.setChecked(false);
							// item.setUserChecked(false);//清除状态，下次可选时不至于默认选中
						}
						if (item.isChecked()) {
							v.setBackgroundResource(R.color.grid_item_focus_color);
						} else {
							v.setBackgroundResource(R.color.color_F8);
						}
						v.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub dddddd
//								SmartToast.makeText(
//										mContext,
//										"选中::" + position + ":::"
//												+ item.getmValue() + ":::"
//												+ "第" + item.getmLinearIndex()
//												+ "行:::" + "data长度：："
//												+ mData.size(),
//										Toast.LENGTH_SHORT).show();
								if (item.isChecked()) {
									item.setChecked(false);
									item.setUserChecked(false);
									invert();
								} else {
									for (int j = 0; j < mData.size(); j++) {
										mData.get(j).setUserChecked(false);
										mData.get(j).setChecked(false);
									}
									for (int j = 0; j < mData.size(); j++) {
										if (j != position) {
											mData.get(j).setChecked(false);
											mData.get(j).setUserChecked(false);
										} else {
											mData.get(j).setChecked(true);
											mData.get(j).setUserChecked(true);
										}
									}
									refrash(item.getmLinearIndex(), item);// 此方法是复杂算法且不是太成熟
																			// ，应该使用类似反选的思想，之所以用这个是因为，我不想改了
								}
								AppLog.Logd("Shi", item.isChecked() + "");

								mHandler.removeMessages(StandardActivity.REFRESH);
								mHandler.sendEmptyMessage(StandardActivity.REFRESH);
								// getUnique();// 获取商品货号和库存
							}

						});

					}
				};
				gv.setAdapter(adapter);
				// gv.setIndex(i);
				mGridList.add(gv);
				mName.setText(sc.getmClassName().trim());
			}
			mStandardItemLinear.addView(item);
		}
	}

	private void refrash(int linearIndex, Standard standard) {
		// TODO Auto-generated method stub
		ArrayList<SubStandarrd> ssl = standard.getmSubList();
		if (ssl == null || ssl.size() == 0) {// 这里应该提示错误的
			return;
		}
		ArrayList<Integer> idList = new ArrayList<Integer>();// 用户选择行的id列表
		for (int i = 0; i < ssl.size(); i++) {// 可获取用户选择规格所含有的商品ID列表
			idList.add(ssl.get(i).getmId());
		}
		ArrayList<Standard> cacheData = new ArrayList<Standard>();
		for (int i = 0; i < mStandardClassList.size(); i++) {
			if (!standard.isChecked() && i == linearIndex) {
				continue;
			}
			cacheData.clear();// 清空临时网格数据以便缓存下一行；
			StandardClass sc = mStandardClassList.get(i);// 获取到了除刚才用户所选择的规格的类别
			ArrayList<Standard> data = sc.getmArrayList();// 该类别下的规格列表
			for (int j = 0; j < data.size(); j++) {
				ArrayList<SubStandarrd> compareSSL = data.get(j).getmSubList();// 对比的商品id列表
				for (int k = 0; k < compareSSL.size(); k++) {// 用户选择的规格包含的商品id
																// 和获取的的商品id的对比，查找是否有相同的(该算法有待改进)
					for (int k2 = 0; k2 < idList.size(); k2++) {
						if (compareSSL.get(k).getmId() == idList.get(k2)) {
							cacheData.add(data.get(j));
							break;
						}
					}
				}
			}
			@SuppressWarnings("unchecked")
			CommonAdapter<Standard> adapter = (CommonAdapter<Standard>) mGridList
					.get(i).getAdapter();
			if (adapter != null) {
				ArrayList<Standard> d = (ArrayList<Standard>) adapter
						.getDatas();
				if (d != null) {
					// d.clear();
					// d.addAll(cacheData);
					setIsHave(d, cacheData);
					for (int j = 0; j < d.size(); j++) {// 判断用户的选择
						d.get(j).setChecked(false);// 重置选择状态
						if (d.get(j).isUserChecked()) {
							d.get(j).setChecked(true);
						}
					}
				}
			}
		}
		twice();
	}

	private void twice() {// 二次判断
		ArrayList<Standard> list = new ArrayList<Standard>();// 以下为获取选择的规格所共有的ID列表
																// i3
		for (int i = 0; i < mStandardClassList.size(); i++) {
			ArrayList<Standard> sl = mStandardClassList.get(i).getmArrayList();
			for (int j = 0; j < sl.size(); j++) {
				if (sl.get(j).isChecked()) {
					list.add(sl.get(j));
				}
			}
		}
		if (list.size() == 0) {// 一个也没选，设置所有都可选
			for (int i = 0; i < mStandardClassList.size(); i++) {
				for (int j = 0; j < mStandardClassList.get(i).getmArrayList()
						.size(); j++) {
					mStandardClassList.get(i).getmArrayList().get(j)
							.setHave(true);
				}
			}
			return;
		}

		if (list.size() <= 1) {
			return;
		}
		ArrayList<Integer> i1 = new ArrayList<Integer>();
		ArrayList<Integer> i2 = new ArrayList<Integer>();
		ArrayList<Integer> i3 = new ArrayList<Integer>();
		for (int i = 0; i < list.get(0).getmSubList().size(); i++) {
			i1.add(list.get(0).getmSubList().get(i).getmId());
		}
		for (int i = 0; i < list.get(1).getmSubList().size(); i++) {
			i2.add(list.get(1).getmSubList().get(i).getmId());
		}

		for (int i = 0; i < i1.size(); i++) {
			if (i2.contains(i1.get(i)) && !i3.contains(i1.get(i))) {
				i3.add(i1.get(i));
			}
		}

		// 以下为设置状态
		ArrayList<Standard> list2 = new ArrayList<Standard>();// 所有的standard
		for (int i = 0; i < mStandardClassList.size(); i++) {
			ArrayList<Standard> sl = mStandardClassList.get(i).getmArrayList();
			for (int j = 0; j < sl.size(); j++) {
				list2.add(sl.get(j));
			}
		}

		if (list2.size() == 0) {
			return;
		}

		for (int i = 0; i < list2.size(); i++) {
			for (int j = 0; j < list2.get(i).getmSubList().size(); j++) {
				if (i3.contains(list2.get(i).getmSubList().get(j).getmId())) {
					list2.get(i).setHave(true);
					break;
				} else {
					list2.get(i).setHave(false);
				}
			}
		}
	}

	private void setIsHave(ArrayList<Standard> d, ArrayList<Standard> cacheData) {
		// TODO Auto-generated method stub
		for (int i = 0; i < d.size(); i++) {
			if (cacheData.contains(d.get(i))) {
				d.get(i).setHave(true);
			} else {
				d.get(i).setHave(false);
			}
		}

	}

	private void invert() {// 反选状态设置
		ArrayList<Standard> list = new ArrayList<Standard>();// 以下为获取选择的规格所共有的ID列表
		// i3
		for (int i = 0; i < mStandardClassList.size(); i++) {
			ArrayList<Standard> sl = mStandardClassList.get(i).getmArrayList();
			for (int j = 0; j < sl.size(); j++) {
				if (sl.get(j).isChecked()) {
					list.add(sl.get(j));
				}
			}
		}
		if (list.size() == 0) {// 一个也没选，设置所有都可选
			for (int i = 0; i < mStandardClassList.size(); i++) {
				for (int j = 0; j < mStandardClassList.get(i).getmArrayList()
						.size(); j++) {
					mStandardClassList.get(i).getmArrayList().get(j)
							.setHave(true);
				}
			}
			return;
		}

		ArrayList<Standard> list2 = new ArrayList<Standard>();// 所有的standard
		for (int i = 0; i < mStandardClassList.size(); i++) {
			ArrayList<Standard> sl = mStandardClassList.get(i).getmArrayList();
			for (int j = 0; j < sl.size(); j++) {
				list2.add(sl.get(j));
			}
		}

		if (list2.size() == 0) {
			return;
		}

		if (list.size() == 1) {// 只有一个选择
			ArrayList<Integer> i1 = new ArrayList<Integer>();
			for (int i = 0; i < list.get(0).getmSubList().size(); i++) {
				i1.add(list.get(0).getmSubList().get(i).getmId());
			}
			for (int i = 0; i < list2.size(); i++) {
				for (int j = 0; j < list2.get(i).getmSubList().size(); j++) {
					if (i1.contains(list2.get(i).getmSubList().get(j).getmId())) {
						list2.get(i).setHave(true);
						break;
					} else {
						list2.get(i).setHave(false);
					}
				}
			}
			return;
		}

		if (list.size() > 1) {
			ArrayList<Integer> i1 = new ArrayList<Integer>();
			ArrayList<Integer> i2 = new ArrayList<Integer>();
			ArrayList<Integer> i3 = new ArrayList<Integer>();
			for (int i = 0; i < list.get(0).getmSubList().size(); i++) {
				i1.add(list.get(0).getmSubList().get(i).getmId());
			}
			for (int i = 0; i < list.get(1).getmSubList().size(); i++) {
				i2.add(list.get(1).getmSubList().get(i).getmId());
			}

			for (int i = 0; i < i1.size(); i++) {
				if (i2.contains(i1.get(i)) && !i3.contains(i1.get(i))) {
					i3.add(i1.get(i));
				}
			}

			for (int i = 0; i < list2.size(); i++) {
				for (int j = 0; j < list2.get(i).getmSubList().size(); j++) {
					if (i3.contains(list2.get(i).getmSubList().get(j).getmId())) {
						list2.get(i).setHave(true);
						break;
					} else {
						list2.get(i).setHave(false);
					}
				}
			}
		}
	}

	private void getUnique() {// 获取唯一的商品货号和库存
		mProductId = -1;
		mStock = -1;
		ArrayList<Standard> list = new ArrayList<Standard>();
		for (int i = 0; i < mStandardClassList.size(); i++) {// 获取所有的被选择规格
			ArrayList<Standard> l = mStandardClassList.get(i).getmArrayList();
			for (int j = 0; j < l.size(); j++) {
				if (l.get(j).isChecked()) {
					list.add(l.get(j));
				}
			}
		}

		if (list.size() < mStandardClassList.size()) {
			SmartToast.makeText(mContext, R.string.standard_no,
					Toast.LENGTH_SHORT).show();
			return;
		}

		ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>();
		mStandard = "";
		for (int i = 0; i < list.size(); i++) {// 获取了所有的ID列表
			ArrayList<Integer> idL = new ArrayList<Integer>();
			idL.clear();
			for (int j = 0; j < list.get(i).getmSubList().size(); j++) {
				idL.add(list.get(i).getmSubList().get(j).getmId());
			}
			l.add(idL);
			if (i == list.size() - 1) {
				mStandard = mStandard + list.get(i).getmValue();
			} else {
				mStandard = mStandard + list.get(i).getmValue() + "、";
			}
		}

		if (l.size() == 0) {
			// 提示一下错误
			SmartToast.makeText(mContext, R.string.standard_error,
					Toast.LENGTH_SHORT).show();
			return;
		}

		ArrayList<Integer> i = l.get(0);
		if (i == null || i.size() == 0) {
			SmartToast.makeText(mContext, R.string.standard_error,
					Toast.LENGTH_SHORT).show();
			return;
		}

		for (int j = 0; j < l.size(); j++) {
			i.retainAll(l.get(j));
			if (i.size() == 1) {
				mProductId = i.get(0);
				break;
			}
		}
		// ArrayList<Integer> idL = new ArrayList<Integer>();
		// for (int i = 0; i < list.get(0).getmSubList().size(); i++) {//
		// 第一个规格的ID列表
		// idL.add(list.get(0).getmSubList().get(i).getmId());
		// }
		// ArrayList<Integer> cacheIdL = new ArrayList<Integer>();
		// for (int i = 0; i < list.size(); i++) {
		// for (int j = 0; j < list.get(i).getmSubList().size(); j++) {
		// for (int j2 = 0; j2 < idL.size(); j2++) {
		// if (idL.get(j2) == list.get(i).getmSubList().get(j)
		// .getmId()) {
		// cacheIdL.add(idL.get(j2));
		// }
		// }
		// if (cacheIdL.size() == 1) {
		// mProductId = cacheIdL.get(0);
		// canBreak = true;// 省略不必要的循环
		// break;
		// } else {
		// cacheIdL.clear();
		// }
		//
		// }
		// if (canBreak) {
		// break;
		// }
		// }
		// 获取库存
		for (int k = 0; k < list.get(0).getmSubList().size(); k++) {
			if (mProductId == list.get(0).getmSubList().get(k).getmId()) {
				mStock = list.get(0).getmSubList().get(k).getmStock();
				mProduct.setmStoreCount(mStock);
				break;
			}
		}

		if (mProductId != -1) {
			mProduct.setmProductId(mProductId);
			// mProduct.setmId(String.valueOf(mProductId));
		}

		if (mProductId == -1) {
			SmartToast.makeText(mContext, R.string.standard_error,
					Toast.LENGTH_SHORT).show();
		}

		// AppLog.Logd("Shi", "mProductId:::" + mProductId + "mStock:::" +
		// mStock);

	}

	private OnClickListener getClickListener() {
		// TODO Auto-generated method stub
		OnClickListener mListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// SmartToast.makeText(mContext, R.string.standard_nothing,
				// Toast.LENGTH_SHORT).show();
				switch (v.getId()) {

				case R.id.standard_shop_car_add:
					getUnique();
					if (mProductId == -1) {
						// SmartToast.makeText(mContext,
						// R.string.standard_error,
						// Toast.LENGTH_SHORT).show();
						break;
					}
					try {
                        mProductUpdateView
                        		.onReqPullToShopCart(StandardActivity.this);
                    } catch (CallBackNullException e) {
                         
                    } catch (ProductCountException e) {
                          
                    }
					break;
				case R.id.standard_shop_now:
					getUnique();
					if (mProductId == -1) {
						// SmartToast.makeText(mContext,
						// R.string.standard_error,
						// Toast.LENGTH_SHORT).show();
						break;
					}
					if (authLogin()) {
						UIHelper.toTakingOrderActivity(mContext, mProduct);
					} else {
						UIHelper.toLoginActivity(mContext);
					}
					break;

				default:
					break;
				}
			}
		};
		return mListener;
	}

	private boolean authLogin() {
		User user = SharePreferenceUtils.getInstance(mContext).getUser();
		if (user != null && !TextUtils.isEmpty(user.getmId())) {
			int id = Integer.parseInt(user.getmId());
			if (id > 0) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		setSelect();
		finish();
		return true;
	}

	private void setSelect() {
		getUnique();
		Intent i = new Intent();
		i.putExtra(IntentBundleKey.PRODUCT_ID, mProductId);
		i.putExtra(IntentBundleKey.PRODUCT_STANDARD, mStandard);
		i.putExtra(IntentBundleKey.PRODUCT_COUNT, mProduct.getmAmount());
		i.putExtra(IntentBundleKey.PRODUCT_STOCK, mStock);
		setResult(-1, i);
	}

	@Override
	public void onFail(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		// showSmartToast("成功加入购物车", Toast.LENGTH_LONG);
		SmartToast.makeText(mContext, "成功加入购物车", Toast.LENGTH_LONG).show();
	}

	@Override
	public void login() {
		// TODO Auto-generated method stub
		UIHelper.toLoginActivity(mContext);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setSelect();
		finish();
	}

}
