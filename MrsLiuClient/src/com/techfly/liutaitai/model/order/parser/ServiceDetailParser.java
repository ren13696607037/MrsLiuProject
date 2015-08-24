package com.techfly.liutaitai.model.order.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.JsonKey;

public class ServiceDetailParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		Service service = new Service();
		if(object != null){
			int code = object.optInt(JsonKey.CODE);
			if(code == 10){
				JSONObject obj = object.optJSONObject(JsonKey.DATA);
				if(obj != null){
					service.setmId(obj.optString(JsonKey.ServiceDetailKey.ID));
					service.setmCash(obj.optString(JsonKey.ServiceDetailKey.VOUCHER));
					service.setmCustomerAddress(obj.optString(JsonKey.ServiceDetailKey.ADDRESS));
					service.setmCustomerName(obj.optString(JsonKey.ServiceDetailKey.UNAME));
					service.setmCustomerPhone(obj.optString(JsonKey.ServiceDetailKey.NUMBER));
					service.setmServiceIcon(obj.optString(JsonKey.ServiceDetailKey.IMAGE));
					service.setmServiceName(obj.optString(JsonKey.ServiceDetailKey.NAME));
					service.setmServicePrice(obj.optString(JsonKey.ServiceDetailKey.PRICE));
					service.setmServiceType(obj.optString(JsonKey.ServiceDetailKey.TYPE));
					service.setmServiceTime(obj.optString(JsonKey.ServiceDetailKey.DATE + "   " + JsonKey.ServiceDetailKey.TIME));
					service.setmServiceStatus(obj.optString(JsonKey.ServiceDetailKey.STATE));
					service.setmServicePerson(obj.optString(JsonKey.ServiceDetailKey.ADDTIME));
				}
			}else{
				return object.optString(JsonKey.MESSAGE);
			}
		}
		return service;
	}

	@Override
	public Object fromJson(String json) {
		try {
			JSONObject object = new JSONObject(json);
			return fromJson(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
