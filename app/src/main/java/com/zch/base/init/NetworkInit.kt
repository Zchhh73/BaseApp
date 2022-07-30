package com.zch.base.init

import android.content.Context
import com.zch.base.BuildConfig
import com.zch.base.constants.HttpConsts
import com.zch.network.RetrofitClient
import com.zch.network.constants.NetWorkConfig
import com.zch.network.retrofithelper.interceptor.HeaderInterceptor
import okhttp3.Interceptor
import java.util.*

/**
 * AI课
 * aicourse-android
 * Description: 网络初始化相关
 * Created by chenhanzhang on 2022/01/11 11:06 上午
 *
 **/
object NetworkInit {

    fun initInternet(context: Context) {
        initNetwork(context)
    }


    private fun initNetwork(context: Context) {
        val hotDomainList = ArrayList<String>()
        val interceptorList = ArrayList<Interceptor>()
        interceptorList.add(HeaderInterceptor())
        NetWorkConfig.getInstance()
            .setBaseUrl(HttpConsts.BASE_AICOURSE_URL)
            .setLogEnabled(BuildConfig.DEBUG)
            .setHotDomainList(hotDomainList)
            .setInterceptorList(interceptorList)
        RetrofitClient.getInstance().createClient(context)
    }
}