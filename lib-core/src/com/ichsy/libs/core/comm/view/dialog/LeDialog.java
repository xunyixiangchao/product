package com.ichsy.libs.core.comm.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ichsy.core_library.R;

/**
 * Created by ss on 2016/11/28.
 */
public class LeDialog {
    private Context context;

    public LeDialog(Context context) {
        this.context = context;
    }

    public void show(UiBuilder builder) {
        if (context == null) return;
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.dialog).create();
        dialog.setCancelable(false);
        dialog.show();
        if (builder != null) {
            View view = builder.onViewCreate(LayoutInflater.from(context));
            if (view != null) {
                builder.onViewDraw(dialog, view);
                dialog.setContentView(view);
            }
        }
    }

    public interface UiBuilder {
        View onViewCreate(LayoutInflater inflater);

        void onViewDraw(Dialog dialog, View view);
    }

}
