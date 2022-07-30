package com.zch.utils

import android.content.Context

/**
 *
 * zch-android
 * Description: 尺寸工具类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
object DensityUtils {
    private var screenW = 0
    private var screenH = 0
    private var screenDensity = 0f
    fun getScreenW(context: Context): Int {
        if (screenW == 0) {
            initScreen(context)
        }
        return screenW
    }

    fun getScreenH(context: Context): Int {
        if (screenH == 0) {
            initScreen(context)
        }
        return screenH
    }

    fun getScreenDensity(context: Context): Float {
        if (screenDensity == 0f) {
            initScreen(context)
        }
        return screenDensity
    }

    private fun initScreen(context: Context) {
        val metric = context.resources.displayMetrics
        screenW = metric.widthPixels
        screenH = metric.heightPixels
        screenDensity = metric.density
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        return (dpValue * getScreenDensity(context) + 0.5f).toInt()
    }

    fun px2dp(context: Context, pxValue: Int): Int {
        return (pxValue / getScreenDensity(context) + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2pxFloat(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f)
    }
}