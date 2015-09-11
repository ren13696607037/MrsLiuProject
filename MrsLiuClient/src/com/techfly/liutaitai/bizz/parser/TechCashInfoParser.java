package com.techfly.liutaitai.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.TechAccount;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.JsonKey;

public class TechCashInfoParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		TechAccount techAccount = new TechAccount();
		if(object != null){
			JSONObject obj = object.optJSONObject(JsonKey.DATA);
			if(obj != null){
				techAccount.setmAccount(obj.optString(JsonKey.TechnicianKey.ACCOUNT));
				techAccount.setmBank(obj.optString(JsonKey.TechnicianKey.BANK));
				techAccount.setmId(obj.optString(JsonKey.TechnicianKey.ID));
				techAccount.setmName(obj.optString(JsonKey.TechnicianKey.NAME));
				techAccount.setmType(obj.optString(JsonKey.TechnicianKey.TYPE));
			}
		}
		return techAccount;
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
