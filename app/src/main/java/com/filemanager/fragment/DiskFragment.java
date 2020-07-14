package com.filemanager.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import com.common.utils.LogUtil;
import com.filemanager.BuildConfig;
import com.filemanager.R;
import com.filemanager.bean.CFile;
import com.filemanager.bean.ClickPath;
import com.filemanager.other.AbsAsyncTask;
import com.filemanager.other.FileManager;
import com.filemanager.utils.FileUtil;
import com.filemanager.utils.SettingUtil;
import com.filemanager.utils.StoragesUtil;
import com.filemanager.utils.USBUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.core.content.FileProvider;

/**
 * 功能描述：全部文集碎片
 * 开发状况：正在开发中
 */

public class DiskFragment extends AbsCommonFragment {

    //保存文件管理实例
    private FileManager mFileMgr;
    //保存是否是磁盘数据
    private boolean isDiskData = false;
    //存储扫描盘
    private List<CFile> storages = new ArrayList<>();
    //路径
    private List<String> mPaths = new ArrayList<>();
    //已使用内存容量
    private long privateUsedBytes = 0;
    //总内存容量
    private long privateTotalBytes = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFileMgr = FileManager.instance();
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setMenuViewId(R.id.tv_disk);

        loadDisks();
        setFocusItemPosition(0);
    }

    /**
     * 加载所有磁盘
     */
    public void loadDisks() {
        mPaths.clear();
        isDiskData = true;
        storages.clear();
        setPathBarVisibility(View.GONE);
        List<String> rootPaths = StoragesUtil.getDiskRootPaths(getActivity());
        for (int i = 0; i < rootPaths.size(); i++) {
            String path = rootPaths.get(i);
            CFile localStorage = new CFile();
            if (i==0){
                File root = new File(path);
                privateUsedBytes += root.getTotalSpace()-root.getFreeSpace();
                privateTotalBytes += root.getTotalSpace();
                localStorage.setIcon(R.mipmap.ic_disk);
                localStorage.setPath(path);
                localStorage.setChildFileCount(FileUtil.getFileChildCount(new File(path)));
                localStorage.setName(getString(R.string.local_disk) +"(" + Formatter.formatFileSize(getContext(),privateTotalBytes)
                                             +"/"+ Formatter.formatFileSize(getContext(),privateUsedBytes) +")");
                storages.add(localStorage);
            }else {
                File usbFile = new File(path);
                File usbFilePath = new File(usbFile.getPath());
                localStorage = new CFile();
                localStorage.setIcon(R.mipmap.ic_disk);
                localStorage.setPath(usbFile.getPath());
                localStorage.setChildFileCount(FileUtil.getFileChildCount(usbFile));
                localStorage.setName(getString(R.string.out_disk) + "("+ Formatter.formatFileSize(getContext(),usbFilePath.getTotalSpace()) +"/"+
                                             Formatter.formatFileSize(getContext(),(usbFilePath.getTotalSpace() - usbFilePath.getUsableSpace())));
                storages.add(localStorage);
            }
        }
        getAdapter().putData(storages);
    }


    @Override
    protected AbsAsyncTask getScanFileTask(String path, Comparator comparator) {
        return mFileMgr.updateFile(path, getAdapter());
    }

    @Override
    protected void onItemClick(CFile file, int position) {
        if(file.isFile()) {
            isFileOpen = true;
            if(file.getType() == CFile.Type.APK) {
                if(SettingUtil.isInstallApk()) {
                    setFocusItemPosition(position);
                    FileUtil.openFileIntent(getContext(),file.getPath());
                } else {
                    installApp(file.getPath());
                }
            } else {
                setFocusItemPosition(position);
                FileUtil.openFileIntent(getContext(),file.getPath());
            }
        } else {
            isNextDir = true;
            isDiskData = false;
            setPathBarVisibility(View.VISIBLE);
            setFocusItemPosition(-1);
            addClickPath(file.getPath() + File.separator, position);
            setPath(file.getPath() + File.separator);
            mFileMgr.updateFile(file.getPath(), getAdapter()).startTask();
        }
    }

    private void installApp(String path) {
        File fileApk = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri= FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", fileApk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            uri=Uri.fromFile(fileApk);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        getActivity().startActivity(intent);
    }

    @Override
    protected AbsAsyncTask getScanTask(Comparator comparator) {
        LogUtil.d("");
        clearClickPath();
        String usbPath = USBUtil.getUsbStorageDir();
        if(!isDiskData) {
            loadDiskData(usbPath);
        } else {
            isDiskData = true;
            getAdapter().putData(storages);
        }
        setPathBarVisibility(View.GONE);
        return  null;
    }

    @Override
    public boolean onKeyDown() {
        if(mActivity != null) {
            mActivity.getMainMenu().currentMenuRequestFocus();
        }
        int pathCount = clickPathSize();
        ClickPath path;
        if (pathCount > 1){
            path = getClickPath(pathCount - 1);
            setFocusItemPosition(path.getPosition());
            removeClickPath(pathCount - 1);
            pathCount = clickPathSize() - 1;
            path = getClickPath(pathCount);
            mCurrentPath = path.getPath();
            setPath(path.getPath());
            mFileMgr.updateFile(path.getPath(), getAdapter(), FileUtil.getSortType(getDefaultSort())).startTask();
        } else if(pathCount == 1 && isNextDir){
            final int position = pathCount - 1;
            path = getClickPath(position < 0 ? 0 : position);
            setFocusItemPosition(path.getPosition());
            loadDisks();
            isNextDir = false;
            return true;
        }
        return isNextDir;
    }


    /**
     * 导入磁盘数据
     * @param usbPath usb路径
     */
    private void loadDiskData(String usbPath) {
        mPaths.clear();
        isDiskData = true;
        storages.clear();
        setPathBarVisibility(View.GONE);
        final File storeFile = Environment.getExternalStorageDirectory();
        File root = new File(storeFile.getPath());
        privateUsedBytes += root.getTotalSpace()-root.getFreeSpace();
        privateTotalBytes += root.getTotalSpace();
        CFile localStorage = new CFile();
        localStorage.setIcon(R.mipmap.ic_disk);
        localStorage.setPath(storeFile.getPath());
        localStorage.setChildFileCount(FileUtil.getFileChildCount(storeFile));
        localStorage.setName(getString(R.string.local_disk) +"(" + Formatter.formatFileSize(getContext(),privateTotalBytes)
                +"/"+ Formatter.formatFileSize(getContext(),privateUsedBytes) +")");
        storages.add(localStorage);
        if(usbPath != null) {
            Log.d("wanglf", "loadDiskData: ...." +usbPath);
            File usbFile = new File(usbPath);
            File usbFilePath = new File(usbFile.getPath());
            localStorage = new CFile();
            localStorage.setIcon(R.mipmap.ic_disk);
            localStorage.setPath(usbFile.getPath());
            localStorage.setChildFileCount(FileUtil.getFileChildCount(usbFile));
            localStorage.setName(getString(R.string.out_disk) + "("+ Formatter.formatFileSize(getContext(),usbFilePath.getTotalSpace()) +"/"+
                        Formatter.formatFileSize(getContext(),(usbFilePath.getTotalSpace() - usbFilePath.getUsableSpace())));
            storages.add(localStorage);
        }
        getAdapter().putData(storages);
    }

    public boolean isDiskData() {
        return isDiskData;
    }



}
