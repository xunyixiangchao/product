package com.leyou.library.le_library.comm.utils;


import android.content.Context;

import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.leyou.library.le_library.comm.collection.AppTrackHelper;

import java.util.HashMap;

import cn.testin.analysis.TestinApi;

/**
 * Created by zhaoye
 */

public class TestABUtil {

    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = 1;
    private static final String DEFAULT_VALUE_STRING = "1";
    private static final double DEFAULT_VALUE_DOUBLE = 1.0D;

    public static boolean getTestInBooleanFlag(Context context, String key) {
        return getTestInBooleanFlag(context, key, DEFAULT_VALUE_BOOLEAN);
    }

    public static boolean getTestInBooleanFlag(Context context, String key, boolean default_value) {
        Boolean booleanFlag = TestinApi.getBooleanFlag(key, default_value);
        onEvent(context, key, booleanFlag.toString());
        return booleanFlag;
    }

    public static int getTestInIntFlag(Context context, String key) {
        return getTestInIntFlag(context, key, DEFAULT_VALUE_INT);
    }

    public static int getTestInIntFlag(Context context, String key, int default_value) {
        Integer integerFlag = TestinApi.getIntegerFlag(key, default_value);
        onEvent(context, key, integerFlag.toString());
        return integerFlag;
    }

    public static String getTestInStringFlag(Context context, String key) {
        return getTestInStringFlag(context, key, DEFAULT_VALUE_STRING);
    }

    public static String getTestInStringFlag(Context context, String key, String default_value) {
        String stringFlag = TestinApi.getStringFlag(key, default_value);
        onEvent(context, key, stringFlag);
        return stringFlag;
    }

    public static void setTestInTrack(Context context, String eventName) {
        setTestInTrack(context, eventName, DEFAULT_VALUE_DOUBLE);
    }

    public static void setTestInTrack(Context context, String eventName, Double value) {
        onEvent(context, eventName, value.toString());
        TestinApi.track(eventName, value);
    }

    private static void onEvent(Context context, String key, String value) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("ExperimentalVariable", key);
        params.put("ParticipationGrouping", value);
        AppTrackHelper.INSTANCE.onEvent(context, "enterAbTest", params);
    }

    public static void saveRequestMayLikeTestAb(Context context, int testAb) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        provider.putCache("MAY_LIKE_AB_2", testAb);
    }

    public static int getRequestMayLikeTestAb(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        String value = provider.getCache("MAY_LIKE_AB_2");
        int testAb = 0;
        try {
            testAb = Integer.parseInt(value);
        } catch (Exception e) {
        }
        return testAb;
    }

    public static String ab2TrackName(int testAb) {
        String trackName;
        switch (testAb) {
            case 0:
                trackName = "乐友";
                break;
            case 1:
                trackName = "达观";
                break;
            case 2:
                trackName = "艾克斯";
                break;
            default:
                trackName = "乐友";
        }

        return trackName;
    }

    public static String ab2TrackScence(int testAb) {
        String trackName;
        switch (testAb) {
            case 0:
                trackName = "leyou";
                break;
            case 1:
                trackName = "daguan";
                break;
            case 2:
                trackName = "aliyun";
                break;
            default:
                trackName = null;
        }
        return trackName;
    }
}
