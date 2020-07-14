package com.common.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.common.R;
import com.common.utils.DensityUtil;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment
        extends DialogFragment {

    protected static final String TAG = "AbsDialogFragment";

    protected View    mRootView;
    protected Context mContext;
    private   int     width=WindowManager.LayoutParams.WRAP_CONTENT;//宽度
    private   int     height=WindowManager.LayoutParams.WRAP_CONTENT;//高度
    public    int     gravity= Gravity.CENTER;
    private Unbinder mUnbind;

    protected abstract int bindContentView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        init();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseNoTitleDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(bindContentView(), container);
        mUnbind = ButterKnife.bind(this,mRootView);
        initView(mRootView);
        return mRootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        //处理默认窗口属性
        handleWindow(window);
        //设置属性信息宽高或者动画
        WindowManager.LayoutParams wlp = window.getAttributes();
        handleLayoutParams(wlp);
        window.setAttributes(wlp);

    }

    protected void init() {}


    protected void initView(View rootView) {}

    protected void initData() {}

    protected void initListener() {}
    /**
     * 用于处理窗口的属性
     *
     * @param window
     */
    protected void handleWindow(Window window) {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //避免,重复加载报错
        manager.executePendingTransactions();
        //提交
        if (manager.findFragmentByTag(tag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mUnbind!=null) mUnbind.unbind();
    }

    /**
     * 处理默认的宽和高
     * 和动画效果
     *
     * @param wl
     */
    protected void handleLayoutParams(WindowManager.LayoutParams wl) {
        wl.width=width;
        wl.height=height;
        wl.gravity = gravity;
    }

    public BaseDialogFragment setWithRatio(@FloatRange(from = 0.0, to = 1.0) float widthRatio){
        int screenWidth = DensityUtil.getScreenWidth(getContext());
        float w = screenWidth * widthRatio;
        this.width = (int) w;
        return this;
    }

    public BaseDialogFragment setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseDialogFragment setHeight(int height) {
        this.height = height;
        return this;
    }

    public BaseDialogFragment setFullScreen(){
        setHeight(WindowManager.LayoutParams.MATCH_PARENT).setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        return this;
    }

    public BaseDialogFragment setGravity(int gravity){
        this.gravity=gravity;
        return this;
    }
}
