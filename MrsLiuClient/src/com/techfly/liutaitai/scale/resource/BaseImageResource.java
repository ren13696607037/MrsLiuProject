package com.techfly.liutaitai.scale.resource;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public abstract class BaseImageResource<T> implements Serializable {

    private static final long serialVersionUID = -1225656737147469204L;

    protected List<T> mResources;

    public int size() {
        if (mResources != null) {
            return mResources.size();
        }
        return 0;
    }

    public T get(int index) {
        if (mResources != null) {
            return mResources.get(index);
        }
        return null;
    }

    public abstract void displayImage(Context context, ImageView view, int index);
    public abstract void displayImage(Context context, ImageView view, int index,ProgressBar progressBar,Handler handler,LinearLayout layout);
    public synchronized void releaseImage(ImageView view) {
        Drawable drawable = view.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        view.setImageBitmap(null);
    }
}
