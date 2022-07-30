package com.zch.utils

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.widget.TextView

/**
 *
 * zch-android
 * Description: textView颜色设置大小设置
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
object TextSpanUtil {
    /**
     * 2段TextView字体颜色大小设置
     */
    fun setTextChangeColor(
        textView: TextView,
        context: Context,
        start: String,
        startStyle: Int,
        end: String,
        endStyle: Int
    ) {
        val content = start + end
        val styledText = SpannableString(content)
        styledText.setSpan(
            TextAppearanceSpan(context, startStyle),
            0,
            start.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(context, endStyle),
            start.length,
            content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = styledText
    }

    /**
     * 3段TextView字体颜色大小设置
     */
    fun setTextChangeColor(
        textView: TextView,
        context: Context,
        start: String,
        startStyle: Int,
        center: String,
        centerStyle: Int,
        end: String,
        endStyle: Int
    ) {
        val content = start + center + end
        val styledText = SpannableString(content)
        styledText.setSpan(
            TextAppearanceSpan(context, startStyle),
            0,
            start.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(context, centerStyle),
            start.length,
            content.length - end.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(context, endStyle),
            content.length - end.length,
            content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = styledText
    }
}