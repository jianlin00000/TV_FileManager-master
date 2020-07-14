package com.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.filemanager.R;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.ScanOfficeTask;

import java.util.Comparator;

/**
 * 功能描述：用于过滤办公类文件
 * 开发状况：正在开发中
 */

public class OfficeFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.office));
        setClassName(getString(R.string.office));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_office);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanOfficeTask(path,comparator,getAdapter());
        }
        return new ScanOfficeTask(comparator,getAdapter());
    }

}
