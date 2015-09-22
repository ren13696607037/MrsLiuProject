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
import com.techfly.liutaitai.util.DateUtils;
import com.techfly.liutaitai.util.JsonKey;

public class JiShiInfoParser implements Parser{
    @Override
    public Object fromJson(JSONObject object) {
        JishiInfo jishi = new  JishiInfo();
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
                    jishi.setmId(jsonObject.optString("id"));
                    jishi.setmName(jsonObject.optString("name"));
                    jishi.setmImg(Constant.YIHUIMALL_BASE_URL+jsonObject.optString("image"));
                    jishi.setmRating((float) jsonObject.optDouble("stars"));
                    jishi.setmServiceTime(jsonObject.optString("times"));
                    if(jsonObject.optString("gender").equals("0")){
                        jishi.setmSex("男");
                    }else if(jsonObject.optString("gender").equals("1")){
                        jishi.setmSex("女");
                    }else{
                        jishi.setmSex("未知");
                    }
                    jishi.setType(jsonObject.optInt("type"));
                    List<TimePoints> list = new ArrayList<TimePoints>();
                    JSONArray  array = jsonObject.optJSONArray("points");
                    if(array!=null){
                        for(int i =0;i<array.length();i++){
                            JSONObject obj = array.optJSONObject(i);
                            AppLog.Logd("Fly", "time======="+DateUtils.getTime(obj.optLong("time"),"yyyy-MM-dd HH:mm"));
                            if(obj.optInt("type")==0){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmTimeMills(obj.optLong("time")-30*60*1000);
                                    points.setmBeforeHalfJHours(false);
                                    
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+30*60*1000);
                                    points1.setmBeforeHalfJHours(true);
                                    list.add(points1);
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(true);
                                    points.setmTimeMills(obj.optLong("time"));
                                    list.add(points);
                                }
                               
                            }else if(obj.optInt("type")==1){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(true);
                                    points.setmTimeMills(obj.optLong("time")+30*60*1000);
                                    list.add(points);
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time"));
                                    list.add(points);
                                }
                                
                             
                            }else if(obj.optInt("type")==2){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time")-30*60*100);
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+30*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(true);
                                    points2.setmBeforeHalfJHours(false);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                    
                                    
                                    
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(true);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time"));
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+60*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(false);
                                    points2.setmBeforeHalfJHours(true);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                }
                               
                                
                                
                            }else if(obj.optInt("type")==3){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time")-30*60*100);
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+30*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(true);
                                    points2.setmBeforeHalfJHours(false);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    
                                    TimePoints points3 = new TimePoints();
                                    points3.setmIsWholeHours(true);
                                    points3.setmBeforeHalfJHours(false);
                                    points3.setmTimeMills(obj.optLong("time")+90*60*1000+60*60*1000);
                                    
                                    list.add(points3);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                    
                                    
                                    
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(true);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time"));
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+60*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(true);
                                    points2.setmBeforeHalfJHours(false);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    
                                    TimePoints points3 = new TimePoints();
                                    points3.setmIsWholeHours(false);
                                    points3.setmBeforeHalfJHours(true);
                                    points3.setmTimeMills(obj.optLong("time")+90*60*1000+60*60*1000);
                                    list.add(points3);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                }
                                
                            }else if(obj.optInt("type")==4){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time")-30*60*100);
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+30*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(true);
                                    points2.setmBeforeHalfJHours(false);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    
                                    TimePoints points3 = new TimePoints();
                                    points3.setmIsWholeHours(true);
                                    points3.setmBeforeHalfJHours(false);
                                    points3.setmTimeMills(obj.optLong("time")+90*60*1000+60*60*1000);
                                    
                                    TimePoints points4 = new TimePoints();
                                    points4.setmIsWholeHours(true);
                                    points4.setmBeforeHalfJHours(false);
                                    points4.setmTimeMills(obj.optLong("time")+90*60*1000+60*60*1000+60*60*1000);
                                    
                                    list.add(points4);
                                    list.add(points3);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                    
                                    
                                    
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(true);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time"));
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+60*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(true);
                                    points2.setmBeforeHalfJHours(false);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    
                                    TimePoints points3 = new TimePoints();
                                    points3.setmIsWholeHours(true);
                                    points3.setmBeforeHalfJHours(false);
                                    points3.setmTimeMills(obj.optLong("time")+90*60*1000+60*60*1000);
                                    
                                    TimePoints points4 = new TimePoints();
                                    points4.setmIsWholeHours(false);
                                    points4.setmBeforeHalfJHours(true);
                                    points4.setmTimeMills(obj.optLong("time")+90*60*1000+60*60*1000+60*60*1000);
                                    
                                    list.add(points4);
                                    list.add(points3);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                }
                                
                            }else if(obj.optInt("type")==5){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time")-30*60*100);
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+30*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(true);
                                    points2.setmBeforeHalfJHours(false);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                    
                                    
                                    
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(true);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time"));
                                 
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(true);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")+60*60*1000);
                                    
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(false);
                                    points2.setmBeforeHalfJHours(true);
                                    points2.setmTimeMills(obj.optLong("time")+90*60*1000);
                                    list.add(points2);
                                    list.add(points1);
                                    list.add(points);
                                }
                            }
                        }
                    }
                    jishi.setmList(list);
                }
            }
        }
        return jishi;
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
