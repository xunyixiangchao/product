package com.ichsy.libs.core.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;

import com.ichsy.core_library.R;
import com.ichsy.core_library.R.id;
import com.ichsy.core_library.R.layout;
import com.ichsy.libs.core.comm.bus.BusEventObserver;
import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.comm.exceptions.CrashHandler;
import com.ichsy.libs.core.comm.permission.PermissionManager;
import com.ichsy.libs.core.comm.utils.DeviceUtil;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.NavigationUtil;
import com.ichsy.libs.core.comm.view.navigation.NavigationController;
import com.ichsy.libs.core.comm.view.navigation.NavigationListener;
import com.ichsy.libs.core.frame.interfaces.FragmentInterface;

import java.util.List;

/**
 * 基础框架层父类
 *
 * @author LiuYuHang Date: 2015年5月12日
 *         <p/>
 *         Modifier： Modified Date： Modify：by xingchun
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.public_libs.frame
 * @File BaseFrameActivity.java
 */
public abstract class BaseFrameActivity extends FragmentActivity implements NavigationListener, FragmentInterface {
    public static final String EVENT_FINISH_ACTIVITY = "EVENT_FINISH_ACTIVITY";
    public static final String EVENT_PUSH_HOMEPAGE = "EVENT_PUSH_HOMEPAGE";

    public static final String BUNDLE_INTENT_SECOND_INTENT = "BUNDLE_INTENT_SECOND_INTENT";
    public static final String BUNDLE_INTENT_SECOND_SCHEME_URL = "BUNDLE_INTENT_SECOND_SCHEME_URL";

//    public static final String BUNDLE_INTENT_SECOND_ACTION_EVENT = "BUNDLE_INTENT_SECOND_ACTION_EVENT";
//    public static final String BUNDLE_INTENT_FIRST_ACTION_EVENT_RUN = "BUNDLE_INTENT_FIRST_ACTION_EVENT_RUN";

//    public static final String CACHE_ACTIVITY_DATA_SAVER = "CACHE_ACTIVITY_DATA_SAVER";

    // 导航栏
    public NavigationController navigationController;
    private Handler mMainHandler;
    private LayoutInflater mLayoutInflater;

    /**
     * 初始化布局，设置的view
     *
     * @return Modifier： Modified Date： Modify：
     */
    protected abstract int onLayoutInflate();

    protected FrameLayout mContentView;
    private boolean isActivityFinish = false;


//    private HashMap<String, Object> activitySelfDataMap;//私有存储的数据

//    /**
//     * 持久存储数据
//     *
//     * @param key
//     * @param value
//     * @param <Value>
//     */
//    public <Value extends Serializable> void put(@NonNull String key, @NonNull Value value) {
////        checkNotNull(key);
////        checkNotNull(value);
//        checkDataMap();
//        activitySelfDataMap.put(key, value);
//    }

//    /**
//     * 获取持久数据
//     *
//     * @param key
//     * @param <Value>
//     * @return
//     */
//    public <Value extends Serializable> Value get(@NonNull String key) {
////        checkNotNull(key);
//        checkDataMap();
//        return (Value) activitySelfDataMap.get(key);
//    }
//
//    public void checkDataMap() {
//        if (null == activitySelfDataMap) {
//            activitySelfDataMap = new HashMap<>();
//        }
//    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

//        if (null != savedInstanceState) {
//            checkDataMap();
//            LogUtils.i("frame", "savedInstanceState begin loading");
//            String cacheString = savedInstanceState.getString(CACHE_ACTIVITY_DATA_SAVER);
//            activitySelfDataMap = ObjectUtils.hex2Object(cacheString, HashMap.class);
//            LogUtils.i("frame", "savedInstanceState begin loading end");
//        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.setContentView(R.layout.frame_root_layout);
//        mLayoutTitle = (LinearLayout) findViewById(id.navigation_root_layout);
        int childId = onLayoutInflate();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("这里是Title");
//        toolbar.setSubtitle("这里是子标题");
//        toolbar.setLogo(R.drawable.icon);
//        setSupportActionBar(toolbar);

