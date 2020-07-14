package com.filemanager.other;

import android.os.AsyncTask;

/**
 * 功能描述：抽象异步任务执行类，该类继承AsyncTask
 * 开发状况：正在开发中
 */

public abstract class AbsAsyncTask<T> extends AsyncTask<Object,Integer,T> {

    /**
     * 开始任务
     */
    public void startTask() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR , "");
    }
}
