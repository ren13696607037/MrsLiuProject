package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Category;
import com.techfly.liutaitai.model.mall.bean.SubCategory;
import com.techfly.liutaitai.model.mall.bean.SubCategoryBanner;
import com.techfly.liutaitai.model.mall.bean.help.ListOrder;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class OrderBasketParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ListOrder lc = new ListOrder();
		ArrayList<MyOrder> mArrayList = new ArrayList<MyOrder>();
		if (object != null) {
			int resultCode = -1;
			try {
				resultCode = object.getInt(JsonKey.CODE);
			} catch (JSONException e) {
				AppLog.Logd("Shi", "ERROR:parse order list with code");
			}
			if (resultCode == 0) {
				JSONObject json = object.optJSONObject(JsonKey.DATA);
				if (json != null) {
					JSONArray mJsonArray = json.optJSONArray(JsonKey.DATAS);
					if (mJsonArray != null) {
						for (int i = 0; i < mJsonArray.length(); i++) {
							JSONObject mJsonObject = mJsonArray
									.optJSONObject(i);
							if (mJsonObject != null) {
								MyOrder order = new MyOrder();

								order.setmNum(mJsonObject
										.optString(JsonKey.OrderBasketKey.ORDER_NUM));

								mArrayList.add(order);
							}
						}
					}
				}

			}
		}
		lc.setmArrayList(mArrayList);
		return lc;
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
