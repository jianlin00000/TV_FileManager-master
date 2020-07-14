package com.filemanager.other;

import com.filemanager.adapter.AbsBaseAdapter;
import com.filemanager.utils.SettingUtil;

import java.io.File;
import java.util.Comparator;

/**
 * 功能描述：扫描图片任务
 * 开发状况：正在开发中
 */

public class ScanPictureTask extends AbsScanAssignFileTask {

    public ScanPictureTask(Comparator comparator, AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    public ScanPictureTask(String path, Comparator comparator, AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return getFileTypeMgr().isPictureFile(suffixName);
    }

    @Override
    protected boolean filterByCondition(File file) {
        if(SettingUtil.getPicSetting()) {
            if (file.length() < 10240) {
                return true;
            }
            return false;
        }
        return super.filterByCondition(file);
    }
}
