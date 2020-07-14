package com.filemanager.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能描述：该类处理了item获取了焦点后最后一个绘制
 *           解决被后面绘制的item遮挡
 * 开发状况：正在开发中
 */

public class TvRecyclerView extends RecyclerView {

    //保存被选中的item位置
    private int mSelectedPosition = 0;

    public TvRecyclerView(Context context) {
        this(context,null);
    }

    public TvRecyclerView(Context context,AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvRecyclerView(Context context,AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        mSelectedPosition = getChildAdapterPosition(getFocusedChild());
        super.onDraw(canvas);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int position) {
        int selectedPosition = mSelectedPosition;
        if (selectedPosition < 0) {
            return position;
        } else {
            if (position == childCount - 1) {
                if (selectedPosition > position) {
                    selectedPosition = position;
                }
                return selectedPosition;
            }
            if (position == selectedPosition) {
                return childCount - 1;
            }
        }
        return position;
    }
}
