package com.filemanager.other;

import com.filemanager.adapter.AbsBaseAdapter;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描视频文件任务
 * 开发状况：正在开发中
 */

public class ScanVideoTask extends AbsScanAssignFileTask {

    public ScanVideoTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    public ScanVideoTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return getFileTypeMgr().isVideoFile(suffixName);
    }

    @Override
    protected boolean filterByCondition(File file) {
        return false;
    }
}
