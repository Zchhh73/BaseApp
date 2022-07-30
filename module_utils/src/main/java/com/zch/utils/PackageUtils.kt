package com.zch.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi

/**
 *
 * zch-android
 * Description: 获取版本相关
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
object PackageUtils {
    fun haveApp(context: Context, packageName: String): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }
        val pm = context.packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (i in installedPackages.indices) {
            val packageInfo = installedPackages[i]
            if (packageName == packageInfo.packageName) {
                return true
            }
        }
        return false
    }

    /**
     * * 获取versionName
     */
    fun getVersionName(context: Context): String {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        var versionName = ""
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    /**
     * 获取versionCode
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    fun getVersionCode(context: Context?): Long {
        if (context == null) {
            return 0
        }
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        var versionCode: Long = 0
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            versionCode = packageInfo.longVersionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun getVersionCodeValue(context: Context): Int {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        var versionCode = 0
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }
}