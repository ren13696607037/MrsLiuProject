package com.techfly.liutaitai.scale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import android.util.Log;

public class PerfUtil {

    private static PerfUtil mInstance = null;
    private int mCoreCount = 0;
    private String mCpuMode = "";
    private float mFrequency = -1;
    private float mTotalMem = -1;
    private PerfLevel mLevel;

    public enum PerfLevel {
        LOW, NORMAL, HIGH,
    }

    private static final String TAG = PerfUtil.class.getSimpleName();

    private void init() {
        String[] cpuInfo = getCpuInfo();
        mCpuMode = cpuInfo[0];
        try {
            mFrequency = Float.parseFloat(cpuInfo[1]);
        } catch (Exception e) {
            mFrequency = -1;
        }
        mCoreCount = getNumCores();
        mTotalMem = getTotalMemory();
        float performance = mCoreCount;
        if (mCpuMode.startsWith("ARMv6") || mCpuMode.startsWith("ARMv5")) {
            performance *= 0.5;
        }
        if (mFrequency > 0) {
            performance *= mFrequency / 1150;
        }
        if (mTotalMem > 0) {
            performance *= mTotalMem / 512;
        }
        if (performance > 6f) {
            mLevel = PerfLevel.HIGH;
        } else if (performance > 1f) {
            mLevel = PerfLevel.NORMAL;
        } else {
            mLevel = PerfLevel.LOW;
        }
        Log.i(TAG, "Cpu Mode : " + mCpuMode + ", Cpu Frequency : "
                + mFrequency + ", Cpu Count : " + mCoreCount
                + ", Total Memory : " + mTotalMem + ", Performance : "
                + performance);
    }

    public PerfLevel getPerfLevel() {
        return mLevel;
    }

    public static PerfUtil getInstance() {
        if (mInstance == null) {
            mInstance = new PerfUtil();
            mInstance.init();
        }
        return mInstance;
    }

    private static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = { "", "" };
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuInfo;
    }

    /**
     * 获取设备CPU个数
     * 
     * @return
     */
    private static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static float getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2;
        float memory = 0f;
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader bufferedRead = new BufferedReader(fr, 8192);
            str2 = bufferedRead.readLine();
            arrayOfString = str2.split("\\s+");
            memory = Float.parseFloat(arrayOfString[1]) / 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memory;
    }
}
