package com.leyou.library.le_library.comm.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.ichsy.libs.core.comm.utils.DialogBuilder
import com.ichsy.libs.core.comm.utils.ViewUtil
import library.liuyh.com.lelibrary.R


/**
 * 构建dialogView
 * Created by liuyuhang on 2017/11/10.
 */
object DialogViewHelper {

    interface DialogBuilderInterface<in T> {
        fun onDialogItemCreate(position: Int, inflater: LayoutInflater, parent: ViewGroup?): View
        fun onDialogItemDraw(dialog: Dialog, position: Int, item: T, convertView: View)
        /**
         * 分割线创建
         */
        fun onLineDraw(): View?

        /**
         * 用户点击关闭
         */
        fun onTapClosed()
    }

//    fun buildLeDialog(context: Context): Dialog {
//        val mCameraDialog = Dialog(context, R.style.dialog_full_window_style)
//        mCameraDialog.setCanceledOnTouchOutside(true)
//        val window = mCameraDialog.window
//        if (null != window) {
//            val lp = window.attributes
//            //            lp.gravity = Gravity.CENTER;
//            lp.width = context.resources.displayMetrics.widthPixels
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//            mCameraDialog.window!!.attributes = lp
//        }
//        window.setWindowAnimations(com.ichsy.core_library.R.style.dialog_pop_from_bottom)
//
//        mCameraDialog.show()
//
//        DialogBuilder.setDialogWindowAttr(mCameraDialog, ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dip2px(context, 462f), Gravity.CENTER)
//        return mCameraDialog
//    }

//    class LeDialog {
//        val title: String
//    }

    /**
     * dialog的属性builder
     */
    class DialogViewBuilder {
        var title: String? = null
        var message: String? = null
        var contentView: View? = null
        var hideCloseBtn = false
        /**
         * dialog的最小高度
         */
        var dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        var maxHeight = 0
        var okBtn: String? = null
        var noBtn: String? = null
        var okListener: View.OnClickListener? = null
        var noListener: View.OnClickListener? = null
    }

//    fun buildLeDialog(context: Context, title: String?, contentView: View): Dialog {
//        return buildLeDialog(context, title, null, contentView, ViewGroup.LayoutParams.WRAP_CONTENT, null, null, null, null)
//    }

    /**
     * 白底圆角的dialog
     */
    @JvmStatic
    fun buildLeDialog(context: Context, builder: DialogViewBuilder): Dialog {
        val mCameraDialog = Dialog(context, R.style.dialog_full_window_style)
        mCameraDialog.setCanceledOnTouchOutside(true)
        val window = mCameraDialog.window
        if (null != window) {
            val lp = window.attributes
            //            lp.gravity = Gravity.CENTER;
            lp.width = context.resources.displayMetrics.widthPixels
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            mCameraDialog.window!!.attributes = lp
        }
        window.setWindowAnimations(R.style.dialog_pop_from_bottom)

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_le_base_layout, null)

        val closeBtn = dialogView.findViewById(R.id.btn_close)
        val dialogBg = dialogView.findViewById(R.id.group_dialog_background) as View

        val titleView = dialogView.findViewById(R.id.tv_title) as TextView
        val messageView = dialogView.findViewById(R.id.tv_message) as TextView
        val contentGroup = dialogView.findViewById(R.id.view_content) as LinearExtLayout
        val btnGroup = dialogView.findViewById(R.id.group_btn) as LinearLayout

        val leftBtn = btnGroup.findViewById(R.id.btn_left) as TextView
        val rightBtn = btnGroup.findViewById(R.id.btn_right) as TextView

        if (builder.maxHeight != 0) {
            contentGroup.setMaxHeight(builder.maxHeight.toFloat())
        }

        if (null != builder.contentView) {
            contentGroup.addView(builder.contentView)
        }

