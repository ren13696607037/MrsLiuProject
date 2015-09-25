package com.techfly.liutaitai.scale.resource;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class LocalImageResource extends BaseImageResource<Integer> implements
        Parcelable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6286157990024398956L;

    public LocalImageResource(List<Integer> resIds) {
        this.mResources = resIds;
    }

    @Override
    public synchronized void displayImage(Context context, ImageView view,
            int index) {
        if (index < this.size()) {
            view.setImageResource(this.get(index));
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
