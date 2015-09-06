package com.techfly.liutaitai.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.Technician;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class TechnicianParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		Technician technician = new Technician();
		if(object != null){
			JSONObject data = object.optJSONObject(JsonKey.DATA);
			if(data != null){
				technician.setmId(data.optInt(JsonKey.TechnicianKey.ID));
				technician.setmType(data.optString(JsonKey.MyServiceKey.TYPE));
				technician.setmTimes(data.optString(JsonKey.TechnicianKey.TIMES));
				technician.setmHeader(Constant.IMG_HEADER_URL + data.optString(JsonKey.TechnicianKey.IMAGE));
				technician.setmName(data.optString(JsonKey.TechnicianKey.NAME));
				technician.setmSex(data.optString(JsonKey.TechnicianKey.SEX));
				technician.setmStar(data.optString(JsonKey.TechnicianKey.STARS));
				technician.setmCity(data.optString(JsonKey.TechnicianKey.CITY));
				technician.setmCityName(data.optString(JsonKey.TechnicianKey.CITYNAME));
				technician.setmAddress(data.optString(JsonKey.TechnicianKey.ADDRESS));
			}
		}
		return technician;
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
