package com.leyou.library.le_library.comm.helper

import android.content.Context
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation
import com.leyou.library.le_library.comm.utils.BarCodeUtil
import com.leyou.library.le_library.comm.utils.GlobalUtil
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * 一维码源码生成工具
 * Created by liuyuhang on 2017/8/10.
 */
class BarCodeHelper {

    companion object {
        var differentTime = -1L
        var serverUpdateTimeSuccess = false

        /**
         * 根据系统时间，初始化时间
         */
        fun updateSeverTime(context: Context, severTime: String) {
            val currentTime = System.currentTimeMillis()

            differentTime = severTime.toLong() - currentTime

            GlobalUtil.saveTimeDiff(context, differentTime)

            serverUpdateTimeSuccess = true
        }

        /**
         * 获取加密之前的原始key
         */
        private fun getSourceKey(context: Context): String? {
            val key =
                    if (TokenOperation.isLogin(context)) {
                        val trueTime = (System.currentTimeMillis() + differentTime).toString()

                        if (differentTime == -1L) {
                            differentTime = GlobalUtil.getTimeDiff(context)
                        }

//                        DateFormat.getDateTimeInstance().format()
                        val format = SimpleDateFormat("MMddHHmm")
                        val t1 = format.format(Date(trueTime.toLong()))

                        val uid = String.format("%011d", TokenOperation.getUserId(context).toLong())
                        t1 + uid
                    } else {
                        null
                    }
            return key
        }

        /**
         * 从服务端获取时间是否成功
         */
        fun isServerUpdateSuccess(): Boolean {
            return serverUpdateTimeSuccess
        }

        /**
         * 获取加密之后的一维码
         */
        fun getEncryptKey(context: Context): String? {
            var encryptValue: String? = null
            try {
                encryptValue = BarCodeUtil.enCode(getSourceKey(context))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return encryptValue
        }

        /**
         * 网络获取失败的时候获取这个方法编码
         */
        fun getDefaultEncryptKey(context: Context): String? {
            var encryptValue: String? = null
            try {
                val uid = String.format("%011d", TokenOperation.getUserId(context).toLong())
                val key = "00000000" + uid
                encryptValue = BarCodeUtil.enCode(key)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return encryptValue
        }
    }


}