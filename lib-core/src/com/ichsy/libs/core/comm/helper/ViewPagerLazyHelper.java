package com.ichsy.libs.core.comm.helper;

import android.support.v4.app.Fragment;

import com.ichsy.libs.core.comm.utils.LogUtils;

/**
 * 协助viewpager实现懒加载的方法
 * Created by liuyuhang on 2017/5/24.
 */

public class ViewPagerLazyHelper {
    /**
     * 懒加载
     */
    private boolean isFragmentFirstVisible = true;
    private boolean isVisible;
    private boolean isCreate = false;

    private ViewPagerFragmentLazyLife fragmentLazyHandler;

//    private static HashMap<String, ViewPagerLazyHelper> viewPagerLazyHelperHashMap;

    /**
     * viewpager中懒加载的生命周期
     */
    public interface ViewPagerFragmentLazyLife {

        /**
         * 当前页面onCreate，相当于Activity的onCreate
         */
        void lazyCreate();

        /**
         * 当前页面onResume，相当于Activity的onResume
         */
        void lazyResume();
    }

//    public static ViewPagerLazyHelper getInstance(String key) {
////        String key = fragment.toString();
//        ViewPagerLazyHelper lazyHelper;
//
//        if (null == viewPagerLazyHelperHashMap) {
//            viewPagerLazyHelperHashMap = new HashMap<>();
//            lazyHelper = new ViewPagerLazyHelper();
//            viewPagerLazyHelperHashMap.put(key, lazyHelper);
//        } else {
//            lazyHelper = viewPagerLazyHelperHashMap.get(key);
//
//            if (null == lazyHelper) {
//                lazyHelper = new ViewPagerLazyHelper();
//                viewPagerLazyHelperHashMap.put(key, lazyHelper);
//            }
//        }
//        return lazyHelper;
//    }

    public ViewPagerLazyHelper() {
    }

    public void onCreate(Fragment fragment, ViewPagerFragmentLazyLife fragmentLazyHandler) {
        this.fragmentLazyHandler = fragmentLazyHandler;
        if (isFragmentFirstVisible && isVisible) {
            onLazyInit();
        }
        isFragmentFirstVisible = false;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            LogUtils.v("fragment", "setUserVisibleHint:" + isFragmentFirstVisible);
            if (!isFragmentFirstVisible) {
                onLazyInit();
            }
        }
    }

    /**
     * 懒加载
     */
    private void onLazyInit() {
        if (!isCreate) {
            fragmentLazyHandler.lazyCreate();
            isCreate = true;
        }
        fragmentLazyHandler.lazyResume();
    }
}
