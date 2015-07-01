package com.techfly.liutaitai.update;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.update.HttpDownload.DivideDownloadListener;
import com.techfly.liutaitai.update.HttpDownload.DownloadListener;
import com.techfly.liutaitai.update.HttpDownload.ProgressListener;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.StroageUtil;
import com.techfly.liutaitai.util.Utility;

public class DownloadService extends Service {

    public static final String DEFAULT_FILE_PATH = Environment
            .getExternalStorageDirectory().getPath()
            + File.separator
            + "TeddyBear.apk";

    public static final String DOWNLOAD_PROGRESS = "com.hylsmart.updatedownloadprogress";
    public static final String DOWNLOAD_FAILED = "com.hylsmart.downloadfailed";
    public static final String DOWNLOAD_COMPLETE = "com.hylsmart.downloadcomplete";
    public static final String DOWNLOAD_CANCEL = "com.hylsmart.downloadcancel";
    public static final String SDCARD_ERROE = "com.hylsmart.sdcardError";

    private DecimalFormat mFormater = new DecimalFormat("###");
    private PendingIntent contentIntent;
    private NotificationManager mNotificationManager = null;
    private ExecutorService mDownloadTasks;
    private WeakHashMap<String, DownloadTask> mTasks;

    private static String TAG = DownloadService.class.getSimpleName();

    private DownloadBinder mBinder = new DownloadBinder();

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    public enum StopFlag {
        CANCEL, ERROR, COMPLETE
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.Logi(TAG, "create service");
        mDownloadTasks = Executors.newCachedThreadPool();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mTasks = new WeakHashMap<String, DownloadService.DownloadTask>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean isCancel = intent.getBooleanExtra(DOWNLOAD_CANCEL, false);
            if (isCancel) {
                Intent bIntent = new Intent(DOWNLOAD_CANCEL);
                DownloadService.this.sendBroadcast(bIntent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        AppLog.Logi(TAG, "bind service");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        AppLog.Logi(TAG, "unbind service");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void submit(final String url, final String path,
            final boolean showNotify) {
        AppLog.Logi(TAG, "submit download task url: " + url + ", path: " + path);
        if (!mTasks.containsKey(url)) {
            DownloadTask task = new DownloadTask(url, path, showNotify);
            task.execute();
        }
    }

    public void cancel(final String url) {
        AppLog.Logi(TAG, "cancel download task url: " + url);
        DownloadTask task = mTasks.get(url);
        if (task != null) {
            task.cancel(StopFlag.CANCEL);
        }
    }

    private class DownloadTask implements DownloadListener, ProgressListener,
            DivideDownloadListener {

        private String downurl;
        private String saveFilePath = DEFAULT_FILE_PATH;
        private ByteArrayOutputStream outputStream;
        private HttpDownload downloader;

        private boolean showNotify;
        private int retryCount = 0;
        private long savedByteCount = 0;
        private long contentLength = 0;
        private int progress;

        public DownloadTask(final String url, final String path,
                final boolean showNotify) {
            this.downurl = url;
            this.showNotify = showNotify;
            Intent intent = new Intent(DownloadService.this,
                    DownloadService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(DOWNLOAD_CANCEL, true);
            contentIntent = PendingIntent.getService(DownloadService.this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (!TextUtils.isEmpty(path)) {
                this.saveFilePath = path;
            }
            File f = new File(this.saveFilePath);
            if (f.exists()) {
                f.delete();
            }
            mTasks.put(url, this);
        }

        public void execute() {
            mDownloadTasks.execute(new Runnable() {

                @Override
                public void run() {
                    long availableSize = StroageUtil.getSdCardAvailableSize();
                    long contentSize = Utility.getContentLength(Utility
                            .executeHttpHeadRequest(DownloadTask.this.downurl));
                    if (contentSize <= 0 || availableSize <= contentSize) {
                        Intent intent = new Intent(SDCARD_ERROE);
                        DownloadService.this.sendBroadcast(intent);
                        return;
                    }
                    DownloadTask.this.outputStream = new ByteArrayOutputStream();
                    DownloadTask.this.downloader = new HttpDownload();
                    DownloadTask.this.downloader.setUseFragementDownload(true);
                    DownloadTask.this.downloader
                            .setDivideDownloadListener(DownloadTask.this);
                    DownloadTask.this.downloader
                            .setProgressListener(DownloadTask.this);
                    DownloadTask.this.downloader.start(
                            DownloadTask.this.downurl,
                            DownloadTask.this.outputStream, new File(
                                    DownloadTask.this.saveFilePath).length(),
                            DownloadTask.this);
                    showNotify("0%");
                }
            });
        }

        public void cancel(StopFlag flag) {
            try {
                downloader.cancel();
                outputStream.close();
                mTasks.remove(downurl);
                Intent intent = null;
                switch (flag) {
                case ERROR:
                    clearNotify();
                    intent = new Intent(DOWNLOAD_FAILED);
                    break;
                case COMPLETE:
                    intent = new Intent(DOWNLOAD_COMPLETE);
                    break;
                case CANCEL:
                    clearNotify();
                    break;
                }
                if (intent != null) {
                    DownloadService.this.sendBroadcast(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDivide() {
            retryCount = 0;
            if (TextUtils.isEmpty(this.saveFilePath)) {
                this.saveFilePath = DEFAULT_FILE_PATH;
            }
            writeToFile(this.saveFilePath, this.outputStream);
        }

        @Override
        public void onProgress(long savedBytes) {
            this.savedByteCount = savedBytes;
            if (contentLength > 0) {
                progress = (int) (((float) this.savedByteCount / this.contentLength) * 100);
                Intent intent = new Intent();
                intent.setAction(DOWNLOAD_PROGRESS);
                intent.putExtra("progress", progress);
                DownloadService.this.sendBroadcast(intent);
                if (progress > 0 && progress < 100) {
                    showNotify(mFormater.format(progress) + "%");
                }
            }
        }

        @Override
        public void onStart(String type, long length, String url) {
            this.contentLength = length;
        }

        @Override
        public void onComplete() {
            Intent intent = new Intent();
            File file = new File(saveFilePath);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            contentIntent = PendingIntent.getActivity(DownloadService.this, 0,
                    intent, 0);
            showNotify("100%");
            cancel(StopFlag.COMPLETE);
        }

        @Override
        public void onError(int errorCode, String err) {
            if (retryCount >= 10 || (savedByteCount > 0)
                    || errorCode == ErrorCode.NETWORK_INVALID_URL) {
                cancel(StopFlag.ERROR);
            } else {
                retryCount++;
                downloader.cancel();
                downloader = null;
                execute();
            }
        }

        private void showNotify(String str) {
            if (showNotify) {
                int icon = android.R.drawable.stat_sys_download;
                long when = System.currentTimeMillis();
                Notification notification = new Notification(icon, "", when);
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.setLatestEventInfo(DownloadService.this,
                        getString(R.string.app_name), str, contentIntent);
                mNotificationManager.notify(this.hashCode(), notification);
            }
        }

        private void clearNotify() {
            showNotify = false;
            mNotificationManager.cancel(this.hashCode());
        }

        private void writeToFile(String filePath, ByteArrayOutputStream os) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath, true);
                os.writeTo(fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        os.flush();
                        os.reset();
                        os.close();
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
