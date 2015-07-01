package com.techfly.liutaitai.update;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author yuancheng
 * 
 * @version 2013-7-9 上午8:52:19
 */
public class UpdateInfo {
    private UpdateMode mUpdateMode;
    private String mUpdateVersion;
    private String mDownloadUrl;
    private String mUpdateDesc;

    public static UpdateInfo getInfo(final String json) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(jObject!=null){
            UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.mUpdateMode = UpdateMode.USER_SELECT;
            updateInfo.mUpdateDesc = "更新说明:"+jObject.optString("log");
            updateInfo.mUpdateVersion = jObject.optString("version");
            updateInfo.mDownloadUrl = jObject.optString("url");
            return updateInfo;
        }
      return null;
    }

    public void setUpdateMode(int updateMode) {
        this.mUpdateMode = UpdateMode.values()[updateMode];
    }

    public UpdateMode getUpdateMode() {
        return this.mUpdateMode;
    }

    public void setUpdateDesc(String updateInfo) {
        this.mUpdateDesc = updateInfo;
    }

    public String getUpdateDesc() {
        return mUpdateDesc;
    }

    public void setUpdateVersion(String updateVersion) {
        this.mUpdateVersion = updateVersion;
    }

    public String getUpdateVersion() {
        return mUpdateVersion;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }
}
