package com.zch.network


import android.content.Context
import com.zch.network.interceptor.LoggingInterceptor
import com.zch.network.utils.GsonTypeAdapterFactory
import com.google.gson.GsonBuilder
import com.zch.network.constants.NetWorkConfig
import com.zch.network.cookie.ForwardingCookieHandler
import com.zch.network.cookie.YDBridgeInterceptor
import com.zch.network.cookie.YDCookieJarContainer
import com.zch.network.cookie.YDPreInterceptor
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ProxySelector
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 *
 * classtime-android
 * Description: 网络封装
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
class RetrofitClient {

    companion object {
        fun getInstance() = SingletonHolder.INSTANCE
        private lateinit var retrofit: Retrofit
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapterFactory(
                        GsonTypeAdapterFactory()
                    ).create()
                )
            )
            .baseUrl(NetWorkConfig.getInstance().getBaseUrl())
            .build()
    }


    private fun getOkHttpClient(): OkHttpClient {
        val printer = if (BuildConfig.DEBUG) {
            LoggingInterceptor.Level.ALL
        } else {
            LoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .addInterceptor(YDPreInterceptor())
            .addNetworkInterceptor(YDBridgeInterceptor())
            .addNetworkInterceptor(LoggingInterceptor(printer))
            .proxySelector(ProxySelector.getDefault())
            .build()
    }

    fun <T> create(service: Class<T>?): T =
        retrofit.create(service!!) ?: throw RuntimeException("Api service is null!")

    private fun buildRetrofit(okHttpClient: OkHttpClient) {
        retrofit = Retrofit.Builder()
            .baseUrl(NetWorkConfig.getInstance().getBaseUrl())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapterFactory(
                        GsonTypeAdapterFactory()
                    ).create()
                )
            )
            .client(okHttpClient)
            .build()
    }

    /**
     * 从外面传入OkHttpClient实例
     *
     * @param okHttpClient
     */
    fun setOkHttpClient(okHttpClient: OkHttpClient) {
        buildRetrofit(okHttpClient)
    }


     fun createClient(
        context: Context,
        hotDomainList: ArrayList<String> = ArrayList<String>(),
        interceptorList: ArrayList<Interceptor> = ArrayList<Interceptor>()
    ) {

        val printer = if (BuildConfig.DEBUG) {
            LoggingInterceptor.Level.ALL
        } else {
            LoggingInterceptor.Level.NONE
        }
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.MILLISECONDS)

        for (interceptor in interceptorList){
            builder.addInterceptor(interceptor)
        }
        val mCookieHandler = ForwardingCookieHandler(context.applicationContext)
        val mCookieJarContainer = YDCookieJarContainer()
        mCookieJarContainer.setCookieJar(JavaNetCookieJar(mCookieHandler))
        builder.cookieJar(mCookieJarContainer)
            .addInterceptor(YDPreInterceptor())
            .addNetworkInterceptor(YDBridgeInterceptor())
            .addNetworkInterceptor(LoggingInterceptor(printer))
            .proxySelector(ProxySelector.getDefault())
            .build()

         retrofit = Retrofit.Builder()
             .baseUrl(NetWorkConfig.getInstance().getBaseUrl())
             .addConverterFactory(
                 GsonConverterFactory.create(
                     GsonBuilder().registerTypeAdapterFactory(
                         GsonTypeAdapterFactory()
                     ).create()
                 )
             )
             .client(builder.build())
             .build()
    }

}