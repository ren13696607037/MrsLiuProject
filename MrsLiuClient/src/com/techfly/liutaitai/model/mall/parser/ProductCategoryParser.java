package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Category;
import com.techfly.liutaitai.model.mall.bean.SubCategory;
import com.techfly.liutaitai.model.mall.bean.SubCategoryBanner;
import com.techfly.liutaitai.model.mall.bean.help.ListCatgory;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class ProductCategoryParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ListCatgory lc = new ListCatgory();
		ArrayList<Category> mArrayList = new ArrayList<Category>();
		if(object != null){
			int resultCode = -1;
			try {
				resultCode = object.getInt(JsonKey.CODE);
			} catch (JSONException e) {
				AppLog.Logi("Shi", "ERROR:parse Category with code");
			}
			if(resultCode == 0){
				JSONArray jsonArray = object.optJSONArray(JsonKey.DATA);
				if(jsonArray!= null){
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.optJSONObject(i);
						if(jsonObject != null){
							Category c = new Category();
							c.setmCategoryId(jsonObject.optInt(JsonKey.ProductCategory.ID));
							c.setmCategoryName(jsonObject.optString(JsonKey.ProductCategory.NAME));
							c.setmSubCategoryList(parseSubList(jsonObject));
							c.setmSubCategoryBannerList(parseBannerList(jsonObject));
							mArrayList.add(c);
						}
					}
				}
			}
		}
		lc.setmArrayList(mArrayList);
		return lc;
	}

	private ArrayList<SubCategoryBanner> parseBannerList(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		ArrayList<SubCategoryBanner> list = new ArrayList<SubCategoryBanner>();
		JSONArray mJsonArray = jsonObject.optJSONArray(JsonKey.ProductCategory.BANNER);
		if(mJsonArray != null){
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJsonObject = mJsonArray.optJSONObject(i);
				if(mJsonObject != null){
					SubCategoryBanner scb = new SubCategoryBanner();
					scb.setmId(mJsonObject.optInt(JsonKey.CategorySubBanner.ID));
					scb.setmTargetId(mJsonObject.optInt(JsonKey.CategorySubBanner.TARGET_ID));
					scb.setmTargetType(mJsonObject.optInt(JsonKey.CategorySubBanner.TARGET_TYPE));
					scb.setmContent(mJsonObject.optString(JsonKey.CategorySubBanner.CONTENT));
					scb.setmImageUrl(mJsonObject.optString(JsonKey.CategorySubBanner.IMAGE_URL));
					list.add(scb);
				}
			}
			
		}
		return list;
	}

	private ArrayList<SubCategory> parseSubList(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		ArrayList<SubCategory> list = new ArrayList<SubCategory>();
		JSONArray mJsonArray = jsonObject.optJSONArray(JsonKey.ProductCategory.SUB_CATEGORY);
		if(mJsonArray != null){
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJsonObject = mJsonArray.optJSONObject(i);
				if(mJsonObject != null){
					SubCategory sc = new SubCategory();
					sc.setmId(mJsonObject.optString(JsonKey.ProductCategory.ID));
					sc.setmName(mJsonObject.optString(JsonKey.ProductCategory.NAME));
					sc.setmImgUrl(mJsonObject.optString(JsonKey.ProductCategory.IMG));
					list.add(sc);
				}
			}
		}
		return list;
	}

	@Override
	public Object fromJson(String json) {
		JSONObject object;
		try {
			object = new JSONObject(json);
			return fromJson(object);
		} catch (JSONException e) {
			AppLog.Logd("Fly", "JSONException" + e.getMessage());
		}
		return null;
	}

}
