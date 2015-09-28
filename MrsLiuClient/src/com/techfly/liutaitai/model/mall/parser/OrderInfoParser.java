package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.Utility;

public class OrderInfoParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {

		MyOrder mo = new MyOrder();

		if (object != null) {
			int resultCode = -1;
			try {
				resultCode = object.getInt(JsonKey.CODE);
			} catch (JSONException e) {
				AppLog.Logi("Shi", "ERROR:parse ProductInfo with code");
			}
			if (resultCode == 0) {
				JSONObject mJsonObject = object.optJSONObject(JsonKey.DATA);
				if (mJsonObject != null) {
					mo.setmTotalCount(mJsonObject
							.optInt(JsonKey.OrderInfoKey.TOTAL_COUNT));
					mo.setmTime(mJsonObject
							.optString(JsonKey.OrderInfoKey.TIME));
					mo.setmPayType(mJsonObject
							.optInt(JsonKey.OrderInfoKey.PAY_TYPE));
					mo.setmCustomerName(mJsonObject
							.optString(JsonKey.OrderInfoKey.CUSTOMER_NAME));
					mo.setmCustomerAddr(mJsonObject
							.optString(JsonKey.OrderInfoKey.CUSTOMER_ADDR));
					mo.setmTotalPrice(Utility.dot2(mJsonObject
							.optDouble(JsonKey.OrderInfoKey.TOTAL_PRICE)));
					mo.setmState(mJsonObject.optInt(JsonKey.OrderInfoKey.STATE));
					mo.setmOffsetValue(mJsonObject
							.optDouble(JsonKey.OrderInfoKey.OFFSET_VALUE));
					mo.setmDeliverFee(mJsonObject
							.optDouble(JsonKey.OrderInfoKey.DELIVER_FEE));
					mo.setmId(mJsonObject.optInt(JsonKey.OrderInfoKey.ORDER_ID)
							+ "");
					mo.setmNum(mJsonObject
							.optString(JsonKey.OrderInfoKey.ORDER_NUM));
					mo.setmTips(mJsonObject
							.optString(JsonKey.OrderInfoKey.COMMENT));
					mo.setmList(parseProducts(mJsonObject
							.optJSONArray(JsonKey.OrderInfoKey.PRODUCTS)));
				}
			}
		}
		return mo;
	}

	private ArrayList<Product> parseProducts(JSONArray optJSONArray) {
		ArrayList<Product> list = new ArrayList<Product>();
		if (optJSONArray != null) {
			for (int i = 0; i < optJSONArray.length(); i++) {
				JSONObject jsonObject = optJSONArray.optJSONObject(i);
				if (jsonObject != null) {
					Product p = new Product();
					p.setmImg(jsonObject.optString(JsonKey.Product2Key.ICON));
					p.setmId(jsonObject
							.optString(JsonKey.Product2Key.PRODUCT_ID));
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
