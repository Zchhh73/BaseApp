package com.zch.utils

import android.content.Context
import android.widget.Toast

/**
 *
 * zch-android
 * Description: toast工具类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class ToastUtils private constructor() {
    companion object {
        private var toast: Toast? = null
        fun showToast(context: Context, resId: Int) {
            showToast(context, context.resources.getString(resId))
        }

        fun showToast(context: Context, resId: Int, duration: Int) {
            showToast(context, context.resources.getString(resId), duration)
        }

        fun showToast(context: Context?, text: String?, duration: Int, vararg args: Any?) {
            showToast(context, String.format(text!!, *args), duration)
        }

        @JvmOverloads
        fun showToast(context: Context?, text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
            if (toast == null) {
                synchronized(ToastUtils::class.java) {
                    if (toast == null) {
                        toast = Toast.makeText(context, text, duration)
                    }
                }
            } else {
                toast!!.setText(text)
                toast!!.duration = duration
            }
            toast!!.show()
        }
    }

    init {
        throw AssertionError()
    }
}