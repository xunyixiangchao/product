package com.leyou.library.le_library.comm.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import library.liuyh.com.lelibrary.R;


/**
 * Package: com.ichsy.syxd.util
 * <p>
 * File: DialogUtil.java
 *
 * @author: 赵然 Date: 2015-3-26
 * <p>
 * Modifier： Modified Date： Modify：
 * <p>
 * Copyright @ 2015 ICHSY
 */
public class DialogUtil {
	// private static float ratio = 0.4f;
	// 弹框的最大高度
	// private static int maxHeight = 1400;
	// 弹框的最小高度
	// private static int minHeight = 400;
	private final float maxScale = (int) 0.6f;
	private final float minScale = 0.2f;
	private static int animationID = R.style.anim_bottom;

	/**
	 * 返回 dialog： 由于dialog弹出后才能设置一些相关参数 所以 第一次获取时会自动弹出了
	 *
	 * @param context 上下文
	 * @param view    弹出dialog中展示的view
	 *                弹框的高度比：占屏幕的多少
	 * @return
	 */
	public static AlertDialog getDialog(Context context, View view, DialogGravity gravity) {
		return getDialog(context, view, gravity, animationID);
	}

	/**
	 * 返回 dialog： 由于dialog弹出后才能设置一些相关参数 所以 第一次获取时会自动弹出了
	 *
	 * @param context 上下文
	 * @param view    弹出dialog中展示的view
	 * @return
	 */
	public static AlertDialog getDialog(Context context, View view) {
		return getDialog(context, view, DialogGravity.BOOTOM);
	}

	/**
	 * 返回 dialog： 由于dialog弹出后才能设置一些相关参数 所以 第一次获取时会自动弹出了
	 *
	 * @param context 上下文
	 * @param view    弹出dialog中展示的view
	 * @return
	 */
	public static AlertDialog getDialog(Context context, View view, int animationID) {
		return getDialog(context, view, DialogGravity.CENTER, animationID);
	}

	/**
	 * 返回 dialog： 由于dialog弹出后才能设置一些相关参数 所以 第一次获取时会自动弹出了
	 *
	 * @param context     上下文
	 * @param view        弹出dialog中展示的view
	 *                    弹框的高度比：占屏幕的多少
	 *                    弹框的最大高度
	 *                    弹框的最小高度
	 * @param animationID 弹出动画
	 * @return
	 */
	public static AlertDialog getDialog(Context context, View view, DialogGravity gravity, int animationID) {

		AlertDialog dialog = new AlertDialog.Builder(context, com.ichsy.core_library.R.style.dialog_full_window_style).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeAllViews();
		}
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		WindowManager windowManager = ((Activity) context).getWindowManager();
		DisplayMetrics metric = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metric);
		params.width = (int) metric.widthPixels;
		// view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		// params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// Log.e("height", "height" + params.height);
		// int finalHeight = (int) (disPlay.getHeight() * ratio);
		// if (finalHeight > maxHeight) {
		// finalHeight = maxHeight;
		// }
		// if (finalHeight < minHeight) {
		// finalHeight = minHeight;
		// }
		// params.height = finalHeight; //
		if (gravity == DialogGravity.BOOTOM) {
			window.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
		} else {
			window.setGravity(Gravity.CENTER | Gravity.FILL_HORIZONTAL);
		}
		window.setAttributes(params);
		if (animationID > 0) {

			window.setWindowAnimations(animationID);
		}
		return dialog;
	}

	public static AlertDialog getCenterDialog(Context context, View view, DialogGravity gravity) {
		AlertDialog dialog = new AlertDialog.Builder(context, com.ichsy.core_library.R.style.dialog).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeAllViews();
		}
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		if (gravity == DialogGravity.BOOTOM) {
			window.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
		} else {
			window.setGravity(Gravity.CENTER);
		}
		if (animationID > 0) {
			window.setWindowAnimations(animationID);
		}
		return dialog;
	}


	public enum DialogGravity {
		BOOTOM, CENTER
	}
}
