package com.techfly.liutaitai.scale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class BitmapUtil {

    public static int[] measureSize(InputStream is) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        return new int[] { options.outWidth, options.outHeight };
    }

    public static Bitmap decodeBitmap(InputStream is, int sampleSize,
            Config config) {
        Bitmap bitmap = null;
        Options options = new Options();
        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = config;
        try {
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (OutOfMemoryError e) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            bitmap = decodeBitmap(is, sampleSize / 2, config);
        }
        return bitmap;
    }

    public static Bitmap decodeBitmap(byte[] data, int sampleSize, Config config) {
        Bitmap bitmap = null;
        Options options = new Options();
        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = config;
        try {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                    options);
        } catch (OutOfMemoryError e) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            bitmap = decodeBitmap(data, sampleSize / 2, config);
        }
        return bitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path) {
        boolean result = false;
        File file = new File(path);
        File fileDir = file.getParentFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fOut = null;
        try {

            file.createNewFile();
            fOut = new FileOutputStream(file);
            result = bitmap.compress(CompressFormat.JPEG, 100, fOut);
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return result;
    }

    public static Bitmap scaleBitmap(Bitmap srcBmp, float scaleRate) {
        if (srcBmp != null && !srcBmp.isRecycled()) {
            Matrix matrix = new Matrix();
            matrix.postScale(scaleRate, scaleRate);
            Bitmap destBmp = Bitmap.createBitmap(srcBmp, 0, 0,
                    srcBmp.getWidth(), srcBmp.getHeight(), matrix, true);
            return destBmp;
        }
        return null;
    }

    public static Bitmap mergeBitmap(List<SoftReference<Bitmap>> bmpRefs,
            int outWidth, Config config) {
        int outHeight = 0;
        List<Float> scaleRates = new ArrayList<Float>();
        for (SoftReference<Bitmap> bmpRef : bmpRefs) {
            Bitmap bitmap = bmpRef.get();
            if (bitmap != null && !bitmap.isRecycled()) {
                float scaleRate = ((float) outWidth) / bitmap.getWidth();
                scaleRates.add(scaleRate);
                outHeight += bitmap.getHeight() * scaleRate;
            } else {
                scaleRates.add(0f);
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(outWidth, outHeight, config);
        Canvas canvas = new Canvas(newBmp);
        outHeight = 0;
        for (int i = 0; i < bmpRefs.size(); i++) {
            Bitmap bitmap = bmpRefs.get(i).get();
            if (bitmap != null && !bitmap.isRecycled()) {
                Bitmap scaleBitmap = scaleBitmap(bitmap, scaleRates.get(i));
                if (scaleBitmap != null) {
                    canvas.drawBitmap(scaleBitmap, 0, outHeight, null);
                    outHeight += scaleBitmap.getHeight();
                    bitmap.recycle();
                    scaleBitmap.recycle();
                }
            }
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBmp;
    }

    public static Bitmap mergeBitmap(List<Bitmap> bitmapList, int outWidth) {
        if (bitmapList == null || bitmapList.size() < 1) {
            return null;
        }
        int outHeight = 0;
        List<Float> scaleRates = new ArrayList<Float>();
        for (Bitmap bitmap : bitmapList) {
            if (bitmap != null && !bitmap.isRecycled()) {
                float scaleRate = ((float) outWidth) / bitmap.getWidth();
                scaleRates.add(scaleRate);
                outHeight += bitmap.getHeight() * scaleRate;
            } else {
                scaleRates.add(0f);
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(outWidth, outHeight,
                Config.ARGB_4444);
        Canvas canvas = new Canvas(newBmp);
        outHeight = 0;
        int sum = bitmapList.size();
        for (int i = 0; i < sum; i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (bitmap != null && !bitmap.isRecycled()) {
                Bitmap scaleBitmap = scaleBitmap(bitmap, scaleRates.get(i));
                if (scaleBitmap != null) {
                    canvas.drawBitmap(scaleBitmap, 0, outHeight, null);
                    outHeight += scaleBitmap.getHeight();
                    // bitmap.recycle();
                    scaleBitmap.recycle();
                }
            }
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBmp;
    }
}
