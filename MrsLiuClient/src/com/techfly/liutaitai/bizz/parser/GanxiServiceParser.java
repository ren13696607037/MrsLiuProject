package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class GanxiServiceParser implements Parser {
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
            JSONObject objd = object.optJSONObject(JsonKey.DATA);
            JSONArray array = objd.optJSONArray("datas");
            if(array!=null){
                for(int i=0;i<array.length();i++){
                    JSONObject obj = array.optJSONObject(i);
                    Product product = new Product();
                    product.setmId(obj.optString("id"));
                    product.setmName(obj.optString("productName"));
                    product.setmPrice((float) obj.optDouble(JsonKey.ProductKey.PRICE));
                    product.setmMarketPrice((float) obj.optDouble(JsonKey.ProductKey.PRICE));
                    product.setmSale(obj.optString(JsonKey.ProductKey.SALENUM));
                    product.setmImg(Constant.IMG_URL +obj.optString(JsonKey.ProductKey.ICON));
                    product.setmDesc(obj.optString("text"));;
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
