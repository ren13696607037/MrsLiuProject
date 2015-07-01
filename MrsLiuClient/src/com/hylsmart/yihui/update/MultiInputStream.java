package com.hylsmart.yihui.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.webkit.MimeTypeMap;

/**
 * Http引擎
 * 
 * @author kuncheng
 */
public class MultiInputStream extends InputStream {
    public static final int DEFAULT_CONNECT_TIME = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
    public static final long DEFAULT_BLOCK_SIZE = 200 * 1024;
    public static final int DEFAULT_RETRY_COUNT = 3;

    private URL _url = null;
    private HttpURLConnection _conn = null;

    private int _contentLength = 0;
    private String _contentType = null;
    private String _realUrl = null;
    private int _responseCode = 0;
    private InputStream _is = null;
    private long _readPos = 0;
    private long _startPos = 0;
    private long _endPos = 0;
    private int _requestCount = 0;
    private byte[] postContent = null;
    private boolean mUseFragmentDownload = true;
    private Map<String, List<String>> _headers;

    private int mRetryCount = DEFAULT_RETRY_COUNT;
    private int mConnTimeout = DEFAULT_CONNECT_TIME;
    private int mReadTimeout = DEFAULT_READ_TIMEOUT;

    public MultiInputStream(String url) throws MalformedURLException {
        super();
        this._url = new URL(url);
        this._startPos = 0;
    }

    public MultiInputStream(URL url) {
        super();
        this._url = url;
        this._startPos = 0;
    }

    public MultiInputStream(String url, long startPosition)
            throws MalformedURLException {
        super();
        this._url = new URL(url);
        this._startPos = startPosition;
    }

    public MultiInputStream(URL url, long startPosition) {
        super();
        this._url = url;
        this._startPos = startPosition;
    }

    public MultiInputStream(URL url, long startPosition, byte[] postContent) {
        super();
        this._url = url;
        this._startPos = startPosition;
        this.postContent = postContent;
    }

    public void setUseFragmentDownload(boolean b) {
        mUseFragmentDownload = b;
    }

