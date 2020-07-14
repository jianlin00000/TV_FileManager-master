package com.common.net;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *@author: cjl
 *@date: 2019/5/6 10:26
 *@desc:
 */
public class RxScheduler {

    /**
     * 指定上游为io线程
     * 下游为主线程
     */
    public static ObservableTransformer io_main(){
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
