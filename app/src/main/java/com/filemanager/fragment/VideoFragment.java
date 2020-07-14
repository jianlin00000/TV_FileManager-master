package com.filemanager.fragment;


import android.content.Context;
import android.os.Bundle;

import com.filemanager.R;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.ScanVideoTask;

import java.util.Comparator;

/**
 * 功能描述：视频碎片
 * 开发状况：正在开发中
 */

public class VideoFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.video));
        setClassName(getString(R.string.video));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_video);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanVideoTask(path, comparator, getAdapter());
        } else {
            return new ScanVideoTask(comparator,getAdapter());
        }
    }
}
