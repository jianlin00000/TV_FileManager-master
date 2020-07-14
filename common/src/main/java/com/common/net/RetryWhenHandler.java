package com.common.net;

import android.text.TextUtils;

import com.common.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 *@author: cjl
 *@date: 2019/4/16 14:46
 *@desc:  失败重试
 */
public class RetryWhenHandler
        implements Function<Observable<? extends Throwable>, Observable<?>> {

    //重试延迟时间
    private int retryDelayMillis = 3000;
    //最大重试次数
    private int maxRetryCount    =3;
    //重试次数
    private int retryCount       = 0;

    private String tag;

    /**
     *
     * @param retryDelayTime 重试时间
     * @param maxRetrycount 重试次数
     */
    public RetryWhenHandler(int retryDelayTime, int maxRetrycount) {
        this.retryDelayMillis = retryDelayTime;
        this.maxRetryCount = maxRetrycount;
    }


    public RetryWhenHandler(int retryDelayTime, int maxRetryCount, String tag) {
        this.retryDelayMillis = retryDelayTime;
        this.maxRetryCount = maxRetryCount;
        this.tag = tag;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {

        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                if (++retryCount<= maxRetryCount){
                    if (!TextUtils.isEmpty(tag)) LogUtil.d(tag+"--RetryWhenHandler--retryCount:"+retryCount);
                    return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
                }
                return Observable.error(throwable);
            }
        });

    }
}
