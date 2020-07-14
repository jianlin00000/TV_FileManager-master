package com.filemanager.utils;

import com.filemanager.bean.Sort;

/**
 * 功能描述：排序工具类
 * 开发状况：正在开发中
 */

public class SortUtil {

    /**
     * 获取以前选择的排序
     * @return 排序实例
     */
    public static Sort getSetSort() {
        Sort.Type type;
        final int typeValue = PreferencesUtil.getInt("typeValue",2);
        final int order = PreferencesUtil.getInt("order",-1);
        switch (typeValue) {
            case 1:
                type = Sort.Type.MODIFY_TIME;
                break;
            case 2:
                type = Sort.Type.FILE_NAME;
                break;
            case 3:
                type = Sort.Type.FILE_SIZE;
                break;
            default:
                type = Sort.Type.MODIFY_TIME;
                break;
        }
        return new Sort(type,order);
    }
}
