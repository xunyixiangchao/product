package com.ichsy.libs.core.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.ichsy.core_library.R.id;
import com.ichsy.core_library.R.layout;
import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.comm.utils.DeviceUtil;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.comm.view.dialog.DialogHUB;
import com.ichsy.libs.core.comm.view.navigation.NavigationController;
import com.ichsy.libs.core.comm.view.navigation.NavigationListener;

import java.util.List;

/**
 * @author LiuYuHang Date: 2015年5月12日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.public_libs.frame
 * @File BaseFrameFragment.java
 */
public abstract class BaseFrameFragment extends Fragment implements NavigationListener {
    private final String BUNDLE_IS_FRAGMENT_RECOVERED = "BUNDLE_IS_FRAGMENT_RECOVERED";//这个fragment是否是从内存中恢复的
    private final String BUNDLE_HAS_SELF_NAVIGATION = "BUNDLE_HAS_SELF_NAVIGATION";//这个fragment是否是从内存中恢复的

    private String title;
    // 导航栏
    protected NavigationController navigationController;

    protected boolean isSelfNavigationController;///是否是自己的导航栏


//    //懒加载
//    private boolean isFragmentFirstVisible = true;
//    private boolean isVisible;
//    private boolean isCreate = false;

    // 用作统计的code
//	private String mPageCode;

    protected View rootView;
    private FrameLayout mContentView;

    private DialogHUB fragmentDialogHUB;
    private boolean isFragmentCreate = false;

    /**
     * 初始化布局，设置的viewID
     *
     * @return 返回layout文件的id Modifier： Modified Date： Modify：
     */
    protected abstract int onLayoutInflate();

    /**
     * fragment的懒加载生命周期，进入当前fragment才会调用，类似activity的 {@link Activity#onCreate(Bundle)}
     *
     * @param view
     * @author LiuYuHang
     * @date 2015年1月15日
     */
    protected abstract void onLazyCreate(View view);

    /**
     * fragment的懒加载生命周期，进入当前fragment才会调用，类似activity的 {@link Activity#onResume()}
     *
     * @author LiuYuHang
     * @date 2015年1月15日
     */
    public abstract void onLazyOnResume(Context context);


//    protected abstract void onLazyResume();

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        LogUtils.v("fragment", "onViewCreated");
//        if (isFragmentFirstVisible && isVisible) {
//            onLazyInit();
//        }
//        isFragmentFirstVisible = false;

//        if (!isFragmentCreate()) {
//            setFragmentCreate(true);
        onLazyCreate(view);
//        }

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        LogUtils.v("fragment", "setUserVisibleHint");
//        super.setUserVisibleHint(isVisibleToUser);
//        this.isVisible = isVisibleToUser;
//        if (isVisibleToUser) {
//            LogUtils.v("fragment", "setUserVisibleHint:" + isFragmentFirstVisible);
//            if (!isFragmentFirstVisible) {
//                onLazyInit();
//            }
//        }
//    }

//    private void onLazyInit() {
//        if (!isCreate) {
//            onLazyCreate(view);
//            isCreate = true;
//        }
//        onLazyResume();
//    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {
//            if (!isCreate) {
//                onLazyCreate();
//            }
//            onLazyResume();
//        }
//    }

    /**
     * 用户点击返回键的处理事件
     *
     * @return 返回按钮是否已经被消费，true为消费，false为没有消费
     */
    public boolean onPressBackMenu() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#findViewById(int)
     */
    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(layout.frame_root_layout, null, false);

//		mPageCode = initPageCode();
        mContentView = (FrameLayout) rootView.findViewById(id.view_root_layout);

//        mContentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 400));

//        mContentView.setBackgroundColor(Color.RED);

        mContentView.removeAllViews();

