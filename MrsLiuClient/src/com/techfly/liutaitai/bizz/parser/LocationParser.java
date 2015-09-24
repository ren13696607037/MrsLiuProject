package com.techfly.liutaitai.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;


public class LocationParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ResultInfo info = new ResultInfo();
		info.setmCode(object.optInt(JsonKey.CODE));
		info.setmData(object.optString(JsonKey.DATA));
		return info;
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
