package com.leyou.library.le_library.comm.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import java.text.DecimalFormat;

/**
 * 价格处理的工具类
 *
 * @author liuyuhang
 * @date 16/8/4
 */
public class PriceUtils {

    public static String getPrice(String price) {
        return getPrice(price, "##0.00");
    }

    public static String getPrice(String value, String formatter) {
        if (TextUtils.isEmpty(value) || TextUtils.isEmpty(formatter) || "null".equals(value.trim())) {
            return "";
        }
        value = value.replace("￥", "");
        value = value.replace("¥", "");

        return "¥" + decimalFormat(value, formatter);
    }

    private static String decimalFormat(String value) {
        return decimalFormat(value, "##0.00");
    }

    private static String decimalFormat(String value, String formatter) {
        try {
            DecimalFormat df = new DecimalFormat(formatter);
            return df.format(Double.parseDouble(value));
        } catch (Exception e) {
            return "";
        }
    }

    public static SpannableStringBuilder trans2SpanRange(String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(value) || "null".equals(value)) {
            return builder;
        }

        String priceSeparator = "-";
        String separator = ".";
        String rmbString = "¥ ";

        SpannableString rmbSpannable = new SpannableString(rmbString);
        rmbSpannable.setSpan(new AbsoluteSizeSpan(12, true), 0, rmbString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(rmbSpannable);

        value = value.replace("￥", "");
        value = value.replace("¥", "");

        String[] priceSplit = value.split(priceSeparator);
        for (String price : priceSplit) {
            price = decimalFormat(price);
            String[] split = price.split("\\.");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                SpannableString spannableString = new SpannableString(s);
                if (i == 0) {
                    spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    builder.append(separator);
                } else {
                    spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                }
            }
            builder.append(priceSeparator);
        }
        int length = builder.length();
        builder.delete(length - 1, length);
        return builder;
    }

    public static SpannableString shoppingCartSizeSpan(String value) {
        return trans2Span(value, 10, 14, 10);
    }

    public static SpannableString trans2Span(String value) {
        return trans2Span(value, 12, 18, 15);
    }

    // 带有人民币符号的价格文字改变大小
    // 必须带有人民币符号 人民币符号部分 整数部分 小数部分
    public static SpannableString trans2Span(String value, int rmbSize, int integerSize, int decimalSize) {
        int pointIndex = value.indexOf(".");
        int length = value.length();
        SpannableString spannableString = new SpannableString(value);
        if (length > 1) {
            spannableString.setSpan(new AbsoluteSizeSpan(rmbSize, true), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (pointIndex == -1) {
                pointIndex = length;
            }
            spannableString.setSpan(new AbsoluteSizeSpan(integerSize, true), 1, pointIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (pointIndex != length) {
                spannableString.setSpan(new AbsoluteSizeSpan(decimalSize, true), pointIndex, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}
