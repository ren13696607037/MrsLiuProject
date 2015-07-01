package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.bean.Address;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class OrderDetailParser implements Parser {

	@Override
	public Object fromJson(JSONObject object) {
		MyOrder order=new MyOrder();
		if (object != null) {
			JSONObject data = object.optJSONObject(JsonKey.DATA);
			if (data!=null) {
				order.setmId(data.optString(JsonKey.MyOrderKey.ID));
				order.setmFree(data.optString(JsonKey.MyOrderKey.FREE));
				Address address=new Address();
				address.setmName(data.optString(JsonKey.MyOrderKey.ADDRNAME));
				address.setmPhone(data.optString(JsonKey.MyOrderKey.ADDRPHONE));
				address.setmAddress(data.optString(JsonKey.MyOrderKey.ADDRESS));
				order.setmAddress(address);
				order.setmNote(data.optString(JsonKey.MyOrderKey.NOTE));
				order.setmPay(data.optInt(JsonKey.MyOrderKey.PAYSTATE));
				order.setmState(data.optInt(JsonKey.MyOrderKey.STATE));
				order.setmTotalPrice(data.optString(JsonKey.MyOrderKey.PRICE));
				order.setmPrice(data.optString(JsonKey.MyOrderKey.PAY));
				order.setmTime(data.optString(JsonKey.MyOrderKey.TIME));
				order.setmNum(data.optString(JsonKey.MyOrderKey.ISEVALUATE));
				ArrayList<Product> list=new ArrayList<Product>();
				JSONArray array=data.optJSONArray(JsonKey.MyOrderKey.GOOD);
				if(array!=null){
					int size=array.length();
					for(int i=0;i<size;i++){
						JSONObject obj=array.optJSONObject(i);
						Product product=new Product();
						product.setmAmount(obj.optInt(JsonKey.MyOrderKey.GOODNUM));
						product.setmId(obj.optString(JsonKey.MyOrderKey.GOODID));
						product.setmImg(obj.optString(JsonKey.MyOrderKey.GOODIMG));
						product.setmName(obj.optString(JsonKey.MyOrderKey.GOODNAME));
						product.setmPrice(obj.optInt(JsonKey.MyOrderKey.GOODPRICE));
						list.add(product);
					}
				}
				order.setmList(list);
			}
		}
		return order;
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
