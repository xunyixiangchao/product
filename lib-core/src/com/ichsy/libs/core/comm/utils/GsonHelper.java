package com.ichsy.libs.core.comm.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * json处理工具类
 * Created by liuyuhang on 16/5/10.
 */
public class GsonHelper {
    private static Gson mGson;

    public static Gson build() {
        synchronized (GsonHelper.class) {
            if (null == mGson) {
                mGson = new com.google.gson.GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double src, Type type, JsonSerializationContext jsonSerializationContext) {
                        if (src == src.longValue()) {
                            return new JsonPrimitive(src.longValue());
                        }
                        return new JsonPrimitive(src);
                    }
                }).create();
            }
            return mGson;
        }
    }

    /**
     * Json排版
     *
     * @param uglyJSONString
     * @return
     */
    public static String formatter(String uglyJSONString) {
        if (TextUtils.isEmpty(uglyJSONString)) {
            return null;
        }

        String prettyJsonStr = uglyJSONString;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(uglyJSONString);
            prettyJsonStr = gson.toJson(je);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prettyJsonStr;
    }

}
