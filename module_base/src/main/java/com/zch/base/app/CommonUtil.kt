package com.zch.base.app

/**
 * 通用Base组件
 *
 * Description: 公共配置
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
object CommonUtil {

    private val DEFULT = object : GlobalConfig {}

    private var mConfig: GlobalConfig = DEFULT

    fun install(config: GlobalConfig) {
        mConfig = config
    }

    fun getConfig() = mConfig

}