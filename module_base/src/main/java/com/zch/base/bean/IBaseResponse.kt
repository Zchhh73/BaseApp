package com.zch.base.bean

/**
 * zch-android
 * Description: 网络数据接口
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
interface IBaseResponse<T> {
    fun code(): String
    fun msg(): String
    fun data(): T
    fun isSuccess(): Boolean
}