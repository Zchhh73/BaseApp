package com.zch.network.constants


import okhttp3.Interceptor
import java.util.ArrayList
import java.util.HashMap

/**
 *
 * classtime-android
 * Description: 域名配置
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
class NetWorkConfig {


    companion object {
        fun getInstance() = SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        val INSTANCE = NetWorkConfig()
    }


    /** 通用参数  */
    private var mParams: HashMap<String, Any>? = null

    /** 通用请求头  */
    private var mHeaders: HashMap<String, String>? = null

    /** 拦截器  */
    private var mInterceptorList: ArrayList<Interceptor> = ArrayList<Interceptor>()

    /** dns配置列表  */
    private var mHotDomainList: ArrayList<String> = ArrayList<String>()

    /** 日志开关  */
    private var mLogEnabled = true

    /** 是否开启cookie  */
    private var mIsCookie = false

    /** 重试次数  */
    private var mRetryCount = 0

    /** 重试时间  */
    private var mRetryTime: Long = 2000

    private lateinit var mBaseUrl: String

    private var mCookieMock = "course_mock_cookie"


    fun setBaseUrl(baseUrl: String): NetWorkConfig {
        mBaseUrl = baseUrl
        return this
    }

    fun setCookieMock(cookieMock: String): NetWorkConfig {
        mCookieMock = cookieMock
        return this
    }

    fun setIsCookie(isCookie: Boolean): NetWorkConfig {
        mIsCookie = isCookie
        return this
    }


    fun setInterceptorList(interceptorList: ArrayList<Interceptor>): NetWorkConfig {
        mInterceptorList = interceptorList
        return this
    }

    fun setHotDomainList(hotDomainList: ArrayList<String>): NetWorkConfig {
        mHotDomainList = hotDomainList
        return this
    }


    fun setParams(params: HashMap<String, Any>?): NetWorkConfig {
        var params = params
        if (params == null) {
            params = HashMap()
        }
        mParams = params
        return this
    }

    fun setHeaders(headers: HashMap<String, String>?): NetWorkConfig {
        var headers = headers
        if (headers == null) {
            headers = HashMap()
        }
        mHeaders = headers
        return this
    }

    fun addHeader(key: String?, value: String?): NetWorkConfig {
        if (key != null && value != null) {
            mHeaders!![key] = value
        }
        return this
    }

    fun removeHeader(key: String?): NetWorkConfig {
        if (key != null) {
            mHeaders!!.remove(key)
        }
        return this
    }

    fun addParam(key: String?, value: String?): NetWorkConfig {
        if (key != null && value != null) {
            mParams!![key] = value
        }
        return this
    }

    fun removeParam(key: String?): NetWorkConfig {
        if (key != null) {
            mParams!!.remove(key)
        }
        return this
    }


    fun setLogEnabled(enabled: Boolean): NetWorkConfig {
        mLogEnabled = enabled
        return this
    }

    fun setRetryCount(count: Int): NetWorkConfig {
        require(count >= 0) {
            // 重试次数必须大于等于 0 次
            "The number of retries must be greater than 0"
        }
        mRetryCount = count
        return this
    }

    fun setRetryTime(time: Long): NetWorkConfig {
        require(time >= 0) {
            // 重试时间必须大于等于 0 毫秒
            "The retry time must be greater than 0"
        }
        mRetryTime = time
        return this
    }

    fun getInterceptorList(): ArrayList<Interceptor> {
        return mInterceptorList
    }

    fun getHotDomainList(): ArrayList<String> {
        return mHotDomainList
    }

    fun getParams(): HashMap<String, Any>? {
        return mParams
    }

    fun getHeaders(): HashMap<String, String>? {
        return mHeaders
    }

    fun isLogEnabled(): Boolean {
        return mLogEnabled
    }

    fun isCookie(): Boolean {
        return mIsCookie
    }

    fun getRetryCount(): Int {
        return mRetryCount
    }

    fun getRetryTime(): Long {
        return mRetryTime
    }

    fun getBaseUrl(): String {
        return mBaseUrl
    }

    fun getCookieMock(): String {
        return mCookieMock
    }
}