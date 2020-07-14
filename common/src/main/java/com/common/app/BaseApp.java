package com.common.app;

import android.app.Application;

/**
 *@author: cjl
 *@date: 2020/3/19 13:26
 *@desc:
 */
public class BaseApp
        extends Application {

    private static BaseApp instance;

    public BaseApp() {
        instance = this;
    }

    public static BaseApp get() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }
}
