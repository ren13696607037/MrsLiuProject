package com.hylsmart.yihui.update;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



import android.os.Handler;
import android.text.TextUtils;

/**
 * @author kuncheng
 * 
 */
public class HttpDownload implements Runnable {
    private final int DEFAULT_BUFFER_SIZE = 20 * 1024;

    private long _bytesRead;
    private URL _downloadUrl;
    private MultiInputStream _is = null;
    private OutputStream _save = null;
    private byte[] _buffer;
    private boolean _stop;
    private boolean _cancel;
    private Thread _downloadThread;
    private int mRetryCount = 0;
    private int mMaxRetryCount = 0;
    private Handler mUIHandler = null;

    private DownloadListener _downloadListener = null;
    private ProgressListener _progressListener = null;
    private DivideDownloadListener _divideListener = null;
    private FileInfoListener _fileInfoListener = null;
    private byte[] postContent = null;

    private int mPriority = NetworkPriorityManager.PRIORITY_NORMAL;
    private boolean mIsUseFragementDownload = true;

    public HttpDownload() {
        mUIHandler = null;
        _bytesRead = 0;
        _stop = false;
        _cancel = false;

        _buffer = new byte[DEFAULT_BUFFER_SIZE];
        mRetryCount = 0;
        _downloadThread = new Thread(this);
    }

    public HttpDownload(Handler h) {
        mUIHandler = h;
        _bytesRead = 0;
        _stop = false;
        _cancel = false;

        _buffer = new byte[DEFAULT_BUFFER_SIZE];
        mRetryCount = 0;
        _downloadThread = new Thread(this);
    }

    public HttpDownload(byte[] postContent) {
        _bytesRead = 0;
        _stop = false;
        _cancel = false;

        _buffer = new byte[DEFAULT_BUFFER_SIZE];

        _downloadThread = new Thread(this);

        this.postContent = postContent;
    }

