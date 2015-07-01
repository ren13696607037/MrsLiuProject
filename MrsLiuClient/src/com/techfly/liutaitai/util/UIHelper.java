package com.techfly.liutaitai.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.techfly.liutaitai.model.home.activities.SearchActivity;
import com.techfly.liutaitai.model.mall.activities.CategoryInfoListActivity;
import com.techfly.liutaitai.model.mall.activities.PicAndTextDetailActivity;
import com.techfly.liutaitai.model.mall.activities.ProductInfoActivity;
import com.techfly.liutaitai.model.mall.activities.TuanGouActivity;
import com.techfly.liutaitai.model.mall.bean.Category;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
import com.techfly.liutaitai.model.shopcar.activities.OrderPayFinishActivity;
import com.techfly.liutaitai.model.shopcar.activities.ShopCarActivity;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.MainActivity;

/**
 * 
 * @author cr 2014-12-18
 * 
 */
public class UIHelper {

	/**
	 * ��ת����������
	 * 
	 * @param context
	 * @param title
	 *            ��������
	 * @param id
	 *            ����id
	 */
	public static void showCategorySecond(Context context, Category mCategory) {
		Intent intent = new Intent(context, CategoryInfoListActivity.class)
				.putExtra(IntentBundleKey.CATEGORY, mCategory);
		context.startActivity(intent);
	}

	public static void toSearchActivity(Context context, int fid) {
		Intent intent = new Intent(context, SearchActivity.class);
		intent.putExtra(JsonKey.ProductGroupBuyKey.ID, fid);
		context.startActivity(intent);
	}

	public static void toProductInfoActivity(Context context, int goodsId,
			int reqFlag) {
		Intent intent = new Intent(context, ProductInfoActivity.class);
		intent.putExtra(JsonKey.AdvertisementKey.GOODSID, goodsId);
		intent.putExtra(IntentBundleKey.PRODUCT_REQ_FLAG, reqFlag);
		context.startActivity(intent);
	}

	public static void toPicAndTextActivity(Context context, int goodsId) {
		Intent intent = new Intent(context, PicAndTextDetailActivity.class);
		intent.putExtra(JsonKey.AdvertisementKey.GOODSID, goodsId);
		context.startActivity(intent);
	}

	public static void toShopCarActivity(Context context) {
		Intent intent = new Intent(context, ShopCarActivity.class);
		intent.putExtra(IntentBundleKey.IS_FROM_HOME_CART, false);
		context.startActivity(intent);
	}

	public static void toOrderDetailActivity(Fragment context, String orderNo) {
		Intent intent = new Intent(context.getActivity(),
				OrderDetailActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, orderNo);
		context.startActivity(intent);

	}

	public static void toOrdeFinishActivity(Fragment context, String orderNo,
			boolean isFromOrderDetail) {
		Intent intent = new Intent(context.getActivity(),
				OrderPayFinishActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, orderNo);
		intent.putExtra(IntentBundleKey.ORDER_DETAIL, isFromOrderDetail);
		context.startActivity(intent);
	}

	public static void toTakingOrderActivity(Context context, Product product) {
		Intent intent = new Intent(context, TakingOrderActivity.class);
		intent.putExtra(IntentBundleKey.PRODUCT, product);
		intent.putExtra(IntentBundleKey.IS_FROM_HOME_CART, false);
		context.startActivity(intent);
	}
	

	public static void toTuangouInfoActivity(Context context) {
		Intent intent = new Intent(context, TuanGouActivity.class);
		context.startActivity(intent);
	}

	public static void toLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}

	public static void toHomeActivity(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	public static void toAddressAddActivity(Fragment context) {
		Intent intent = new Intent(context.getActivity(),
				ChangeAddressActivity.class);
		intent.putExtra(IntentBundleKey.ADDRESS_EXTRA,
				Constant.ORDER_ADDRESS_INTENT);
		context.startActivityForResult(intent, Constant.ORDER_ADDRESS_INTENT);
	}

	public static void toAddressManageActivity(Fragment context) {
		Intent intent = new Intent(context.getActivity(),
				AddressManageActivity.class);
		intent.putExtra(IntentBundleKey.ADDRESS_EXTRA,
				Constant.ORDER_CITY_INTENT);
		context.startActivityForResult(intent, Constant.ORDER_CITY_INTENT);
	}

}