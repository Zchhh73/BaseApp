package com.zch.base.util

import android.content.Context
import android.content.res.Configuration

/**
 * Description:
 * Created by chenhanzhang on 2022/7/13 12:32
 **/
object PadUtils {

    /**
     * 判断是否为Pad
     *
     * @param context
     * @return 是Pad返回true
     */
    fun isPad(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

}