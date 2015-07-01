package com.techfly.liutaitai.net.pscontrol;

import org.json.JSONObject;

public class ParserHelper implements Parser {
    private Parser mParser;
    public Parser getmParser() {
        return mParser;
    }

    public void setmParser(Parser mParser) {
        this.mParser = mParser;
    }

    @Override
    public Object fromJson(JSONObject object) {
        // TODO Auto-generated method stub
        return mParser.fromJson(object);
    }

    @Override
    public Object fromJson(String json) {
        // TODO Auto-generated method stub
         return mParser.fromJson(json);
    }

}