    public void setRetryCount(int count) {
        mRetryCount = count;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public void setConnTimeout(int to) {
        mConnTimeout = to;
    }

    public int getConnTimeout() {
        return mConnTimeout;
    }

    public void setReadTimeout(int to) {
        mReadTimeout = to;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public void open() throws IOException {
        open(DEFAULT_BLOCK_SIZE);
    }

    public void open(long readSize) throws IOException {
        int count = 0;
        while (true) {
            try {
                preOpen(readSize);
                break;
            } catch (IOException e) {
                e.printStackTrace();
                if (count < mRetryCount) {
                    count++;
                    continue;
                } else {
                    throw e;
                }
            }
        }

        _realUrl = _conn.getURL().toString();
        String fileType = MimeTypeMap.getFileExtensionFromUrl(_realUrl);
        if (fileType != null) {
            _contentType = fileType;
        } else {
            _contentType = _conn.getContentType();
        }
        _headers = _conn.getHeaderFields();
        _readPos = _startPos;
        if (!mUseFragmentDownload) {
            _endPos = _contentLength - 1;
        } else {
            if (_contentLength > _startPos + DEFAULT_BLOCK_SIZE) {
                _endPos = _startPos + DEFAULT_BLOCK_SIZE - 1;
            } else {
                _endPos = _contentLength - 1;
            }
        }
    }

    private void preOpen(long readSize) throws IOException {
        _requestCount = 0;
        openConnection(_startPos, _startPos + readSize - 1, postContent);
        _contentType = _conn.getContentType();
        if (_contentType != null && _contentType.startsWith("text/vnd.wap.wml")) {
            _conn.disconnect();
            openConnection(_startPos, _startPos + readSize - 1, postContent);
        }

        _responseCode = _conn.getResponseCode();
        while (_responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            _url = new URL(_conn.getHeaderField("Location"));
            openConnection(0, DEFAULT_BLOCK_SIZE - 1, postContent);
            _responseCode = _conn.getResponseCode();
        }

        if (0 == _responseCode) {
            throw new IOException();
        }

        String range = _conn.getHeaderField("Content-Range");
        if (range == null) {
            _contentLength = _conn.getContentLength();
        } else {
            _contentLength = Integer.parseInt(range.substring(range
                    .indexOf("/") + 1));
        }
    }

    public String getRealUrl() {
        return _realUrl;
    }

    public String getContentType() {
        return _contentType;
    }

    public int getContentLength() {
        return _contentLength;
    }

    public int getResponseCode() throws IOException {
        return _responseCode;
    }

    @Override
    public int read() throws IOException {
        if (_startPos >= _contentLength) {
            return -1;
        } else {
            int n = _is.read();
            if (n != -1) {
                return n;
            } else {
                openNextHttpConnection();
                return read();
            }
        }
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }

    @Override
    public void close() throws IOException {
        closeConnection();
    }

    @Override
    public void mark(int readlimit) {
        super.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }

    private int handleReadError(byte[] b, int offset, int length,
            byte[] tempBuf, int fillLen) throws IOException {
        if (_readPos >= _contentLength) {
            return -1;
        }
        System.arraycopy(tempBuf, 0, b, offset, fillLen);
        _endPos = _readPos - 1;
        openNextHttpConnection();

        int num = read(b, offset + fillLen, length - fillLen);
        if (num != -1) {
            return num + fillLen;
        } else {
            return fillLen;
        }
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        int requestLength = length;
        int leftCurrentDataLength = (int) (this._endPos - this._readPos + 1);
        byte[] tempBuf = null;
        if (_startPos >= _contentLength) {
            return -1;
        }
        if (leftCurrentDataLength >= requestLength) {
            tempBuf = new byte[requestLength];
            int fillLen = this.fillData(tempBuf);
            if (fillLen < requestLength) {
                return handleReadError(b, offset, length, tempBuf, fillLen);
            } else {
                System.arraycopy(tempBuf, 0, b, offset, requestLength);
                return requestLength;
            }
        } else {
            tempBuf = new byte[leftCurrentDataLength];
            int fillLen = this.fillData(tempBuf);
            if (fillLen < leftCurrentDataLength) {
                return handleReadError(b, offset, length, tempBuf, fillLen);
            }
            System.arraycopy(tempBuf, 0, b, offset, leftCurrentDataLength);
            openNextHttpConnection();

            int num = read(b, offset + leftCurrentDataLength, length
                    - leftCurrentDataLength);
            if (num != -1) {
                return num + leftCurrentDataLength;
            } else {
                return leftCurrentDataLength;
            }
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return super.skip(n);
    }

    private synchronized int fillData(byte[] buf) throws IOException {
        int size = buf.length;
        int readDataSize = 0;
        int remainDataSize = size;
        int alreadyReadDataSize = 0;
        while (alreadyReadDataSize < size && readDataSize != -1 && _is != null) {
            readDataSize = _is.read(buf, alreadyReadDataSize, remainDataSize);
            if (readDataSize == -1) {
                break;
            }
            alreadyReadDataSize += readDataSize;
            remainDataSize = size - alreadyReadDataSize;
        }
        this._readPos += alreadyReadDataSize;
        return alreadyReadDataSize;
    }

    private void openNextHttpConnection() throws IOException {
        closeConnection();

        _startPos = _endPos + 1;
        if (_startPos >= _contentLength && _startPos != 0) {
            _startPos = _contentLength;
            return;
        }

        _endPos = _startPos + DEFAULT_BLOCK_SIZE - 1;
        if (_endPos >= _contentLength) {
            _endPos = _contentLength - 1;
        }

        int tryCnt = 0;
        while (true) {
            try {
                openConnection(_startPos, _endPos, postContent);
                break;
            } catch (IOException e) {
                e.printStackTrace();
                if (tryCnt < mRetryCount) {
                    tryCnt++;
                    continue;
                } else {
                    throw e;
                }
            }
        }
    }

    private void openConnection(long start, long end, byte[] postContent)
            throws IOException {
        _requestCount++;

        _conn = (HttpURLConnection) _url.openConnection();
        _conn.setConnectTimeout(mConnTimeout);
        _conn.setReadTimeout(mReadTimeout);
        _conn.setRequestProperty("Accept-Encoding", "identity");

        if (postContent == null) {
            // _conn.setReadTimeout(DEFAULT_READ_TIME);
            _conn.setRequestProperty("Connection", "Keep-Alive");
            if (mUseFragmentDownload) {
                _conn.setRequestProperty("RANGE", "bytes=" + start + "-" + end);
            }
            _conn.connect();
            _is = _conn.getInputStream();
        } else {
            _conn.setRequestProperty("Connection", "Keep-Alive");
            if (mUseFragmentDownload) {
                _conn.setRequestProperty("RANGE", "bytes=" + start + "-" + end);
            }
            _conn.setRequestProperty("Authorization", "Basic");
            _conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            _conn.setDoInput(true);
            _conn.setDoOutput(true);
            _conn.setRequestMethod("POST");

            byte[] data = postContent;
            long t = System.currentTimeMillis();
            _conn.getOutputStream().write(data);
            _conn.connect();

            _is = _conn.getInputStream();
        }
        if (!mUseFragmentDownload) {
            skipBytes(start);
        }
    }

    private void closeConnection() throws IOException {
        if (null != _conn) {
            _conn.disconnect();
            _conn = null;
        }
        if (_is != null) {
            _is.close();
            _is = null;
        }
    }

    public Map<String, List<String>> getHeads() {
        return _headers;
    }

    public long skipBytes(long n) throws IOException {
        if (_is != null) {
            long i = skipBytesFromStream(_is, n);
            return i;
        }
        return -1;
    }

    /* 重写了Inpustream 中的skip(long n) 方法，将数据流中起始的n 个字节跳过 */
    private long skipBytesFromStream(InputStream inputStream, long n)
            throws IOException {
        long remaining = n;
        // SKIP_BUFFER_SIZE is used to determine the size of skipBuffer
        int SKIP_BUFFER_SIZE = 2048;
        // skipBuffer is initialized in skip(long), if needed.
        byte[] skipBuffer = null;
        int nr = 0;
        if (skipBuffer == null) {
            skipBuffer = new byte[SKIP_BUFFER_SIZE];
        }
        byte[] localSkipBuffer = skipBuffer;
        if (n <= 0) {
            return 0;
        }
        while (remaining > 0) {
            if (inputStream != null) {
                nr = inputStream.read(localSkipBuffer, 0,
                        (int) Math.min(SKIP_BUFFER_SIZE, remaining));
            } else {
                nr = -1;
            }
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }
        return n - remaining;
    }

}
