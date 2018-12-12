package com.leyou.library.le_library.comm.collection

import android.content.Context
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation
import com.leyou.library.le_library.config.LeConstant
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI
import org.json.JSONObject


/**
 * 数据采集的代理类，所有数据采集相关代码写在此页面
 * Created by liuyuhang on 2017/12/18.
 */

object AppTrackHelper {

    /**
     * 必须在application中初始化
     */
    fun init(context: Context?, channel: String?) {
        //神策
        val SA_DEBUG_MODE = if (LeConstant.BuildType.buildMode == LeConstant.BuildType.BUILD_MODE_RELEASE) {
            SensorsDataAPI.DebugMode.DEBUG_OFF
        } else {
            SensorsDataAPI.DebugMode.DEBUG_AND_TRACK
        }
        val SENSOR_API_URL = when (LeConstant.BuildType.buildMode) {
            LeConstant.BuildType.BUILD_MODE_RELEASE,
            LeConstant.BuildType.BUILD_MODE_RELEASE_LOG,
            LeConstant.BuildType.BUILD_MODE_RC -> "http://bi.ruiyunit.com:8106/sa?project=production"
            else -> "http://bi.ruiyunit.com:8106/sa?project=default"
        }
        SensorsDataAPI.sharedInstance(context, SENSOR_API_URL, SA_DEBUG_MODE)
        try {
            // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
            val eventTypeList = arrayListOf<SensorsDataAPI.AutoTrackEventType>()
            // $AppStart
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START)
            // $AppEnd
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END)
            // $AppViewScreen
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN)
            // $AppClick
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK)
            SensorsDataAPI.sharedInstance(context).enableAutoTrack(eventTypeList)
            // 开启点击图
            SensorsDataAPI.sharedInstance().enableHeatMap()
            SensorsDataAPI.sharedInstance().enableAppHeatMapConfirmDialog(false)
//            onTrackInstallation(context, channel)
            login(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onTrackInstallation(context: Context, channel: String?) {
        try {
            val properties = JSONObject()
            // 设置渠道名称属性
            properties.put("\$utm_source", channel ?: "develop")
            // AppInstall 事件名称，此处仅仅是一个示例，可以根据实际需求自定义事件名称
            // 记录激活事件，追踪渠道效果
            SensorsDataAPI.sharedInstance(context).trackInstallation("AppInstall", properties)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onBasePageViewEvent(context: Context, impl: AppTrackInterface) {
        if (impl.isCustomPageTopicTrack) {
            val pageTopic = impl.customPageTopic
            if (!(pageTopic == null || pageTopic == "")) {
                AppTrackHelper.onAppPageViewEvent(context, pageTopic)
            }
        }
    }

    fun onAppPageViewEvent(context: Context?, pageTopic: String) {
        val properties = JSONObject()
        properties.put("pageName", pageTopic)
        onEvent(context, "appPageView", properties)
    }

    fun onAppWebViewPageViewEvent(context: Context?, pageTopic: String) {
        val properties = JSONObject()
        properties.put("pageType", "活动页")
        properties.put("pageName", pageTopic)
        onEvent(context, "appPageView", properties)
    }

    /**
     * 事件埋点
     *
     */
    fun onEvent(context: Context?, eventName: String?, properties: JSONObject?) {
        SensorsDataAPI.sharedInstance(context).track(eventName, properties)
    }

    fun onEvent(context: Context?, eventName: String?, paramsMap: HashMap<String, Any>?) {
        val properties = JSONObject()
        paramsMap?.forEach {
            properties.put(it.key, it.value)
        }

        onEvent(context, eventName, properties)
    }

    fun login(context: Context?) {
        SensorsDataAPI.sharedInstance().login(TokenOperation.getUserId(context))
    }

    fun logout() {
        SensorsDataAPI.sharedInstance().logout()
    }


}