        if (null == builder.okBtn && null == builder.noBtn) {
            btnGroup.visibility = View.GONE
        } else {
            btnGroup.visibility = View.VISIBLE

            leftBtn.apply {
                text = builder.okBtn
                setOnClickListener {
                    mCameraDialog.dismiss()
                    builder.okListener?.onClick(it)
                }
            }

            rightBtn.apply {
                text = builder.noBtn
                setTextColor(context.resources.getColor(R.color.le_color_bg_primary))
                setOnClickListener {
                    mCameraDialog.dismiss()
                    builder.noListener?.onClick(it)
                }
            }
        }
        mCameraDialog.setContentView(dialogView)

        closeBtn.apply {
            setOnClickListener { mCameraDialog.dismiss() }
            visibility = if (builder.hideCloseBtn) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        titleView.apply {
            text = builder.title
            visibility = if (null == builder.title) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        messageView.apply {
            text = builder.message
            visibility = if (null == builder.message) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        mCameraDialog.show()

        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, builder.dialogHeight)
        lp.leftMargin = context.resources.getDimensionPixelSize(R.dimen.default_layout_padding)
        lp.rightMargin = context.resources.getDimensionPixelSize(R.dimen.default_layout_padding)
        dialogBg.layoutParams = lp
//
        DialogBuilder.setDialogWindowAttr(mCameraDialog, ViewGroup.LayoutParams.MATCH_PARENT, builder.dialogHeight, Gravity.CENTER)

        return mCameraDialog
    }


//    fun buildLeDialog(context: Context, title: String?, message: String?, contentView: View?, dialogHeight: Int, yesBtn: String?, noBtn: String?, yesListener: View.OnClickListener?, noListener: View.OnClickListener?): Dialog {
//        val mCameraDialog = Dialog(context, R.style.dialog_full_window_style)
//        mCameraDialog.setCanceledOnTouchOutside(true)
//        val window = mCameraDialog.window
//        if (null != window) {
//            val lp = window.attributes
//            //            lp.gravity = Gravity.CENTER;
//            lp.width = context.resources.displayMetrics.widthPixels
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//            mCameraDialog.window!!.attributes = lp
//        }
//        window.setWindowAnimations(com.ichsy.core_library.R.style.dialog_pop_from_bottom)
//
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_le_base_layout, null)
//
//        val titleView = dialogView.findViewById(R.id.tv_title) as TextView
//        val messageView = dialogView.findViewById(R.id.tv_message) as TextView
//        val contentGroup = dialogView.findViewById(R.id.view_content) as LinearLayout
//        val btnGroup = dialogView.findViewById(R.id.group_btn) as LinearLayout
//
//        val leftBtn = btnGroup.findViewById(R.id.btn_left) as TextView
//        val rightBtn = btnGroup.findViewById(R.id.btn_right) as TextView
//
//        if (null != contentView) {
//            contentGroup.addView(contentView)
//        }
//
//        if (null == yesBtn && null == noBtn) {
//            btnGroup.visibility = View.GONE
//        } else {
//            btnGroup.visibility = View.VISIBLE
//
//            leftBtn.apply {
//                text = yesBtn
//                setOnClickListener {
//                    mCameraDialog.dismiss()
//                    yesListener?.onClick(it)
//                }
//            }
//
//            rightBtn.apply {
//                text = noBtn
//                setTextColor(context.resources.getColor(R.color.le_color_bg_primary))
//                setOnClickListener {
//                    mCameraDialog.dismiss()
//                    noListener?.onClick(it)
//                }
//            }
//        }
//
//        mCameraDialog.setContentView(dialogView)
//
//        titleView.apply {
//            text = title
//            visibility = if (null == title) {
//                View.GONE
//            } else {
//                View.VISIBLE
//            }
//        }
//        messageView.apply {
//            text = message
//            visibility = if (null == message) {
//                View.GONE
//            } else {
//                View.VISIBLE
//            }
//        }
//
//        mCameraDialog.show()
////
//        DialogBuilder.setDialogWindowAttr(mCameraDialog, ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight, Gravity.CENTER)
//
//        return mCameraDialog
//    }


