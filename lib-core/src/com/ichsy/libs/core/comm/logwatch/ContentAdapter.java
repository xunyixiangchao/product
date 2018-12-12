package com.ichsy.libs.core.comm.logwatch;

import android.content.Context;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.utils.ViewHolder;
import com.ichsy.libs.core.frame.adapter.BaseFrameAdapter;

public class ContentAdapter extends BaseFrameAdapter<InfoBean> {

    public ContentAdapter(Context context) {
        super(context);
    }

    @Override
    public View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.logwatch_float_content_view_item, null);
    }

    @Override
    public void onViewAttach(int position, final InfoBean item, View convertView) {
        TextView packageName = ViewHolder.get(convertView, R.id.float_content_view_package_name);
        packageName.setText(item.getClassesContent());

        packageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData myClip = ClipData.newPlainText("text", text);
                myClipboard.setText(item.getClassesContent());
            }
        });
    }

}
