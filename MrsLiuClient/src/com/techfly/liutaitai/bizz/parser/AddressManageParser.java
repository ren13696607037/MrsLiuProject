package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class AddressManageParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<AddressManage> list=new ArrayList<AddressManage>();
		if (object != null) {
			JSONArray array = object.optJSONObject(JsonKey.DATA).optJSONArray(JsonKey.VoucherKey.DATAS);
			if (array!=null) {
				int size=array.length();
				for(int i=0;i<size;i++){
					JSONObject data=array.optJSONObject(i);
					AddressManage address=new AddressManage();
					address.setmId(data.optString(JsonKey.AddressKey.ID));
					address.setmName(data.optString(JsonKey.AddressKey.NAME));
					address.setmPhone(data.optString(JsonKey.AddressKey.MOBILE));
					address.setmDetail(data.optString(JsonKey.AddressKey.ADDRESS));
					address.setmCityId(data.optString(JsonKey.AddressKey.CITY));
					address.setDefault(data.optInt(JsonKey.AddressKey.DEFAULT) == 0 ? false : true );
					address.setmCity(data.optString(JsonKey.AddressKey.CNAME));
					list.add(address);
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
