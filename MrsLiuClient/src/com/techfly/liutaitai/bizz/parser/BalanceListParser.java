package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class BalanceListParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<Balance> list = new ArrayList<Balance>();
		if(object != null){
			JSONArray data = object.optJSONObject(JsonKey.DATA).optJSONArray(JsonKey.BalanceKey.DATAS);
			if(data != null){
				int size = data.length();
				for(int i = 0; i < size; i++){
					Balance balance = new Balance();
					JSONObject obj = data.optJSONObject(i);
					balance.setmId(obj.optString(JsonKey.BalanceKey.ID));
					balance.setmPrice(obj.optString(JsonKey.BalanceKey.MONEY));
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
