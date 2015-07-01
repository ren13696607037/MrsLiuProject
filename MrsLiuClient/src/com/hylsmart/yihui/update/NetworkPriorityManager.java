package com.hylsmart.yihui.update;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络优先级管理器
 * 
 * @author fgtian
 */
public class NetworkPriorityManager {
    public final static int PRIORITY_SYSTEM_HIGH = 0;
    public final static int PRIORITY_HIGH = 1;
    public final static int PRIORITY_NORMAL = 2;
    public final static int PRIORITY_LOW = 3;
    public final static int PRIORITY_IDLE = 4;

    public final static int DELAY_TIME = 20;
    public final static boolean HANDLE_DIFFENT_PRIORITY = true;

    private final static int PRIORITY_LEVEL_COUNT = PRIORITY_IDLE + 1;

    /**
     * 当前优先级的线程数
     * 
     * @author fgtian
     */
    private static class PriorityItem {
        private int mThreadCount;

        public PriorityItem() {
            mThreadCount = 0;
        }

        /**
         * 添加一个线程
         */
        public void add() {
            ++mThreadCount;
        }

        /**
         * 删除一个线程
         */
        public void sub() {
            if (mThreadCount > 0) {
                --mThreadCount;
            }
        }

        /**
         * 获取线程数
         * 
         * @return
         */
        public int get() {
            return mThreadCount;
        }
    }

    private static NetworkPriorityManager mInstance = null;
    private List<PriorityItem> mItems = null;

    public static NetworkPriorityManager getInstance() {
        if (null == mInstance) {
            mInstance = new NetworkPriorityManager();
        }
        return mInstance;
    }

    private NetworkPriorityManager() {
        mItems = new ArrayList<PriorityItem>();
        for (int i = 0; i < PRIORITY_LEVEL_COUNT; i++) {
            mItems.add(new PriorityItem());
        }
    }

    /**
     * 为一个线程注册优先级
     * 
     * @param priority
     *            优先级
     */
    public void registerPriority(int priority) {
        if (priority < 0 || priority >= PRIORITY_LEVEL_COUNT) {
            return;
        }
        synchronized (this) {
            mItems.get(priority).add();
        }
    }

    /**
     * 在当前优先级下，删除一个线程
     * 
     * @param priority
     *            优先级
     */
    public void unRegisterPriority(int priority) {
        if (priority < 0 || priority >= PRIORITY_LEVEL_COUNT) {
            return;
        }
        synchronized (this) {
            mItems.get(priority).sub();
        }
    }

    /**
     * 获取比当前优先级高的线程的数量，根据不同的优先级，返回不同的数值： 优先级0：一个线程当作4个线程 优先级1：一个线程当作3个线程 以此类推
     * 
     * @param priority
     *            优先级
     * @return 优先级的数量
     */
    public int getPriorityLevel(int priority) {
        if (priority <= 0 || priority >= PRIORITY_LEVEL_COUNT) {
            return 0;
        }
        synchronized (this) {
            int ret = 0;
            if (HANDLE_DIFFENT_PRIORITY) {
                for (int i = 0; i < priority; i++) {
                    ret += mItems.get(i).get() * (4 - i);
                }
            } else {
                for (int i = 0; i < priority; i++) {
                    ret += mItems.get(i).get();
                }
            }
            return ret;
        }
    }

    public int getDelayTime(int priority) {
        return getPriorityLevel(priority) * DELAY_TIME;
    }

}
