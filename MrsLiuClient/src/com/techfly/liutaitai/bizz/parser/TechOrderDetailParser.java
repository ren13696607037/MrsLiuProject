package com.techfly.liutaitai.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class TechOrderDetailParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		AppLog.Loge("xll", object.toString());
		TechOrder service = new TechOrder();
		if(object != null){
			int code = object.optInt(JsonKey.CODE);
			if(code == 0){
				JSONObject obj = object.optJSONObject(JsonKey.DATA);
				if(obj != null){
					service.setmId(obj.optString(JsonKey.ServiceDetailKey.ID));
					service.setmOrderNo(obj.optString(JsonKey.ServiceDetailKey.SID));
					service.setmVoucher(obj.optString(JsonKey.ServiceDetailKey.VOUCHER));
					service.setmCustomerAddress(obj.optString(JsonKey.ServiceDetailKey.ADDRESS));
					service.setmCustomerName(obj.optString(JsonKey.ServiceDetailKey.UNAME));
					service.setmCustomerPhone(obj.optString(JsonKey.ServiceDetailKey.NUMBER));
					service.setmServiceIcon(Constant.IMG_URL + obj.optString(JsonKey.ServiceDetailKey.IMAGE));
					service.setmServiceName(obj.optString(JsonKey.ServiceDetailKey.NAME));
					service.setmServicePrice(obj.optString(JsonKey.ServiceDetailKey.PRICE));
					service.setmOrderTime(obj.optString(JsonKey.ServiceDetailKey.TIME));
					service.setmServiceStatus(obj.optString(JsonKey.ServiceKey.STATE));
					service.setmServiceType(obj.optString(JsonKey.ServiceDetailKey.TYPE));
					service.setmCustomerTime(obj.optString(JsonKey.ServiceDetailKey.ADDTIME));
					service.setmStartTime(obj.optString(JsonKey.ServiceDetailKey.STARTTIME));
					service.setmMinutes(obj.optString("minutes"));
					service.setmPayWay(obj.optString("paytype"));
					service.setmServiceNum(obj.optString("serviceNum"));
					service.setmClear(obj.optString("needClean"));
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
