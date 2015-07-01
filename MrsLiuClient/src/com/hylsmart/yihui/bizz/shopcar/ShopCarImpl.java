package com.hylsmart.yihui.bizz.shopcar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.hylsmart.yihui.dao.DbAdapter;
import com.hylsmart.yihui.dao.Persistence;
import com.hylsmart.yihui.dao.ShopCarDbAdapter;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.util.AppLog;

public class ShopCarImpl implements IShopCar {
	private List<Product> mProductLists;
	/**
	 * 添加菜品 到购物车
	 */
	@Override
	public boolean addProduct(Product product) {
		if (null == mProductLists) {
			mProductLists = new ArrayList<Product>();
		}
		if (mProductLists.size() == 0 || !mProductLists.contains(product)) {
			product.setmAmount(product.getmAmount());
			mProductLists.add(product);
			return true;
		}
		return false;
	}
	/**
     * 从数据库中获取购物车中商品列表 ，应用初始话 时调用
     */
    public void getDbShopProList(Context context){
        DbAdapter adapter = new ShopCarDbAdapter(context);
        for(Persistence persistence :adapter.getDataList()){
            Product product = (Product) persistence; 
            if (null == mProductLists) {
                mProductLists = new ArrayList<Product>();
            }
            mProductLists.add(product);
         }
    }
	public List<Product> getmproductLists() {
		return mProductLists;
	}

	public void setmproductLists(List<Product> mproductLists) {
		this.mProductLists = mproductLists;
	}
	

	@Override
	public void cancelproduct(String productId) {
		if (null != mProductLists) {
			Product product = new Product();
			product.setmId(productId);
			int index = mProductLists.indexOf(product);
			if (index != -1) {
				mProductLists.remove(index);
			}
		}
	}

	@Override
	public void emptyproductes() {
		if (null != mProductLists) {
			mProductLists.clear();
			mProductLists = null;
		}
	
	}

	@Override
	public int updateproduct(boolean isFromShopCar, Product product, boolean isAddNum) {
		AppLog.Logd("Fly", "updateproduct=====");
		if (null == mProductLists) {
			return 0;
		}
		int amount = product.getmAmount();
		int index = mProductLists.indexOf(product);
		if (index != -1) {
			product = mProductLists.get(index);
		}
		if (isFromShopCar) {
			if (isAddNum) {
				product.setmAmount(product.getmAmount() + 1);
			} else {
				product.setmAmount(product.getmAmount() - 1);
				if (product.getmAmount() <= 0) {
					cancelproduct(product.getmId());
					return 0;
				}
			}
		} else {
			product.setmAmount(product.getmAmount() + amount);
		}

		mProductLists.set(index, product);
		return product.getmAmount();
	}

	@Override
	public float getTotalPrice() {
		float totalPrice = 0;
		if (null == mProductLists || mProductLists.size() == 0) {
			totalPrice = 0;
		} else {
			for (Product product : mProductLists) {
				if (product.ismIsCheck()) {
					totalPrice = (float) (totalPrice + product.getmAmount() * product.getmPrice());
				}
			}
		}
		long l1 = Math.round(totalPrice * 100); // 四舍五入
		totalPrice = (float) (l1 / 100.00); // 注意：使用 100.0 而不是 100
		return totalPrice;
	}

	@Override
	public List<Product> getShopproductList() {
		return mProductLists;
	}

	@Override
	public int getCheckedProductAmountSum() {
		int sum = 0;
		if (null == mProductLists || mProductLists.size() == 0) {
			sum = 0;
		} else {
			for (Product product : mProductLists) {
				if (product.ismIsCheck()) {
					sum = sum + product.getmAmount();
				}
			}
		}
		return sum;
	}

	@Override
	public int getProductAmountSum() {
		int sum = 0;
		if (null == mProductLists || mProductLists.size() == 0) {
			sum = 0;
		} else {
			for (Product product : mProductLists) {
				sum = sum + product.getmAmount();
			}
		}
		return sum;
	}

	

	@Override
	public boolean setProductChecked(String productId, boolean isChecked) {
		if (null == mProductLists || mProductLists.size() == 0) {
			return false;
		}
		for (Product product : mProductLists) {
			if (productId != null && product.getmId() != null && product.getmId().equals(productId)) {
				product.setmIsCheck(isChecked);
				return true;
			}
		}
		return false;
	}

    @Override
    public void emptyproductes(Context context) {
        // TODO Auto-generated method stub
        DbAdapter adapter = new ShopCarDbAdapter(context);
//        List<Persistence> list =adapter.getDataList();
        adapter.deleteData();
//        if(null!=list&&list.size()>0){
//            for(Persistence p:list){
//                Product pro = (Product) p;
//                adapter.deleteData(pro.getmId());
//           }
//        }
       if (null != mProductLists) {
          
           mProductLists.clear();
           mProductLists = null;
       }
     
    }

	

}
