package com.ichsy.libs.core.comm.logwatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.logwatch.MyWindowManager;

public class FloatView extends LinearLayout {
	
	private TextView mTextMove;
	private MyWindowManager manager;
	public int mWidth;
	public int mHeight;
	private int preX;
	private int preY;
	private int downX;
	private int downY;
	private int x;
	private int y;
	private boolean isMove;

	public FloatView(Context context) {
		this(context, null);
	}

	public FloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 填充布局，并添加至
		LayoutInflater.from(context).inflate(R.layout.logwatch_float_view, this);
		mTextMove = (TextView) findViewById(R.id.move);
		// 宽高
		mWidth = mTextMove.getLayoutParams().width;
		mHeight = mTextMove.getLayoutParams().height;
		manager = MyWindowManager.getInstance(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			preX = (int) event.getRawX();
			preY = (int) event.getRawY();
			downX = (int) event.getRawX();
			downY = (int) event.getRawY();
			isMove = false;
			break;
		case MotionEvent.ACTION_MOVE:
			x = (int) event.getRawX();
			y = (int) event.getRawY();
			manager.move(this, x - preX, y - preY);
			preX = x;
			preY = y;
			break;
		case MotionEvent.ACTION_UP:
			int currentX = (int) event.getRawX();
			int currentY = (int) event.getRawY();
			int limitX = (downX - currentX) > 0 ? (downX - currentX):(currentX - downX);
			int limitY = (downY - currentY) > 0 ? (downY - currentY):(currentY - downY);
			isMove = !(limitX < 5 || limitY < 5);
			if (!isMove) {// 是否是移动，主要是区分click
				manager.showContent();
			}
			break;
		}
		return super.onTouchEvent(event);
	}
}
