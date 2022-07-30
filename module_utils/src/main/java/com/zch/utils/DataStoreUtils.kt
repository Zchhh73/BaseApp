package com.zch.utils

import android.text.TextUtils

/**
 *
 * zch-android
 * Description: 本地保存配置
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class DataStoreUtils {
    companion object {

        /**
         * 设置token
         *
         * @param token
         */
        var token: String?
            get() = MMKVUtils.getString("token")
            set(token) {
                MMKVUtils.put("token", token)
            }

        /**
         * 判断是否登陆
         *
         * @return
         */
        val isLogin: Boolean
            get() = !TextUtils.isEmpty(token)

        /**
         * 是否需要更新
         */
        @JvmStatic
        var isUpdate: Boolean
            get() = MMKVUtils.getBoolean("isUpdate") == false
            set(isUpdate) {
                MMKVUtils.put("isUpdate", isUpdate)
            }

        /**
         * 是否第一次登录
         */
        @JvmStatic
        var isFirst: Boolean
            get() = MMKVUtils.getBoolean("isFirst") ==true
            set(isUpdate) {
                MMKVUtils.put("isFirst", isUpdate)
            }

    }

}