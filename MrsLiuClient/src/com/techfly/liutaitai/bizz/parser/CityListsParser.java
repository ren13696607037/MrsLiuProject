package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.City;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class CityListsParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<City> list=new ArrayList<City>();
		if (object != null) {
			JSONArray array = object.optJSONArray(JsonKey.DATA);
			if (array != null) {
				int size=array.length();
				for(int i=0;i<size;i++){
					City area=new City();
					JSONObject obj=array.optJSONObject(i);
					area.setmId(obj.optString(JsonKey.AddressKey.ID));
					area.setmName(obj.optString(JsonKey.AddressKey.NAME));
					list.add(area);
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