    fun <T> buildMenuDialogFromBottom(context: Activity, title: String, items: Array<T>, uiBuilder: DialogBuilderInterface<T>): Dialog {
        return buildMenuDialogFromBottom(context, title, items, 246f, uiBuilder)
    }

    fun <T> buildMenuDialogFromBottom(context: Context, title: String, items: Array<T>, dialogHeight: Float, uiBuilder: DialogBuilderInterface<T>): Dialog {
        val layoutInflater = LayoutInflater.from(context)
        val dialog = DialogBuilder.buildDialogFromBottom(context)
        val root = layoutInflater.inflate(R.layout.dialog_listview_layout, null)
        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dip2px(context, 288f))
        val dialogTitle = root.findViewById(R.id.relativelayout_dialog_title) as RelativeLayout
        if (TextUtils.isEmpty(title)) {
            dialogTitle.visibility = View.GONE
        } else {
            dialogTitle.visibility = View.VISIBLE
        }
        val closeButton = root.findViewById(R.id.iv_close) as ImageView
        val titleView = root.findViewById(R.id.tv_title) as TextView
        val menusGroup = root.findViewById(R.id.group_menus) as LinearLayout
        val contentScrollView = root.findViewById(R.id.sv_content) as ScrollView

        contentScrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                if (dialogHeight == 0f) {
                    LinearLayout.LayoutParams.WRAP_CONTENT
                } else {
                    ViewUtil.dip2px(context, dialogHeight)
                }
        )

        closeButton.setOnClickListener {
            uiBuilder.onTapClosed()
            dialog.dismiss()
        }

        titleView.text = title

        menusGroup.removeAllViews()

        items.forEachIndexed { index, t ->
            val childView = uiBuilder.onDialogItemCreate(index, layoutInflater, menusGroup)

            menusGroup.addView(childView)
            uiBuilder.onDialogItemDraw(dialog, index, items[index], childView)

            val line = uiBuilder.onLineDraw()
            if (null != line && index != items.size - 1) {
                menusGroup.addView(line)
            }
        }

        dialog.setContentView(root)
        DialogBuilder.setDialogWindowAttr(dialog, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
        return dialog
    }

    fun buildDialogFromBottom(context: Activity, title: String, defaultSelect: String?, dialogItems: Array<String?>, clickListener: DialogInterface.OnClickListener): Dialog {
//        val dialog = DialogBuilder.buildDialogFromBottom(context)
//
//        val dialogView = DialogViewHelper.buildMenuView(context.layoutInflater, title, defaultSelect, dialogItems, DialogInterface.OnClickListener { _, which ->
//            clickListener.onClick(dialog, which)
////            mSaleTypeView.text = dialogItems[which]
//            dialog.dismiss()
//        })
//        dialogView.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.setContentView(dialogView)
//        DialogBuilder.setDialogWindowAttr(dialog, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
//        return dialog

        return buildMenuDialogFromBottom(context, title, dialogItems, object : DialogBuilderInterface<String?> {
            /**
             * 用户点击关闭
             */
            override fun onTapClosed() {
            }

            override fun onLineDraw(): View? = null


            override fun onDialogItemCreate(position: Int, inflater: LayoutInflater, parent: ViewGroup?): View {
                return inflater.inflate(R.layout.activity_single_text_item, null)
            }

            override fun onDialogItemDraw(dialog: Dialog, position: Int, item: String?, convertView: View) {
                val inflater = context.layoutInflater
//                val itemView = inflater.inflate(R.layout.activity_single_text_item, null)
                val contentView = convertView.findViewById(R.id.tv_content) as TextView

                contentView.text = item

                if (defaultSelect?.isNotEmpty() == true && defaultSelect == item) {
                    contentView.setTextColor(inflater.context.resources.getColor(R.color.le_color_text_accent))
                } else {
                    contentView.setTextColor(inflater.context.resources.getColor(R.color.le_color_text_primary))
                }

                convertView.setOnClickListener {
                    dialog.dismiss()
                    clickListener.onClick(null, position)
//                    itemClick.onClick(null, index)
                }
            }

        })


    }
}