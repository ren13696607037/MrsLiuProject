package com.hylsmart.yihui.net.pscontrol;

import org.json.JSONObject;

public interface Parser {
    Object fromJson(JSONObject object);
    
    Object fromJson(String json);
}
