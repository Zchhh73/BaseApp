package com.zch.base.error

/**
 * zch-android
 * Description: 网络错误提示
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
enum class ERROR(private val code: String, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN("1000", "未知错误"),
    /**
     * 解析错误
     */
    PARSE_ERROR("1001", "数据请求出错"),
    /**
     * 网络错误
     */
    NETWORD_ERROR("1002", "网络连接失败,请检查网络"),
    /**
     * 协议出错
     */
    HTTP_ERROR("1003", "服务异常，请重试"),

    /**
     * 证书出错
     */
    SSL_ERROR("1004", "证书出错"),
    /**
     * 域名解析错误
     */
    UNKNOWN_HOST("1005", "域名解析错误"),
    /**
     * 连接超时
     */
    TIMEOUT_ERROR("1006", "连接超时,请稍后再试"),
    /**
     * 请求失败
     */
    REQUEST_FAILED("1007"  , "请求失败");

    fun getValue(): String {
        return err
    }

    fun getKey(): String {
        return code
    }

}