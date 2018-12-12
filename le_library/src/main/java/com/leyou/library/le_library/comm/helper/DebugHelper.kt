package com.leyou.library.le_library.comm.helper

import android.content.Context
import com.ichsy.libs.core.comm.bus.url.UrlParser
import com.ichsy.libs.core.comm.utils.LogUtils

/**
 *
 * Created by liuyuhang on 2017/6/6.
 */

class DebugHelper {
    var debugOpenerTime: Long = 0
    var debugOpenerCount = 0

    companion object Instance {
        private var isDebugOpen: Boolean = false

        val debugHelper = DebugHelper()

        fun isOpenDebug(): Boolean {
            return isDebugOpen
        }


        fun setOpenDebug() {
            isDebugOpen = true
        }
    }


    /**
     * 尝试打开debug模式
     */
    fun tryOpen(callback: () -> Unit) {
        if (isDebugOpen) {
            callback()
        } else {
            if (debugOpenerCount < 10) {
                val nowTime = System.currentTimeMillis()
                if (debugOpenerTime == 0L) {
                    debugOpenerTime = nowTime
                } else if (nowTime - debugOpenerTime < 200) {
                    debugOpenerTime = nowTime
                    debugOpenerCount++

                    LogUtils.i("lyh", "open count:" + debugOpenerCount)
                } else {
                    debugOpenerTime = 0
                    debugOpenerCount = 0
                }
            } else {
                setOpenDebug()
                callback()
//                openDebug(context, url)
            }
        }
    }


    private fun openDebug(context: Context, url: String) {
//        LogUtils.i("lyh", "open debug activity")
        UrlParser.getInstance().parser(context, url)
    }
}
