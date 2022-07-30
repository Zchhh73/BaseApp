package com.zch.network.bean


/**
 *
 * classtime-android
 * Description: 网络数据基类
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
data class BaseResult<T>(
    val msg: String,
    val code: Int,
    val data: T
) : IBaseResponse<T> {

    override fun code() = code

    override fun msg() = msg

    override fun data() = data

    override fun isSuccess() = code == 0

}