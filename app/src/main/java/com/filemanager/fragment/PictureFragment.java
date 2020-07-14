package com.filemanager.fragment;
import android.content.Context;
import android.os.Bundle;

import com.filemanager.R;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.ScanPictureTask;

import java.util.Comparator;

/**
 * 功能描述：图片浏览碎片
 * 开发状况：正在开发中
 */

public class PictureFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.picture));
        setClassName(getString(R.string.picture));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_picture);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanPictureTask(path,comparator, getAdapter());
        } else {
            return new ScanPictureTask(comparator, getAdapter());
        }
    }
}
