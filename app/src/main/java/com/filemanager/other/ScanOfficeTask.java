package com.filemanager.other;

import com.filemanager.adapter.AbsBaseAdapter;

import java.util.Comparator;

/**
 * 功能描述：扫描办公文件任务
 * 开发状况：正在开发中
 */

public class ScanOfficeTask extends AbsScanAssignFileTask {

    public ScanOfficeTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator ,adapter);
    }

    public ScanOfficeTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator ,adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return FileTypeMgr.instance().isOfficeFile(suffixName);
    }
}
