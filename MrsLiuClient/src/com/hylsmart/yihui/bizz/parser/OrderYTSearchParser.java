package com.hylsmart.yihui.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hylsmart.yihui.model.pcenter.bean.Content;
import com.hylsmart.yihui.model.pcenter.bean.Logistics;
import com.hylsmart.yihui.net.pscontrol.Parser;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.JsonKey;

public class OrderYTSearchParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<Content> list = new ArrayList<Content>();
		int resultCode = -1;
		Logistics logistics = new Logistics();
		JSONArray array = object.optJSONArray(JsonKey.OrderSearchKey.LIST);
		JSONArray error = object.optJSONArray(JsonKey.OrderSearchKey.ERROR);
		if (array != null && array.length() > 0) {
			resultCode = 0;
		} else if (error != null && error.length() > 0) {
			JSONObject reason = error.optJSONObject(0);
			logistics.setmReason(reason
					.optString(JsonKey.OrderSearchKey.REASON));
		}
		logistics.setmResultCode(resultCode);
		if (resultCode == 0) {
			if (array != null) {
				int size = array.length();
				for (int i = 0; i < size; i++) {
					Content suggest = new Content();
					JSONObject data = array.optJSONObject(i);
					suggest.setmMessage(data
							.optString(JsonKey.OrderSearchKey.REMARK));
					suggest.setmTime(data
							.optString(JsonKey.OrderSearchKey.TIME));
					suggest.setmContent(data
							.optString(JsonKey.OrderSearchKey.ZONE));
					list.add(suggest);
				}
				logistics.setmList(list);
			}

		}

		return logistics;
	}

	@Override
	public Object fromJson(String json) {
		JSONObject object;
		try {
			object = new JSONObject(json);
			return fromJson(object);
		} catch (JSONException e) {
			AppLog.Logd("Fly", "JSONException" + e.getMessage());
			try {
				JSONArray array = new JSONArray(json);
				return fromJson(array);
			} catch (JSONException e1) {
				e1.printStackTrace();
				return json;
			}
		}
	}

	public Object fromJson(JSONArray array) {
		ArrayList<Content> list = new ArrayList<Content>();
		Logistics logistics = new Logistics();
		if (array != null) {
			int size = array.length();
			for (int i = 0; i < size; i++) {
				Content suggest = new Content();
				JSONObject data = array.optJSONObject(i);
				suggest.setmMessage(data.optString(JsonKey.OrderSearchKey.INRO));
				suggest.setmTime(data.optString(JsonKey.OrderSearchKey.UTIME));
				list.add(suggest);
			}
			logistics.setmList(list);
		}

		return logistics;
	}

}
