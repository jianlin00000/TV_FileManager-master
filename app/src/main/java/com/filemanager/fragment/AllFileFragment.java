package com.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.filemanager.R;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.ScanAllFileTask;
import java.util.Comparator;

/**
 * 功能描述：全部文件，包含本地和磁盘
 * 开发状况：正在开发中
 */

public class AllFileFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.all_file));
        setClassName(getString(R.string.all_file));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_all);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanAllFileTask(path,comparator,getAdapter());
        }
        return new ScanAllFileTask(comparator, getAdapter());
    }
}
