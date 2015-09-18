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

public class NewProductInfoParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
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
					p.setmName(jsonObject.optString("title"));
					p.setmDesc(jsonObject.optString("description"));
					p.setmContent(jsonObject.optString("text"));
					p.setmId(jsonObject.optString("id"));
					p.setmPrice((float) jsonObject.optDouble("price"));
					p.setmImg(Constant.IMG_URL+jsonObject.optString("img"));
					p.setmImgArray(parseImages(jsonObject));
					p.setmCommentsList(parseComments(jsonObject));
					
				}
			}
		}

		return p;
	}

	

	



	
	private ArrayList<Comments> parseComments(JSONObject jsonObject) {
		ArrayList<Comments> list = new ArrayList<Comments>();
		JSONArray jsonArray = jsonObject
				.optJSONArray("reviews");
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject mJsonObject = jsonArray.optJSONObject(i);
				if (mJsonObject != null) {
					Comments c = new Comments();
					c.setmId(mJsonObject.optInt("id"));
					c.setmContent(mJsonObject
							.optString("content"));
					c.setmName(mJsonObject
                            .optString("mobile"));
					c.setmPhone(mJsonObject
                            .optString("mobile"));
//					c.setmReplay(mJsonObject
//							.optString(JsonKey.CommentsKey.REPLAY));
					c.setmStarScore(mJsonObject
							.optString("stars"));
					c.setmTime(mJsonObject
                            .optString("time"));
					c.setmTitle(mJsonObject
                            .optString("content"));
//					c.setmVerfy(mJsonObject.optInt(JsonKey.CommentsKey.VERIFY));
					list.add(c);
				}
			}
		}
		return list;
	}

	private List<String> parseImages(JSONObject jsonObject) {
		String str = jsonObject
				.optString("imgs");
		JSONObject obj =null;
		ArrayList<String> list = new ArrayList<String>();
        try {
            obj = new JSONObject(str);
        } catch (JSONException e) {
        
        }
        if(obj==null){
            return list;
        }
	    JSONArray mJsonArray = obj.optJSONArray("imgList");
		if (mJsonArray != null) {
			for (int i = 0; i < mJsonArray.length(); i++) {
				String mJsonObject = mJsonArray.optString(i);
				if (mJsonObject != null) {
					list.add(Constant.IMG_URL+mJsonObject);
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
