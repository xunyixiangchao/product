package com.ichsy.libs.core.comm.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ichsy.core_library.R;

/**
 * Created by ss on 2016/9/21.
 */

public class UpdateDialog extends Dialog {

    public UpdateDialog(Context context) {
        super(context);
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private boolean isForce;
        private String title;
        private String message;
        private String confirmText;
        private View.OnClickListener confirmListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }


        public Builder setConfirmButton(String confirmText, View.OnClickListener confirmListener) {
            this.confirmText = confirmText;
            this.confirmListener = confirmListener;
            return this;
        }

        public Builder setForce(boolean isForce) {
            this.isForce = isForce;
            return this;
        }

        public UpdateDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final UpdateDialog dialog = new UpdateDialog(context, R.style.dialog);
            dialog.setCancelable(false);

            View layout = inflater.inflate(R.layout.frame_dialog_update_layout, null);

            ((TextView) layout.findViewById(R.id.tv_title)).setText(title);
            ((TextView) layout.findViewById(R.id.tv_content)).setText(message);
            ((TextView) layout.findViewById(R.id.tv_confirm)).setText(confirmText);
            layout.findViewById(R.id.iv_cancel).setVisibility(isForce ? View.GONE : View.VISIBLE);
            if (confirmListener != null) {
                layout.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        confirmListener.onClick(v);
                    }
                });
                layout.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        confirmListener.onClick(v);
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}



