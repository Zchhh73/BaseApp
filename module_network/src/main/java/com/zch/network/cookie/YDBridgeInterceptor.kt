package com.zch.network.cookie

import com.zch.network.constants.NetWorkConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.lang.StringBuilder


/**
 * 用来处理Cookie的，把YDPreInterceptor预处理的cookie给捞回来，曲线救国
 */
class YDBridgeInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val userRequest = chain.request()
        val requestBuilder = userRequest.newBuilder()
        var cookieAdd: String? = ""
        if (userRequest.header(NetWorkConfig.getInstance().getCookieMock()) != null) {
            cookieAdd = userRequest.header(NetWorkConfig.getInstance().getCookieMock())
            requestBuilder.removeHeader(NetWorkConfig.getInstance().getCookieMock())
        }

        var cookieString = if (userRequest.header(COOKIE) != null) {
            userRequest.header(COOKIE) + ";" + cookieAdd
        } else {
            cookieAdd!!
        }

        requestBuilder.header(COOKIE, duplicateCookieRemove(cookieString))

        val networkResponse = chain.proceed(requestBuilder.build())
        val responseBuilder = networkResponse.newBuilder()
                .request(userRequest)
        return responseBuilder.build()

    }

    /**
     * 去掉重复的cookie
     */
    private fun duplicateCookieRemove(cookieString: String): String {
        val cookieList = cookieString.split(";")
        val map = hashMapOf<String, String>()
        var list: List<String>
        for (cookieStr in cookieList) {
            list = cookieStr.split("=")
            if (list.size == 1) {
                map[list[0].trim()] = ""
            } else if (list.size > 1) {
                map[list[0].trim()] = list[1]
            }
        }

        var sb = StringBuilder()
        for (it in map) {
            sb.append(it.key).append("=").append(it.value).append(";")
        }
        return sb.toString()
    }

    companion object {

        private val COOKIE = "Cookie"
    }
}