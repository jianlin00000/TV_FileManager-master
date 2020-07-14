package com.common.widgets;

import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.common.R;

/**
 *@author: cjl
 *@date: 2020/6/17 16:59
 *@desc: 右侧弹出dialog基类
 */
public abstract class BaseRightDialog
        extends BaseDialogFragment {


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setGravity(Gravity.RIGHT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setWindowAnimations(R.style.anim_right);
    }
}
