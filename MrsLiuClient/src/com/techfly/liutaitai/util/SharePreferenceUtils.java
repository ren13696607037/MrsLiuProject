package com.techfly.liutaitai.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.techfly.liutaitai.model.home.bean.ContactInfo;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.model.pcenter.bean.User;


public class SharePreferenceUtils {
	private static final String SP_FILE_NAME="hylyihui";
	private static final String USER_ID="user_id";
	private static final String USER_PHONE="user_phone";
	private static final String USER_PASS="user_pass";
	private static final String USER_NICK="user_nick";
	private static final String USER_IMG = "user_img";
	private static final String USER_TOKEN = "user_token";
	private static final String USER_TYPE = "user_type";
	private static final String USER_MONEY = "user_money";
    private static final String IS_FISRT="is_first";
    private static final String SHARE_CONTENT="share_content";
    private static final String SHARE_URL="share_url";
	private static SharePreferenceUtils sharePreferenceUtils = null;
    private SharedPreferences mSharePreference = null;
    private Context mContext = null;
    private static final String AREA_ID="area_id";
    private static final String AREA_NAME="area_name";

    public static SharePreferenceUtils getInstance(Context context) {
        if (sharePreferenceUtils == null) {
            sharePreferenceUtils = new SharePreferenceUtils(context.getApplicationContext());
        }
        return sharePreferenceUtils;
    }

    private SharePreferenceUtils(Context context) {
        mContext = context;
        mSharePreference = mContext.getSharedPreferences(SP_FILE_NAME, Context.MODE_WORLD_WRITEABLE);
    }
    
    public synchronized void saveUser(User user){
    	 if (mSharePreference != null && user != null) {
             Editor editor = mSharePreference.edit();
             editor.putString(USER_ID, user.getmId());
             editor.putString(USER_PHONE, user.getmPhone());
             editor.putString(USER_NICK, user.getmNick());
             editor.putString(USER_PASS, user.getmPass());
             editor.putString(USER_IMG, user.getmImage());
             editor.putString(USER_TOKEN, user.getmToken());
             editor.putString(USER_TYPE, user.getmType());
             editor.putString(USER_MONEY, user.getmMoney());
             editor.commit();
         }
    }
    public synchronized void clearUser() {
        if (mSharePreference != null) {
            Editor editor = mSharePreference.edit();
            editor.remove(USER_ID);
            editor.remove(USER_PHONE);
            editor.remove(USER_NICK);
            editor.remove(USER_PASS);
            editor.remove(USER_IMG);
            editor.remove(USER_TOKEN);
            editor.remove(USER_TYPE);
            editor.remove(USER_MONEY);
            editor.commit();
        }
    }

    public synchronized User getUser() {
        if (mSharePreference != null) {
            String id = mSharePreference.getString(USER_ID, null);
            String phone = mSharePreference.getString(USER_PHONE, null);
            String pass=mSharePreference.getString(USER_PASS, null);
            String nick=mSharePreference.getString(USER_NICK, null);
            String image=mSharePreference.getString(USER_IMG, null);
            String token = mSharePreference.getString(USER_TOKEN, null);
            String type = mSharePreference.getString(USER_TYPE, null);
            String money = mSharePreference.getString(USER_MONEY, null);
            User user = new User();
            if (!TextUtils.isEmpty(id)) {
                user.setmId(id);
                user.setmPhone(phone);
                user.setmPass(pass);
                user.setmNick(nick);
                user.setmImage(image);
                user.setmToken(token);
                user.setmType(type);
                user.setmMoney(money);
                return user;
            }
        }
        return null;
    }
    
    public synchronized void saveContactInfo(ContactInfo info){
   	 if (mSharePreference != null && info != null) {
            Editor editor = mSharePreference.edit();
            editor.putString(JsonKey.HomeKey.QQ, info.getmQQ());
            editor.putString(JsonKey.HomeKey.WEIXIN, info.getmWeixin());
            editor.commit();
        }
   }
    
    public synchronized ContactInfo getContactInfo() {
        if (mSharePreference != null) {
            String qq = mSharePreference.getString(JsonKey.HomeKey.QQ, "");
            String weixin = mSharePreference.getString(JsonKey.HomeKey.WEIXIN, "");
            ContactInfo info = new ContactInfo();
            if (!TextUtils.isEmpty(qq)) {
                info.setmQQ(qq);
                info.setmWeixin(weixin);
                return info;
            }
        }
        return null;
    }

    public Boolean isFirst(){
        if (mSharePreference != null) {
            Boolean isDisplay = mSharePreference.getBoolean(IS_FISRT, true);
                return isDisplay;
        }
        return null;
    }
    
    public void saveIsFirst(Boolean isDisplay){
        if (mSharePreference != null && isDisplay != null) {
            Editor editor = mSharePreference.edit();
            editor.putBoolean(IS_FISRT, isDisplay);
            editor.commit();
        }
    }
    
    public void saveShareInfo(String shareUrl,String shareContent){
        if (mSharePreference != null && !TextUtils.isEmpty(shareContent) && !TextUtils.isEmpty(shareUrl)) {
            Editor editor = mSharePreference.edit();
            editor.putString(SHARE_URL, shareUrl);
            editor.putString(SHARE_CONTENT, shareContent);
            editor.commit();
        }
    }
    public String getShareUrl(){
        if (mSharePreference != null ) {
            String url = mSharePreference.getString(SHARE_URL,"");
            return url;
        }
        return "";
    }
    public String getShareContent(){
        if (mSharePreference != null ) {
            String content = mSharePreference.getString(SHARE_CONTENT,"");
            return content;
        }
        return "";
    }
    public synchronized void saveArea(Area area){
        if (mSharePreference != null && area != null) {
            Editor editor = mSharePreference.edit();
            editor.putString(AREA_ID, area.getmId());
            editor.putString(AREA_NAME, area.getmName());
            editor.commit();
        }
    }
    public synchronized void clearArea() {
        if (mSharePreference != null) {
            Editor editor = mSharePreference.edit();
            editor.remove(AREA_ID);
            editor.remove(AREA_NAME);
            editor.commit();
        }
    }

    public synchronized Area getArea() {
        if (mSharePreference != null) {
            String id = mSharePreference.getString(AREA_ID, null);
            String name = mSharePreference.getString(AREA_NAME, null);
            if(!TextUtils.isEmpty(id)){
                Area area = new Area();
                area.setmId(id);
                area.setmName(name);
                return area;
            }else{
                Area area = new Area();
                area.setmId("1");
                area.setmName("安徽");
                return area;
            }
        }
        return null;
    }
}
