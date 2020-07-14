package com.filemanager;

import android.app.Application;

import com.filemanager.other.FileTypeMgr;
import com.filemanager.other.MimeType;

/**
 * 功能描述：文件app
 * 开发状况：正在开发中
 */

public class FileApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MimeType.get().readData(this);
        AppHelper.instance().initCoreApp(this);
        FileTypeMgr.instance().initFileType(this);
    }

}
