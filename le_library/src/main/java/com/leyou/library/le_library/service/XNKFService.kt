package com.leyou.library.le_library.service

import android.content.Context
import cn.xiaoneng.uiapi.Ntalker
import com.capelabs.leyou.xiaoneng.XNHelper
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation
import com.leyou.library.le_library.config.LeConstant
import com.leyou.library.le_library.config.LeConstant.BuildType.BUILD_MODE_BETA
import com.leyou.library.le_library.config.LeSettingInfo
import library.liuyh.com.lelibrary.R


/**
 * 小能客服服务
 * Created by liuyuhang on 2017/8/15.
 */
class XNKFService : IBusinessService {

    interface UnReadMessage {
        fun onMessageUnRead(message: String?, unReadCount: Int)
    }

    companion object {
        const val TALK_ID_MESSAGE_CENTER = "message_center"//消息中心
        const val TALK_ID_AFTER_SALE = "after_sale"//售后，退货位置
        const val TALK_ID_ORDER_INFO = "order_info"//订单列表
        const val TALK_ID_O2O_ORDER_INFO = "o2o_order_info"//o2o订单列表
        const val TALK_ID_PRODUCT_INFO = "product_detail"//商品详情
        const val TALK_ID_O2O_PRODUCT_INFO = "o2o_product_detail"//o2o商品详情

        /**
         * 从配置接口中获取客服id
         */
        fun getTalkId(talkId: String): String {
            return if (LeConstant.BuildType.buildMode == BUILD_MODE_BETA) { //测试环境返回测试环境的id
                LeConstant.N_TALK.TEST_ID
            } else {
                val talkMap = LeSettingInfo.INSTANCE.setting.kfSetting?.kf_talk_android_map
                talkMap?.get(talkId) ?: LeConstant.N_TALK.SETTING_ID
            }

        }

        /**
         * 登陆
         */
        fun loginXNKF(context: Context) {
            if (TokenOperation.isLogin(context)) {
                XNHelper.getInstance().login(TokenOperation.getUser(context).user_id, TokenOperation.getUser(context).nickname, 0)
            }
        }

        fun logoutXNFK() {
            XNHelper.getInstance().logout()
        }

        /**
         * 与客服打开聊天窗口
         */
        fun talk(context: Context, talkId: String, title: String) {
            if (!TokenOperation.isLogin(context)) {
                TokenOperation.jumpLogin(context)
                return
            }
            XNHelper.getInstance().startChat(context, talkId, title, null)
        }

        /**
         * 获取最后一条聊天记录
         */
        fun getLastMessage(): String? {
            val list = XNHelper.getInstance().list

            if (list?.isNotEmpty() == true) {
                return list[0]?.get("textmsg") as String?
            } else {
                return null
            }

        }

        /**
         *获取是否有未读消息标识
         * return true:有未读，false，没有未读
         */
        fun hashUnReadMessage(): Boolean {
            val list = XNHelper.getInstance().list

            if (list?.isNotEmpty() ?: false) {
                return list[0]?.get("isunread") as? Boolean? ?: false
            } else {
                return false
            }
        }

        fun setOnUnReadmsgListener(listener: UnReadMessage) {
            //获取未读消息数
            Ntalker.getExtendInstance().message().setOnUnreadmsgListener { settingId, userName, msgContent, messageCount ->

                ThreadPoolUtil.runOnMainThread {
                    listener.onMessageUnRead(msgContent, messageCount)
//                    listener.onUnReadMsg(settingId, userName, msgContent, messageCount)
                }
//                runOnUiThread {
//                    val item = adapter.getItem(0)
//
//                    val hasUnReadMessage = messageCount > 0
//
//                    item?.content = msgContent ?: "点此查看与客服聊天记录"
//                    item?.isNewMessage = hasUnReadMessage
//                    adapter.notifyDataSetChanged()
//                }

            }
        }

    }

    override fun install(context: Context) {
        super.install(context)
//        loginXNKF(context)
        XNHelper.getInstance().init(context.applicationContext, LeConstant.N_TALK.SITE_ID, LeConstant.N_TALK.SDK_KEY, context.resources.getString(R.string.app_name))
        //XNHelper.getInstance().isDebug(true)
    }

    override fun unInstall(context: Context) {
        super.unInstall(context)
    }
}