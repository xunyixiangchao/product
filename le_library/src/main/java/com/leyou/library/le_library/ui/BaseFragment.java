package com.leyou.library.le_library.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.comm.view.dialog.DialogHUB;
import com.ichsy.libs.core.comm.view.dialog.DialogUiBuilder;
import com.ichsy.libs.core.comm.view.navigation.NavigationController;
import com.ichsy.libs.core.frame.BaseFrameFragment;
import com.leyou.library.le_library.comm.collection.AppTrackHelper;
import com.leyou.library.le_library.comm.collection.AppTrackInterface;
import com.leyou.library.le_library.comm.handler.UserLoginStatusHandler;
import com.leyou.library.le_library.comm.handler.UserLogoutStatusHandler;
import com.leyou.library.le_library.comm.helper.AppWatcher;
import com.leyou.library.le_library.comm.helper.FrameUiHelper;
import com.leyou.library.le_library.comm.helper.LeNavigationTitleHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import library.liuyh.com.lelibrary.R;

/**
 * base fragment
 *
 * @author liuyuhang
 * @date 16/4/26
 */
public abstract class BaseFragment extends BaseFrameFragment implements UserLoginStatusHandler, UserLogoutStatusHandler, AppTrackInterface
//        , LifecycleRegistryOwner
{
    private Unbinder butterKnifeUnbinder;

//    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
//
//    @Override
//    public LifecycleRegistry getLifecycle() {
//        return lifecycleRegistry;
//    }

    @Override
    protected void onLazyCreate(View view) {
//        getDialogHUB().setDialogBackground(Color.WHITE);

        butterKnifeUnbinder = ButterKnife.bind(this, view);

        navigationController.setBackgroundColor(getResources().getColor(R.color.le_color_white));
        navigationController.setDividerResource(R.color.le_color_line);
//        rootView.setBackgroundColor(getResources().getColor(R.color.le_bg_white));
        rootView.setBackgroundResource(R.color.le_base_background);

//        getDialogHUB().setProgressUiBuilder(new DialogUiBuilder() {
//
//
//            @Override
//            public View onViewCreate(LayoutInflater inflater) {
//                return inflater.inflate(R.layout.activity_loading_animation_layout, null);
//            }
//
//            @Override
//            public void onViewDraw(View view, String message) {
//                View loadingView = view.findViewById(R.id.view_loading);
//                ((AnimationDrawable) loadingView.getBackground()).start();
//            }
//        });

        DialogUiBuilder dialogUiBuilder = FrameUiHelper.INSTANCE.getInstance().getDialogUiBuilder(getActivity());
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
                    iconView.setBackgroundResource(resId);
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

        navigationController.setNavigationSettingCallback(new NavigationController.NavigationSettingCallback() {
            @Override
            public void onNavigationTitleChanged(Context context, String title) {
                LeNavigationTitleHelper.INSTANCE.putTitle(context, title);
//                LogUtils.i("onResume", "setNavigationSettingCallback: " + title);

//                AppTrack_2602.onBasePageViewEvent(title);
            }
        });

        if (isSelfNavigationController) {
            getDialogHUB().setMarginTopAndBottom(getNavigationHeight(), 0);
        }

    }

    public void finishWebView() {

    }

    /**
     * 返回错误页面的ErrorImage
     *
     * @return
     */
    protected
    @DrawableRes
    int onSetErrorViewImageRes() {
        return R.drawable.public_empty_pic;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnifeUnbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(getCustomPageTopic())) {
            AppTrackHelper.INSTANCE.onAppPageViewEvent(getActivity(), getCustomPageTopic());
        }
        LogUtils.i("frame", getClass().getName() + "onResume");
    }

    @Override
    public void onLazyOnResume(Context context) {
//        BuglyLog.i("onResume", "====Fragment=====" + this.getClass().getSimpleName());
        AppWatcher.getInstance().tagClass("进入", this);

        LogUtils.i("frame", getClass().getName() + "onLazyOnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        BuglyLog.i("onPause", "====Fragment=====" + this.getClass().getSimpleName());
        AppWatcher.getInstance().tagClass("离开", this);
    }

    public int getNavigationHeight() {
        if (ObjectUtils.isNull(getActivity())) return 0;
        return ViewUtil.dip2px(getActivity(), 42) + 1;
    }

    @Override
    public String getCustomPageTopic() {
        return null;
    }

    @Override
    public boolean isCustomPageTopicTrack() {
        return true;
    }

    @Override
    public DialogHUB getDialogHUB() {
        DialogHUB dialogHUB = super.getDialogHUB();
        dialogHUB.setDialogBackground(Color.WHITE);
        return dialogHUB;
    }

    @Override
    public void onUserLogin() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (ObjectUtils.isNotNull(fragments)) {
            for (Fragment fragment : fragments) {
                if (ObjectUtils.isNull(fragment)) continue;

                if (fragment instanceof UserLoginStatusHandler) {
                    ((UserLoginStatusHandler) fragment).onUserLogin();
                }
            }
        }
    }

    @Override
    public void onUserLogout() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
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
}
