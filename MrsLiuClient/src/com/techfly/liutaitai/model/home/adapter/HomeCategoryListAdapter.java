package com.techfly.liutaitai.model.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.home.bean.Category;
import com.techfly.liutaitai.model.home.bean.CategoryItem;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.UIHelper;

public class HomeCategoryListAdapter extends CommonAdapter<Category> {

	private Context mContext;
	private ArrayList<Category> mdata;
	private ArrayList<CategoryItem> itemList;
	private LinearLayout parentLayout;
	private float scale;
	
	private ArrayList<Integer> mdotsList = new ArrayList<Integer>();
	private Boolean mIsTwo;
	
	public HomeCategoryListAdapter(Context context,
			List<Category> data, int layoutId) {
		super(context, data, layoutId);
		mContext = context;
		mdata =  (ArrayList<Category>) data;
		
		mdotsList.add(R.drawable.ic_blue_dot);
		mdotsList.add(R.drawable.ic_yellow_dot);
		mdotsList.add(R.drawable.ic_purple_dot);
		mdotsList.add(R.drawable.ic_orange_dot);
		mdotsList.add(R.drawable.ic_green_dot);
		mdotsList.add(R.drawable.ic_red_dot);
	}

	@Override
	public void convert(ViewHolder holder, final Category item, int position) {
		
		
		
		LinearLayout goodsParentLayout = holder.getView(R.id.floor_goods_parent_layout);
		LayoutParams goodsParentLayotParams = (LayoutParams) goodsParentLayout.getLayoutParams();
		goodsParentLayotParams.height = (int) (Constant.SCREEN_WIDTH*0.47);
		goodsParentLayout.setLayoutParams(goodsParentLayotParams);
		
		/*RelativeLayout categoryLayout = holder.getView(R.id.category_layout);
		LayoutParams categoryLayoutParams = (LayoutParams) categoryLayout.getLayoutParams();
		categoryLayoutParams.height = goodsParentLayotParams.height;
		categoryLayout.setLayoutParams(categoryLayoutParams);*/
		
		LinearLayout mCategoryFloorNameLayout = holder.getView(R.id.category_floor_name_layout);
		mCategoryFloorNameLayout.setOnClickListener(null);
		
		holder.setText(R.id.category_floor_name, String.format(mContext.getResources().getString(R.string.category_name), position+1,item.getmName()));
		TextView nameText = holder.getView(R.id.category_floor_name);
		if(position%2 == 0){
			nameText.setTextColor(mContext.getResources().getColor(R.color.blue_home_category_name_color));
		}else{
			nameText.setTextColor(mContext.getResources().getColor(R.color.orange_home_category_name_color));
		}
		
		ImageView CategoryImage = holder.getView(R.id.category_image);
		RelativeLayout.LayoutParams CategoryImageParams = (android.widget.RelativeLayout.LayoutParams) CategoryImage.getLayoutParams();
		CategoryImageParams.width = (int) (Constant.SCREEN_WIDTH * 0.33);
		CategoryImageParams.height = CategoryImageParams.width;
	    CategoryImageParams.width = android.widget.RelativeLayout.LayoutParams.MATCH_PARENT;
		CategoryImage.setLayoutParams(CategoryImageParams);
		
		holder.setText(R.id.category_name, item.getmCategoryShow().getmTargetName());
		holder.setText(R.id.category_describe, item.getmCategoryShow().getmDescription());
		holder.setImageResource(item.getmCategoryShow().getmImage(), R.id.category_image);
		RelativeLayout mCategoryLayout = holder.getGroup(R.id.category_layout);
		
		
		
		mCategoryLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				UIHelper.toSearchActivity(mContext, item.getmCategoryShow().getmTargetId());
			}
		});
		
		if(item.getmCommodityShow() != null && item.getmCommodityShow().size() >= 2){
			
			ImageView imageOne = holder.getView(R.id.floor_goods_image_one);
			RelativeLayout.LayoutParams imageOneParams = (android.widget.RelativeLayout.LayoutParams) imageOne.getLayoutParams();
			imageOneParams.width = (int) (goodsParentLayotParams.height * 0.5);
			imageOneParams.height = imageOneParams.width;
			imageOne.setLayoutParams(imageOneParams);
			
			holder.setText(R.id.floor_goods_name_one, item.getmCommodityShow().get(0).getmTargetName());
			holder.setText(R.id.floor_goods_describe_one, item.getmCommodityShow().get(0).getmDescription());
			holder.setImageResource(item.getmCommodityShow().get(0).getmImage(), R.id.floor_goods_image_one);
			RelativeLayout mFloorGoodsOneLayout = holder.getGroup(R.id.floor_goods_one_layout);
			mFloorGoodsOneLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
//					UIHelper.toProductInfoActivity(mContext, item.getmCommodityShow().get(0).getmTargetId(),ProductInfoFragment.FLAG_NORMAL);;
				}
			});
			
			ImageView imageTwo = holder.getView(R.id.floor_goods_image_two);
			RelativeLayout.LayoutParams imageTwoParams = (android.widget.RelativeLayout.LayoutParams) imageTwo.getLayoutParams();
			imageTwoParams.width = (int) (goodsParentLayotParams.height * 0.5);
			imageTwoParams.height = imageTwoParams.width;
			imageTwo.setLayoutParams(imageTwoParams);
			
			holder.setText(R.id.floor_goods_name_two, item.getmCommodityShow().get(1).getmTargetName());
			holder.setText(R.id.floor_goods_describe_two, item.getmCommodityShow().get(1).getmDescription());
			holder.setImageResource(item.getmCommodityShow().get(1).getmImage(), R.id.floor_goods_image_two);
			RelativeLayout mFloorGoodsTwoLayout = holder.getGroup(R.id.floor_goods_two_layout);
			mFloorGoodsTwoLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
