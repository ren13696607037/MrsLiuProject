package com.techfly.liutaitai.bizz.shopcar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.techfly.liutaitai.model.mall.bean.Product;

public class ShopCar {
    private static ShopCar mShopCar;
    private static Object object = new Object();
    private ShopCarImpl mShopCarImpl = new ShopCarImpl();
   
    private ShopCar(){
        
    }
    public static ShopCar getShopCar(){
        if(null == mShopCar){
            synchronized (object) {
             if(null==mShopCar){
                 mShopCar = new ShopCar();
             }   
            }
        }
        return mShopCar;
    }
    public void setShopproductList(List<Product> productList){
        mShopCarImpl.setmproductLists(productList);
    }
 
    /**
     * 清空 购物车
     */
    public void emptyproductes(){
//        mShopCarImpl.emptyproductes();
    }
    public void  getDbShopProList(Context context){
        mShopCarImpl.getDbShopProList(context);
    }
    /**
     * 清空 购物车
     */
    public void emptyproductes(Context context){
        mShopCarImpl.emptyproductes(context);
    }
    /**
     * 更新产品
     * @param product  购买的商品
     * @param isAddNum true 代表增加商品的数量， false 代表减少商品的数量
     * @param isFromShopCar false 代表从商品购买页面加入购物车，true 代表在购物车页面 改变购买商品的数量或者移除
     */
    public void updateproduct(boolean isFromShopCar,Product product,boolean isAddNum){
        boolean isAddSuccess  = mShopCarImpl.addProduct(product);
        if(isAddSuccess){
            OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(new Bundle());
            return;
        }else{
            mShopCarImpl.updateproduct(isFromShopCar,product, isAddNum);
            OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(new Bundle());
        }
    }
    
    /**
     * 购物车中的商品被选中
     * @param product
     */
    public void setProductCheck(Product product){
    	List<Product> productes = mShopCarImpl.getmproductLists();
    	if(productes.contains(product)){
    		product = productes.get(productes.indexOf(product));
    		product.setmIsCheck(!product.ismIsCheck());
    		mShopCarImpl.getmproductLists().set(productes.indexOf(product), product);
    		}
        OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(new Bundle());
    }
   
   
    /**
     * 得到总价格
     * @return
     */
    public float getTotalPrice(){
        return mShopCarImpl.getTotalPrice();
    }

   
    /**
     * 得到购物菜品列表
     * @return
     */
    public List<Product> getShopproductList(){
        return         mShopCarImpl.getShopproductList();
    }
  
    /**
     * 得到购物菜品列表
     * @return
     */
    public ArrayList<Product> getCheckShopproductList(){
    	ArrayList<Product> list = new ArrayList<Product>();
    	if(getShopproductList()!=null){
    	    for(Product product: mShopCarImpl.getShopproductList()){
                if(product.ismIsCheck()){
                    list.add(product);
                }
            }
    	}
    	   return   list      ;
    }
    
    /**
     * 得到总价格
     * @return
     */
    public float getDTotalPrice(int price){
        return mShopCarImpl.getTotalPrice()-price;
    }
    /**
     *  得到购物车中选中食品的份数
     * @param isAddproduct 是否加菜的标示
     * @return
     */
    public int getShopCheckedAmountSum(){
        return mShopCarImpl.getCheckedProductAmountSum();
    }
    /**
     *  得到购物车中食品的份数
     * @param isAddproduct 是否加菜的标示
     * @return
     */
    public int getShopAmountSum(){
        return mShopCarImpl.getProductAmountSum();
    }
  
    /**
     * 删除购物车中被选中的条目
     * @return
     */
    public boolean delCartCheckedItem(){
    	List<Product> productList = new ArrayList<Product>();
    	for(Product pro:getShopproductList() ){
    		if(pro.ismIsCheck()){
    			productList.add(pro);
    		}
    	}
    	getShopproductList().removeAll(productList);
    	OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(null);
    	return true;
    }
    /**
     * get check sum 
     * @return
     */
    public int getCheckSum(){
    	 int amount=0;
    	for(Product pro:getShopproductList() ){
    		if(pro.ismIsCheck()){
    			amount =amount +pro.getmAmount();
    		}
    			
    }
    	return amount;
    }
   
   
}
