package com.hylsmart.yihui.net;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.hylsmart.yihui.util.AppLog;

/**
 * Manager for the queue
 * 
 * @author fly.f.ren
 * 
 */
public abstract class RequestManager {
  
    /**
     * the queue :-)
     */
    private static RequestQueue mRequestQueue;

    /**
     * Nothing to see here.
     */
    protected RequestManager() {
        // no instances
    }

    /**
     * @param context
     *            application context
     */
    public static void init(Context context) {//获取RequestQueue
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * @return instance of the queue
     * @throws IllegalStatException
     *             if init has not yet been called
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }
    

    public static void getRequestData(Context context,Listener<Object> listener, ErrorListener errorListener,
            RequestParam param) {
        // to do auto-generate 
         String uri =param. buildRequestUrl().toString();
         RequestManager.init(context);
         Log.i("Fly", "uri====="+uri);
         Request<Object> request = null;
        try {
            request = new ObjectRequest(param, listener, errorListener);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            AppLog.Logd("Fly", "JSONException"+e.getMessage());
            return;
        }
        RequestManager.getRequestQueue().add(request);
        
    }
    
   

}
