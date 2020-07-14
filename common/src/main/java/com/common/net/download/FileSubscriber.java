package com.common.net.download;

import com.common.R;
import com.common.app.BaseApp;
import com.common.utils.LogUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 *@author: cjl
 *@date: 2019/4/15 09:45
 *@desc: 文件下载的Observer
 */
public class FileSubscriber<T> implements Observer<T> {

    private DownloadListener mDownloadListener =null;
    private String           mUrl              =null;
    private File             mFile             =null;
    private Disposable       mDisposable       =null;

    public FileSubscriber(String url, File file,DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
        mUrl = url;
        mFile=file;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable=d;
        if (mDownloadListener!=null){
            mDownloadListener.onStart();
        }
    }

    @Override
    public void onNext(T t) {
        LogUtil.i("");
    }


    @Override
    public void onComplete() {
        LogUtil.i("");
        DownloadManager.getInstance().removeRecord(mUrl);
        if (mDownloadListener!=null)mDownloadListener.onComplete(mUrl,mFile);
        mDisposable.dispose();
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("e:"+e.getMessage());
        DownloadManager.getInstance().removeRecord(mUrl);
        if (mDownloadListener!=null){
            mDownloadListener.onFailed(getErrorMsg(e));
        }
        disposable();
    }

    public void disposable(){
        LogUtil.i("");
        if (mDisposable!=null&&!mDisposable.isDisposed())mDisposable.dispose();
        if (mDownloadListener!=null){
            mDownloadListener=null;
        }
    }

    /**
     * 获取具体的异常信息
     * @param t
     * @return
     */
    private String getErrorMsg(Throwable t){
        BaseApp context = BaseApp.get();
        String error="";
        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            //连接错误
            error=context.getString(R.string.connect_error);
        } else if (t instanceof InterruptedException) {
            //连接超时
            error=context.getString(R.string.connect_timeout);
        } else if (t instanceof JsonParseException || t instanceof JSONException
                || t instanceof ParseException) {
            //解析错误
            error=context.getString(R.string.parse_error);
        } else if (t instanceof SocketTimeoutException) {
            //请求超时
            error=context.getString(R.string.request_timeout);
        } else if (t instanceof HttpException) {
            error=context.getString(R.string.net_error)+":"+t.getMessage();
        }  else {
            error=t.getMessage();
        }
        return error;
    }



}
