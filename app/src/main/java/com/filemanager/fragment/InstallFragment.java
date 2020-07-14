package com.filemanager.fragment;

import android.content.Context;
import android.os.Bundle;

import com.filemanager.R;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.ScanApkTask;
import com.filemanager.utils.SettingUtil;
import com.filemanager.utils.ToastUtil;

import java.util.Comparator;

/**
 * 功能描述：安装页碎片
 * 开发状况：正在开发中
 */

public class InstallFragment extends FileClassFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setPath(getString(R.string.install));
        setClassName(getString(R.string.install));
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.btn_install);
    }

    @Override
    public boolean isLimitCondition() {
        final boolean isInstall = SettingUtil.isInstallApk();
        if(!isInstall) {
            ToastUtil.toast(getContext(),R.string.can_not_install_out_app);
        }
        return !isInstall;
    }

    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        if(path != null) {
            return new ScanApkTask(path,comparator, getAdapter());
        }
        return new ScanApkTask(comparator, getAdapter());
    }
}
