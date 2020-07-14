package com.filemanager.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;

import com.filemanager.R;
import com.filemanager.bean.CFile;
import com.filemanager.fragment.AbsCommonFragment;
import com.filemanager.other.FileManager;
import com.filemanager.widget.HintDialog;

import java.io.File;
import java.util.List;

/**
 * 功能描述：Dialog弹出框工具类
 * 开发状况：正在开发中
 */

public class DialogUtil {

    private DialogUtil() {
    }

    /**
     * 创建新文件夹dialog
     * @param fragment 运行环境
     */
    public static void createNewFolderDialog(final AbsCommonFragment fragment) {
        final HintDialog dialog = new HintDialog(fragment.getContext());
        dialog.setInputHintText(getString(fragment.getContext(),R.string.input_new_file_name));
        dialog.setIOkCallback(new HintDialog.IOkCallback() {
            @Override
            public void onOk(String text) {
                if(!TextUtils.isEmpty(text)) {
                    try {
                        FileManager.instance().createNewFile(text);
                        fragment.refresh();
//                        activity.getAdapter().notifyDataSetChanged();
                        ToastUtil.toast(fragment.getContext(),R.string.create_new_file_success);
                    } catch (Exception e) {
                        ToastUtil.toast(fragment.getContext(),R.string.create_new_file_fail);
                    }
                } else {
                    ToastUtil.toast(fragment.getContext(),R.string.new_file_name_not_empty);
                }
            }
        });
        dialog.show();
    }

    /**
     * 删除文件dialog
     * @param fragment 运行环境
     * @param file 需要删除的文件路径
     */
    public static void deleteFileDialog(final AbsCommonFragment fragment,final CFile file) {
        final HintDialog dialog = new HintDialog(fragment.getContext());
        dialog.setInputOrHint(true);
        dialog.setHintText(getString(fragment.getContext(),R.string.can_not_delete_file));
        dialog.setIOkCallback(new HintDialog.IOkCallback() {
            @Override
            public void onOk(String text) {
                final boolean isSuccess = FileManager.instance().deleteFile(file.getPath());
                if(isSuccess) {
                    fragment.refresh();
                    ToastUtil.toast(fragment.getContext(),R.string.delete_file_success);
                } else {
                    ToastUtil.toast(fragment.getContext(),R.string.delete_file_fail);
                }
            }
        });
        dialog.show();
    }

    /**
     * 删除文件dialog
     * @param context 运行环境
     * @param path 需要删除的文件路径
     */
    public static void deleteFileDialog(final AbsCommonFragment context, final List<CFile> path) {
        final HintDialog dialog = new HintDialog(context.getContext());
        dialog.setInputOrHint(true);
        dialog.setHintText(getString(context.getContext(),R.string.can_not_delete_file));
        dialog.setIOkCallback(new HintDialog.IOkCallback() {
            @Override
            public void onOk(String text) {
                try {
                    for (CFile cFile:path) {
                        FileManager.instance().deleteFile(cFile.getPath());
                    }
                    context.refresh();
                    ToastUtil.toast(context.getContext(),R.string.delete_file_success);
                } catch (Exception e) {
                    ToastUtil.toast(context.getContext(),R.string.delete_file_fail);
                }

            }
        });
        dialog.show();
    }

    /**
     * 文件太长dialog
     * @param context 运行环境
     * @param callback 接口回掉
     */
    public static void fileTooLengthDialog(Context context, HintDialog.IOkCallback callback){
        final HintDialog dialog = new HintDialog(context);
        dialog.setInputOrHint(true);
        dialog.setTitle(R.string.copy_title);
        dialog.setHintText(getString(context,R.string.sure_copy_hint));
        dialog.setIOkCallback(callback);
        dialog.show();
    }

    /**
     * 重命名文件
     * @param fragment 运行环境
     * @param path 文件路径
     */
    public static void renameFileDialog(final AbsCommonFragment fragment, final String path) {
        final File renameFile = new File(path);
        final HintDialog dialog = new HintDialog(fragment.getContext());
        dialog.setInputHintText(renameFile.getName());
        dialog.setTitle(R.string.rename_file);
        dialog.setIOkCallback(new HintDialog.IOkCallback() {
            @Override
            public void onOk(String text) {
                if(!TextUtils.isEmpty(text)) {
                    if(renameFile.isFile()) {
                        text = text + "." + FileUtil.getSuffixName(renameFile.getName());
                    }
                    final File newFile = new File(renameFile.getParentFile(),text);
                    if(!newFile.exists()) {
                        if(renameFile.renameTo(newFile)) {
                            fragment.refresh();
                            ToastUtil.toast(fragment.getContext(),R.string.rename_success);
                        } else {
                            ToastUtil.toast(fragment.getContext(),R.string.rename_fail);
                        }
                    } else {
                        ToastUtil.toast(fragment.getContext(),R.string.rename_fail);
                    }
                } else {
                    ToastUtil.toast(fragment.getContext(),R.string.not_input_nothing);
                }
            }
        });
        dialog.show();
    }

    /**
     * 显示文件信息dialog
     * @param context 运行环境
     * @param path 文件路径
     */
    public static void showFileInfoDialog(Context context, final String path) {
        final File file = new File(path);
        final StringBuilder builder = new StringBuilder();
        final HintDialog dialog = new HintDialog(context);
        dialog.setInputOrHint(true);
        dialog.setTitle(file.getName());
        if(file.isFile()) {
            builder.append(getString(context, R.string.file_size) + Formatter.formatFileSize(context,file.length()) + "\n");
        }
        builder.append(getString(context, R.string.modify_time) + TimeUtil.getDateStr(file.lastModified())  + "\n");
        builder.append(getString(context, R.string.file_location) + file.getPath());
        dialog.setHintText(builder.toString());
        dialog.setCancelVisible(View.GONE);
        dialog.setHintTextGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        dialog.show();
    }

    /**
     * 获取字符串
     * @param context 运行环境
     * @param resId 资源Id
     * @return 字符串
     */
    private static String getString(Context context, int resId) {
        return context.getString(resId);
    }
}
