package com.hylsmart.yihui.update;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hylsmart.yihui.MainActivity;
import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bean.ResultInfo;
import com.hylsmart.yihui.bizz.parser.CommonParser;
import com.hylsmart.yihui.dialog.AskDialog;
import com.hylsmart.yihui.dialog.AskDialog.OnAskDialogClickListener;
import com.hylsmart.yihui.dialog.AskDialogActivity;
import com.hylsmart.yihui.net.HttpURL;
import com.hylsmart.yihui.net.RequestManager;
import com.hylsmart.yihui.net.RequestParam;
import com.hylsmart.yihui.update.DownloadMgr.DownloadListener;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.JsonKey;
import com.hylsmart.yihui.util.RequestParamConfig;
import com.hylsmart.yihui.util.SmartToast;

/**
 * 
 * @author yuancheng
 * 
 * @version 2013-7-9 上午8:51:54
 */

public class UpdateMgr {

    private static UpdateMgr mUpdateMgr;

    private Context mContext;
    private UpdateInfo mInfo;
    private String mApkPath = Environment.getExternalStorageDirectory()
            .getPath() + File.separator + "yihui.apk";
    private UpdateEventCallback mUpdateCallback;

    public interface UpdateEventCallback {
        public void onUpdateFailEvent();

        public void onUpdateCancelEvent();

        public void onUpdateCompleteEvent();
    }

    public class onAdviceUpdateEvent implements UpdateEventCallback {

        @Override
        public void onUpdateFailEvent() {
            Toast.makeText(mContext,
                    mContext.getString(R.string.update_failed),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpdateCancelEvent() {
        }

        @Override
        public void onUpdateCompleteEvent() {
            installApk();
        }

    }

    public class onForceUpdateEvent implements UpdateEventCallback {
        @Override
        public void onUpdateFailEvent() {
            Toast.makeText(mContext,
                    mContext.getString(R.string.update_failed),
                    Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    exitApp();
                }
            }).start();
        }

        @Override
        public void onUpdateCancelEvent() {
            exitApp();
        }

        @Override
        public void onUpdateCompleteEvent() {
            installApk();
        }
    }

    public class onSilentUpdateEvent implements UpdateEventCallback {

        @Override
        public void onUpdateFailEvent() {
        }

        @Override
        public void onUpdateCancelEvent() {
        }

        @Override
        public void onUpdateCompleteEvent() {
            AskDialogActivity
                    .setOnAskDialogClickListener(new OnAskDialogClickListener() {

                        @Override
                        public void onAskDialogConfirm() {
                            installApk();
                        }

                        @Override
                        public void onAskDialogCancel() {
                        }
                    });
            Intent intent = new Intent(mContext, AskDialogActivity.class);
            intent.putExtra(AskDialogActivity.TAG_MESSAGE,
                    mInfo.getUpdateDesc());
            mContext.startActivity(intent);
        }
    }

    public void setUpdateEventCallback(UpdateEventCallback callback) {
        this.mUpdateCallback = callback;
    }

    public static UpdateMgr getInstance(Context context) {
         if (mUpdateMgr == null) {
        	 mUpdateMgr = new UpdateMgr(context);
         }
        return mUpdateMgr;
    }

    public void checkUpdateInfo(UpdateEventCallback callback,
            boolean autoUpdate) {
        this.mUpdateCallback = callback;
        //TODO 后台请求获取下载地址
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            RequestParam param = new RequestParam();
            HttpURL url = new HttpURL();
            url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.UPDATE_VERSION);
            url.setmGetParamPrefix(RequestParamConfig.VERSION).setmGetParamValues(pInfo.versionCode+"");
            param.setmHttpURL(url);
            param.setmParserClassName(CommonParser.class.getName());
            RequestManager.getRequestData(mContext, createMyReqSuccessListener(autoUpdate), createMyReqErrorListener(), param);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private Response.Listener<Object> createMyReqSuccessListener(final boolean autoUpdate) {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                    if(object instanceof ResultInfo){
                        ResultInfo info = (ResultInfo) object;
                        if(info.getmCode()==0 && !TextUtils.isEmpty(info.getmData())&&!"null".equals(info.getmData())){
                            update(UpdateInfo.getInfo(info.getmData()),autoUpdate);
                        }else{
                            if(!(mContext instanceof MainActivity)){
                                SmartToast.makeText(mContext, "当前已是最新版本!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
					
				
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
            }
       };
    }

    public void update(final UpdateInfo info, boolean autoUpdate) {
        if (info == null) {
            return;
        }
        mInfo = info;
        generateApkPath(mContext.getString(R.string.app_name),
                mInfo.getUpdateVersion());
        AskDialog dialog;
            dialog = new AskDialog(mContext,
                    mContext.getString(R.string.update), mInfo.getUpdateDesc());
            dialog.setListener(new OnAskDialogClickListener() {

                @Override
                public void onAskDialogConfirm() {
                    startDownload(mInfo);
                }

                @Override
                public void onAskDialogCancel() {
                }
            });
            dialog.show();
    }

    private UpdateMgr(Context context) {
        mContext = context;
    }

    private void startDownload(final UpdateInfo info) {
        final DownloadMgr mgr = new DownloadMgr(mContext,
                mContext.getString(R.string.app_name), null);
//        mgr.setShowProgress(!UpdateMode.SILENT_UPDATE.equals(info
//                .getUpdateMode()));
        mgr.setShowProgress(true);
        
        mgr.setListener(new DownloadListener() {

            @Override
            public void onDownloadError() {
                getUpdateCallback(info.getUpdateMode()).onUpdateFailEvent();
            }

            @Override
            public void onDownloadComplete() {
                installApk();
//                AskDialogActivity
//                .setOnAskDialogClickListener(new OnAskDialogClickListener() {
//
//                    @Override
//                    public void onAskDialogConfirm() {
//                        installApk();
//                    }
//
//                    @Override
//                    public void onAskDialogCancel() {
//                    }
//                });
//        Intent intent = new Intent(mContext, AskDialogActivity.class);
//        intent.putExtra(AskDialogActivity.TAG_MESSAGE,
//                mInfo.getUpdateDesc());
//        mContext.startActivity(intent);
//                getUpdateCallback(info.getUpdateMode()).onUpdateCompleteEvent();
            }

            @Override
            public void onDownloadCancel() {
                getUpdateCallback(info.getUpdateMode()).onUpdateCancelEvent();
            }
        });
        if (!mgr.submitTask(info.getDownloadUrl(), mApkPath)) {
            getUpdateCallback(info.getUpdateMode()).onUpdateFailEvent();
        }

    }

    private void generateApkPath(String appName, String version) {
        mApkPath = Environment.getExternalStorageDirectory().getPath()
                + File.separator + appName + version + ".apk";
        AppLog.Logd("Fly", " mApkPath"+ mApkPath);
    }

    private UpdateEventCallback getUpdateCallback(UpdateMode mode) {
        if (mUpdateCallback != null) {
            return mUpdateCallback;
        }
        switch (mode) {
        case USER_SELECT:
            return new onAdviceUpdateEvent();
        case FORCE_UPDATE:
            return new onForceUpdateEvent();
        case SILENT_UPDATE:
            return new onSilentUpdateEvent();
        default:
            return null;
        }
    }

    private void installApk() {
        File file = new File(mApkPath);
        if (file.exists()) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }

    private void exitApp() {
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

}
