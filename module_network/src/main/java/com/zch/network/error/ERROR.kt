package com.zch.network.error

/**
 *
 * classtime-android
 * Description: 网络错误提示
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
enum class ERROR(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, "未知错误"),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "数据请求出错"),
    /**
     * 网络错误
     */
    NETWORD_ERROR(1002, "网络连接失败,请检查网络"),
    /**
     * 协议出错
     */
    HTTP_ERROR(1003, "服务异常，请重试"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "证书出错"),
    /**
     * 域名解析错误
     */
    UNKNOWN_HOST(1005, "域名解析错误"),
    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "连接超时,请稍后再试"),
    /**
     * 请求失败
     */
    REQUEST_FAILED(1007  , "请求失败");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}