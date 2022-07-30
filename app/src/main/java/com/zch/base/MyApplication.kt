package com.zch.base

import android.app.Application
import com.zch.base.base.BaseApplication
import com.zch.base.constants.Constss
import com.zch.base.init.NetworkInit

/**
 *
 * BaseDemo
 * Description:
 * Created by chenhanzhang on 2022/7/30 21:21
 *
 **/
class MyApplication : BaseApplication() {

    companion object {
        private var application: Application? = null
        var passedPrivacy = true

        /**
         * 是否是调试模式
         */
        var ON_DEBUG: Boolean = BuildConfig.DEBUG

    }



    override fun onCreate() {
        super.onCreate()
        initGlobalConst()
        NetworkInit.initInternet(application!!)
    }

    private fun initGlobalConst() {
        application = this
        Constss.application = application
        Constss.passedPrivacy = passedPrivacy
    }
}