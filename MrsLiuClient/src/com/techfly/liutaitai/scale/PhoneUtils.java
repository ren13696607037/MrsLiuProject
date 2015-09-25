package com.techfly.liutaitai.scale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneUtils {
    Context context;

    public PhoneUtils(Context context) {
        this.context = context;
    }

    public static boolean checkDoubleNumber(String num) {
        return Pattern.compile("^[0-9]{1,5}\\.?[0-9]{0,2}$").matcher(num)
                .find();
    }

    public static boolean checkEmail(String email) {
        return Pattern
                .compile(
                        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?")
                .matcher(email).find();
    }

    public static boolean checkMobile(String num) {
        Matcher localMatcher = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$")
                .matcher(num);
        Log.i("phone<<<<<<<<<<", num);
        return localMatcher.matches();
    }

    public static boolean checkNumber(String num) {
        return Pattern.compile("^[0-9]*$").matcher(num).find();
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getDeviceId() {
        return ((TelephonyManager) this.context.getSystemService("phone"))
                .getDeviceId();
    }

}