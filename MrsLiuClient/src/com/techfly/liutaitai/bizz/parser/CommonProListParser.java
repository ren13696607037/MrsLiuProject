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

public class CommonProListParser implements Parser {
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
            JSONArray array = object.optJSONObject(JsonKey.DATA).optJSONArray(JsonKey.DATAS);
            if(array!=null){
                for(int i=0;i<array.length();i++){
                    JSONObject obj = array.optJSONObject(i);
                    Product product = new Product();
                    product.setmId(obj.optString(JsonKey.CollectKey.ID));
                    product.setmName(obj.optString(JsonKey.CollectKey.TITLE));
                    product.setmPrice((float) obj.optDouble(JsonKey.CollectKey.PRICE));
//                    product.setmAmount(obj.optInt(JsonKey.p))
                    product.setmImg(Constant.YIHUIMALL_BASE_URL+obj.optString(JsonKey.CollectKey.IMAGE));
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
