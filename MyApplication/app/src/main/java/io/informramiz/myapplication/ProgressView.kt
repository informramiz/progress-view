package io.informramiz.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.DimenRes
import android.util.AttributeSet
import android.view.View


/**
 * Created by Ramiz Raja on 11/11/2018.
 */
fun Context.getDimension(@DimenRes dimen: Int) = resources.getDimension(dimen)

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val VIEW_PADDING = 0
    }

    private val normalStrokeWidth = context.getDimension(R.dimen.line_thickness_normal)
    private val progressStrokeWidth = context.getDimension(R.dimen.line_thickness_progress)
    private var progress = 0f
    private var circleRadius = context.getDimension(R.dimen.circle_radius)

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
        strokeWidth = normalStrokeWidth
        color = Color.GRAY
    }

    override fun onDraw(canvas: Canvas) {
        val viewCenterY = height.toFloat() / 2

        //draw start circle
        val firstCircleCx = circleRadius + VIEW_PADDING
        canvas.drawCircle(firstCircleCx, viewCenterY, circleRadius, getFirstCirclePaint())

        //draw end circle
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