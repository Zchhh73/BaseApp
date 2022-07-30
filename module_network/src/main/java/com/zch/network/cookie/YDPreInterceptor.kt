package com.zch.network.cookie

import com.zch.network.constants.NetWorkConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 用来预处理Cookie的，把Http请求的Header的Cookie重命名为其他的字段，防止被OkHttp的BridgeIntercepter覆盖
 */
class YDPreInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val userRequest = chain.request()
        val requestBuilder = userRequest.newBuilder()
        if (userRequest.header(COOKIE) != null) {
            requestBuilder.addHeader(NetWorkConfig.getInstance().getCookieMock(), userRequest.header(COOKIE)!!)
        }
        val networkResponse = chain.proceed(requestBuilder.build())
        val responseBuilder = networkResponse.newBuilder()
                .request(userRequest)
        return responseBuilder.build()

    }

    companion object {
        private const val COOKIE = "Cookie"
    }
}