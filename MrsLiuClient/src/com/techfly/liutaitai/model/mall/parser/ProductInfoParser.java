package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Comments;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class ProductInfoParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		// TODO Auto-generated method stub
		Product p = new Product();
		if (object != null) {
			int resultCode = -1;
			try {
				resultCode = object.getInt(JsonKey.CODE);
			} catch (JSONException e) {
				AppLog.Logi("Shi", "ERROR:parse ProductInfo with code");
			}
			if (resultCode == 0) {
				JSONObject jsonObject = object.optJSONObject(JsonKey.DATA);
				if (jsonObject != null) {
					p.setmId(String.valueOf(jsonObject
							.optInt(JsonKey.ProductInfo.ID)));
					p.setmName(jsonObject.optString(JsonKey.ProductInfo.NAME));
					p.setmPrice((float) jsonObject
							.optDouble(JsonKey.ProductInfo.PRICE));
					p.setmMarketPrice((float) jsonObject
							.optDouble(JsonKey.ProductInfo.MARKET_PRICE));
					p.setmIsCollect(jsonObject
							.optBoolean(JsonKey.ProductInfo.COLLECT));
					p.setmImg(jsonObject.optString(JsonKey.ProductInfo.ICON));
					p.setmStoreCount(jsonObject
							.optInt(JsonKey.ProductInfo.STORE_COUNT));
					p.setmRebate(jsonObject
							.optDouble(JsonKey.ProductInfo.REBATE));
					p.setmImgArray(parseImages(jsonObject));
					JSONObject comment = jsonObject
							.optJSONObject(JsonKey.ProductInfo.COMMENT);
					if (comment != null) {
						p.setmCommentCount(comment
								.optInt(JsonKey.ProductInfo.COMMENT_COUNT));
						p.setmCommentReputably(comment
								.optString(JsonKey.ProductInfo.COMMENT_PERCENT));
						p.setmCommentRating(comment.optInt(JsonKey.ProductInfo.COMMENT_STAR));
					}

					p.setmCommentsList(parseComments(comment));
				
				}
			}
		}

		return p;
	}
	
	

	

	private ArrayList<String> parseNum(JSONObject standard) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		JSONArray num = standard.optJSONArray(JsonKey.ProductInfo.STANDARD_NUM);
		if(num != null){
			for (int i = 0; i < num.length(); i++) {
				String string = num.optString(i);
				list.add(string);
			}
		}
		return list;
	}

	private ArrayList<Comments> parseComments(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		ArrayList<Comments> list = new ArrayList<Comments>();
		JSONArray jsonArray = jsonObject
				.optJSONArray(JsonKey.ProductInfo.COMMENT_COMMENTS);
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject mJsonObject = jsonArray.optJSONObject(i);
				if (mJsonObject != null) {
					Comments c = new Comments();
					c.setmId(mJsonObject.optInt(JsonKey.CommentsKey.ID));
					c.setmContent(mJsonObject
							.optString(JsonKey.CommentsKey.CONTENT));
					c.setmName(mJsonObject.optString(JsonKey.CommentsKey.NAME));
					c.setmPhone(mJsonObject
							.optString(JsonKey.CommentsKey.PHONE));
					c.setmReplay(mJsonObject
							.optString(JsonKey.CommentsKey.REPLAY));
					c.setmStarScore(mJsonObject
							.optString(JsonKey.CommentsKey.STAR_SCORE));
					c.setmTime(mJsonObject.optString(JsonKey.CommentsKey.TIME));
					c.setmTitle(mJsonObject
							.optString(JsonKey.CommentsKey.TITLE));
					c.setmVerfy(mJsonObject.optInt(JsonKey.CommentsKey.VERIFY));
					list.add(c);
				}
			}
		}
		return list;
	}

	private List<String> parseImages(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		JSONArray mJsonArray = jsonObject
				.optJSONArray(JsonKey.ProductInfo.IMAGES);
		ArrayList<String> list = new ArrayList<String>();
		if (mJsonArray != null) {
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mJsonObject = mJsonArray.optJSONObject(i);
				if (mJsonObject != null) {
					list.add(Constant.IMG_URL+mJsonObject
							.optString(JsonKey.ProductInfo.IMAGES_PATH));
				}
			}
		}
		return list;
	}

	@Override
	public Object fromJson(String json) {
		// TODO Auto-generated method stub
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
