package com.hylsmart.yihui.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.hylsmart.yihui.model.pcenter.bean.User;
import com.hylsmart.yihui.net.pscontrol.Parser;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.JsonKey;


public class LoginParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		User user = new User();
		if (object != null) {
			JSONObject obj = object.optJSONObject(JsonKey.DATA);
			if (obj != null) {
				user.setmId(obj.optString(JsonKey.UserKey.ID));
				user.setmPhone(obj.optString(JsonKey.UserKey.MOBILE));
				user.setmPass(obj.optString(JsonKey.UserKey.PASS));
				user.setmNick(obj.optString(JsonKey.UserKey.NAME));
			} else {
				user.setmMessage(object.optString(JsonKey.MESSAGE));
			}
		}
		return user;
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
