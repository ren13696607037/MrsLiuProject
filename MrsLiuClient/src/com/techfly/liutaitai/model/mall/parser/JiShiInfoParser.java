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
                    jishi.setmIdleTime(jsonObject.optInt("serviceTime"));
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
                            // 以下是时间点的控制 分为 半小时，正小时，和正小时与半小时的结合 三个控制点
                            if(obj.optInt("type")==1){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(false);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")-30*60*1000);
                                    if( list.contains(points1)){
                                        points1.setmIsWholeHours(true);
                                        list.set(list.indexOf(points1), points1);
                                    }else{
                                        list.add(points1);
                                    }
                              
                                }else{
                                    TimePoints points = new TimePoints();
                                    points.setmBeforeHalfJHours(true);
                                    points.setmTimeMills(obj.optLong("time"));
                                    if( list.contains(points)){
                                        points.setmIsWholeHours(true);
                                        list.set(list.indexOf(points), points);
                                    }else{
                                        list.add(points);
                                    }
                                }
                               
                            }else if(obj.optInt("type")%2==0){
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                
                                    //前 30 分钟前半段 展示
                                    TimePoints points1 = new TimePoints();
                                    points1.setmIsWholeHours(false);
                                    points1.setmBeforeHalfJHours(false);
                                    points1.setmTimeMills(obj.optLong("time")-30*60*1000);
                                    if( list.contains(points1)){
                                        points1.setmIsWholeHours(true);
                                        list.set(list.indexOf(points1), points1);
                                    }else{
                                        list.add(points1);
                                    }
                                    // 中间整个小时的展示
                                    for(int k =0;k<obj.optInt("type")/2-1;k++){
                                        TimePoints points = new TimePoints();
                                        points.setmIsWholeHours(true);
                                        points.setmTimeMills(obj.optLong("time")+30*60*1000+k*60*60*1000);
                                        list.add(points);
                                    }
                                    
                                    //后 30 分钟后半段展示  
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(true);
                                    points.setmTimeMills(obj.optLong("time")+30*60*1000+(obj.optInt("type")/2-1)*60*60*1000);
                                    if( list.contains(points)){
                                        points.setmIsWholeHours(true);
                                        list.set(list.indexOf(points), points);
                                    }else{
                                        list.add(points);
                                    }
                                  
                                }else{
                                    for(int k =0;k<obj.optInt("type")/2;k++){
                                        // 整个小时
                                        TimePoints points = new TimePoints();
                                        points.setmIsWholeHours(true);
                                        points.setmBeforeHalfJHours(false);
                                        points.setmTimeMills(obj.optLong("time")+k*60*60*1000);
                                        list.add(points);
                                    }
                                   
                                }
                             
                            }else {
                                if(DateUtils.getTime(obj.optLong("time"),"mm").equals("30")){
                                    TimePoints points = new TimePoints();
                                    points.setmIsWholeHours(false);
                                    points.setmBeforeHalfJHours(false);
                                    points.setmTimeMills(obj.optLong("time")-30*60*1000);
                                    if( list.contains(points)){
                                        points.setmIsWholeHours(true);
                                        list.set(list.indexOf(points), points);
                                    }else{
                                        list.add(points);
                                    }
                                    for(int j=0;j<obj.optInt("type")/2;j++){
                                        TimePoints points1 = new TimePoints();
                                        points1.setmIsWholeHours(true);
                                        points1.setmTimeMills(obj.optLong("time")+30*60*1000+j*60*1000);
                                        list.add(points1);
                                    }
                                  
                                }else{
                                    
                                    for(int j=0;j<obj.optInt("type")/2;j++){
                                        TimePoints points1 = new TimePoints();
                                        points1.setmIsWholeHours(true);
                                        points1.setmTimeMills(obj.optLong("time")+30*60*1000+j*60*1000);
                                        list.add(points1);
                                    }
                                
                                    TimePoints points2 = new TimePoints();
                                    points2.setmIsWholeHours(false);
                                    points2.setmBeforeHalfJHours(true);
                                    points2.setmTimeMills(obj.optLong("time")+(obj.optInt("type")/2+1)*60*60*1000);
                                    if( list.contains(points2)){
                                        points2.setmIsWholeHours(true);
                                        list.set(list.indexOf(points2), points2);
                                    }else{
                                        list.add(points2);
                                    }
                                  
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
