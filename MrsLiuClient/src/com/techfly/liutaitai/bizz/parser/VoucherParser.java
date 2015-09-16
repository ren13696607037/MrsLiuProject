package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.Voucher;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class VoucherParser implements Parser{

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<Voucher> list = new ArrayList<Voucher>();
		if(object != null){
			JSONArray data = object.optJSONObject(JsonKey.DATA).optJSONArray(JsonKey.VoucherKey.DATAS);
			if(data != null){
				int size = data.length();
				for(int i = 0; i < size; i++){
					JSONObject obj = data.optJSONObject(i);
					Voucher voucher = new Voucher();
					voucher.setmId(obj.optString(JsonKey.VoucherKey.ID));
					String need = obj.optString(JsonKey.VoucherKey.NEED);
					if(need.contains(".")){
						voucher.setmNeed(need.split("\\.")[0]);
					}else{
						voucher.setmNeed(need);
					}
					String money = obj.optString(JsonKey.VoucherKey.MONEY);
					if(money.contains(".")){
						voucher.setmPrice(money.split("\\.")[0]);
					}else{
						voucher.setmPrice(money);
					}
					list.add(voucher);
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
