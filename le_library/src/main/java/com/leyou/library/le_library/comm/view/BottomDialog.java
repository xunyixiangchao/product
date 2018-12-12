package com.leyou.library.le_library.comm.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.leyou.library.le_library.comm.utils.DialogUtil;

/**
 * 底部弹出的dialog
 *
 * Created by ss on 2017/8/10.
 */

public class BottomDialog {
    private Context context;

    public BottomDialog(Context context) {
        this.context = context;
    }

    public void show(UiBuilder builder) {
        if (builder != null) {
            View view = builder.onViewCreate(LayoutInflater.from(context));
            if (view != null) {
                AlertDialog dialog = DialogUtil.getDialog(context, view);
                builder.onViewDraw(dialog, view);
                dialog.setContentView(view);
                dialog.show();
            }
        }
    }

    public interface UiBuilder {
        View onViewCreate(LayoutInflater inflater);

        void onViewDraw(Dialog dialog, View view);
    }
}
