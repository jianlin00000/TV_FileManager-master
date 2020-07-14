package com.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.common.BuildConfig;

import androidx.annotation.StringRes;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: ToastUtils.java
 * Author: Victor
 * Date: 2018/8/13 15:26
 * Description: 吐司工具类
 * -----------------------------------------------------------------
 */

public class ToastUtils {

    /**
     * 调试模式下可显示
     *
     * @param msg
     */
    public static void showDebug(Context context,String msg) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调试模式下可显示
     *
     * @param resId
     */
    public static void showDebug(Context context,@StringRes int resId) {
        if (BuildConfig.DEBUG) {
            final String text = context.getResources().getString(resId);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 短暂显示
     *
     * @param msg
     */
    public static void showShort(Context context,CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示
     *
     * @param resId
     */
    public static void showShort(Context context,@StringRes int resId) {
        final String text = context.getResources().getString(resId);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示
     *
     * @param msg
     */
    public static void showLong(Context context,CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 短暂显示
     *
     * @param resId
     */
    public static void showLong(Context context,int resId) {
        final String text = context.getResources().getString(resId);
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
