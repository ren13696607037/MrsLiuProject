package com.techfly.liutaitai.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;


public class RegisterParser implements Parser {

    @Override
    public Object fromJson(JSONObject object) {
        User user=new User();
        if(object!=null){
            JSONObject obj = object.optJSONObject(JsonKey.DATA);
            if(obj!=null){
                user.setmId(obj.optString(JsonKey.UserKey.ID));
                user.setmPass(obj.optString(JsonKey.UserKey.PASS));
                user.setmPhone(obj.optString(JsonKey.UserKey.MOBILE));
                user.setmNick(obj.optString(JsonKey.UserKey.NAME));
            }else{
            	user.setmMessage(object.optString(JsonKey.MESSAGE));
            }
        }
        
        return user;
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
