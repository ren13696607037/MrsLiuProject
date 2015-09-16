package com.techfly.liutaitai.model.mall.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.model.mall.bean.ServiceInfo;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;

public class ServiceInfoParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        ServiceInfo p = new ServiceInfo();
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
                    p.setmId(String.valueOf(jsonObject
                            .optInt(JsonKey.ProductInfo.ID)));
                    p.setmName("title");
                    p.setmDesc(jsonObject.optString("descri"));
                    p.setmImg(Constant.IMG_URL+jsonObject.optString("image"));
                    p.setmPrice(jsonObject.optString("price"));
                    p.setmType(jsonObject.optString("type"));
                    p.setmServiceConent("耗时"+jsonObject.optString("minutes")+"分钟/售后"
                    +jsonObject.optString("servicedays")+"天");
                    p.setmUnit(jsonObject.optString("unit"));
                    p.setCollect(jsonObject.optInt("collected") == 0 ? false : true);
                }
            }
        }
        return p;
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
