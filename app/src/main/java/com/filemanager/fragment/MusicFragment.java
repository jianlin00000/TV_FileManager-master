package com.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.filemanager.R;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.ScanMusicTask;

import java.util.Comparator;

/**
 * 功能描述：音乐碎片
 * 开发状况：正在开发中
 */

public class MusicFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.music));
        setClassName(getString(R.string.music));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_music);
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanMusicTask(path, comparator, getAdapter());
        }
        return new ScanMusicTask(comparator, getAdapter());
    }
}
