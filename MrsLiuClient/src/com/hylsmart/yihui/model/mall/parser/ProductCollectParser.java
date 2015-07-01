package com.hylsmart.yihui.model.mall.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.hylsmart.yihui.net.pscontrol.Parser;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.JsonKey;

public class ProductCollectParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		// TODO Auto-generated method stub
		if(object != null){
			int resultCode = -1;
			try {
				resultCode = object.getInt(JsonKey.CODE);
			} catch (JSONException e) {
				AppLog.Logi("Shi", "ERROR:parse ProductCollect with code");
			}
			if(resultCode == 0){
				return 0x111;
			}
		}
		return 0x110;
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
