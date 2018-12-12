package com.leyou.library.le_library.comm.helper

import android.content.Context
import com.ichsy.libs.core.dao.SharedPreferencesProvider
import com.leyou.library.le_library.model.LocationCityInfo

/**
 * 定位相关
 * Created by liuyuhang on 2017/6/20.
 */
class CitiesHelper {

    companion object Instance {
        private val CACHE_KEY_CITY = "CACHE_KEY_CITY"
        private val CACHE_KEY_CITIES_LIST_DATA = "CACHE_KEY_CITIES_LIST_DATA"

        fun saveCurrentCity2Cache(context: Context, cityInfo: LocationCityInfo) {
            SharedPreferencesProvider().getProvider(context).putCache(CACHE_KEY_CITY, cityInfo)
        }

        /**
         *获取当前存储在缓存中的当前选择城市，如果没有，会获取北京
         */
        fun getCurrentCityFromCacheWithDefault(context: Context): LocationCityInfo {
//            var cacheCity = getCurrentCityFromCache(context)
//            if (null == cacheCity) {
//                cacheCity = LocationCityInfo("110100", "北京市")//如果获取不到，设置默认为北京的
////                saveCurrentCity2Cache(context, cacheCity)
//            }
//            return cacheCity
            return getCurrentCityFromCacheWithDefault(context, LocationCityInfo("110100", "北京市"))
        }

        /**
         *获取当前存储在缓存中的当前选择城市，如果没有，会获取传递的城市
         */
        private fun getCurrentCityFromCacheWithDefault(context: Context, defaultCity: LocationCityInfo): LocationCityInfo {
            var cacheCity = getCurrentCityFromCache(context)
            if (null == cacheCity) {
                cacheCity = defaultCity//如果获取不到，设置默认为北京的
//                saveCurrentCity2Cache(context, cacheCity)
            }
            return cacheCity
        }


        /**
         *获取当前存储在缓存中的当前选择城市
         */
        fun getCurrentCityFromCache(context: Context): LocationCityInfo? {
            return SharedPreferencesProvider().getProvider(context).getCache(CACHE_KEY_CITY, LocationCityInfo::class.java)
//            if (null == cacheCity) {
//                cacheCity = LocationCityInfo("110100", "北京市")//如果获取不到，设置默认为北京的
//                saveCurrentCity2Cache(context, cacheCity)
//            }
//            return cacheCity
        }


        /**
         * 保存服务端获取的city列表
         */
        fun saveSeverCityList(context: Context, cities: List<LocationCityInfo>?) {
            if (cities != null && cities.isNotEmpty()) {
                val cacheData = CitiesCacheData()
                cacheData.cities = cities
                SharedPreferencesProvider().getProvider(context).putCache(CACHE_KEY_CITIES_LIST_DATA, cacheData)
            }

        }

        fun getSeverCityList(context: Context): List<LocationCityInfo>? {
            val cacheData = SharedPreferencesProvider().getProvider(context).getCache(CACHE_KEY_CITIES_LIST_DATA, CitiesCacheData::class.java)
            return cacheData?.cities ?: listOf<LocationCityInfo>()
        }
    }

    class CitiesCacheData {
        var cities: List<LocationCityInfo>? = null
    }

}