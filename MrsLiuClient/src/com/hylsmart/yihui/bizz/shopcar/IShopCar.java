package com.hylsmart.yihui.bizz.shopcar;

import java.util.List;

import android.content.Context;

import com.hylsmart.yihui.model.mall.bean.Product;

public interface IShopCar {
	/**
	 * 往购物车中添加商品
	 * @param product
	 * @return
	 */
    boolean addProduct(Product product);
    /**
     * 把商品从购物车中移除
     * @param productId
     */
    void cancelproduct(String productId);
    /**
     * 清空购物车
     */
    void emptyproductes();
    /**
     * 清空购物车
     */
    void emptyproductes(Context context);
    /**
     * 更新购物车中此商品的购买数量
     * @param productId
     * @param isAddNum
     */
    int updateproduct(boolean isFromShopCar,Product product,boolean isAddNum);
    /**
     * 购物车中选中结算商品的总价
     * @return
     */
    float getTotalPrice();
    /**
     * 得到购物车商品列表
     * @return
     */
    List<Product> getShopproductList();
    /**
     * 购物车中所有选中商品的数量
     * @return
     */
    int getCheckedProductAmountSum();
    /**
     * 购物车中所有商品的数量
     * @return
     */
    int getProductAmountSum();
    /**
     * 设置商品是否选中
     * @param productId
     * @param isChecked
     * @return
     */
    boolean setProductChecked(String productId,boolean isChecked);
 
    	
    
}
