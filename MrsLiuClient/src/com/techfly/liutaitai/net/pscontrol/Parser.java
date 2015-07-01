package com.techfly.liutaitai.net.pscontrol;

import org.json.JSONObject;

public interface Parser {
    Object fromJson(JSONObject object);
    
    Object fromJson(String json);
}
