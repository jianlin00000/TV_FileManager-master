package com.filemanager.other;

import com.filemanager.adapter.AbsBaseAdapter;

import java.util.Comparator;

/**
 * 功能描述：扫描音乐任务
 * 开发状况：正在开发中
 */

public class ScanMusicTask extends AbsScanAssignFileTask {

    public ScanMusicTask(String path, Comparator comparator,AbsBaseAdapter adapter) {
        super(path, true , comparator, adapter);
    }

    public ScanMusicTask(Comparator comparator,AbsBaseAdapter adapter) {
        super(comparator, adapter);
    }

    @Override
    protected boolean isAssignFile(String suffixName) {
        return getFileTypeMgr().isMusicFile(suffixName);
    }
}
