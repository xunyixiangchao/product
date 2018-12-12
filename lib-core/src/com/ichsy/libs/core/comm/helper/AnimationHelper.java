package com.ichsy.libs.core.comm.helper;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 动画工具类
 * Created by liuyuhang on 2017/10/31.
 */

public class AnimationHelper {

    /**
     * 旋转view
     *
     * @param animView
     */

    public static ObjectAnimator rotate(View animView, long duration) {
        return rotate(animView, duration, 359f, -1);
    }

    public static ObjectAnimator rotate(View animView, long duration, int repeat) {
       return rotate(animView, duration, 359f, repeat);
    }

    public static ObjectAnimator rotate(View animView, long duration, float angle, int repeat) {
//        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(animView, "rotation", 0f, angle);
        scaleX.setInterpolator(new LinearInterpolator());
        scaleX.setDuration(duration);
        scaleX.setRepeatCount(repeat);
        return scaleX;
    }
}
