package com.zch.base.app

import androidx.lifecycle.ViewModelProvider
import com.zch.base.error.ExceptionHandle
import com.zch.base.base.ViewModelFactory

/**
 * 通用Base组件
 *
 * Description: 公共配置
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
interface GlobalConfig {

    fun viewModelFactory(): ViewModelProvider.Factory? = ViewModelFactory.getInstance()

    fun globalExceptionHandle(e: Throwable) = ExceptionHandle.handleException(e)

}