        // 侵入式状态栏
//		if (VERSION.SDK_INT >= 19) {
//			View navigationView = findViewById(id.navigation_root_layout);
//			// navigationView.setPadding(navigationView.getPaddingLeft(),
//			// DeviceUtil.getStatusHeight(this),
//			// navigationView.getPaddingRight(),
//			// navigationView.getPaddingBottom());
//			// 透明状态栏
//			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// // 透明导航栏
//			// //
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//			Window window = getWindow();
//			/*
//			 * window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//			 * , WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			 */
//			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//			int statusBarHeight = DeviceUtil.getStatusHeight(this);
//			navigationView.setPadding(0, statusBarHeight, 0, 0);
//		}

        mContentView = (FrameLayout) findViewById(id.view_root_layout);
//        mContentView.removeAllViews();

        if (childId > 0) {
            View childView = LayoutInflater.from(this).inflate(childId, null);
            mContentView.addView(childView);
        }

        // 初始化并绑定导航栏
        this.navigationController = initNavigationController(getWindow().getDecorView());

        BusManager.getInstance().register(EVENT_FINISH_ACTIVITY, activityEventObserver);
        BusManager.getInstance().register(EVENT_PUSH_HOMEPAGE, activityEventObserver);
    }

    /**
     * 获取传递过来的参数
     *
     * @param name
     * @return
     */
    public String getStringExtra(String name) {
        Intent intent = getIntent();
        String result;
        if (null == intent) {
            return null;
        } else if (!TextUtils.isEmpty(result = intent.getStringExtra(name))) {
            return result;
        } else if (!(intent.getIntExtra(name, -100) == -100)) {
            return intent.getIntExtra(name, -100) + "";
        } else {
            return result;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != activityEventObserver) {
            BusManager.getInstance().unRegister(EVENT_FINISH_ACTIVITY, activityEventObserver);
            BusManager.getInstance().unRegister(EVENT_PUSH_HOMEPAGE, activityEventObserver);
        }
        BusManager.getInstance().onActivityDestroy(this);

        isActivityFinish = true;
    }

    private BusEventObserver activityEventObserver = new BusEventObserver() {

        @Override
        public void onBusEvent(String event, Object message) {
            if (EVENT_FINISH_ACTIVITY.equals(event)) {
                if (null != message) {
                    String className = (String) message;
                    if (getActivity().getClass().getName().equals(className)) {
                        finish();
                    }
                }
            } else if (EVENT_PUSH_HOMEPAGE.equals(event)) {
                //当前页面的activity类名
                String currentActivityClassName = getActivity().getClass().getName();
                //主页面的类名
                String mainActivityClass = (String) message;

                if (!currentActivityClassName.equals(mainActivityClass)) {
                    finish();
                }
            }
        }
    };

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }

    /**
     * 初始化NavigationController
     *
     * @return 返回初始化的初始化NavigationController控制器
     */
    public NavigationController initNavigationController(View view) {
        final NavigationController navigationController = new NavigationController(this, this);
        navigationController.bindNavigation(view);

        View imageButton = LayoutInflater.from(this).inflate(R.layout.frame_navigation_title_left_button, null);
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                navigationController.popBack();
            }
        });
        View leftButton = imageButton.findViewById(R.id.view_navigation_left_button);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationController.popBack();
            }
        });

        navigationController.setLeftButton(imageButton);

        return navigationController;
    }


    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#setContentView(android.view.View)
     */
    @Override
    public void setContentView(int child) {
        // super.setContentView(view);
        super.setContentView(layout.frame_root_layout);
        mContentView.removeAllViews();
//        mContentView.addView(view);

        mContentView = (FrameLayout) findViewById(R.id.view_root_layout);
//        mContentView.removeAllViews();

        if (child > 0) {
            mContentView.addView(LayoutInflater.from(this).inflate(child, null));
        }
        // 初始化并绑定导航栏
        this.navigationController = initNavigationController(getWindow().getDecorView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        CrashHandler.instance().setCurrentActivityName(this);
    }

    public int getNavigationHeight() {
        return 0;
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (null == mLayoutInflater) {
            mLayoutInflater = super.getLayoutInflater();
        }
        return mLayoutInflater;
    }

    /**
     * 通过总线逻辑跳转页面
     *
     * @param intent
     */
    @Override
    public void pushActivity(Intent intent) {
//        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE);
//        UrlParser.getInstance().parser(this, uri, intent);
        NavigationUtil.navigationTo(this, intent);
    }

    /**
     * 通过总线逻辑跳转页面
     *
     * @param clz 跳转的activity类名
     */
    @Override
    public void pushActivity(Class clz) {
//        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE, clz.getName());
//        UrlParser.getInstance().parser(this, uri);
        NavigationUtil.navigationTo(this, clz);
    }

    /**
     * 通过总线逻辑跳转页面
     *
     * @param clz 跳转的activity类名
     */
    @Override
    public void pushActivity(Class clz, Intent intent) {
//        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE, clz.getName());
//        UrlParser.getInstance().parser(this, uri, intent);

        NavigationUtil.navigationTo(this, clz, intent);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ichsy.public_libs.view.navigation.NavigationListener#popBack()
     */
    @Override
    public void popBack() {
        boolean cost = onPressBackMenu();
        LogUtils.i("frame", "popBack cost is: " + cost + " activity is:" + getClass().getName());
        if (!cost) {
            onBackPressed();
        }
    }

    /**
     * 用户关闭页面的监听，返回true表示被消费，false表示未被消费
     *
     * @return
     */
    protected boolean onPressBackMenu() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && onPressBackMenu() || super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();

        //页面关闭的时候会默认隐藏键盘
        DeviceUtil.hidKeyBoard(getApplicationContext(), mContentView, true);
    }

    /*
         * (non-Javadoc)
         *
         * @see android.app.Activity#findViewById(int)
         */
    @Override
    public View findViewById(int id) {
        // return super.findViewById(id);
        if (mContentView == null) {
            return super.findViewById(id);
        } else {
            return mContentView.findViewById(id);
        }
    }

    /**
     * 替换布局为fragment，fragment必须为BaseFrameFragment的子类，在fragment中将可以控制父布局的部分控件，如导航条
     *
     * @param viewId   布局id
     * @param fragment
     */
    public Fragment replaceFragment(int viewId, Fragment fragment) {
        return replaceFragment(viewId, fragment, false);
    }

    public Fragment replaceFragment(int viewId, Fragment fragment, boolean selfNavigationController) {
        return replaceFragment(viewId, fragment, fragment.getClass().getSimpleName(), selfNavigationController);
    }

    public Fragment replaceFragment(@IdRes int viewId, @NonNull Fragment fragment, String fragmentTag, boolean selfNavigationController) {
        return replaceFragment(getSupportFragmentManager(), viewId, fragment, fragmentTag, selfNavigationController);
    }

    private final Object object = new Object();

    /**
     * 替换布局为fragment，fragment必须为BaseFrameFragment的子类，在fragment中将可以控制父布局的部分控件，如导航条
     *
     * @param fm
     * @param viewId                   布局id
     * @param fragment
     * @param fragmentTag              fragment的tag，用来查找fragment
     * @param selfNavigationController 是否使用自己的navigation
     */
    @Override
    public Fragment replaceFragment(FragmentManager fm, @IdRes int viewId, @NonNull Fragment fragment, String fragmentTag, boolean selfNavigationController) {
        synchronized (object) {
//        FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            List<Fragment> fragments = getFragments();

            if (null != fragments) {
                for (Fragment frag : fragments) {
                    if (null == frag) continue;
                    ft.hide(frag);
                }
            }

//        String fragmentTag = fragment.getClass().getSimpleName();
//        Fragment cache = fm.findFragmentByTag(fragmentTag);
            Fragment cache = findFragmentByTag(fm, fragmentTag);

//        boolean isNewAddFragment = false;

            if (null == cache) {
                if (fragment instanceof BaseFrameFragment) {
                    if (selfNavigationController) {
                        ((BaseFrameFragment) fragment).setNavigationController(null);
                    } else {
                        ((BaseFrameFragment) fragment).setNavigationController(navigationController);
                    }
//                ((BaseFrameFragment) fragment).onLazyOnResume();
                }
                fm.executePendingTransactions();
                if (fragment.isAdded()) {
                    ft.show(fragment);
                } else {
//                isNewAddFragment = true;
                    ft.add(viewId, fragment, fragmentTag);
                }
            } else {
                fragment = cache;
                ft.show(fragment);
            }
            ft.commitAllowingStateLoss();

            if (null != cache) {
                if (fragment instanceof BaseFrameFragment) {
                    BaseFrameFragment cacheFragment = ((BaseFrameFragment) cache);
                    if (null == cacheFragment.navigationController) {
                        if (null != cacheFragment.getActivity()) {
                            String className = cacheFragment.getActivity().getClass().getName();
                            onActivityError("关闭页面 " + className + " 原因：null == cacheFragment.navigationController");
                            BusManager.getInstance().postEvent(EVENT_FINISH_ACTIVITY, className);
                        }
                    } else {
//                    if (isNewAddFragment) {
                        cacheFragment.onLazyOnResume(this);
//                    }
                    }
                }
            }
            return fragment;
        }
    }

    @Override
    public Fragment findFragmentByTag(FragmentManager fm, String fragmentTag) {
        List<Fragment> fragments = getFragments();
        if (null != fragments) {
            for (Fragment fragment : fragments) {
                if (null == fragment) continue;
                if (fragmentTag.equals(fragment.getTag())) {
                    return fragment;
                }
            }
        }
        return fm.findFragmentByTag(fragmentTag);
    }

    @Override
    public List<Fragment> getFragments() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.isDestroyed()) {
            return null;
        }
        return fm.getFragments();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getFragments();

        if (null != fragments) {
            for (Fragment frag : fragments) {
                if (null == frag) continue;
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }

    }


    protected void onActivityError(String message) {

    }

