package com.hylsmart.yihui.net.pscontrol;

import android.text.TextUtils;

import com.hylsmart.yihui.util.AppLog;

/**
 * deliver parser control 
 * @author Administrator
 *
 */
public class DeliverParser implements IDeliverParser{
    private static DeliverParser mDeliverParser;
    private static Object mObject = new Object();
    ParserHelper mHelper = new ParserHelper();
    private DeliverParser (){
        
    }
    
    public static DeliverParser newDeliverParser(){
        if(mDeliverParser==null){
            synchronized (mObject) {
             if(mDeliverParser ==null){
                 mDeliverParser = new DeliverParser();
             }   
            }
        }
        return mDeliverParser;
    }

    @Override
    public Object deliverJson(String parserClassName, String json) {
        // TODO Auto-generated method stub
        try {
            if(parserClassName==null || TextUtils.isEmpty(json) || json.equals("null")){
                return null;
            }
            mHelper.setmParser((Parser) Class.forName(parserClassName).newInstance());
        } catch (InstantiationException e) {
           AppLog.Logd("Fly", "InstantiationException"+e.getMessage());
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            AppLog.Logd("Fly", "IllegalAccessException"+e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            AppLog.Logd("Fly", "ClassNotFoundException"+e.getMessage());
        }
        return   mHelper.fromJson(json);
    }
    
  
   
  
}
