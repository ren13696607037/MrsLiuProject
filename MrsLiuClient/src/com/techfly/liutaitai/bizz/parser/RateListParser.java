package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.Rate;
import com.techfly.liutaitai.model.pcenter.bean.RequestRate;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class RateListParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		RequestRate requestRate = new RequestRate();
		ArrayList<Rate> list=new ArrayList<Rate>();
		if (object != null) {
			JSONObject data = object.optJSONObject(JsonKey.DATA);
			if (data!=null) {
				requestRate.setmAverge(data.optString(JsonKey.RateKey.AVERGE));
				requestRate.setmCount(data.optString(JsonKey.RateKey.COUNT));
				JSONArray array = data.optJSONArray(JsonKey.RateKey.REVIEWS);
				if(array != null){
					int size=array.length();
					for(int i=0;i<size;i++){
						JSONObject obj=array.optJSONObject(i);
						Rate rate=new Rate();
						rate.setmId(obj.optString(JsonKey.RateKey.ID));
						rate.setmContent(obj.optString(JsonKey.RateKey.CONTENT));
						rate.setmName(obj.optString(JsonKey.RateKey.MOBILE));
						rate.setmTime(obj.optString(JsonKey.RateKey.TIME));
						list.add(rate);
					}
				}
				requestRate.setmRates(list);
			}
		}
		return requestRate;
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
