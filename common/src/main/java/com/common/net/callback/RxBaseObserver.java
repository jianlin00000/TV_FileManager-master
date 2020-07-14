package com.common.net.callback;

import android.content.Context;

import com.google.gson.JsonParseException;
import com.common.R;
import com.common.app.BaseApp;
import com.common.net.response.BaseResponse;
import com.common.utils.LogUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Observer基类
 * 注：用于解析后台返回数据"data"字段为jsonObject的情形
 */

public abstract class RxBaseObserver<T> extends DisposableObserver<BaseResponse<T>> {

    //----------------http类错误----------------------------------------
    public static final int CONNECT_ERROR = 1001;//连接错误,网络异常
    public static final int CONNECT_TIMEOUT = 1002;//连接超时
    public static final int PARSE_ERROR = 1003;//解析错误
    public static final int UNKNOWN_ERROR = 1004;//未知错误
    public static final int REQUEST_TIMEOUT = 1005;//请求超时


    protected Context context  = BaseApp.get();


    public RxBaseObserver() {}


    @Override
    public void onError(Throwable e) {
        dealException(e);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void  onError(int errorCode,String errorMsg);


    /**
     * 处理异常错误
     * @param t
     */
    private void dealException(Throwable t) {
        String errorMsg=null;
        if (t!=null){
            errorMsg=t.getMessage();
            LogUtil.e(errorMsg);
        }
        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            //连接错误
            onError(CONNECT_ERROR, context.getString(R.string.connect_error)+":"+errorMsg);
        } else if (t instanceof InterruptedException) {
            //连接超时
            onError(CONNECT_TIMEOUT, context.getString(R.string.connect_timeout)+":"+errorMsg);
        } else if (t instanceof JsonParseException || t instanceof JSONException
                || t instanceof ParseException) {
            //解析错误
            onError(PARSE_ERROR, context.getString(R.string.parse_error)+":"+errorMsg);
        } else if (t instanceof SocketTimeoutException) {
            //请求超时
            onError(REQUEST_TIMEOUT, context.getString(R.string.request_timeout)+":"+errorMsg);
        } else if (t instanceof HttpException) {
            onError(UNKNOWN_ERROR, context.getString(R.string.net_error)+":"+errorMsg);
        } else if (t instanceof UnknownError) {
            //未知错误
            onError(UNKNOWN_ERROR, t.getMessage()+":"+errorMsg);
        } else {
            onError(UNKNOWN_ERROR, t.getMessage()+":"+errorMsg);
        }
    }
}
