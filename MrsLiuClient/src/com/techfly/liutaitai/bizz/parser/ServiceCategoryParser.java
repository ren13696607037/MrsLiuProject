package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.SortRule;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class ServiceCategoryParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo info = new ResultInfo();
        ArrayList<SortRule> list = new ArrayList<SortRule>();
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
                    SortRule rule = new SortRule();
                    if(i==0){
                        rule.setmIsSelect(true);  
                    }
                    rule.setmId(obj.optString("id"));
                    rule.setmName(obj.optString("name"));
                    list.add(rule);
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
