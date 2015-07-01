package com.techfly.liutaitai.model.mall.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class PicTextDetailParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		// TODO Auto-generated method stub
		if (object != null) {
			int resultCode = -1;
			try {
				resultCode = object.getInt(JsonKey.CODE);
			} catch (JSONException e) {
				AppLog.Logi("Shi", "ERROR:parse PicTextDetail with code");
			}
			if (resultCode == 0) {
				String s = object.optString(JsonKey.DATA);
				return s;
			}
		}
		return null;
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
