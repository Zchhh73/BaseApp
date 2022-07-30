package com.zch.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent


/**
 *
 * zch-android
 * Description: 打开微信
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
object WxUtil {

    /**
     *打开微信
     */
    fun openWx(context: Context) {
        if (isWxVisible(context)) {
            val intent = Intent(Intent.ACTION_MAIN)
            val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            context.startActivity(intent)
        } else {
            ToastUtils.showToast(context, "您还没有安装微信，请先安装微信客户端!")
        }

    }

    /**
     * 判断 用户是否安装微信客户端
     */
    fun isWxVisible(context: Context): Boolean {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getInstalledPackages(0)
        for (i in packageInfo.indices) {
            val pn = packageInfo[i].packageName
            if (pn == "com.tencent.mm") {
                return true
            }
        }
        return false
    }


}