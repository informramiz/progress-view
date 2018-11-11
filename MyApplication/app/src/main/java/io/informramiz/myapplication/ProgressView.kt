package io.informramiz.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


/**
 * Created by Ramiz Raja on 11/11/2018.
 */
class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val VIEW_PADDING = 0
    }

    private val normalStrokeWidth = 2f
    private val progressStrokeWidth = 16f
    private var progress = 1f

    private val normalPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        strokeWidth = normalStrokeWidth
        color = Color.GRAY
    }

    private val progressPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        strokeWidth = progressStrokeWidth
        color = Color.BLUE
    }

    private val circlePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    override fun onDraw(canvas: Canvas) {
        val viewCenterY = height.toFloat() / 2

        //draw circles
        val circleRadius = height.toFloat() / 4
        val circleSize = circleRadius * 2

        //draw first circle
        val firstCircleCx = circleRadius + VIEW_PADDING
        canvas.drawCircle(firstCircleCx, viewCenterY, circleRadius, getFirstCirclePaint())

        //draw second circle
        val secondCircleCx = width - circleRadius - VIEW_PADDING
        canvas.drawCircle(secondCircleCx, viewCenterY, circleRadius, getSecondCirclePaint())


        //draw normal line
        val startX = firstCircleCx + circleRadius
        val endX = width - circleRadius * 2
        canvas.drawLine(startX, viewCenterY, endX, viewCenterY, normalPaint)

        //draw progress line
        if (progress > 0f) {
            canvas.drawLine(startX, viewCenterY, getProgressLineWidth(circleRadius), viewCenterY, progressPaint)
        }
    }

    private fun getProgressLineWidth(circleRadius: Float): Float {
        return (width * progress) - (circleRadius * 2)
    }

    private fun getFirstCirclePaint(): Paint {
        return if (progress == 0f) circlePaint else progressPaint
    }

    private fun getSecondCirclePaint(): Paint {
        return if (progress == 1f) progressPaint else circlePaint
    }
}