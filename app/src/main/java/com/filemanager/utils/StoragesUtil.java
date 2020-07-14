package com.filemanager.utils;

import android.content.Context;
import android.os.StatFs;

import com.common.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import androidx.core.content.ContextCompat;

/**
 *@author: cjl
 *@date: 2020/7/8 18:25
 *@desc: 存储相关工具类
 */
public class StoragesUtil {

    /**
     * 获取所有盘的根路径
     * @param context
     * @return
     */
    public static List<String> getDiskRootPaths(Context context) {
        List<String> storages = new ArrayList<>();
        try {
            File[] externalStorageFiles = ContextCompat.getExternalFilesDirs(context, null);
            String base = String.format("/Android/data/%s/files", context.getPackageName());
            for (File file : externalStorageFiles) {
                if (file != null) {
                    String path = file.getAbsolutePath();
                    if (path.contains(base)) {
                        String finalPath = path.replace(base, "");
                        if (validPath(finalPath)) {
                            storages.add(finalPath);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("getStorages excption:"+e.toString());
            return null;
        }
        return storages;
    }

    /**
     * 校验路径合法性
     * @param path
     * @return
     */
    private static boolean validPath(String path) {
        try {
            StatFs stat = new StatFs(path);
            stat.getBlockCountLong();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
