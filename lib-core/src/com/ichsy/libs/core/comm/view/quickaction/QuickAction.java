package com.ichsy.libs.core.comm.view.quickaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class QuickAction extends PopupWindow {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private BitmapDrawable mBackground;

    private ViewGroup mRootView;

    private Display mDefaultDisplay;

    private ImageView mArrowTop;

    private ImageView mArrowBottom;

    private ImageView mArrowLeft;

    private ImageView mArrowRight;

    private LinearLayout mQuickActionLayout;

    private int[] mAnchorLocations;

    private int mScreenWidth;

    private int mScreenHeight;

    private List<ActionItem> mActionItems = new ArrayList<ActionItem>();

    private LayoutInflater mInflater;

    private OnClickQuickActionListener mClickListener;

    // ===========================================================
    // Constructors
    // ===========================================================

    public QuickAction(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mDefaultDisplay = wm.getDefaultDisplay();

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initParams();

        // Quick Action ����
        initQuickAction(context);
    }

    // ===========================================================
    // Public Methods
    // ===========================================================


    /**
     * ��ʾQuick Action
     *
     * @param anchor
     */
    public void show(View anchor) {
        if (!isShowing()) {

            // λ��
            Direction showDirection = computeDisplayPosition(anchor);

            Log.e("QuickAction", "showDirection = " + test(showDirection));

            // ���λ�ã���ʾ��ͷ
            int[] locations = preShow(anchor, showDirection);

            // ��ʾPopupWindow
            if (locations != null) {
                showAtLocation(anchor, Gravity.NO_GRAVITY, locations[0], locations[1]);
            }

        } else {
            dismiss();
        }
    }


    /**
     * @param quickAction
     */
    public void addQuickAction(View quickAction) {
        mQuickActionLayout.addView(quickAction);
    }

    public void addQuickActionItem(ActionItem item) {
        mActionItems.add(item);

        View container = mInflater.inflate(R.layout.action_item_horizontal, null);

//        View line = container.findViewById(R.id.line);
        ImageView iconView = (ImageView) container.findViewById(R.id.iv_icon);
        TextView titleView = (TextView) container.findViewById(R.id.tv_title);

//        ViewUtil.setViewVisibility(mQuickActionLayout.getChildCount() == 0 ? View.GONE : View.VISIBLE, line);

        Drawable icon = item.getIcon();
        if (icon != null) {
            iconView.setImageDrawable(icon);
        } else {
            iconView.setVisibility(View.GONE);
        }

        String title = item.getTitle();
        if (title != null) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
        }

        final int actionId = item.getActionId();

        container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClickQuickAction(actionId);
                    dismiss();
                }
            }
        });

        if (mQuickActionLayout.getChildCount() != 0) {
            View line = new View(mRootView.getContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewUtil.dip2px(mRootView.getContext(), 97), 1);
//            layoutParams.setMargins(ViewUtil.px2dip(container.getContext(), 9), 0, ViewUtil.px2dip(container.getContext(), 9), 0);
//            line.setPadding(ViewUtil.px2dip(container.getContext(), 9), 0, ViewUtil.px2dip(container.getContext(), 9), 0);
            line.setLayoutParams(layoutParams);
            line.setBackgroundColor(Color.parseColor("#555555"));
            mQuickActionLayout.addView(line);
        }

        mQuickActionLayout.addView(container);
    }


    public void setOnClickQuickActionListener(OnClickQuickActionListener listener) {
        mClickListener = listener;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Private Methods
    // ===========================================================

    private void initParams() {
        // �����PopupWindow��ʱ��dismiss popup window
        setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        // �����������
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);

        // ���ö���Ч��
        setAnimationStyle(R.style.quickaction);

        // �������Ӧ
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void initQuickAction(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = (ViewGroup) mInflater.inflate(
                R.layout.quickaction_vertical, null);
        mQuickActionLayout = (LinearLayout) mRootView
                .findViewById(R.id.layout_quickaction);
        mArrowTop = (ImageView) mRootView.findViewById(R.id.arrow_top);
        mArrowBottom = (ImageView) mRootView.findViewById(R.id.arrow_bottom);
        mArrowLeft = (ImageView) mRootView.findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) mRootView.findViewById(R.id.arrow_right);

        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setContentView(mRootView);

    }

    /**
     * Popup Window�Զ����ã�֧�����������ĸ�λ�ã���
     */
    private Direction computeDisplayPosition(View anchor) {

        Direction showDirection = null;

        mAnchorLocations = new int[2];
        // ��ȡ������Window�ľ��λ��
        anchor.getLocationOnScreen(mAnchorLocations);

        mScreenWidth = mDefaultDisplay.getWidth();
        mScreenHeight = mDefaultDisplay.getHeight();

        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int popupWidth = mRootView.getMeasuredWidth();
        int popupHeight = mRootView.getMeasuredHeight();

        // �� �����е����⣬û�м�ȥ״̬���ͱ������ĸ߶�

        // QuickAction������ʾ��anthor�������ң���������ʾ��һ��
        boolean canShowTop = mAnchorLocations[1] - popupHeight > 0;
        boolean canShowBottom = mAnchorLocations[1] + anchor.getHeight() + popupHeight < mScreenHeight;
        boolean canShowRight = mAnchorLocations[0] + anchor.getWidth() + popupWidth < mScreenWidth;
        boolean canShowLeft = mAnchorLocations[0] - popupWidth > 0;

        // �� �ж��������Ͻ�

        if (!canShowTop && canShowBottom) {
            showDirection = Direction.BOTTOM;
        } else if (canShowTop && !canShowBottom) {
            showDirection = Direction.TOP;
        } else if (!canShowLeft && canShowRight) {
            showDirection = Direction.RIGHT;
        } else if (canShowLeft && !canShowRight) {
            showDirection = Direction.LEFT;
        }

//		Log.e("Test", "up -- ay = " + mAnchorLocations[1]
//				+ " , ah = " + anchor.getHeight()
//				+ " , ph = " + popupHeight
//				+ " , sh = " + mScreenHeight);

        Log.e("Test", "right -- ax = " + mAnchorLocations[0]
                + " , aw = " + anchor.getWidth()
                + " , pw = " + popupWidth
                + " , sw = " + mScreenWidth);


        return showDirection;
    }

    private int[] preShow(View anchor, Direction showDirection) {

        if (mBackground == null) {
            // Ĭ������Ϊ͸��
            setBackgroundDrawable(new BitmapDrawable());
        } else {
            setBackgroundDrawable(mBackground);
        }

        if (showDirection == null) {
            return null;
        }

        int[] locations = new int[2];

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                mQuickActionLayout.getLayoutParams();

        RelativeLayout.LayoutParams arrowParams = null;
        int arrowPos = 0;
        int anchorCenterX = 0;
        int anchorCenterY = 0;

        // * ���¼�ͷ�������Ϊgone����ȡpopup window height�������ڱ䶯

        switch (showDirection) {
            case TOP:
                mArrowTop.setVisibility(View.INVISIBLE);
                mArrowBottom.setVisibility(View.VISIBLE);
                mArrowLeft.setVisibility(View.GONE);
                mArrowRight.setVisibility(View.GONE);

                params.setMargins(0, 0, 0, -6);
                mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                anchorCenterX = mAnchorLocations[0] + anchor.getWidth() / 2;

                locations[0] = anchorCenterX - mRootView.getMeasuredWidth() / 2;
                locations[1] = mAnchorLocations[1] - mRootView.getMeasuredHeight();

                if (locations[0] <= 0) {
                    locations[0] = 0;
                } else if (locations[0] + mRootView.getMeasuredWidth() >= mScreenWidth) {
                    locations[0] = mScreenWidth - mRootView.getMeasuredWidth();
                }

                // ��ͷ
                arrowParams = (RelativeLayout.LayoutParams)
                        mArrowBottom.getLayoutParams();
                arrowPos = anchorCenterX - locations[0] - mArrowBottom.getMeasuredWidth() / 2;
                arrowParams.setMargins(arrowPos, 0, 0, 0);

                break;

            case BOTTOM:
                mArrowTop.setVisibility(View.VISIBLE);
                mArrowBottom.setVisibility(View.INVISIBLE);
                mArrowLeft.setVisibility(View.GONE);
                mArrowRight.setVisibility(View.GONE);

                // �м�
                params.setMargins(0, mArrowTop.getMeasuredHeight() - ViewUtil.dip2px(anchor.getContext(), 0), ViewUtil.dip2px(anchor.getContext(), 9), 0);

                mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                anchorCenterX = mAnchorLocations[0] + anchor.getWidth() / 2;

                // Popup Window
                locations[0] = anchorCenterX - mRootView.getMeasuredWidth() / 2;
                locations[1] = mAnchorLocations[1] + anchor.getHeight();

                if (locations[0] <= 0) {
                    locations[0] = 0;
                } else if (locations[0] + mRootView.getMeasuredWidth() >= mScreenWidth) {
                    locations[0] = mScreenWidth - mRootView.getMeasuredWidth();
                }

                // ��ͷ
                arrowParams = (RelativeLayout.LayoutParams)
                        mArrowTop.getLayoutParams();
                arrowPos = anchorCenterX - locations[0] - mArrowTop.getMeasuredWidth() / 2;
                arrowParams.setMargins(arrowPos, 0, 0, 0);

                break;

            case LEFT:
                mArrowTop.setVisibility(View.INVISIBLE);
                mArrowBottom.setVisibility(View.INVISIBLE);
                mArrowLeft.setVisibility(View.GONE);
                mArrowRight.setVisibility(View.VISIBLE);
                params.setMargins(0, 0, -3, 0);
                mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                anchorCenterY = mAnchorLocations[1] + anchor.getHeight() / 2;

                locations[0] = mAnchorLocations[0] - mRootView.getMeasuredWidth();
                locations[1] = anchorCenterY - mRootView.getMeasuredHeight() / 2;

                if (locations[1] <= 0) {
                    locations[1] = 0;
                } else if (locations[1] + mRootView.getMeasuredHeight() >= mScreenHeight) {
                    locations[1] = mScreenHeight - mRootView.getMeasuredHeight();
                }

                // ��ͷ
                arrowParams = (RelativeLayout.LayoutParams)
                        mArrowRight.getLayoutParams();
                arrowPos = anchorCenterY - locations[1] - mArrowRight.getMeasuredHeight() / 2;
                arrowParams.setMargins(0, arrowPos, 0, 0);

                break;

            case RIGHT:
                mArrowTop.setVisibility(View.INVISIBLE);
                mArrowBottom.setVisibility(View.INVISIBLE);
                mArrowLeft.setVisibility(View.VISIBLE);
                mArrowRight.setVisibility(View.GONE);
                params.setMargins(mArrowLeft.getMeasuredWidth() - 3, 0, 0, 0);
                mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                anchorCenterY = mAnchorLocations[1] + anchor.getHeight() / 2;

                locations[0] = mAnchorLocations[0] + anchor.getWidth();
                locations[1] = anchorCenterY - mRootView.getMeasuredHeight() / 2;

                if (locations[1] <= 0) {
                    locations[1] = 0;
                } else if (locations[1] + mRootView.getMeasuredHeight() >= mScreenHeight) {
                    locations[1] = mScreenHeight - mRootView.getMeasuredHeight();
                }

                // ��ͷ
                arrowParams = (RelativeLayout.LayoutParams)
                        mArrowLeft.getLayoutParams();
                arrowPos = anchorCenterY - locations[1] - mArrowLeft.getMeasuredHeight() / 2;
                arrowParams.setMargins(0, arrowPos, 0, 0);
                break;

        }

        return locations;
    }

    private String test(Direction direction) {
        String value = new String();

        if (direction == null) {
            return value;
        }

        switch (direction) {
            case TOP:
                value = "TOP";
                break;

            case BOTTOM:
                value = "BOTTOM";
                break;

            case LEFT:
                value = "LEFT";
                break;
            case RIGHT:
                value = "RIGHT";
                break;
        }
        return value;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }

    public interface OnClickQuickActionListener {
        void onClickQuickAction(int actionId);
    }

}
