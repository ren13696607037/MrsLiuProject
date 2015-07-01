package com.techfly.liutaitai.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class StroageUtil {

    /**
     * 获取SDCard空闲大小
     */
    public static long getSdCardAvailableSize() {
        File sdcardDir = android.os.Environment.getExternalStorageDirectory();
        StatFs sf = null;
        try {
            sf = new StatFs(sdcardDir.getPath());
        } catch (Exception e) {
            return 0;
        }
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    /**
     * 判断存储卡是否可用
     */
    public static boolean checkSDCardStatus() {
        String sdcardStatus = Environment.getExternalStorageState();
        return sdcardStatus.equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
