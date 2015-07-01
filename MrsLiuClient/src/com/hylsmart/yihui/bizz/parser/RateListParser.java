package com.hylsmart.yihui.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hylsmart.yihui.model.pcenter.bean.Rate;
import com.hylsmart.yihui.net.pscontrol.Parser;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.JsonKey;

public class RateListParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<Rate> list=new ArrayList<Rate>();
		if (object != null) {
			JSONArray array = object.optJSONArray(JsonKey.DATA);
			if (array!=null) {
				int size=array.length();
				for(int i=0;i<size;i++){
					JSONObject data=array.optJSONObject(i);
					Rate rate=new Rate();
					rate.setmId(data.optString(JsonKey.MyOrderKey.GOODID));
					rate.setmContent(data.optString(JsonKey.MyOrderKey.RATECONTENT));
					rate.setmName(data.optString(JsonKey.MyOrderKey.RATENAME));
					rate.setmTime(data.optString(JsonKey.MyOrderKey.TIME));
					list.add(rate);
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