//					UIHelper.toProductInfoActivity(mContext, item.getmCommodityShow().get(1).getmTargetId(),ProductInfoFragment.FLAG_NORMAL);;
				}
			});
		}
		
		parentLayout = holder.getGroup(R.id.category_parent_layout);
		/*LayoutParams parentLayotParams = (LayoutParams) parentLayout.getLayoutParams();
		parentLayotParams.height = (int) (Constant.SCREEN_WIDTH*0.29);
		parentLayout.setLayoutParams(parentLayotParams);
		parentLayout.removeAllViews();*/
		
		itemList = item.getmItems();
		int size = itemList.size();
		if(size <= 4){
			initCell(itemList);
			mIsTwo = false;
		}else{
			mIsTwo = true;
			List<CategoryItem> frontList =  itemList.subList(0, (size+1)/2);
			initCell(frontList);
			
			View view = new View(mContext);
			LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) 1);
			viewParams.leftMargin=15;
			viewParams.rightMargin=15;
			view.setLayoutParams(viewParams);
			view.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bg_dashed));
			parentLayout.addView(view);
			
			List<CategoryItem> backList =  itemList.subList((size+1)/2, size);
			initCell(backList);
		}
		
	}

	private void initCell(List<CategoryItem> item){
		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		scale = displayMetrics.scaledDensity;
		int size = item.size();
		
		
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.height = (int) (Constant.SCREEN_WIDTH*0.15);
		layoutParams.leftMargin=15;
		layoutParams.rightMargin = 15;
		layout.setLayoutParams(layoutParams);
		for(int i=0;i<size;i++){
			final int itemId = item.get(i).getmId();
			LinearLayout categoryCell = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.home_category_cell, layout, false);
			/*DrawableCenterTextView categoryCell = new DrawableCenterTextView(mContext);
			categoryCell.setSingleLine();
			categoryCell.setEllipsize(TruncateAt.END);
			categoryCell.setPadding(5, 30, 5, 30);
			categoryCell.setGravity(Gravity.CENTER);
			LayoutParams cellParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
			categoryCell.setLayoutParams(cellParams);
			Drawable drawable= mContext.getResources().getDrawable(R.drawable.ic_red_dot);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			categoryCell.setCompoundDrawables(drawable, null, null, null);
			categoryCell.setCompoundDrawablePadding(0);
			categoryCell.setTextSize(mContext.getResources().getDimension(R.dimen.app_text_size_second)/scale);
			categoryCell.setTextColor(mContext.getResources().getColor(R.color.TextColorBLACK_NORMAL));
			categoryCell.setText(item.get(i).getmName());
			categoryCell.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.head_back_response_area));*/
			LayoutParams cellParams = (LayoutParams) categoryCell.getLayoutParams();
			cellParams.width = 0;
			cellParams.weight = 1;
			categoryCell.setLayoutParams(cellParams);
			categoryCell.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					UIHelper.toSearchActivity(mContext, itemId);
				}
			});
			TextView categoryName = (TextView) categoryCell.findViewById(R.id.home_category_cell_name);
			categoryName.setText(item.get(i).getmName());
			
			ImageView categoryDot = (ImageView) categoryCell.findViewById(R.id.home_category_cell_dot);
			categoryDot.setImageResource(mdotsList.get((int) (Math.random()*6)));
			
			
			
			layout.addView(categoryCell);
			if(i != size-1){
				View view = new View(mContext);
				LayoutParams viewParams = new LayoutParams((int)1, LayoutParams.MATCH_PARENT);
				viewParams.setMargins(0, 15, 0, 15);
				view.setLayoutParams(viewParams);
				view.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bg_dashed));
				layout.addView(view);
			}
		}
		parentLayout.addView(layout);
	}

	

}
