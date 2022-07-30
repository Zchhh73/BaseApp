package com.zch.base.event
/**
 * 通用Base组件
 *
 * Description: 错误事件
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class ErrorEvent @JvmOverloads constructor(
    var type: Int = DATA_ERROR, var isShowErrorNet:Boolean=false
){

    companion object {
        const val HTTP_ERROR = 0
        const val DATA_ERROR = 1
    }
}