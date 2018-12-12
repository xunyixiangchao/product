package com.ichsy.libs.core.frame;

import com.ichsy.libs.core.comm.view.dialog.DialogHUB;
import com.ichsy.libs.core.frame.BaseFrameActivity;

/**
 * 带有自定义DialogHub的activity基类
 * Created by liuyuhang on 2017/2/22.
 */

public abstract class BaseDialogFrameActivity extends BaseFrameActivity {
    // 进度条
    protected DialogHUB mDialogHUB;

    public DialogHUB getDialogHUB() {
        if (mDialogHUB == null) {
            mDialogHUB = initDialogHub();
        }
        return mDialogHUB;
    }

    /**
     * 绑定ProgressHub
     *
     * @return
     */
    public DialogHUB initDialogHub() {
        DialogHUB dialogHUB = new DialogHUB();
        dialogHUB.bindDialog(this);
        return dialogHUB;
    }
}
