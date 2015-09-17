package com.techfly.liutaitai.model.order.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class ServiceDetailParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		AppLog.Loge("xll", object.toString());
		Service service = new Service();
		if(object != null){
			int code = object.optInt(JsonKey.CODE);
			if(code == 0){
				JSONObject obj = object.optJSONObject(JsonKey.DATA);
				if(obj != null){
					service.setmId(obj.optString(JsonKey.ServiceDetailKey.ID));
					service.setmNum(obj.optString(JsonKey.ServiceDetailKey.SID));
					service.setmCash(obj.optString(JsonKey.ServiceDetailKey.VOUCHER));
					service.setmCustomerAddress(obj.optString(JsonKey.ServiceDetailKey.ADDRESS));
					service.setmCustomerName(obj.optString(JsonKey.ServiceDetailKey.UNAME));
					service.setmCustomerPhone(obj.optString(JsonKey.ServiceDetailKey.NUMBER));
					service.setmServiceIcon(Constant.IMG_URL + obj.optString(JsonKey.ServiceDetailKey.IMAGE));
					service.setmServiceName(obj.optString(JsonKey.ServiceDetailKey.NAME));
					service.setmServicePrice(obj.optString(JsonKey.ServiceDetailKey.PRICE));
					service.setmServiceType(obj.optString(JsonKey.ServiceDetailKey.TYPE));
					service.setmServiceTime(obj.optString(JsonKey.ServiceDetailKey.TIME));
					service.setmServiceStatus(obj.optString(JsonKey.ServiceKey.STATE));
					service.setmServicePerson(obj.optString(JsonKey.ServiceDetailKey.ADDTIME));
					service.setmTechId(obj.optString(JsonKey.ServiceDetailKey.MID));
					service.setmAliNo(obj.optString(JsonKey.ServiceKey.ALINO));
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
