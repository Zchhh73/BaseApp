package com.zch.base.widget

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import kotlin.jvm.JvmOverloads
import com.zch.base.R
import android.content.DialogInterface
import android.view.View

/**
 *
 * BaseDemo
 * Description:
 * Created by chenhanzhang on 2022/7/30 19:43
 */
class LoadingDialog : Dialog {
    private var mLoadingTV: TextView? = null

    @JvmOverloads
    constructor(context: Context?, themeResId: Int = R.style.DialogTheme) : super(
        context!!,
        themeResId
    ) {
        init()
    }

    protected constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) :
            super(context!!, cancelable, cancelListener) {
        init()
    }

    private fun init() {
        this.setContentView(R.layout.loading_dialog)
        mLoadingTV = findViewById<View>(R.id.tv_loading) as TextView
    }

    fun setLoadingText(text: String?) {
        mLoadingTV!!.text = text
    }

    fun setLoadingText(resId: Int) {
        mLoadingTV!!.setText(resId)
    }
}