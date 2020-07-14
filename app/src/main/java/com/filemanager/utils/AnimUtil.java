package com.filemanager.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.filemanager.R;

/**
 * 功能描述：动画工具类
 * 开发状况：正在开发中
 */

public class AnimUtil {

    private AnimUtil() {
    }

    /**
     * 开始动画
     * @param animView 需要动画的视图
     */
    public static void startAnim(View animView) {
        animView.clearAnimation();
        final Animation animation = AnimationUtils.loadAnimation(animView.getContext(), R.anim.anim_clean);
        animation.setInterpolator(new LinearInterpolator());
        animView.startAnimation(animation);
    }

    /**
     * 停止动画
     * @param animView 动画视图
     */
    public static void stopAnim(View animView) {
        if(animView != null) {
            animView.clearAnimation();
        }
    }
}
