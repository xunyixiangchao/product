package com.leyou.library.le_library.config

/**
 * 拼接api链接的工具类
 * Created by liuyuhang on 2017/8/29.
 */
class UrlProvider {

    companion object {

        fun getPhpUrl(url: String): String {
            return LeConstant.UrlConstant.USER_URL_BASE + format(url)
        }

        fun getB2cUrl(url: String): String {
            return LeConstant.API.URL_BASE + format(url)
        }

        fun getO2oUrl(url: String): String {
            return LeConstant.API.URL_BASE_O2O + format(url)
        }

        private fun format(url: String): String {
            return if (!url.endsWith("/")) {
                "$url/"
            } else {
                url
            }
        }

    }
}