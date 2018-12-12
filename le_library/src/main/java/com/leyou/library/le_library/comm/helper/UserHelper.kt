package com.leyou.library.le_library.comm.helper

import android.content.Context
import com.ichsy.libs.core.net.http.RequestListener
import com.ichsy.libs.core.net.http.RequestOptions
import com.leyou.library.le_library.comm.network.LeHttpHelper
import com.leyou.library.le_library.config.UrlProvider
import com.leyou.library.le_library.model.BaseRequest
import com.leyou.library.le_library.model.BaseResponse
import com.leyou.library.le_library.model.ImResponse

/**
 * 用户相关的工具类
 * Created by liuyuhang on 2017/12/13.
 */
class UserHelper {

    companion object {

        /**
         * 通过服务端同步未读消息数量
         */
        fun syncImUnreadCountFromServer(context: Context, listener: RequestListener) {
            val requestOperation = RequestOptions()
            requestOperation.toastDisplay(false)
            val httpHelper = LeHttpHelper(context, requestOperation)
            httpHelper.post(UrlProvider.getPhpUrl("im/getImCount"), BaseRequest(), ImResponse::class.java, listener)
        }

        /**
         * 从服务器清除环信消息
         */
        fun cleanImUnreadCount(context: Context, listener: RequestListener) {
            val httpHelper = LeHttpHelper(context)
            httpHelper.post(UrlProvider.getPhpUrl("im/clearImCount"), BaseRequest(), BaseResponse::class.java, listener)
        }
    }

}