package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class ProTuangouParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo info = new ResultInfo();
        ArrayList<Product> list = new ArrayList<Product>();
        try {
           resultCode = object
                    .getInt(JsonKey.CODE);
           info.setmCode(resultCode);
           info.setmMessage(object.optString(JsonKey.MESSAGE));
        } catch (JSONException e) {
            AppLog.Logd("Fly", "JSONException"+e.getMessage());
        }
        if(resultCode==0){
            JSONArray array = object.optJSONArray(JsonKey.DATA);
            if(array!=null){
                for(int i=0;i<array.length();i++){
                    JSONObject obj = array.optJSONObject(i);
                    Product product = new Product();
                    product.setmId(obj.optString(JsonKey.TuanGouKey.ID));
                    product.setmName(obj.optString(JsonKey.TuanGouKey.NAME));
                    product.setmPrice((float) obj.optDouble(JsonKey.TuanGouKey.PRICE));
                    product.setmMarketPrice((float) obj.optDouble(JsonKey.TuanGouKey.MARKET_PRICE));
                    product.setmSale(obj.optString(JsonKey.TuanGouKey.SALENUM));
                    product.setmImg(obj.optString(JsonKey.TuanGouKey.ICON));
                    list.add(product);
                }
            }
        }
        info.setObject(list);
        return info;
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