        int childId = onLayoutInflate();
        if (childId != 0) {
            View childView;
//			if (this instanceof PluginBaseFragment) {// 判断当前是不是插件fragment
//				childView = MyResources.getResource(getClass()).inflate(getActivity(), childId, container, false);
//			} else {
            childView = View.inflate(getActivity(), childId, null);
//			}
            mContentView.addView(childView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }


        //如果自己是重新恢复，并且没有自己的navigation，才需要走navigation的逻辑判断
        boolean isFragmentRecover = false;
        boolean isHasSelfNavigationController = false;

        if (null != savedInstanceState) {
            isFragmentRecover = savedInstanceState.getBoolean(BUNDLE_IS_FRAGMENT_RECOVERED, false);
            isHasSelfNavigationController = savedInstanceState.getBoolean(BUNDLE_HAS_SELF_NAVIGATION, false);
        }

        if (!isFragmentRecover || isHasSelfNavigationController || null == navigationController) {
            //如果没有给他设置过NavigationController，初始化一个新的，因为有可能每个fragment都有自己的导航栏
            if (navigationController == null) {
                isSelfNavigationController = true;
                BaseFrameActivity activity;
                if (null != (activity = (BaseFrameActivity) getActivity())) {
                    activity.navigationController.hideNavigation(true);
//                // 初始化并绑定导航栏
                    navigationController = activity.initNavigationController(rootView);
                }
            } else {
                isSelfNavigationController = false;
                ViewUtil.setViewVisibility(View.GONE, rootView.findViewById(id.navigation_root_layout));
            }
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //保存fragment状态
        outState.putSerializable(BUNDLE_IS_FRAGMENT_RECOVERED, true);
        outState.putSerializable(BUNDLE_HAS_SELF_NAVIGATION, isSelfNavigationController);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        onLazyOnResume(this);

        Activity activity = getActivity();
        if (null != activity) {
            onLazyOnResume(activity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BusManager.getInstance().onActivityDestroy(this);
    }

    //    public Fragment replaceFragment(int viewId, Fragment fragment, boolean selfNavigationController) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        if (fragment instanceof BaseFrameFragment && !selfNavigationController) {
//            ((BaseFrameFragment) fragment).setNavigationController(navigationController);
//        }
//        transaction.replace(viewId, fragment, fragment.getClass().getSimpleName());
//        transaction.commitAllowingStateLoss();
//        return fragment;
//    }

    /**
     * 替换布局为fragment，fragment必须为BaseFrameFragment的子类，在fragment中将可以控制父布局的部分控件，如导航条
     *
     * @param viewId                   布局id
     * @param fragment
     * @param selfNavigationController 是否使用自己的navigation
     */
//    public synchronized Fragment replaceFragment(int viewId, Fragment fragment, boolean selfNavigationController) {
//        BaseFrameActivity activity = (BaseFrameActivity) getActivity();
//        return activity.replaceFragment(activity.getSupportFragmentManager(), viewId, fragment, fragment.getClass().getSimpleName(), selfNavigationController);
//    }
    public synchronized Fragment replaceFragment(int viewId, Fragment fragment, boolean selfNavigationController) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        List<Fragment> fragments = fm.getFragments();
        if (null != fragments) {
            for (Fragment frag : fragments) {
                if (null == frag) {
                    continue;
                }
                ft.hide(frag);
            }
        }

        String fragmentTag = fragment.getClass().getSimpleName();
        Fragment cache = fm.findFragmentByTag(fragmentTag);

        if (null == cache) {
            if (fragment instanceof BaseFrameFragment) {
                if (selfNavigationController) {
                    ((BaseFrameFragment) fragment).setNavigationController(null);
                } else {
                    ((BaseFrameFragment) fragment).setNavigationController(navigationController);
                }
//                ((BaseFrameFragment) fragment).onLazyOnResume();
            }

            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(viewId, fragment, fragmentTag);
            }
        } else {
            fragment = cache;
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();

//        if (null != cache) {
//            if (fragment instanceof BaseFrameFragment && null != getActivity()) {
//                ((BaseFrameFragment) cache).onLazyOnResume(getActivity());
//            }
//        }
        return fragment;
    }

    /**
     * 获取fragment的DialogHub，因为布局的原因，fragment要有自己的dialog
     *
     * @return
     */
    public DialogHUB getDialogHUB() {
        if (null == fragmentDialogHUB) {
            fragmentDialogHUB = new DialogHUB();
            fragmentDialogHUB.bindDialog(getActivity(), rootView);
        }
        return fragmentDialogHUB;
    }


    /**
     * 设置fragment的导航栏
     *
     * @param controller
     */
    public void setNavigationController(NavigationController controller) {
        this.navigationController = controller;
    }

    public NavigationController getNavigationController() {
        return this.navigationController;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ichsy.public_libs.view.navigation.NavigationListener#popBack()
     */
    @Override
    public void popBack() {
        if (null != getActivity()) {
            DeviceUtil.hidKeyBoard(getActivity(), mContentView, true);
            getActivity().finish();
        }

    }

    /**
     * 通过总线逻辑跳转页面
     *
     * @param intent
     */
    @Override
    public void pushActivity(Intent intent) {
        if (null == getActivity()) return;
        ((BaseFrameActivity) getActivity()).pushActivity(intent);
    }

    /**
     * 通过总线逻辑跳转页面
     *
     * @param clz 跳转的activity类名
     */
    @Override
    public void pushActivity(Class clz) {
        if (null == getActivity()) return;
        ((BaseFrameActivity) getActivity()).pushActivity(clz);
    }

    /**
     * 通过总线逻辑跳转页面
     *
     * @param clz 跳转的activity类名
     */
    @Override
    public void pushActivity(Class clz, Intent intent) {
        if (null == getActivity()) return;
        ((BaseFrameActivity) getActivity()).pushActivity(clz, intent);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFragmentCreate() {
        return isFragmentCreate;
    }

    public void setFragmentCreate(boolean fragmentCreate) {
        isFragmentCreate = fragmentCreate;
    }
}
