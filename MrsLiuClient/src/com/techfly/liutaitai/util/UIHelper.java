package com.techfly.liutaitai.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.techfly.liutaitai.MainActivity;
import com.techfly.liutaitai.model.home.activities.SearchActivity;
import com.techfly.liutaitai.model.mall.activities.AfterSaleServiceActivity;
import com.techfly.liutaitai.model.mall.activities.GanxiActivity;
import com.techfly.liutaitai.model.mall.activities.OrderEvaActivity;
import com.techfly.liutaitai.model.mall.activities.OrderInfoActivity;
import com.techfly.liutaitai.model.mall.activities.PicAndTextDetailActivity;
import com.techfly.liutaitai.model.mall.activities.ProductInfoActivity;
import com.techfly.liutaitai.model.mall.activities.ServiceListActivity;
import com.techfly.liutaitai.model.mall.activities.ShengXianActivity;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.shopcar.activities.OrderPayFinishActivity;
import com.techfly.liutaitai.model.shopcar.activities.ShopCarActivity;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.scale.GalleryData;
import com.techfly.liutaitai.scale.ImageEntity;
import com.techfly.liutaitai.scale.ImageScaleActivity;

/**
 * 
 * @author cr 2014-12-18
 * 
 */
public class UIHelper {



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

	public static void toPicAndTextActivity(Context context, String desc,String id) {
		Intent intent = new Intent(context, PicAndTextDetailActivity.class);
		intent.putExtra(JsonKey.AdvertisementKey.GOODSID, desc);
		  intent.putExtra(IntentBundleKey.ID, id);
		context.startActivity(intent);
	}

	public static void toShopCarActivity(Context context, int type) {
		Intent intent = new Intent(context, ShopCarActivity.class);
		intent.putExtra(IntentBundleKey.TYPE, type);
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

	public static void toTakingOrderActivity(Context context, Product product,
			int type) {
		Intent intent = new Intent(context, TakingOrderActivity.class);
		intent.putExtra(IntentBundleKey.PRODUCT, product);
		intent.putExtra(IntentBundleKey.IS_FROM_HOME_CART, false);
		intent.putExtra(IntentBundleKey.TYPE, type);
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

	public static void toServiceListActivity(Fragment context, int type) {
		Intent intent = new Intent(context.getActivity(),
				ServiceListActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	public static void toGanxiListActivity(Fragment context, int type) {
		Intent intent = new Intent(context.getActivity(), GanxiActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	/**
	 * 到生鲜 鲜花页面
	 * 
	 * @param context
	 * @param type
	 */
	public static void toShengxianListActivity(Fragment context, int type) {
		Intent intent = new Intent(context.getActivity(),
				ShengXianActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	/**
	 * 普通的跳转页面，无需携带任何参数
	 * 
	 * @param fragment
	 * @param className
	 */
	public static void toClassActivity(Fragment fragment, String className) {
		Intent intent = null;
		try {
			intent = new Intent(fragment.getActivity(),
					Class.forName(className));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (intent != null) {
			fragment.startActivityForResult(intent, 0);
			;
		}

	}

	/**
	 * 普通的跳转页面，无需携带任何参数
	 * 
	 * @param fragment
	 * @param className
	 */
	public static void toSomeIdActivity(Fragment fragment, String className,
			String id, int type) {
		Intent intent = null;
		try {
			intent = new Intent(fragment.getActivity(),
					Class.forName(className));
			intent.putExtra(IntentBundleKey.ID, id);
			intent.putExtra(IntentBundleKey.TYPE, type);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (intent != null) {
			fragment.startActivityForResult(intent, 0);
			;
		}

	}

	public static void toOrderInfoActivity(Fragment context, String id) {
		Intent intent = new Intent(context.getActivity(),
				OrderInfoActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, id);
		context.startActivity(intent);
	}

	public static void toAfterActivity(Fragment context, String id) {
		Intent intent = new Intent(context.getActivity(),
				AfterSaleServiceActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, id);
		context.startActivity(intent);
	}

	public static void toOrderEvaActivity(Fragment context, MyOrder order) {
		Intent intent = new Intent(context.getActivity(),
				OrderEvaActivity.class);
		intent.putExtra(IntentBundleKey.ORDER, order);
		context.startActivity(intent);
	}
	/**
     * 查看大图
     */
    public static void showImage(Context context, int index, ArrayList<ImageEntity> listItems,boolean isDel) {
        if(listItems==null || listItems.size()<=0 ){
            return;
        }
        ArrayList<String> tempPath = new ArrayList<String>();
        ArrayList<GalleryData> tempRemark = new ArrayList<GalleryData>();
        for(ImageEntity entity : listItems){
            tempPath.add(entity.getImagePath());
            GalleryData mGData = new GalleryData(entity.getRemark(),"");
            tempRemark.add(mGData);
        }
        Intent intent = new Intent(context, ImageScaleActivity.class);
        intent.putStringArrayListExtra(IntentBundleKey.PICTURE_URL, tempPath);
        intent.putParcelableArrayListExtra(IntentBundleKey.PICTURE_TITLE, tempRemark);
        intent.putExtra(IntentBundleKey.PICTURE_INDEX, index);
        intent.putExtra(IntentBundleKey.PICTURE_DEL, isDel);
        intent.putParcelableArrayListExtra(IntentBundleKey.PICTURE_ENTITY, listItems);
        context.startActivity(intent);
    }
}
