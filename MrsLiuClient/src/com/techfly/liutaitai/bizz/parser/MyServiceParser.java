package com.techfly.liutaitai.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.MyService;
import com.techfly.liutaitai.model.pcenter.bean.Technician;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class MyServiceParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		AppLog.Loge("xll", object.toString());
		MyService service = new MyService();
		if(object != null){
			JSONObject data = object.optJSONObject(JsonKey.DATA);
			if(data != null){
				Technician technician = new Technician();
				service.setmId(data.optString(JsonKey.MyServiceKey.ID));
				service.setmOrderingNum(data.optString(JsonKey.MyServiceKey.ING_ORDERS));
				service.setmOrderNum(data.optString(JsonKey.MyServiceKey.NEW_ORDERS));
				service.setmServiceingNum(data.optString(JsonKey.MyServiceKey.SERVICE_ORDERS));
				service.setmType(data.optString(JsonKey.MyServiceKey.TYPE));
				technician.setmTimes(data.optString(JsonKey.TechnicianKey.TIMES));
				technician.setmHeader(Constant.IMG_HEADER_URL + data.optString(JsonKey.TechnicianKey.IMAGE));
				technician.setmName(data.optString(JsonKey.TechnicianKey.NAME));
				technician.setmSex(data.optString(JsonKey.TechnicianKey.SEX));
				service.setmPrice(data.optString(JsonKey.MyServiceKey.MONEY));
				service.setmTechnician(technician);
				AppLog.Loge("xll", technician.toString());
			}
		}
		return service;
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
