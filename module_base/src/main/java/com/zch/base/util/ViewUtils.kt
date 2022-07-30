package com.zch.base.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import java.lang.reflect.Field


/**
 * 通用Base组件
 *
 * Description: 视图工具类
 * Created by zch on 2022/8/1 18:04
 *
 */
object ViewUtils {
    /**
     * 计算状态栏高度
     *
     * @param context Context 对象
     * @return 状态栏高度值
     */
    fun getStatusBarHeight(context: Context): Int {
        val c: Class<*>
        val obj: Any
        val field: Field
        val x: Int
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            field.isAccessible = true
            x = field[obj].toString().toInt()
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    /**
     * 修改小米手机StatusBar样式颜色
     *
     * @param activity 要修改的Activity
     * @param darkmode 是否是darkmode
     * @return 是否已完成
     */
    fun setStatusBarDarkMode(darkmode: Boolean, activity: Activity) {
        if (isMIUI("xiaomi")){
            //前版本miui设置颜色
            val clazz: Class<out Window?> = activity.window.javaClass
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //新版本miui 颜色设置
        var flag =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() and (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        if (darkmode) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            flag = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
        activity.window.decorView.systemUiVisibility = flag
    }

    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val r = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            r.displayMetrics
        ).toInt()
    }

    fun setStatusBarFullTransparent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val window: Window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) { //19表示4.4
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    fun setHalfTransparent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val decorView: View = activity.window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (Build.VERSION.SDK_INT >= 19) { //19表示4.4
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 修改小米手机StatusBar样式颜色
     *
     * @param activity 要修改的Activity
     * @param darkmode 是否是darkmode
     * @return 是否已完成
     */
    fun setMiuiStatusBarDarkMode(activity: Activity, darkmode: Boolean): Boolean {
        if (!isMIUI("xiaomi")){
            return false
        }
        val clazz: Class<out Window?> = activity.window.javaClass
        try {
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            return true
        } catch (e: java.lang.Exception) {
//            e.printStackTrace();
        }
        return false
    }


    /**
     * 是否是小米手机
     */
    fun isMIUI(phoneName:String): Boolean {
        val manufacturer: String = Build.MANUFACTURER
        return phoneName.equals(manufacturer, ignoreCase = true)
    }

}