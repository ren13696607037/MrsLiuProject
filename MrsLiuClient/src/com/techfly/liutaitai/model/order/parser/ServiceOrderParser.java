package com.techfly.liutaitai.model.order.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class ServiceOrderParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<Service> list = new ArrayList<Service>();
		if(object != null){
			int code = object.optInt(JsonKey.CODE);
			if(code == 0){
				JSONArray array = object.optJSONObject(JsonKey.DATA).optJSONArray(JsonKey.VoucherKey.DATAS);
				AppLog.Loge("xll", array.toString());
				if(array != null){
					int size = array.length();
					for(int i = 0; i < size; i++){
						Service service = new Service();
						JSONObject obj = array.optJSONObject(i);
						service.setmId(obj.optString(JsonKey.ServiceKey.ID));
						service.setmCash(obj.optString(JsonKey.ServiceKey.DATE));
						service.setmServiceIcon(Constant.YIHUIMALL_BASE_URL + obj.optString(JsonKey.ServiceKey.IMAGE));
						service.setmServiceName(obj.optString(JsonKey.ServiceKey.NAME));
						service.setmServicePerson(obj.optString(JsonKey.ServiceKey.TECH));
						service.setmServiceType(obj.optString(JsonKey.ServiceKey.TYPE));
						service.setmServiceTime(obj.optString(JsonKey.ServiceKey.TIME));
						service.setmServicePrice(obj.optString(JsonKey.ServiceKey.PRICE));
						service.setmServiceStatus(obj.optString(JsonKey.ServiceKey.STATE));
						list.add(service);
					}
				}
			}else{
				return object.optString(JsonKey.MESSAGE);
			}
		}
		return list;
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
