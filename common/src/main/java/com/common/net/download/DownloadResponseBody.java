package com.common.net.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author: cjl
 * @date: 2019/4/14
 * @desc:
 */
public class DownloadResponseBody extends ResponseBody {

    private ResponseBody     responseBody      =null;
    private DownloadListener mDownloadListener =null;
    private BufferedSource   bufferedSource;
    public static final int PROGRESS=0x1000;

    private Handler mHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==PROGRESS){
                int readBytes =msg.arg1;
                int totalBytes=msg.arg2;
                boolean isDone= (boolean) msg.obj;
                if (null != mDownloadListener) {
                    mDownloadListener.onProgress(readBytes, totalBytes, isDone);
                }
                if (isDone){
                    mHandler.removeMessages(PROGRESS);
                }
            }

        }
    };



    public DownloadResponseBody(ResponseBody responseBody, DownloadListener downloadListener) {
        this.responseBody = responseBody;
        mDownloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    //返回内容长度，没有则返回-1
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }


    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                LogUtil.i("进度："+totalBytesRead+",总长度："+contentLength());

                Message msg = Message.obtain();
                msg.what=PROGRESS;
                msg.arg1= (int) totalBytesRead;
                msg.arg2= (int) responseBody.contentLength();
                msg.obj=bytesRead == -1;
                mHandler.sendMessage(msg);
                return bytesRead;
            }
        };

    }
}
