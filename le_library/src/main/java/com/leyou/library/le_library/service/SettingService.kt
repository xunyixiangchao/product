package com.leyou.library.le_library.service

import android.content.Context
import com.ichsy.libs.core.net.http.HttpContext
import com.ichsy.libs.core.net.http.RequestListener
import com.ichsy.libs.core.net.http.cache.SimpleCacheAdapter
import com.leyou.library.le_library.comm.network.LeHttpHelper
import com.leyou.library.le_library.config.LeConstant
import com.leyou.library.le_library.model.LeSetting
import com.leyou.library.le_library.model.response.SystemSettingResponse

/**
 *
 * 配置接口数据
 * Created by liuyuhang on 2018/2/7.
 */

class SettingService : IBusinessService {
    private var settingInfo: LeSetting? = null

//    private lateinit var context: Context

    /**
     * 本次是否从服务器更新成功
     */
    private var updateSuccess = false

    companion object {
        private val instance by lazy { SettingService() }

        fun get(): SettingService = instance
    }

    override fun install(context: Context) {
        super.install(context)

//        this@SettingService.context = context
        requestAppSetting(context)
    }

    /**
     * 获取
     */
    private fun getSettingInfo(context: Context): LeSetting? {
        if (null == settingInfo || !updateSuccess) {
            requestAppSetting(context)
        }
        return settingInfo
    }

    /**
     * 从网络获取app配置信息
     *
     */
    private fun requestAppSetting(context: Context) {
        val httpHelper = LeHttpHelper(context)

        val cacheAdapter = SimpleCacheAdapter(context, httpHelper)
        cacheAdapter.doPost(LeConstant.API.URL_BASE + LeConstant.API.URL_GLOBAL_SETTING, null, SystemSettingResponse::class.java, object : RequestListener() {
            override fun onHttpRequestComplete(url: String, httpContext: HttpContext) {
                super.onHttpRequestComplete(url, httpContext)

                val response = httpContext.getResponseObject<SystemSettingResponse>()
                //
                if (response.header.res_code == 0) {
                    updateSuccess = true
                    settingInfo = response.body
                } else {
                    updateSuccess = false
                }
            }
        }
        )
    }
}