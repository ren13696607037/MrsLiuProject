package com.techfly.liutaitai.scale.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.techfly.liutaitai.scale.BitmapUtil;
import com.techfly.liutaitai.scale.PerfUtil;
import com.techfly.liutaitai.scale.PerfUtil.PerfLevel;


public class AssetImageResource extends BaseImageResource<String> implements
        Parcelable {

    private static final long serialVersionUID = -6755216333447798684L;
    private Config mConfig;

    public AssetImageResource(List<String> files) {
        mResources = files;
        if (PerfUtil.getInstance().getPerfLevel() == PerfLevel.HIGH) {
            mConfig = Config.ARGB_8888;
        } else {
            mConfig = Config.RGB_565;
        }
    }

    @Override
    public synchronized void displayImage(Context context, ImageView view,
            int index) {
        if (index < this.size()) {
            AssetManager am = context.getAssets();
            try {
                InputStream is = am.open(this.get(index));
                Bitmap bitmap = BitmapUtil.decodeBitmap(is, 1, mConfig);
                view.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void displayImage(Context context, ImageView view, int index, ProgressBar progressBar, Handler handler, LinearLayout layout) {
        // TODO Auto-generated method stub
        
    }


}
