package com.techfly.liutaitai.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.techfly.liutaitai.net.pscontrol.DeliverParser;
import com.techfly.liutaitai.net.pscontrol.IDeliverParser;
import com.techfly.liutaitai.util.AppLog;

/**
 * Wrapper for Volley requests to facilitate parsing of json responses.
 * 
 * @param <T>
 */
public class ObjectRequest extends JsonRequest<Object> {

    private  Listener<Object> mListener;
    private RequestParam mRequestParam;
    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the Object from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public ObjectRequest(int method,RequestParam param, JSONObject jsonRequest,
            Listener<Object> listener, ErrorListener errorListener) {
        super(method, param.buildRequestUrl(), (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                    errorListener);
        mListener = listener;
        mRequestParam = param;
    }
//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        // TODO Auto-generated method stub
//        return mRequestParam.getPostParamMap();
//    }
    
    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     * @throws JSONException 
     *
     * @see #ObjectRequest(int, String, JSONObject, Listener, ErrorListener)
     */
    public ObjectRequest(RequestParam param, Listener<Object> listener,
            ErrorListener errorListener) throws JSONException {
        this(param.requestMethod()==-1 ? Method.GET : param.requestMethod(), param, 
                param.requestMethod()!=-1?null:null,
                listener, errorListener);
       
    }
    @Override  
    public Map<String, String> getHeaders() throws AuthFailureError {  
        if(mRequestParam.ismIsLogin()){
            HashMap<String, String> headers = (HashMap<String, String>) super.getHeaders();
            headers.put("lt-token",mRequestParam.getmToken());  
            headers.put("lt-id", mRequestParam.getmId());  
            // MyLog.d(TAG, "headers=" + headers);  
            return headers;  
        }else{
           return super.getHeaders();
        }
        
      
    }  
    @Override
    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        try {
            byte[] data = response.data;
            String charset = HttpHeaderParser.parseCharset(response.headers);
            String jsonString =
                new String(data, charset );
            IDeliverParser deliverParser = DeliverParser.newDeliverParser();
        	Object  object = null;
            if(mRequestParam.getmParserClassName()==null){
            	object = jsonString;
            }else{
               object = deliverParser.deliverJson(mRequestParam.getmParserClassName(),jsonString);
            }
            
            return Response.success(object,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } 
    }
    @Override
    protected void deliverResponse(Object response) {
        if(response!=null){
            AppLog.Logd("response==="+response.toString());
            mListener.onResponse(response);
        }


    }
//    @Override
//    public String getBodyContentType() {
//        return "text/xml; charset=" + getParamsEncoding();
//    }
    
//    @Override
//    public byte[] getBody() {
//        Map<String, String> params = null;
//        try {
//            params = getParams();
//        } catch (AuthFailureError e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if (params != null && params.size() > 0) {
//            return encodeParameters(params, getParamsEncoding());
//        }
//        return null;
//    }
    /**
     * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
     */
//    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
//        StringBuilder encodedParams = new StringBuilder();
//        try {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
//                encodedParams.append('=');
//                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
//                encodedParams.append('&');
//            }
//            String s = encodedParams.toString();
//            s =s.substring(0, s.length()-1);
//            AppLog.Logd("Fly", "任飞 111111111"+s);
//            return s.getBytes(paramsEncoding);
//        } catch (UnsupportedEncodingException uee) {
//            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
//        }
//    }
}
