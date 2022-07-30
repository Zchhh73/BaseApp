package com.zch.network.bean

/**
 *
 * classtime-android
 * Description: 网络数据接口
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
interface IBaseResponse<T> {
    fun code(): Int
    fun msg(): String
    fun data(): T
    fun isSuccess(): Boolean
}