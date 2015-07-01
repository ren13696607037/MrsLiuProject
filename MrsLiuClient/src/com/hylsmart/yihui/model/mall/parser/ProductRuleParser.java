package com.hylsmart.yihui.model.mall.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hylsmart.yihui.model.mall.bean.ProductRule;
import com.hylsmart.yihui.net.pscontrol.Parser;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.JsonKey;

public class ProductRuleParser implements Parser {

    @Override
    public Object fromJson(JSONObject object) {
        List<ProductRule> list = new ArrayList<ProductRule>();
        if (object != null) {
            int resultCode = -1;
            try {
                resultCode = object.getInt(JsonKey.CODE);
            } catch (JSONException e) {
                AppLog.Logi("Shi", "ERROR:parse ProductInfo with code");
            }
            if (resultCode == 0) {
                JSONObject jsonObject = object.optJSONObject(JsonKey.DATA);
           }
        
       
            
       }
        return null;
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
