package com.filemanager.other;

import com.filemanager.adapter.AbsBaseAdapter;
import com.filemanager.bean.Sort;
import com.filemanager.utils.ProgressUtil;

import java.util.Collections;

/**
 * 功能描述：排序异步任务
 * 开发状况：正在开发中
 */

public class SortTask extends AbsAsyncTask {

    //选择的排序内容
    private Sort mSort;
    //保存适配器
    private AbsBaseAdapter mAdapter;

    public SortTask(AbsBaseAdapter adapter, Sort sort) {
        mSort = sort;
        mAdapter = adapter;
        ProgressUtil.showDialog(adapter.getContext(), "排序中……",true,null);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Collections.sort(mAdapter.getData(), new SortComparator(mSort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        ProgressUtil.dismissDialog();
        mAdapter.notifyDataSetChanged();
    }
}
