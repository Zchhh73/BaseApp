package com.zch.base.event
/**
 * 通用Base组件
 *
 * Description: 消息体
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class Message @JvmOverloads constructor(
    var code: Int = 0,
    var msg: String = "",
    var arg1: Int = 0,
    var arg2: Int = 0,
    var obj: Any? = null
)