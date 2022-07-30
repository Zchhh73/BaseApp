package com.zch.base.constants

import android.text.TextUtils
import com.zch.utils.MMKVUtils

/**
 *
 * BaseDemo
 * Description:
 * Created by chenhanzhang on 2022/7/30 21:32
 *
 **/
object HttpConsts {
    val BASE_AICOURSE_URL: String = getAICourseBaseUrl()

    /**
     * 线上
     */
    const val AICOURSE_SERVER_BASE_URL_ONLINE = "https://ai.ydshengxue.com/"
    /**
     * 预发
     */
    const val AICOURSE_SERVER_BASE_URL_PRE = "https://hikari-ai-gateway-user-pre.inner.ydshengxue.com/"
    /**
     * 测试
     */
    const val AICOURSE_SERVER_BASE_URL_TEST_INNER = "https://hikari-ai-gateway-user.inner.ydshengxue.com/"

    /**
     * 商品服务Url
     */
    private fun getAICourseBaseUrl(): String {
        val devModeUrl = MMKVUtils.getString(MMKVConsts.AICOURSE_DEV_MODE_BASE_URL)
        return if (TextUtils.isEmpty(devModeUrl)) {
            AICOURSE_SERVER_BASE_URL_ONLINE
        } else {
            devModeUrl!!
        }
    }
}