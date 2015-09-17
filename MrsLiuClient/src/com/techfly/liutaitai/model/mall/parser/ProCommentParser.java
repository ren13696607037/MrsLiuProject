package com.techfly.liutaitai.model.mall.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.Comments;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class ProCommentParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo info = new ResultInfo();
        ArrayList<Comments> list = new ArrayList<Comments>();
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
            JSONArray jsonArray = objd.optJSONArray("reviews");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject mJsonObject = jsonArray.optJSONObject(i);
                    if (mJsonObject != null) {
                        Comments c = new Comments();
                        c.setmId(mJsonObject.optInt("id"));
                        c.setmContent(mJsonObject
                                .optString("content"));
                        c.setmName(mJsonObject
                                .optString("mobile"));
                        c.setmPhone(mJsonObject
                                .optString("mobile"));
//                      c.setmReplay(mJsonObject
//                              .optString(JsonKey.CommentsKey.REPLAY));
                        c.setmStarScore(mJsonObject
                                .optString("stars"));
                        c.setmTime(mJsonObject
                                .optString("time"));
                        c.setmTitle(mJsonObject
                                .optString("content"));
//                      c.setmVerfy(mJsonObject.optInt(JsonKey.CommentsKey.VERIFY));
                        list.add(c);
                    }
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
