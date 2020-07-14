package com.common.net.callback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *@author: cjl
 *@date: 2018/5/18 16:13
 *@desc: 普通Observer基类
 */
public abstract class BaseObserver<T>
        implements Observer<T> {
    protected Disposable disposable;


    @Override
    public void onSubscribe(Disposable d) {
        disposable=d;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
    }

}
