package com.zch.base.base

import android.app.Application
import android.content.Context
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSize

/**
 * 通用Base组件
 * Description: application基类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
open class BaseApplication : Application() {
    companion object {

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, layout: RefreshLayout? ->
                ClassicsHeader(
                    context
                )
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, layout: RefreshLayout? ->
                ClassicsFooter(
                    context
                ).setDrawableSize(20f)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initAutoSize()
        MMKV.initialize(this)


    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

    }

    private fun initAutoSize() {
        AutoSize.checkAndInit(this);
    }
}