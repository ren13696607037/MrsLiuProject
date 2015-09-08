package com.techfly.liutaitai.model.mall.activities;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.Standard;
import com.techfly.liutaitai.model.mall.bean.StandardClass;
import com.techfly.liutaitai.model.mall.fragment.ProductInfoFragment;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.DensityUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.view.GridViewForScrollView;
import com.techfly.liutaitai.util.view.ProductUpdateView;

public class StandardDialog extends Dialog {

	private Context mContext;
	private Product mProduct;

	private RelativeLayout mParent;
	private TextView mAddShopCarTv;
	private TextView mShopNowTv;
	private LinearLayout mStandardItemLinear;

	private ProductUpdateView mProductUpdateView;

	private ArrayList<StandardClass> mStandardClassList;
	private ArrayList<GridViewForScrollView> mGridList;

	public StandardDialog(ProductInfoFragment context, Product mProduct) {
		super(context.getActivity(), R.style.StandardWindowStyle);
		mContext = context.getActivity();
		this.mProduct = mProduct;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window theWindow = getWindow();
		theWindow.setGravity(Gravity.RIGHT | Gravity.FILL_VERTICAL);
		setContentView(R.layout.activity_standard);
		WindowManager.LayoutParams lp = theWindow.getAttributes();
		theWindow.setAttributes(lp);
//		theWindow.setBackgroundDrawable(null);
		theWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

			}
		});
		setWindow();
		if (mProduct != null) {
			mStandardClassList = mProduct.getmStandardClassList();
			AppLog.Logd("Shi",
					"mStandardClassList:::" + mStandardClassList.size());
		}
		initViews();
	}

	private void setWindow() {
		// TODO Auto-generated method stub
		mParent = (RelativeLayout) findViewById(R.id.standard_parent_linear);
		LayoutParams params = (LayoutParams) mParent.getLayoutParams();
		int top = DensityUtils.dp2px(mContext, 48);
		int left = Constant.SCREEN_WIDTH / 3;
		params.setMargins(left, top, 0, 0);
		params.width = Constant.SCREEN_WIDTH * 2 / 3;
		mParent.setLayoutParams(params);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mAddShopCarTv = (TextView) findViewById(R.id.standard_shop_car_add);
		mAddShopCarTv.setOnClickListener(getClickListener());

		mShopNowTv = (TextView) findViewById(R.id.standard_shop_now);
		mShopNowTv.setOnClickListener(getClickListener());

		mProductUpdateView = (ProductUpdateView) findViewById(R.id.standard_count_product_update_count);
		mStandardItemLinear = (LinearLayout) findViewById(R.id.standard_item_linear);
		loadData();
	}

	private android.view.View.OnClickListener getClickListener() {
		// TODO Auto-generated method stub
		android.view.View.OnClickListener mListener = new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		};
		return mListener;
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (mGridList == null) {
			mGridList = new ArrayList<GridViewForScrollView>();
		}
		mGridList.clear();

		for (int i = 0; i < mStandardClassList.size(); i++) {
			StandardClass sc = mStandardClassList.get(i);
			View item = LayoutInflater.from(mContext).inflate(
					R.layout.standard_linear_item, null);
			// LinearLayout mItemLinear = (LinearLayout) item
			// .findViewById(R.id.standard_item_linear);
			TextView mName = (TextView) item
					.findViewById(R.id.standard_item_name);
			// mName.setOnClickListener(getClickListener());
			GridViewForScrollView gv = (GridViewForScrollView) item
					.findViewById(R.id.standard_item_grid);
			if (sc != null) {
				CommonAdapter<Standard> adapter = new CommonAdapter<Standard>(
						mContext, sc.getmArrayList(), R.layout.item_standard_1) {

					@Override
					public void convert(ViewHolder holder, final Standard item,
							final int position) {
						// TODO Auto-generated method stub
						holder.setText(R.id.standard_item_text_1,
								item.getmValue());
						View v = holder.getView(R.id.standard_item_text_1);
						v.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SmartToast.makeText(
										mContext,
										"选中::" + position + ":::"
												+ item.getmValue() + ":::"
												+ item.getmLinearIndex(),
										Toast.LENGTH_SHORT).show();
							}
						});
						AppLog.Logd("Shi",
								"item.isHave():::position:::" + item.isHave());
						v.setEnabled(item.isHave());
						v.setClickable(item.isHave());
					}
				};
				gv.setAdapter(adapter);
				gv.setOnItemSelectedListener(getSelectedListener());

				mGridList.add(gv);
				mName.setText(sc.getmClassName().trim());
			}
			mStandardItemLinear.addView(item);
		}
	}

	private OnItemSelectedListener getSelectedListener() {
		// TODO Auto-generated method stub
		OnItemSelectedListener mListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SmartToast.makeText(mContext, "选中" + position,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				SmartToast.makeText(mContext, R.string.standard_nothing,
						Toast.LENGTH_SHORT).show();
			}

		};
		return mListener;
	}

}
