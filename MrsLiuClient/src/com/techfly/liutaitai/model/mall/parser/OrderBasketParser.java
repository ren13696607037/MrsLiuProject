package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Product;
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

								order.setmId(mJsonObject
										.optInt(JsonKey.OrderBasketKey.ORDER_ID)
										+ "");
								order.setmNum(mJsonObject
										.optString(JsonKey.OrderBasketKey.ORDER_NUM));
								order.setmUnit(mJsonObject
										.optString(JsonKey.OrderBasketKey.UNIT));
								order.setmState(mJsonObject
										.optInt(JsonKey.OrderBasketKey.STATUS));
								order.setmTime(mJsonObject
										.optString(JsonKey.OrderBasketKey.TIME));
								order.setmType(mJsonObject
										.optInt(JsonKey.OrderBasketKey.TYPE));
								order.setmNum(mJsonObject
										.optString(JsonKey.OrderBasketKey.ORDER_NUM));
								order.setmTotalCount(mJsonObject
										.optInt(JsonKey.OrderBasketKey.TOTAL_COUNT));
								order.setmTotalPrice(mJsonObject
										.optString(JsonKey.OrderBasketKey.TOTAL_PRICE));
								order.setmList(parseProducts(mJsonObject
										.optJSONArray(JsonKey.OrderBasketKey.PRODUCTS)));

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

	private ArrayList<Product> parseProducts(JSONArray optJSONArray) {
		ArrayList<Product> list = new ArrayList<Product>();
		if (optJSONArray != null) {
			int count = optJSONArray.length() > 3 ? 3 : optJSONArray.length();
			for (int i = 0; i < count; i++) {
				JSONObject jsonObject = optJSONArray.optJSONObject(i);
				if (jsonObject != null) {
					Product p = new Product();
					p.setmImg(jsonObject.optString(JsonKey.Product2Key.ICON));
					p.setmId(jsonObject.optInt(JsonKey.Product2Key.PRODUCT_ID)
							+ "");
					p.setmUnit(jsonObject.optString(JsonKey.Product2Key.UNIT));
					p.setmPrice((float) jsonObject
							.optDouble(JsonKey.Product2Key.PRICE));
					p.setmAmount(jsonObject.optInt(JsonKey.Product2Key.COUNT));
					p.setmOrderNum(jsonObject
							.optString(JsonKey.Product2Key.ORDER_NUM));
					p.setmName(jsonObject.optString(JsonKey.Product2Key.NAME));

					list.add(p);
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
