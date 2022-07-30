package com.zch.basedemo.network.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * Description: 请求头
 * Created by zhangchenhan on 2022/01/11 5:41 下午
 *
 **/
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorised = originalRequest.newBuilder().build()
        return chain.proceed(authorised)

    }
}