//    /**
//     * 检查是否可以进行下一次跳转
//     *
//     * @return
//     */
//    private Intent getNextIntent() {
//        Bundle intentExt = getIntent().getExtras();
//        if (null != intentExt) {
//            return intentExt.getParcelable(BUNDLE_INTENT_SECOND_INTENT);
//        }
//        return null;
//    }

    /**
     * 检测并跳转到下个intent(如果有)
     */
    protected void pushNextIntent() {
        Bundle intentExt = getIntent().getExtras();
        if (null != intentExt) {
//            intentExt.getParcelable(BUNDLE_INTENT_SECOND_INTENT);
            //执行登陆之后的后续逻辑
            Intent parcelableIntent = intentExt.getParcelable(BUNDLE_INTENT_SECOND_INTENT);
            if (null != parcelableIntent) {
                pushActivity(parcelableIntent);
            } else {
                String schemeUrl = intentExt.getString(BUNDLE_INTENT_SECOND_SCHEME_URL);
                UrlParser.getInstance().parser(this, schemeUrl);
            }

//            String actionEvent = intentExt.getString(BUNDLE_INTENT_SECOND_ACTION_EVENT);
//            if (ObjectUtils.isNotNull(actionEvent)) {
//                parcelableIntent.putExtra(BUNDLE_INTENT_FIRST_ACTION_EVENT_RUN, parcelableIntent);
//                setResult(Activity.RESULT_OK, parcelableIntent);
//            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (null != getFragments()) {
            for (Fragment fragment : getFragments()) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        PermissionManager.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    public boolean isActivityFinish() {
        return isActivityFinish;
    }

    /**
     * 获取主线程的handler
     *
     * @return
     */
    public Handler getMainHandler() {
        if (null == mMainHandler) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        return mMainHandler;
    }

}
