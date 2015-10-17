package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.Jishi;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class JiShiParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo info = new ResultInfo();
        ArrayList<Jishi> list = new ArrayList<Jishi>();
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
                    Jishi product = new Jishi();
                    product.setmId(obj.optString("id"));
                    product.setmName(obj.optString("name"));
                    product.setmImg(Constant.YIHUIMALL_BASE_URL+obj.optString("image"));
                    product.setmRating((float) obj.optDouble("stars"));
                    product.setmServiceTime(obj.optString("times"));
                    if(obj.optString("gender").equals("0")){
                        product.setmSex("男");
                    }else if(obj.optString("gender").equals("1")){
                        product.setmSex("女");
                    }else{
                        product.setmSex("未知");
                    }
                   
                    product.setType(obj.optInt("type"));
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
