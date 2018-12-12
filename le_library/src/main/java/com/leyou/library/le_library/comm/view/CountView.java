package com.leyou.library.le_library.comm.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ichsy.libs.core.comm.utils.DeviceUtil;
import com.ichsy.libs.core.comm.utils.DialogBuilder;
import com.ichsy.libs.core.comm.utils.LogUtils;

import library.liuyh.com.lelibrary.R;

/**
 * 计数器的view
 * Created by liuyuhang on 2018/1/11.
 */
public class CountView extends LinearLayout implements View.OnClickListener {
    private ICountViewListener countViewListener;


    public interface ICountViewListener {
        /**
         * 用户点击加号
         *
         * @param before
         * @param after
         */
        void onPlus(int before, int after);

        /**
         * 用户点击减号
         *
         * @param before
         * @param after
         */
        void onMinus(int before, int after);

        /**
         * 用户手动输入数字
         *
         * @param before
         * @param after
         */
        void onInputNumber(int before, int after);
    }

    public CountView(Context context) {
        super(context);
        init();
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_count_view_layout, this);

        rootView.findViewById(R.id.button_minus).setOnClickListener(this);
        rootView.findViewById(R.id.button_plus).setOnClickListener(this);
        rootView.findViewById(R.id.edit_count).setOnClickListener(this);
    }

    /**
     * 设置监听器
     *
     * @param countViewListener
     */
    public void setCountViewListener(ICountViewListener countViewListener) {
        this.countViewListener = countViewListener;
    }

    public int getCurrentCount() {
        TextView count = (TextView) findViewById(R.id.edit_count);
        return Integer.parseInt(count.getText().toString());
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (null != countViewListener) {
            final TextView showCountView = (TextView) findViewById(R.id.edit_count);

            final int currentCount = getCurrentCount();
            if (v.getId() == R.id.button_minus) {
                countViewListener.onMinus(currentCount, currentCount - 1);
                DeviceUtil.hidKeyBoard(getContext(), v, true);
                showCountView.setText(String.valueOf(currentCount - 1));
            } else if (v.getId() == R.id.button_plus) {
                countViewListener.onMinus(currentCount, currentCount + 1);
                DeviceUtil.hidKeyBoard(getContext(), v, true);
                showCountView.setText(String.valueOf(currentCount + 1));
            } else if (v.getId() == R.id.edit_count) {
                AlertDialog.Builder dialog = DialogBuilder.buildAlertDialog(getContext(), "输入数量", "");

                View childView = LayoutInflater.from(getContext()).inflate(R.layout.activity_shoppingcart_product_count_edit_dialog, null);
                final EditText countView = (EditText) childView.findViewById(R.id.edit_count);
                countView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                dialog.setView(childView);

                countView.setText(String.valueOf(getCurrentCount()));
                //全选中
                countView.setSelection(0, countView.getText().length());

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(countView.getText().toString())) {
                            int productCount = Integer.valueOf(countView.getText().toString().length() > 5 ? "200" : countView.getText().toString());
                            countViewListener.onInputNumber(getCurrentCount(), productCount);
                            showCountView.setText(String.valueOf(productCount));
//                            if (productCount > 99) {
//                                productCount = 99;
//                            } else if (productCount < 1) {
//                                productCount = 1;
//                            }
//                            getData().get(position).native_quantity_increment = productCount - getItem(position).quantity;
//                            notifyDataSetChanged();
//                            mCartStatusChangedListener.onProductItemChanged(position);
                        } else {
                            dialog.dismiss();
                        }


                    }
                });
                dialog.setNegativeButton("取消", null);

                dialog.show();

                DeviceUtil.hidKeyBoard(getContext(), countView, false);
            }
        } else {
            LogUtils.e("error", "未设置 " + getClass().getCanonicalName() + " 的监听器");
        }

    }
}
