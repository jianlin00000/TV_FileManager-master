package com.filemanager.adapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.filemanager.R;
import com.filemanager.bean.CFile;

/**
 * 功能描述：文件路径选择适配器
 * 开发状况：正在开发中
 */

public class FolderAdapter extends AbsBaseAdapter<CFile,FolderAdapter.FolderViewHolder>{

    public FolderAdapter(RecyclerView context) {
        super(context, R.layout.listview_select_item);
    }

    @Override
    protected FolderViewHolder onCreateViewHolder(View view, int viewType) {
        return new FolderViewHolder(view);
    }

    @Override
    protected void onBindViewData(FolderViewHolder holder, CFile javaBean, int position) {
        holder.name.setText(javaBean.getName());
        holder.setDrawableLeft(javaBean.getIcon());
    }

    @Override
    protected void focusStatusHandle(FolderViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(getContext().getResources().getColor(R.color.color5));
    }

    @Override
    protected void loseFocusStatusHandle(FolderViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    //视图支持器
    public class FolderViewHolder extends RecyclerView.ViewHolder {
        //文件夹名
        TextView name;
        public FolderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_folder_name);
        }

        /**
         * 设置文件左边的图标
         * @param resId 资源Id
         */
        public void setDrawableLeft(int resId) {
            name.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
        }
    }
}
