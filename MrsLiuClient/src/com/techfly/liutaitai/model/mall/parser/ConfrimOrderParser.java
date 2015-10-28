package com.techfly.liutaitai.model.mall.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.model.mall.bean.ConfirmOrder;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.net.pscontrol.Parser;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.JsonKey;

public class ConfrimOrderParser implements Parser{

    @Override
    public Object fromJson(JSONObject object) {
        int  resultCode =-1;
        ResultInfo info = new ResultInfo();
        try {
           resultCode = object
                    .getInt(JsonKey.CODE);
           info.setmCode(resultCode);
           info.setmMessage(object.optString(JsonKey.MESSAGE));
        } catch (JSONException e) {
            AppLog.Logd("Fly", "JSONException"+e.getMessage());
        }
        ConfirmOrder confirm = new ConfirmOrder();
        if(resultCode==0){
            JSONObject objd = object.optJSONObject(JsonKey.DATA);
            confirm.setmDeliverFee((float) objd.optDouble("cost"));
            confirm.setmIsUseVoucher(objd.optString("voucher").equals("1"));
            JSONObject objd2 = objd.optJSONObject("userAddress");
            if(objd2!=null){
                AddressManage add = new AddressManage();
                add.setmDetail(objd2.optString("address"));
                add.setmId(objd2.optString("id"));
                add.setmName(objd2.optString("name"));
                add.setmCity(objd2.optString("city"));
                add.setmPhone(objd2.optString("mobile"));
                confirm.setAddress(objd2.optString("address"));
                confirm.setAddressId(objd2.optString("id"));
                confirm.setName(objd2.optString("name"));
                confirm.setPhone(objd2.optString("mobile"));
                confirm.setmDeliverMinMoney((float) objd.optDouble("gratisCost"));
                confirm.setmAddressManage(add);
            }
        }
        
        info.setObject(confirm);
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

