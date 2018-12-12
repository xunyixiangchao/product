package com.leyou.library.le_library.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.bus.url.filter.IntentFilter;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.view.navigation.NavigationController;
import com.ichsy.libs.core.frame.BaseDialogFrameActivity;
import com.ixintui.pushsdk.PushSdkApi;
import com.leyou.library.le_library.comm.collection.AppTrackHelper;
import com.leyou.library.le_library.comm.collection.AppTrackInterface;
import com.leyou.library.le_library.comm.helper.AppWatcher;
import com.leyou.library.le_library.comm.helper.LeNavigationTitleHelper;
import com.sensorsdata.analytics.android.sdk.ScreenAutoTracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 处理系统级业务，例如埋点统计，bugTags等
 * <p>
 * Created by liuyuhang on 2017/3/9.
 */

public abstract class BaseSystemActivity extends BaseDialogFrameActivity implements AppTrackInterface, ScreenAutoTracker {
//    private String mTitle = "";
//
//    public void setTrackTitle(String title) {
//        mTitle = title;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("frame", getClass().getSimpleName() + " onCreate...");
        LogUtils.i("BaseSystemActivity", this.getClass().getName() + " onCrate\nfrom activity: " +
                getIntent().getSerializableExtra(IntentFilter.INTENT_BUNDLE_FROM_ACTIVITY));

        navigationController.setNavigationSettingCallback(new NavigationController.NavigationSettingCallback() {
            private boolean isCreate;

            @Override
            public void onNavigationTitleChanged(Context context, String title) {
                LeNavigationTitleHelper.INSTANCE.putTitle(context, title);
                if (isCreate) {
                    return;
                }
                String customPageTopic = getCustomPageTopic();
                if (customPageTopic == null || customPageTopic.equals("")) {
                    isCreate = true;
                    AppTrackHelper.INSTANCE.onAppPageViewEvent(context, title);
                    return;
                }
                if (!isCustomPageTopicTrack()) {
                    isCreate = true;
                    AppTrackHelper.INSTANCE.onAppPageViewEvent(context, title);
                }
            }
        });
        AppTrackHelper.INSTANCE.onBasePageViewEvent(this, this);
    }

//    /**
//     * 安装服务
//     *
//     * @param service
//     */
//    @SafeVarargs
//    protected final void registerService(Class<? extends IBusinessService>... service) {
//        if (null == service || service.length == 0) return;
//
//        for (Class<? extends IBusinessService> serviceClass : service) {
//            try {
//                serviceClass.newInstance().install(this);
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                LogUtils.e(IBusinessService.Companion.getTAG(), "registerService name: " + serviceClass.getCanonicalName() + " failed: error message: " + e.getMessage());
//            }
//        }
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        AppWatcher.getInstance().tagClass("进入", this);

        //// TODO: 2018/12/10
//        if (!LeConstant.BuildType.buildMode.equals(BUILD_MODE_RELEASE)) {
//            EventObject eventObject = new EventObject("onResume");
//            BusManager.getInstance().postEvent(EventKeys.EVENT_ACTIVITY_LIFE_DELEGATE, eventObject);
//        }

        PushSdkApi.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppWatcher.getInstance().tagClass("离开", this);

        //// TODO: 2018/12/10
//        if (!LeConstant.BuildType.buildMode.equals(BUILD_MODE_RELEASE)) {
//            EventObject eventObject = new EventObject("onPause");
//            BusManager.getInstance().postEvent(EventKeys.EVENT_ACTIVITY_LIFE_DELEGATE, eventObject);
//        }

        PushSdkApi.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppWatcher.getInstance().tagClass("销毁", this);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (!LeConstant.BuildType.buildMode.equals(BUILD_MODE_RELEASE)) {
//            EventObject eventObject = new EventObject("dispatchTouchEvent");
//            eventObject.params = event;
//            BusManager.getDefault().postEvent(EventKeys.EVENT_ACTIVITY_LIFE_DELEGATE, eventObject);
//
//        }
//        return super.dispatchTouchEvent(event);
//    }

    @Override
    protected void onActivityError(String message) {
        super.onActivityError(message);
        AppWatcher.getInstance().tag(message);
    }

    @Override
    public boolean isCustomPageTopicTrack() {
        return true;
    }

    @Override
    public String getCustomPageTopic() {
        return null;
    }

    /**
     * 返回当前页面的Url
     * 用作下个页面的referrer
     *
     * @return String
     */
    @Override
    public String getScreenUrl() {
        return null;
    }

    /**
     * 返回自定义属性集合
     * 我们内置了一个属性:$screen_name,代表当前页面名称, 默认情况下,该属性会采集当前Activity的CanonicalName,即:
     * activity.getClass().getCanonicalName(), 如果想自定义页面名称, 可以在Map里put该key进行覆盖。
     * 注意:screen_name的前面必须要要加"$"符号
     *
     * @return JSONObject
     * @throws JSONException JSONException
     */
    @Override
    public JSONObject getTrackProperties() throws JSONException {
        JSONObject properties = new JSONObject();
        String target = getCustomPageTopic();
        if (TextUtils.isEmpty(target)) {
            target = LeNavigationTitleHelper.INSTANCE.getTitle(this);
        }
        //记录打开 首页Fragment 的 $AppViewSceen 事件。
        properties.put("$title", target);
//        .put("$screen_name", getClass().getCanonicalName());
        return properties;
    }
}
