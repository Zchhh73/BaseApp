package com.zch.base.bean


/**
 * zch-android
 * Description: 网络数据基类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
data class BaseResult<T>(
    val msg: String,
    val code: String,
    val data: T
) : IBaseResponse<T> {

    override fun code() = code

    override fun msg() = msg

    override fun data() = data

    override fun isSuccess() = code.endsWith("0000")

}