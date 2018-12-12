package com.ichsy.libs.core.frame;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.XMLReader;

/**
 * TextView 自定义可点击标签
 *
 * @author guanbr
 * @date 2018/1/16
 */

public class MyTagHandler implements Html.TagHandler {
    private int sIndex = 0;
    private int eIndex = 0;
    private final Context mContext;
    private OnClickTextListener mListener;

    public interface OnClickTextListener {
        void onClick();
    }

    public MyTagHandler(Context context, OnClickTextListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.toLowerCase().equals("clk")) {
            if (opening) {
                sIndex = output.length();
            } else {
                eIndex = output.length();
                output.setSpan(new MySpan(), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private class MySpan extends ClickableSpan implements View.OnClickListener {
        @Override
        public void onClick(View widget) {
            avoidHintColor(widget);
            if (mListener != null) {
                mListener.onClick();
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
        }
    }
}