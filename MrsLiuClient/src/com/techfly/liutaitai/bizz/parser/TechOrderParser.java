package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class TechOrderParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<TechOrder> list = new ArrayList<TechOrder>();
		if(object != null){
			int code = object.optInt(JsonKey.CODE);
			if(code == 0){
				JSONArray array = object.optJSONObject(JsonKey.DATA).optJSONArray(JsonKey.VoucherKey.DATAS);
				AppLog.Loge("xll", array.toString());
				if(array != null){
					int size = array.length();
					for(int i = 0; i < size; i++){
						TechOrder service = new TechOrder();
						JSONObject obj = array.optJSONObject(i);
						service.setmId(obj.optString(JsonKey.ServiceKey.ID));
						service.setmVoucher(obj.optString(JsonKey.ServiceDetailKey.VOUCHER));
						service.setmServiceIcon(Constant.IMG_URL + obj.optString(JsonKey.ServiceDetailKey.IMAGE));
						service.setmServiceName(obj.optString(JsonKey.ServiceKey.NAME));
						service.setmServicePrice(obj.optString(JsonKey.ServiceDetailKey.PRICE));
						service.setmServiceStatus(obj.optString(JsonKey.ServiceKey.STATE));
						service.setmCustomerAddress(obj.optString(JsonKey.ServiceDetailKey.ADDRESS));
						service.setmCustomerTime(obj.optString(JsonKey.ServiceDetailKey.ADDTIME));
						service.setmOrderTime(obj.optString(JsonKey.ServiceDetailKey.TIME));
						service.setmOrderNo(obj.optString(JsonKey.ServiceDetailKey.SID));
						service.setmStartTime(obj.optString(JsonKey.ServiceDetailKey.STARTTIME));
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
