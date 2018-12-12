package com.ichsy.libs.core.frame.interfaces;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * fragment相关的接口
 * Created by liuyuhang on 2016/12/29.
 */

public interface FragmentInterface {

    /**
     * 获取当前页面包含的所有fragment
     *
     * @return
     */
    List<Fragment> getFragments();

    /**
     * 根据fragment的tag，获取他的fragment
     *
     * @param fm
     * @param fragmentTag
     * @return
     */
    Fragment findFragmentByTag(FragmentManager fm, String fragmentTag);

    /**
     * 替换布局为fragment，fragment必须为BaseFrameFragment的子类，在fragment中将可以控制父布局的部分控件，如导航条
     *
     * @param fm
     * @param viewId                   布局id
     * @param fragment
     * @param fragmentTag              fragment的tag，用来查找fragment
     * @param selfNavigationController 是否使用自己的navigation
     */
    Fragment replaceFragment(FragmentManager fm, @IdRes int viewId, @NonNull Fragment fragment, String fragmentTag, boolean selfNavigationController);
}
