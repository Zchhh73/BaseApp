package com.zch.base.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zch.utils.DensityUtils.dp2px

/**
 * 通用Base组件
 * Description: 加载view
 * Created by zch on 2022/8/1 3:33 下午
 **/
class LoadingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint: Paint
    private var radius = 0f
    private var radiusOffset = 0f
    private var stokeWidth = 2f
    private val argbEvaluator = ArgbEvaluator()
    private val startColor = Color.parseColor("#EEEEEE")
    private val endColor = Color.parseColor("#111111")
    var lineCount = 10
    var avgAngle = 360f / lineCount
    var time = 0
    var centerX = 0f
    var centerY = 0f
    var startX = 0f
    var endX = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (measuredWidth / 2).toFloat()
        radiusOffset = radius / 2.5f
        centerX = (measuredWidth / 2).toFloat()
        centerY = (measuredHeight / 2).toFloat()
        stokeWidth = dp2px(context, 2f).toFloat()
        paint.strokeWidth = stokeWidth
        startX = centerX + radiusOffset
        endX = startX + radius / 3f
        removeCallbacks(increaseTask)
        postDelayed(increaseTask, 80)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in lineCount - 1 downTo 0) {
            val temp = Math.abs(i + time) % lineCount
            val fraction = (temp + 1) * 1f / lineCount
            val color = argbEvaluator.evaluate(fraction, startColor, endColor) as Int
            paint.color = color
            canvas.drawLine(startX, centerY, endX, centerY, paint)
            // 线的两端画个点，看着圆滑
            canvas.drawCircle(startX, centerY, stokeWidth / 2, paint)
            canvas.drawCircle(endX, centerY, stokeWidth / 2, paint)
            canvas.rotate(avgAngle, centerX, centerY)
        }
    }

    private val increaseTask: Runnable = object : Runnable {
        override fun run() {
            time++
            postInvalidate(0, 0, measuredWidth, measuredHeight)
            postDelayed(this, 80)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(increaseTask)
    }

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        stokeWidth = dp2px(context!!, stokeWidth).toFloat()
        paint.strokeWidth = stokeWidth
    }
}