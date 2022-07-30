package com.zch.base.network.network


import com.zch.base.BuildConfig
import com.zch.base.constants.Constss
import com.zch.network.cookie.ForwardingCookieHandler
import com.zch.network.cookie.YDBridgeInterceptor
import com.zch.network.cookie.YDCookieJarContainer
import com.zch.network.cookie.YDPreInterceptor
import com.zch.network.interceptor.LoggingInterceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.ProxySelector
import java.util.concurrent.TimeUnit

object OkHttpClientProvider {

    private var sClient: OkHttpClient? = null

    const val COOKIE_MOCK = "course_mock_cookie"

    val okHttpClient: OkHttpClient
        @Synchronized
        get() {
            if (sClient == null) {
                sClient = createClient()
            }
            return sClient as OkHttpClient
        }

    private fun createClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)


        val mCookieHandler = ForwardingCookieHandler(Constss.application)
        val print=if (BuildConfig.DEBUG){
            LoggingInterceptor.Level.ALL
        }else{
            LoggingInterceptor.Level.NONE
        }
        val mCookieJarContainer = YDCookieJarContainer()
        mCookieJarContainer.setCookieJar(JavaNetCookieJar(mCookieHandler))
        builder.cookieJar(mCookieJarContainer)
                .addInterceptor(YDPreInterceptor())
                .addNetworkInterceptor(LoggingInterceptor(print))
                .addNetworkInterceptor(YDBridgeInterceptor())
                .proxySelector(ProxySelector.getDefault())
                .build()

        // No timeouts by default
        return builder.build()
    }
}