    public String getRealUrl() {
        if (null != _is) {
            return _is.getRealUrl();
        }
        return "";
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public void setMaxRetryCount(int count) {
        mMaxRetryCount = count;
    }

    public int getMaxRetryCount() {
        return mMaxRetryCount;
    }

    public void setProgressListener(ProgressListener e) {
        this._progressListener = e;
    }

    public void setDivideDownloadListener(DivideDownloadListener e) {
        this._divideListener = e;
    }

    public void setStopped(boolean stop) {
        _stop = stop;
    }

    public boolean isStopped() {
        return _stop;
    }

    public void cancelOpen() {
        _cancel = true;
    }

    public void cancel() {
        stopDownload();
    }

    public void getHeadInfo(final String url, FileInfoListener e) {
        _fileInfoListener = e;

        if (url == null) {
            openError(ErrorCode.NETWORK_IO_EXCEPTION);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _downloadUrl = new URL(url);
                    _bytesRead = 0;

                    openConnection(false);

                    fileInfo(_is.getContentType(), _is.getContentLength(),
                            _is.getRealUrl());
                } catch (IOException e) {
                    openError(ErrorCode.NETWORK_IO_EXCEPTION);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void start(String url, OutputStream save, DownloadListener e) {
        this.start(url, save, 0, e);
    }

    public void start(String url, OutputStream save, DownloadListener e,
            String encode) {
        this.start(url, save, 0, e, encode);
    }

    public void start(String url, OutputStream save, DownloadListener e,
            byte[] postContent) {
        this.postContent = postContent;
        this.start(url, save, 0, e);
    }

    public void start(String url, OutputStream save, long startPosition,
            DownloadListener listener) {
        if (TextUtils.isEmpty(url)) {
            downloadError(ErrorCode.NETWORK_IO_EXCEPTION,
                    "url is null or empty");
            return;
        }

        NetworkPriorityManager.getInstance().registerPriority(mPriority);
        mRetryCount = 0;
        String strUrl = null;
        strUrl = encode(url);
        _downloadListener = listener;
        _save = save;
        _bytesRead = startPosition;

        if (strUrl == null) {
            downloadError(ErrorCode.NETWORK_IO_EXCEPTION, "url is null");
            return;
        }

        try {
            _downloadUrl = new URL(strUrl);
            _downloadThread.start();
        } catch (MalformedURLException e) {
            downloadError(ErrorCode.NETWORK_INVALID_URL, e.getMessage());
            e.printStackTrace();
        }
    }

    public void start(String url, OutputStream save, long startPosition,
            DownloadListener listener, String code) {
        String strUrl = null;
        strUrl = encode(url, code);
        _downloadListener = listener;
        _save = save;
        _bytesRead = startPosition;

        if (strUrl == null) {
            downloadError(ErrorCode.NETWORK_IO_EXCEPTION, "url is null");
            return;
        }
        try {
            _downloadUrl = new URL(strUrl);
            _downloadThread.start();
        } catch (MalformedURLException e) {
            downloadError(ErrorCode.NETWORK_INVALID_URL, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        performDownload();
    }

    private void performDownload() {
        try {
            if (openConnection(true)) {
                long length = _is.getContentLength();

                if (length > 0) {
                    this.start(_is.getContentType(), length, _is.getRealUrl());
                    this.readInputStream(length);
                } else {
                    downloadError(ErrorCode.NETWORK_IO_EXCEPTION, "length <= 0");
                }
            } else {
                mRetryCount = mMaxRetryCount;
                downloadError(ErrorCode.NETWORK_INVALID_URL, "下载失败");
            }
        } catch (IOException e) {
            downloadError(ErrorCode.NETWORK_IO_EXCEPTION, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            downloadError(ErrorCode.NETWORK_IO_EXCEPTION, e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean openConnection(boolean readyDownload) throws IOException {
        _is = new MultiInputStream(_downloadUrl, _bytesRead, postContent);
        _is.setUseFragmentDownload(mIsUseFragementDownload);
        if (readyDownload) {
            try {
                _is.open();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            _is.open(1);
        }

        int responseCode = _is.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK
                && responseCode != HttpURLConnection.HTTP_PARTIAL) {
            // 404
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                if (readyDownload) {
                    downloadError(ErrorCode.NETWORK_RESOURCE_NOT_FIND,
                            "http-code:404");
                } else {
                    openError(ErrorCode.NETWORK_RESOURCE_NOT_FIND);
                }
            } else {
                if (readyDownload) {
                    downloadError(ErrorCode.NETWORK_IO_EXCEPTION, "http-code:"
                            + responseCode);
                } else {
                    openError(ErrorCode.NETWORK_IO_EXCEPTION);
                }
            }
            return false;
        }

        return true;
    }

    private void readInputStream(long length) {
        int bytesCount = 0;

        while (isStopped() == false) {
            if (_bytesRead < length) {
                try {
                    bytesCount = _is.read(_buffer);

                    if (bytesCount == -1) {
                        break;
                    } else {
                        _bytesRead = _bytesRead + bytesCount;
                        _save.flush();
                        _save.write(_buffer, 0, bytesCount);
                        this.progress(_bytesRead);
                        this.divide();
                    }
                } catch (IOException e) {
                    downloadError(ErrorCode.NETWORK_IO_EXCEPTION,
                            e.getMessage());
                    e.printStackTrace();
                }

                /**
				 */
                int delay = NetworkPriorityManager.getInstance().getDelayTime(
                        mPriority);
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                complete();
                break;
            }
        }
    }

    private void fileInfo(String type, long length, String url) {
        if (!this._cancel && this._fileInfoListener != null) {
            this._fileInfoListener.onFileInfo(type, length, url);
        }
    }

    private void start(String type, long length, String url) {
        if (!this._cancel && this._downloadListener != null) {
            this._downloadListener.onStart(type, length, url);
        }
    }

    private void progress(long savedLength) {
        if (!this._cancel && this._progressListener != null) {
            this._progressListener.onProgress(savedLength);
        }
    }

    private void divide() {
        if (!this._cancel && this._divideListener != null) {
            this._divideListener.onDivide();
        }
    }

    private void complete() {
        if (!this._cancel && this._downloadListener != null) {
            this.closeStream();
            this._downloadListener.onComplete();
        }
    }

    private void downloadError(int errorCode, String str) {
        if (!this._cancel && this._downloadListener != null) {
            retry2(errorCode, str);
        }
    }

    private void openError(int errorCode) {
        if (!this._cancel && this._fileInfoListener != null) {
            retry1(errorCode);
        }
    }

    private void stopDownload() {
        NetworkPriorityManager.getInstance().unRegisterPriority(mPriority);
        this._cancel = true;
        this._downloadListener = null;
        this._progressListener = null;
        this._divideListener = null;

        setStopped(true);

        closeStream();
    }

    private void closeStream() {
        try {
            if (_is != null) {
                _is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != _downloadThread) {
            _downloadThread.interrupt();
            try {
                // _downloadThread.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _downloadThread = null;
        }

        try {
            if (_save != null) {
                _save.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface DownloadListener {
        void onStart(String type, long length, String url);

        void onComplete();

        void onError(int errorCode, String err);
    }

    public interface DivideDownloadListener {
        void onDivide();
    }

    public interface ProgressListener {
        void onProgress(long savedBytes);
    }

    public interface FileInfoListener {
        void onFileInfo(String type, long length, String url);

        void onError(int errorCode);
    }

    private String encode(String url) {
        return UrlEncode.encodeUTF8(url);
    }

    private String encode(String url, String code) {
        return UrlEncode.encode(url, code);
    }

    /**
     * 获取长度
     */
    public long getContentLength() {
        return _is.getContentLength();
    }

    private void retry1(int errorCode) {
        if (null != mUIHandler && mRetryCount < mMaxRetryCount) {
            stopDownload();
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    doRetry();
                }
            });
        } else {
            this._fileInfoListener.onError(errorCode);
            stopDownload();
        }
    }

    private void retry2(int errorCode, String err) {
        if (null != mUIHandler && mRetryCount < mMaxRetryCount) {
            stopDownload();
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    doRetry();
                }
            });
        } else {
            this._downloadListener.onError(errorCode, err);
            stopDownload();
        }
    }

    private void doRetry() {
        // 重新请求
        _downloadThread = new Thread(HttpDownload.this);
        mRetryCount++;
        _downloadThread.start();
    }

    public void setUseFragementDownload(boolean b) {
        mIsUseFragementDownload = b;
    }
}
