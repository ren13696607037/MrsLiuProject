package com.techfly.liutaitai.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

public class SmartToast extends Toast {
    private static SmartToast mSmartToast;
    
    public SmartToast(Context context) {
        super(context);
       
    }

    public static SmartToast makeText(Context context, CharSequence text,
            int duration) {
        if(mSmartToast==null){
            mSmartToast = new SmartToast(context);
        }
        Toast parentToast = Toast.makeText(context, text, duration);
        View nextView = parentToast.getView();
        mSmartToast .setView(nextView);
        mSmartToast .setDuration(duration);
        return mSmartToast ;
    }

    public static Toast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
}
    public static void showText(Context context, int resId){
        makeText(context, resId, Toast.LENGTH_LONG).show();
    }
    public static void showText(Context context, String str){
        makeText(context, str, Toast.LENGTH_LONG).show();
    }
    @Override
    public void show() {
            super.show();
            return;
     }
}
