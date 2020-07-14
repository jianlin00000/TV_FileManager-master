package com.filemanager.other;

import android.text.TextUtils;


import com.filemanager.adapter.AbsBaseAdapter;

import java.util.Comparator;

/**
 * 功能描述：扫描apk任务
 * 开发状况：正在开发中
 */

public class ScanApkTask extends AbsScanAssignFileTask {

    public ScanApkTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    public ScanApkTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return TextUtils.equals(suffixName,"apk");
    }
}
