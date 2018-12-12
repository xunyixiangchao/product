package com.ichsy.libs.core.comm.bus.url.filter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.comm.bus.url.route.UrlRoute;
import com.ichsy.libs.core.comm.utils.ArrayUtil;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.NavigationUtil;
import com.ichsy.libs.core.comm.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 总线中控制intent跳转的逻辑处理
 * <p/>
 * Created by liuyuhang on 16/4/25.
 */
public class IntentFilter extends BusBaseFilter {
    public static final String INTENT_BUNDLE_FROM_ACTIVITY = "INTENT_BUNDLE_FROM_ACTIVITY";
    public static final String PARAMS_ATTACH_INTENT_KEY = "PARAMS_ATTACH_INTENT_KEY";
    public static final String PARAMS_ATTACH_RUNNABLE_KEY = "PARAMS_ATTACH_RUNNABLE_KEY";
    public static IntentClassFilter currentIntent;

    public static final String PARAMS_BUNDLE_TRANSITION = "params_bundle_transition";

    //默认的resultCode
    public static final int ACTIVITY_RESULT_CODE_DEFAULT = Activity.RESULT_OK;

    public class IntentClassFilter {
        public String intentClass;
        public long time;
    }

//    private UrlParser.UrlParserFilter urlParserFilter;

    @Override
    public String[] filterWhat() {
        return new String[]{UrlParser.SCHEME_RULE_NATIVE};
    }

    @Override
    public void onAction(Context context, HashMap<String, String> params, HashMap<String, Object> attachMap) {
        Intent intent;
        if (null == attachMap || null == (intent = (Intent) attachMap.get(PARAMS_ATTACH_INTENT_KEY))) {
            intent = new Intent();
        }

        String className;//目标activity
        if (params == null) {
            className = intent.getComponent().getClassName();
            params = new HashMap<>();
        } else {
            String nativeName = params.get(UrlParser.SCHEME_RULE_NATIVE);
            params.remove(UrlParser.SCHEME_RULE_NATIVE);

            //className从路由表获取
            String routeClassName = UrlRoute.getInstance().getClassName(nativeName);
            if (TextUtils.isEmpty(routeClassName)) {
                className = nativeName;
            } else {
                className = routeClassName;
            }
        }

        if (TextUtils.isEmpty(className)) {
            className = intent.getComponent().getClassName();
        }

        final Intent finalIntent = intent;
        if (!(context instanceof Activity)) {
            finalIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        ArrayUtil.arrayMap(params, new ArrayUtil.MapHandler<String, String>() {

            @Override
            public void onNext(String key, String value) {
                Log.v("intent", "key:" + key + " value:" + value);
                //拼装总线url后面的参数到intent参数中
                finalIntent.putExtra(key, value);
            }
        });

        String fromActivity = context.getClass().getName();
        String toActivity = className;

        LogUtils.i("urlparser", "处理intent的filter，intent is:" + intent + "\nfrom = " + fromActivity + "\nto = " + toActivity);

        //传递来源
        finalIntent.putExtra(INTENT_BUNDLE_FROM_ACTIVITY, fromActivity);

        //判断是否忽略多次打开的延迟
        String quickOpen = intent.getStringExtra("quickOpen");

        /* 最短时间校验，验证两次打开activity的时间，如果太短不做任何操作，可以防止重复点击的问题 */
        if (null != currentIntent && currentIntent.intentClass.equals(toActivity) && !"true".equals(quickOpen)) {

            long currentTime = System.currentTimeMillis();
            long intentTime = currentIntent.time;

            if (currentTime - intentTime < 400) {
                LogUtils.i("intentfilter", "currentTime - intentTime: " + (currentTime - intentTime));
                return;
            }
        }

        currentIntent = new IntentClassFilter();
        currentIntent.intentClass = toActivity;
        currentIntent.time = System.currentTimeMillis();

        onPushActivity(context, fromActivity, toActivity, finalIntent, attachMap);
    }

    protected void onPushActivity(Context context, String fromActivity, String toActivity, final Intent finalIntent, HashMap<String, Object> attachMap) {
        Log.v("urlparser", "cant find bus_mapping file, start activity: " + toActivity);

//        if (null != urlParserFilter) {
//            urlParserFilter.onFilter(context, attachMap);
//        }

        finalIntent.setClassName(context, toActivity);
        pushActivity(context, finalIntent, attachMap);
    }

    /**
     * 打开activity的逻辑
     *
     * @param context
     * @param finalIntent
     */
    public void pushActivity(Context context, Intent finalIntent, HashMap<String, Object> attachMap) {
        boolean isActivity = context instanceof Activity;
        try {
            int resultCode = finalIntent.getIntExtra(NavigationUtil.INTENT_BUNDLE_ACTIVITY_REQUEST_CODE, ACTIVITY_RESULT_CODE_DEFAULT);
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, shareView, "shareName");

            if (isActivity) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && null != attachMap && null != attachMap.get(PARAMS_BUNDLE_TRANSITION)) {
                    HashMap<String, View> transitionMap = (HashMap<String, View>) attachMap.get(PARAMS_BUNDLE_TRANSITION);

                    Pair<View, String>[] pairs = new Pair[transitionMap.size()];

                    int i = 0;
                    for (Map.Entry<String, View> stringViewEntry : transitionMap.entrySet()) {
                        pairs[i] = Pair.create(stringViewEntry.getValue(), stringViewEntry.getKey());
                        i++;
                    }
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);

                    if (resultCode != ACTIVITY_RESULT_CODE_DEFAULT) {
                        ((Activity) context).startActivityForResult(finalIntent, resultCode, options.toBundle());
                    } else {
                        context.startActivity(finalIntent, options.toBundle());
                    }
                } else {
                    if (resultCode != ACTIVITY_RESULT_CODE_DEFAULT) {
                        ((Activity) context).startActivityForResult(finalIntent, resultCode);
                    } else {
                        context.startActivity(finalIntent);
                    }
                }
            } else {
                context.startActivity(finalIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showMessage(context, "启动页面发生错误：" + e.getMessage());
        }

    }

//    public void setUrlParserFilter(UrlParser.UrlParserFilter urlParserFilter) {
//        this.urlParserFilter = urlParserFilter;
//    }

}
