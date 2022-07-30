package com.zch.base.base

import android.view.View

/**
 * 通用Base组件
 *
 * Description: 返回点击事件
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
abstract class CommonClick {
    /**
     * 返回键
     * @param view
     */
    abstract fun onBack(view: View)
}