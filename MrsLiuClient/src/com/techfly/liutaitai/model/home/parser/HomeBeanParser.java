package com.techfly.liutaitai.model.home.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.home.bean.Advertisement;
import com.techfly.liutaitai.model.home.bean.Banner;
import com.techfly.liutaitai.model.home.bean.Category;
import com.techfly.liutaitai.model.home.bean.CategoryItem;
import com.techfly.liutaitai.model.home.bean.CategoryShow;
import com.techfly.liutaitai.model.home.bean.HomeBean;
import com.techfly.liutaitai.model.home.bean.HotGood;
import com.techfly.liutaitai.model.home.bean.ProductGroupBuy;
import com.techfly.liutaitai.model.home.bean.SecKill;
import com.techfly.liutaitai.model.home.bean.SecKillItem;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class HomeBeanParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		HomeBean homeBean = new HomeBean();
		if(object.optInt(JsonKey.CODE)== 0 && object.optJSONObject(JsonKey.DATA) != null){
			JSONObject data = object.optJSONObject(JsonKey.DATA);
			
			homeBean.setmQQ(data.optString(JsonKey.HomeKey.QQ));
			homeBean.setmWeixin(data.optString(JsonKey.HomeKey.WEIXIN));
			homeBean.setmShareContent(data.optString(JsonKey.HomeKey.SHARE_CONTENT));
			homeBean.setmShareUrl(data.optString(JsonKey.HomeKey.SHARE_URL));
			//解析首页banner条
			JSONArray bannerArray = data.optJSONArray(JsonKey.HomeKey.COMMODITYBANNER);
			ArrayList<Banner> bannerList = new ArrayList<Banner>();
			if(bannerArray != null && bannerArray.length() != 0){
				for(int i=0;i<bannerArray.length();i++){
					JSONObject tempBanner = bannerArray.optJSONObject(i);
					Banner banner = new Banner();
					banner.setmId(tempBanner.optInt(JsonKey.BannerKey.ID));
					banner.setmTargetId(tempBanner.optInt(JsonKey.BannerKey.TARGETID));
					banner.setmTargetType(tempBanner.optInt(JsonKey.BannerKey.TARGETTYPE));
					banner.setmImage(tempBanner.optString(JsonKey.BannerKey.IMAGE));
					banner.setmContent(tempBanner.optString(JsonKey.BannerKey.CONTENT));
					
					bannerList.add(banner);
				}
			}
			homeBean.setmCommodityBanner(bannerList);
			
			//解析首页广告
			JSONObject adObj = data.optJSONObject(JsonKey.HomeKey.ADVERTISING);
			Advertisement ad = new Advertisement();
			ad.setmId(adObj.optInt(JsonKey.AdvertisementKey.ID));
			ad.setmImage(adObj.optString(JsonKey.AdvertisementKey.IMAGE));
			ad.setmContent(adObj.optString(JsonKey.AdvertisementKey.CONTENT));
			ad.setmGoodsId(adObj.optInt(JsonKey.AdvertisementKey.GOODSID));
			homeBean.setmAdvertising(ad);
			
			//解析团购商品列表
			JSONArray tuanGouArray = data.optJSONArray(JsonKey.HomeKey.TUANGOUCARD);
			ArrayList<ProductGroupBuy> tuanGouList = new ArrayList<ProductGroupBuy>();
			if(tuanGouArray != null && tuanGouArray.length() != 0){
				for(int i=0;i<tuanGouArray.length();i++){
					JSONObject tempProduct = tuanGouArray.optJSONObject(i);
					ProductGroupBuy product = new ProductGroupBuy();
					product.setmId(tempProduct.optInt(JsonKey.ProductGroupBuyKey.ID));
					product.setmCommodityId(tempProduct.optInt(JsonKey.ProductGroupBuyKey.COMMODITYID));
					product.setmCommodityName(tempProduct.optString(JsonKey.ProductGroupBuyKey.COMMODITYNAME));
					product.setmPrice(tempProduct.optDouble(JsonKey.ProductGroupBuyKey.PRICE));
					product.setmOldPrice(tempProduct.optDouble(JsonKey.ProductGroupBuyKey.OLDPRICE));
					product.setmSaleNum(tempProduct.optInt(JsonKey.ProductGroupBuyKey.SALENUM));
					product.setmImage(tempProduct.optString(JsonKey.ProductGroupBuyKey.IMAGE));
					
					tuanGouList.add(product);
				}
			}
			homeBean.setmTuanGouCard(tuanGouList);
			
			//解析首页分类
			JSONArray categoryArray = data.optJSONArray(JsonKey.HomeKey.COMMODITYCARD);
			ArrayList<Category> categoryList = new ArrayList<Category>();
			if(categoryArray != null && categoryArray.length() != 0){
				for(int i=0;i<categoryArray.length();i++){
					JSONObject tempCategory = categoryArray.optJSONObject(i);
					Category category = new Category();
					category.setmName(tempCategory.optString(JsonKey.CategoryKey.NAME));
					JSONArray itemArray = tempCategory.optJSONArray(JsonKey.CategoryKey.ITEMS);
					ArrayList<CategoryItem> itemList = new ArrayList<CategoryItem>();
					if(itemArray != null && itemArray.length() != 0){
						for(int j=0;j<itemArray.length();j++){
							JSONObject tempItem = itemArray.optJSONObject(j);
							CategoryItem item = new CategoryItem();
							item.setmId(tempItem.optInt(JsonKey.CategoryItemKey.ID));
							item.setmName(tempItem.optString(JsonKey.CategoryItemKey.Name));
							itemList.add(item);
						}
					}
					category.setmItems(itemList);
					
					CategoryShow categoryShow = new CategoryShow();
					JSONObject categoryShowJson = tempCategory.optJSONObject(JsonKey.CategoryKey.CATEGORYSHOW);
					if(categoryShowJson != null){
						categoryShow.setmImage(categoryShowJson.optString(JsonKey.CategoryShowKey.IMAGE));
						categoryShow.setmDescription(categoryShowJson.optString(JsonKey.CategoryShowKey.DESCRIPTION));
						categoryShow.setmTargetId(categoryShowJson.optInt(JsonKey.CategoryShowKey.TARGETID));
						categoryShow.setmTargetName(categoryShowJson.optString(JsonKey.CategoryShowKey.TARGETNAME));
					}
					category.setmCategoryShow(categoryShow);
					
					ArrayList<CategoryShow> commodityShow = new ArrayList<CategoryShow>();
					JSONArray showArray = tempCategory.optJSONArray(JsonKey.CategoryKey.COMMODITYSHOW);
					if(showArray.length() != 0){
						for(int j=0;j<showArray.length();j++){
							CategoryShow commodityShowItem = new CategoryShow();
							JSONObject tempCommodityShowItem = showArray.optJSONObject(j);
							commodityShowItem.setmImage(tempCommodityShowItem.optString(JsonKey.CategoryShowKey.IMAGE));
							commodityShowItem.setmDescription(tempCommodityShowItem.optString(JsonKey.CategoryShowKey.DESCRIPTION));
							commodityShowItem.setmTargetId(tempCommodityShowItem.optInt(JsonKey.CategoryShowKey.TARGETID));
							commodityShowItem.setmTargetName(tempCommodityShowItem.optString(JsonKey.CategoryShowKey.TARGETNAME));
							
							commodityShow.add(commodityShowItem);
						}
					}
					category.setmCommodityShow(commodityShow);
					
					categoryList.add(category);
				}
			}
			homeBean.setmCommodityCard(categoryList);
			
			//解析热卖商品
			ArrayList<HotGood> commodityHot = new ArrayList<HotGood>();
			JSONArray hotArray = data.optJSONArray(JsonKey.HomeKey.COMMODITYHOT);
			if(hotArray.length() != 0){
				for(int m=0;m<hotArray.length();m++){
					HotGood hotGood = new HotGood();
					JSONObject hotJSON = hotArray.optJSONObject(m);
					hotGood.setmId(hotJSON.optInt(JsonKey.HotGoodKey.ID));
					hotGood.setmCommodityId(hotJSON.optInt(JsonKey.HotGoodKey.COMMODITYID));
					hotGood.setmCommodityName(hotJSON.optString(JsonKey.HotGoodKey.COMMODITYNAME));
					hotGood.setmDescription(hotJSON.optString(JsonKey.HotGoodKey.DESCRIPTION));
					hotGood.setmImage(hotJSON.optString(JsonKey.HotGoodKey.IMAGE));
					hotGood.setmMarketPrice(hotJSON.optDouble(JsonKey.HotGoodKey.MARKETPRICE));
					hotGood.setmPrice(hotJSON.optDouble(JsonKey.HotGoodKey.PRICE));
					
					commodityHot.add(hotGood);
				}
			}
			homeBean.setmCommodityHot(commodityHot);
			
			SecKill secKill = new SecKill();
			JSONObject secKillJson = data.optJSONObject(JsonKey.HomeKey.SECKILLRESPONSE);
			secKill.setmStartTime(secKillJson.optString(JsonKey.SecKillKey.STARTTIME));
			secKill.setmStatus(secKillJson.optInt(JsonKey.SecKillKey.STATUS));
			ArrayList<SecKillItem> secKillList = new ArrayList<SecKillItem>();
			JSONArray secKillArray = secKillJson.optJSONArray(JsonKey.SecKillKey.ITEMS);
			if(secKillArray.length() != 0){
				for(int n=0;n<secKillArray.length();n++){
					SecKillItem item = new SecKillItem();
					JSONObject itemJson = secKillArray.optJSONObject(n);
					item.setmId(itemJson.optInt(JsonKey.SecKillItemKey.ID));
					item.setmCommodityId(itemJson.optInt(JsonKey.SecKillItemKey.COMMODITYID));
					item.setmName(itemJson.optString(JsonKey.SecKillItemKey.NAME));
					item.setmImage(itemJson.optString(JsonKey.SecKillItemKey.IMAGE));
					item.setmOldPrice(itemJson.optDouble(JsonKey.SecKillItemKey.OLDPRICE));
					item.setmPrice(itemJson.optDouble(JsonKey.SecKillItemKey.PRICE));
					item.setmEnabled(itemJson.optBoolean(JsonKey.SecKillItemKey.ENABLED));
					
					secKillList.add(item);
				}
			}
			secKill.setmItems(secKillList);
			homeBean.setmSecKill(secKill);
			
			return homeBean;
		}
		return null;
	}

	@Override
	public Object fromJson(String json) {
		JSONObject object;
		try {
			object = new JSONObject(json);
			return fromJson(object);
		} catch (JSONException e) {
			e.printStackTrace();
			AppLog.Logd("Fly", "JSONException" + e.getMessage());
		}
		return null;
	}

}
