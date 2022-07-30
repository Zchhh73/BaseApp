package com.zch.base.listener

/**
 * 通用Base组件
 *
 * Description: 对话框加载监听
 * Created by zch on 2022/8/1 20:14
 *
 **/
interface OnLoadingViewListener {
    fun onShowLoadingDialog()
    fun onShowLoadingDialog(loadingText: String)
    fun onDismissLoadingDialog()
}