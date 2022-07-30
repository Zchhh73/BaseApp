package com.zch.base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
/**
 * 通用Base组件
 *
 * Description: viewModel工厂类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class ViewModelFactory : ViewModelProvider.Factory {

    companion object {
        private var sInstance: ViewModelFactory? = null

        fun getInstance() = sInstance ?: ViewModelFactory().also { sInstance = it }
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.newInstance()
    }
}