package com.leyou.library.le_library.comm.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by lis
 * Date 2016/6/30.
 */
public class SpannableUtil {
	public static CharSequence setTextColor(Context context, CharSequence text, int color) {
		if(TextUtils.isEmpty(text)){
			return "";
		}
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(color))
				, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

	public static CharSequence setTextColor(Context context, CharSequence text, int start, int end, int color) {
		if(TextUtils.isEmpty(text)){
			return "";
		}
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(color))
				, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

	public static CharSequence setTextFont(CharSequence text, int size) {
		if(TextUtils.isEmpty(text)){
			return "";
		}
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(size,true);
		style.setSpan(span, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;

	}
	public static CharSequence setTextFont(CharSequence text, int start, int end, int size) {
		if(TextUtils.isEmpty(text)){
			return "";
		}
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(size,true);
		style.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;

	}
}
