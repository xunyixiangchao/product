package com.ichsy.libs.core.comm.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ichsy.core_library.R;

/**
 * Dialog的构建类
 * Created by liuyuhang on 16/5/3.
 */
public class DialogBuilder {

    /**
     * 构建一个ProgressDialog
     *
     * @param context
     * @param content 显示的内容
     * @return
     */
    public static ProgressDialog buildProgressDialog(Context context, String content) {
        ProgressDialog dialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(content)) {
            dialog.setMessage(content);
        }
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 构建一个ProgressDialog
     *
     * @param context
     * @param content 显示的内容
     * @return
     */
    public static ProgressDialog buildProgressDialog(Context context, String content, int progress) {
        ProgressDialog dialog = buildProgressDialog(context, content);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog.setProgressNumberFormat("%1d M/%2d M");
        }
        dialog.setProgress(progress);
        dialog.setMax(0);
        return dialog;
    }

    /**
     * 构建一个AlertDialog
     *
     * @param context
     * @param title
     * @param description
     * @return
     */
    public static AlertDialog.Builder buildAlertDialog(Context context, String title, CharSequence description) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.MDDialogTheme);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        if (!TextUtils.isEmpty(description)) {
            dialog.setMessage(description);
        }

        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", null);
        return dialog;
    }

//    public static android.support.v7.app.AlertDialog.Builder buildMDAlertDialog(Context context, String title, CharSequence description) {
//        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
//        dialog.setTitle(title);
//        if (!TextUtils.isEmpty(description)) {
////            builder.setMessage(description);
//            dialog.setMessage(description);
//        }
//
//        dialog.setCancelable(false);
//        dialog.setNegativeButton("确定", null);
//        return dialog;
//    }

    /**
     * 构建一个AlertDialog
     *
     * @param context
     * @param title
     * @param description
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showAlertDialog(Context context, String title, String description, String okButton, String cancelButton, final View.OnClickListener listener) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_base_layout, null);

        dialog.setContentView(rootView);
//        dialog.setView(rootView);

        TextView titleView = (TextView) rootView.findViewById(R.id.tv_dialog_title);
        TextView contentView = (TextView) rootView.findViewById(R.id.tv_dialog_content);

        TextView okBt = (TextView) rootView.findViewById(R.id.bt_dialog_button1);
        TextView cancelBt = (TextView) rootView.findViewById(R.id.bt_dialog_button2);

        titleView.setText(title);
        if (!TextUtils.isEmpty(description)) {
            description = description.replace("\\r\\n", "<br />");
            contentView.setText(Html.fromHtml(description));
        }

        if (TextUtils.isEmpty(okButton)) {
            okBt.setVisibility(View.GONE);
        } else {
            okBt.setVisibility(View.VISIBLE);
            okBt.setText(okButton);

            okBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onClick(v);
                    }

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            cancelBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }

        if (TextUtils.isEmpty(cancelButton)) {
            cancelBt.setVisibility(View.GONE);
        } else {
            cancelBt.setVisibility(View.VISIBLE);
            cancelBt.setText(cancelButton);
            cancelBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(okButton) && !TextUtils.isEmpty(cancelButton)) {
            rootView.findViewById(R.id.view_line).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.view_line).setVisibility(View.GONE);
        }

        dialog.setCancelable(false);
//        builder.setPositiveButton(okButton, listener);
//        builder.setNegativeButton(cancelButton, null);
        dialog.show();
        DialogBuilder.setDialogWindowAttr(dialog, ViewUtil.dip2px(context, 280), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 重新设置dialog的大小，如果不设置，dialog会特别小
     *
     * @param dlg
     * @param width
     * @param height
     */
    public static WindowManager.LayoutParams setDialogWindowAttr(Dialog dlg, int width, int height) {
        return setDialogWindowAttr(dlg, width, height, Gravity.CENTER);
    }

    public static WindowManager.LayoutParams setDialogWindowAttr(Dialog dlg, int width, int height, int gravity) {
        Window window = dlg.getWindow();
        if (null != window) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = gravity;
            lp.width = width;//宽高可设置具体大小
            lp.height = height;
            dlg.getWindow().setAttributes(lp);
            return lp;
        }
        return null;

    }

//    public static Dialog buildDialog(Context context) {
//        Dialog mCameraDialog = new Dialog(context, R.style.dialog_full_window_style);
//
//        mCameraDialog.setCanceledOnTouchOutside(true);
//
//        return mCameraDialog;
//    }

    /**
     * 从底部弹出dialog
     */
    public static Dialog buildDialogFromBottom(Context context) {
        return buildDialogFromBottom(context, null);
    }

    public static Dialog buildDialogFromBottom(Context context, View view) {
        Dialog mCameraDialog = new Dialog(context, R.style.dialog_full_window_style);

        if (null != view) {
            mCameraDialog.setContentView(view);
        }
        Window dialogWindow = mCameraDialog.getWindow();
        mCameraDialog.setCanceledOnTouchOutside(true);
        dialogWindow.setGravity(Gravity.BOTTOM);
        // 添加动画
        dialogWindow.setWindowAnimations(R.style.dialog_pop_from_bottom);

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = -20; // 新位置Y坐标
//        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 宽度
////      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
////      lp.alpha = 9f; // 透明度
//        view.measure(0, 0);

//        lp.height = view.getMeasuredHeight();
//        lp.alpha = 9f; // 透明度
//        dialogWindow.setAttributes(lp);
//        WindowManager.LayoutParams lp = setDialogWindowAttr(mCameraDialog, context.getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
        Window window = mCameraDialog.getWindow();
        if (null != window) {
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.gravity = Gravity.CENTER;
            lp.width = context.getResources().getDisplayMetrics().widthPixels;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mCameraDialog.getWindow().setAttributes(lp);
        }
//        if (lp != null) {
//            lp.gravity = Gravity.BOTTOM;
//        }

        mCameraDialog.show();
        return mCameraDialog;
    }

//    public static View getCloseMenuView(Context context, String title, View contentView) {
//        View root = LayoutInflater.from(context).inflate(R.layout.dialog_listview_layout, null);
//
//        TextView titleView = (TextView) root.findViewById(R.id.tv_title);
//        LinearLayout menusGroup = (LinearLayout) root.findViewById(R.id.group_menus);
//
//        titleView.setText(title);
//
//        menusGroup.removeAllViews();
//
//        menusGroup.addView(contentView);
//
//        return root;
//    }
}
