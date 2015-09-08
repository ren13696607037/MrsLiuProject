package com.techfly.liutaitai.bizz.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class ServiceParser implements Parser {
    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo info = new ResultInfo();
        ArrayList<Service> list = new ArrayList<Service>();
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
                    Service service = new Service();
                    service.setmId(obj.optString("id"));
                    service.setmServiceName(obj.optString("title"));
                    service.setmServiceIcon(Constant.YIHUIMALL_BASE_URL+obj.optString("images"));
                    service.setmServicePerson(obj.optString("time"));
                    service.setmServicePrice(obj.optString("price"));
                    list.add(service);
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
