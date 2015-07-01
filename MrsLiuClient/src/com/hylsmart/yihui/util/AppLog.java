package com.hylsmart.yihui.util;

import android.util.Log;

public class AppLog {
    public static final String TAG = "WisdomResiden";
    private static final boolean DEBUG = true;

    /**
     * @return void
     * @param @param content
     * @Description: show all log v type by  TAG
     */
    public static final void Logv(String content) {
        if (DEBUG) {
            Log.v(TAG, content);
        }
    }

    /**
     * @return void
     * @param tag
     * @param content
     * @Description: show all log v type by @param tag
     */
    public static final void Logv(String tag, String content) {
        if (DEBUG) {
            Log.v(tag, content);
        }
    }

    /**
     * @return void
     * @param @param content
     * @Description: show all log e type by TAG
     */
    public static final void Loge(String content) {
        Log.e(TAG, content);
    }

    /**
     * @return void
     * @param tag
     * @param content
     * @Description: show all log e type by @param tag
     */
    public static final void Loge(String tag, String content) {
        Log.e(tag, content);
    }

    public static final void Logi(String content) {
        Logi(TAG, content);
    }

    public static final void Logi(String tag, String content) {
        if (DEBUG) {
            Log.i(tag, content);
        }
    }

    public static final void Logw(String content) {
        Logw(TAG, content);
    }

    public static final void Logw(String tag, String content) {
        if (DEBUG) {
            Log.w(tag, content);
        }
    }

    public static final void Logd(String content) {
        Logd(TAG, content);
    }

    public static final void Logd(String tag, String content) {
        if (DEBUG) {
            Log.d(tag, content);
        }
    }

}