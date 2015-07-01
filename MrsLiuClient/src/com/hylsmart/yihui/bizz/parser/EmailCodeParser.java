package com.hylsmart.yihui.bizz.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.net.pscontrol.Parser;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.JsonKey;

public class EmailCodeParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo result = new ResultInfo();
        try {
           resultCode = object
                    .getInt(JsonKey.CODE);
           result.setmCode(resultCode);
        } catch (JSONException e) {
            AppLog.Logd("Fly", "JSONException"+e.getMessage());
        }
        JSONObject data=object.optJSONObject(JsonKey.DATA);
        if(data!=null){
        	result.setmData(data.optString(JsonKey.UserKey.TOKEN));
            result.setmMessage(data.optString(JsonKey.UserKey.CODEURL));
        }else{
        	result.setmData(object.optString(JsonKey.DATA));
            result.setmMessage(object.optString(JsonKey.MESSAGE));
        }
        
        return result;
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
