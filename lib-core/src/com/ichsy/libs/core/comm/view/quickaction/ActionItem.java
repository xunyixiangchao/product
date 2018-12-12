package com.ichsy.libs.core.comm.view.quickaction;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ActionItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Drawable mIcon;
	private String mTitle;
	private int mActionId = -1;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public ActionItem(Context context, String title, int actionId, int resourceId) {
		mIcon = context.getResources().getDrawable( resourceId );
		mTitle = title;
		mActionId = actionId;
	}
	
	// ===========================================================
	// Public Methods
	// ===========================================================

	public Drawable getIcon() {
		return mIcon;
	}

	public void setIcon(Drawable icon) {
		this.mIcon = icon;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public int getActionId() {
		return mActionId;
	}

	public void setActionId(int actionId) {
		this.mActionId = actionId;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Private Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
