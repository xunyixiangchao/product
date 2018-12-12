package com.ichsy.libs.core.comm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间辅助工具类
 * Created by liuyuhang on 2016/11/7.
 */

public class DateHelper {

    public static boolean isToday(long t) {
        boolean b = false;
        Date time = new Date(t);
        Date today = new Date();
        String nowDate = dateFormater2.get().format(today);
        String timeDate = dateFormater2.get().format(time);
        if (nowDate.equals(timeDate)) {
            b = true;
        }
        return b;
    }

    public static String getFormatter(long time, String formatter) {
        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }


    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
    };
}
