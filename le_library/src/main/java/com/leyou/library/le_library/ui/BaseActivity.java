package com.leyou.library.le_library.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichsy.libs.core.comm.bus.BusEventObserver;
import com.ichsy.libs.core.comm.bus.BusManager;
import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.comm.view.dialog.DialogUiBuilder;
import com.leyou.library.le_library.comm.handler.UserLoginStatusHandler;
import com.leyou.library.le_library.comm.handler.UserLogoutStatusHandler;
import com.leyou.library.le_library.comm.helper.DebugHelper;
import com.leyou.library.le_library.comm.helper.FrameUiHelper;
import com.leyou.library.le_library.config.EventKeys;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import library.liuyh.com.lelibrary.R;

/**
 * 乐友activity的父类
 *
 * @author liuyuhang
 * @date 16/4/26
 */
public abstract class BaseActivity extends BaseSystemActivity implements UserLoginStatusHandler, UserLogoutStatusHandler, BusEventObserver
//        , LifecycleRegistryOwner
{

//    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
//
//    @Override
//    public LifecycleRegistry getLifecycle() {
//        return lifecycleRegistry;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        LogUtils.i("url_parser_debug", "parser url is: leyou://native?native=" + getClass().getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.le_color_white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

//        String intentTitle = get

//        if (enableSlider()) {
//            SlidrConfig config = new SlidrConfig.Builder()
//                    .primaryColor(getResources().getColor(R.color.colorPrimary))
//                    .sensitivity(1f)
//                    .scrimColor(Color.BLACK)
//                    .scrimStartAlpha(0.8f)
//                    .scrimEndAlpha(0f)
//                    .velocityThreshold(2400)
//                    .distanceThreshold(0.25f)
//                    .edge(true | false)
//                    .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
////                                .listener(new SlidrListener(){...})
//                    .build();
//            Slidr.attach(this, config);
//        }

//        mainHandler = new Handler(Looper.getMainLooper());

        getDialogHUB().setDialogBackground(Color.WHITE);
        getDialogHUB().setMarginTopAndBottom(getNavigationHeight(), 0);

        navigationController.setBackgroundColor(getResources().getColor(R.color.le_color_white));
        navigationController.setDividerResource(R.color.le_color_line);
//        mContentView.setBackgroundColor(getResources().getColor(R.color.le_bg_white));
        mContentView.setBackgroundResource(R.color.le_base_background);
//        mContentView.setBackgroundResource(R.color.le_color_background);
//        mContentView.setBackgroundResource(R.color.background_material_light);

//        getDialogHUB().setMarginTopAndBottom(0, (int) getResources().getDimension(R.dimen.navigation_bottom_height));

        DialogUiBuilder dialogUiBuilder = FrameUiHelper.INSTANCE.getInstance().getDialogUiBuilder(getContext());
        getDialogHUB().setProgressUiBuilder(dialogUiBuilder);

        getDialogHUB().setMessageViewUiBuilder(new DialogUiBuilder() {

            @Override
            public View onViewCreate(@NonNull LayoutInflater inflater) {
                return inflater.inflate(R.layout.activity_empty_view_layout, null);
            }

            @Override
            public void onViewDraw(@NonNull View view, String message) {
                ImageView iconView = (ImageView) view.findViewById(R.id.imageview_icon);
                TextView messageView = (TextView) view.findViewById(R.id.textview_message);

                int resId = onSetErrorViewImageRes();

                if (resId != 0) {
                    iconView.setImageResource(resId);
                }
                messageView.setText(message);
            }
        });

        getDialogHUB().setNetErrorUiBuilder(new DialogUiBuilder() {
            @Override
            public View onViewCreate(@NonNull LayoutInflater inflater) {
                return inflater.inflate(R.layout.view_net_bad_layout, null);
            }

            @Override
            public void onViewDraw(@NonNull View view, String message) {
            }

        });

        BusManager.getInstance().register(EventKeys.EVENT_LOGIN_ON_LOG, observer);
        BusManager.getInstance().register(EventKeys.EVENT_LOGIN_OUT_LOG, observer);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            DebugHelper.Instance.getDebugHelper().tryOpen(this, "leyou://native?native=com.capelabs.leyou.ui.activity.debug.DebugActivity");
            DebugHelper.Instance.getDebugHelper().tryOpen(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    UrlParser.getInstance().parser(getActivity(), "leyou://native?native=com.capelabs.leyou.ui.activity.debug.DebugActivity");
                    return null;
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回错误页面的ErrorImage
     *
     * @return
     */
    protected int onSetErrorViewImageRes() {
        return R.drawable.public_empty_pic;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BusManager.getInstance().unRegister(EventKeys.EVENT_LOGIN_ON_LOG, observer);
        BusManager.getInstance().unRegister(EventKeys.EVENT_LOGIN_OUT_LOG, observer);
    }

    public void pushActivity(String className) {
        UrlParser.getInstance().parser(getContext(), "leyou://native?native=" + className);
    }

    /**
     * 获取顶部导航栏的高度
     *
     * @return
     */
    @Override
    public int getNavigationHeight() {
        return ViewUtil.dip2px(this, 42) + 1;
    }

    /**
     * 重写此方法可以启用或者禁用滑动关闭
     *
     * @return
     */
    @Deprecated
    public boolean enableSlider() {
        return true;
    }

    private FrameEventObserver observer = new FrameEventObserver();

    private class FrameEventObserver implements BusEventObserver {

        /**
         * 当收到event事件，会执行此函数
         *
         * @param event   event事件
         * @param message 事件所带的参数
         */
        @Override
        public void onBusEvent(String event, Object message) {
            if (event.equals(EventKeys.EVENT_LOGIN_ON_LOG)) {
                if (!isActivityFinish()) {
                    if ((boolean) message) {
                        onUserLogin();
                    }
                }
            } else if (event.equals(EventKeys.EVENT_LOGIN_OUT_LOG)) {
                if (!isActivityFinish()) {
                    if (!(boolean) message) {
                        onUserLogout();
                    }
                }
            }
        }
    }

    @Override
    public void onBusEvent(String event, Object message) {

    }

    @Override
    public void onUserLogin() {
        List<Fragment> fragments = getFragments();
        if (ObjectUtils.isNotNull(fragments)) {
            for (Fragment fragment : fragments) {
                if (ObjectUtils.isNull(fragment)) {
                    continue;
                }
                if (fragment instanceof UserLoginStatusHandler) {
                    ((UserLoginStatusHandler) fragment).onUserLogin();
                }
            }
        }
    }

    @Override
    public void onUserLogout() {
        List<Fragment> fragments = getFragments();
        if (ObjectUtils.isNotNull(fragments)) {
            for (Fragment fragment : fragments) {
                if (ObjectUtils.isNull(fragment)) {
                    continue;
                }
                if (fragment instanceof UserLogoutStatusHandler) {
                    ((UserLogoutStatusHandler) fragment).onUserLogout();
                }
            }
        }
    }

    public void finishWebView() {

    }

}
