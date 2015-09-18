package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.JishiInfo;
import com.techfly.liutaitai.model.mall.bean.TimePoints;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class JiShiInfoParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        JishiInfo product = new  JishiInfo();
        if (object != null) {
            int resultCode = -1;
            try {
                resultCode = object.getInt(JsonKey.CODE);
            } catch (JSONException e) {
                AppLog.Logi("Shi", "ERROR:parse ProductInfo with code");
            }
            if (resultCode == 0) {
                JSONObject jsonObject = object.optJSONObject(JsonKey.DATA);
                if (jsonObject != null) {
                    product.setmId(jsonObject.optString("id"));
                    product.setmName(jsonObject.optString("name"));
                    product.setmImg(Constant.YIHUIMALL_BASE_URL+jsonObject.optString("image"));
                    product.setmRating((float) jsonObject.optDouble("stars"));
                    product.setmServiceTime(jsonObject.optString("times"));
                    if(jsonObject.optString("gender").equals("0")){
                        product.setmSex("男");
                    }else if(jsonObject.optString("gender").equals("1")){
                        product.setmSex("女");
                    }else{
                        product.setmSex("未知");
                    }
                    product.setType(jsonObject.optInt("type"));
                    List<TimePoints> list = new ArrayList<TimePoints>();
                    JSONArray  array = jsonObject.optJSONArray("points");
                    if(array!=null){
                        for(int i =0;i<array.length();i++){
                            JSONObject obj = array.optJSONObject(i);
                            TimePoints points = new TimePoints();
                            points.setmIsWholeHours(obj.optInt("type")==0);
                            points.setmTimeMills(obj.optLong("time"));
                            list.add(points);
                        }
                    }
                     
                }
            }
        }
        return product;
    }

    @Override
    public Object fromJson(String json) {
        // TODO Auto-generated method stub
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
