package com.leyou.library.le_library.comm.helper

import com.google.gson.reflect.TypeToken
import com.ichsy.libs.core.comm.utils.GsonHelper


/**
 * 协助crash上报的helper
 * Created by liuyuhang on 2017/12/8.
 */
class CrashHelper {
    data class CrashRequest(
            val url: String?//请求链接
            , val requestData: String?//请求数据
            , val responseData: String?//返回结果
            , val costTime: Long?//消耗时间
    )

    companion object {
        private val instance = CrashHelper()

        fun instance(): CrashHelper {
            return instance
        }
    }

    private val requestMap: LinkedHashMap<String, CrashRequest> = object : LinkedHashMap<String, CrashRequest>() {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, CrashRequest>?): Boolean {
            return size > 5
        }
    }

    /**
     * 加入列表
     */
    fun putRequest(request: CrashRequest) {
        requestMap.put("request", request)
    }

    fun getRequestMap(): String {
        return GsonHelper.build().toJson(requestMap, object : TypeToken<LinkedHashMap<String, CrashRequest>>() {}.type)
    }
}