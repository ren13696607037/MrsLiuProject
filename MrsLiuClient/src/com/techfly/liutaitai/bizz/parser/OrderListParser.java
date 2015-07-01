package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class OrderListParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		ArrayList<MyOrder> list=new ArrayList<MyOrder>();
		if (object != null) {
			JSONArray array = object.optJSONArray(JsonKey.DATA);
			if (array!=null) {
				int size=array.length();
				for(int i=0;i<size;i++){
					JSONObject data=array.optJSONObject(i);
					MyOrder order=new MyOrder();
					order.setmId(data.optString(JsonKey.MyOrderKey.ID));
					order.setmImg(data.optString(JsonKey.MyOrderKey.IMG));
					order.setmTotalPrice(data.optString(JsonKey.MyOrderKey.PAY));
					order.setmPrice(data.optString(JsonKey.MyOrderKey.PRICE));
					order.setmTitle(data.optString(JsonKey.MyOrderKey.NAME));
					order.setmState(data.optInt(JsonKey.MyOrderKey.STATE));
					order.setmNum(data.optString(JsonKey.MyOrderKey.ISEVALUATE));
					order.setmTime(data.optString(JsonKey.MyOrderKey.TIME));
					JSONObject logistics=data.optJSONObject(JsonKey.MyOrderKey.LOGISTICS);
					if(logistics!=null){
						order.setmExpCom(logistics.optString(JsonKey.MyOrderKey.GOODNAME));
						order.setmExpNo(logistics.optString(JsonKey.MyOrderKey.LOGNO));
						order.setmExpShortName(logistics.optString(JsonKey.MyOrderKey.SHORTNAME));
					}
					
					list.add(order);
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
