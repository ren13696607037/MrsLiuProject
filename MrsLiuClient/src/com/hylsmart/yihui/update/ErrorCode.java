package com.hylsmart.yihui.update;

/**
 * 提供错误码和错误提示的类
 * 
 * @author kuncheng
 * 
 */
public class ErrorCode {
    public static final int ERROR_UNKNOW = -1;
    // 网络错误码
    public static final int NETWORK_IO_EXCEPTION = 0; // 网络I/O异常
    public static final int NETWORK_RESOURCE_NOT_FIND = 1; // 服务器无资源
    public static final int NETWORK_READ_TIME_OUT = 2; // 读数据超时
    public static final int NETWORK_INVALID_URL = 3; // 无效url

    // 存储卡异常错误码
    public static final int FULL_STORAGE = 4; // 存储卡满
    public static final int STORAGE_INVALID = 5; // 存储卡无效
    public static final int STORAGE_SHARED = 13; // 存储卡被电脑共享

    // 下载错误码
    public static final int DOWNLOAD_TEMPLE_FILE_DELETE = 6; // 下载临时文件被删除
    public static final int DOWNLOAD_WRITE_FAILED = 7; // 写数据失败

    // 播放错误码
    public static final int PLAY_NO_FILE = 8; // 没有播放文件
    public static final int PLAY_MEDIA_ERROR_SERVER_DIED = 9; // 播放器死掉
    public static final int PLAY_FORMAT_ERROR = 10; // 媒体格式错误
    public static final int PLAY_STATE_ERROR = 11; // 播放器状态错误
    public static final int PLAY_HTTP_ERROR = 14;
    public static final int PLAY_RESOURCE_ERROR = 15;

    // 录音错误码
    public static final int RECORD_EXCEPTION = 12;

    /*	*//**
     * 根据错误码取得错误信息
     * 
     * @param context
     *            Context对象
     * @param errorCode
     *            错误码
     * @return 返回错误提示信息
     */
    /*
     * public static String getErrorString(Context context, int errorCode) {
     * switch (errorCode) { case NETWORK_IO_EXCEPTION: return "网络很不给力哦，再试一次吧";
     * case NETWORK_RESOURCE_NOT_FIND: return "服务器无资源"; case
     * NETWORK_READ_TIME_OUT: return "网络超时"; case NETWORK_INVALID_URL: return
     * "无效URL"; case FULL_STORAGE: return "SD卡剩余空间不足"; case STORAGE_INVALID:
     * return "未发现SD卡，无法下载歌曲资源！"; default: break; } return null; }
     */
}
