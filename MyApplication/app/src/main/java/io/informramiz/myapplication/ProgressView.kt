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

    enum class Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private val normalStrokeWidth = context.getDimension(R.dimen.line_thickness_normal)
    private val progressStrokeWidth = context.getDimension(R.dimen.line_thickness_progress)
    private var progress = 0.5f
    private var circleRadius = context.getDimension(R.dimen.circle_radius)
    private var orientation: Orientation = Orientation.HORIZONTAL

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
        //draw start circle
        val startCircleCenter = getStartCircleCenter()
        canvas.drawCircle(startCircleCenter.x, startCircleCenter.y, circleRadius, getFirstCirclePaint())

        //draw end circle
        val endCircleCenter = getEndCircleCenter()
        canvas.drawCircle(endCircleCenter.x, endCircleCenter.y, circleRadius, getSecondCirclePaint())


        //draw normal line
        val lineStartPoint = getLineStartPoint()
        val lineEndPoint = getLineEndPoint()
        canvas.drawLine(lineStartPoint.x, lineStartPoint.y, lineEndPoint.x, lineEndPoint.y, normalPaint)

        //draw progress line
        if (progress > 0f) {
            val lineWidth = Math.abs(lineEndPoint.x - lineStartPoint.x)
            val lineHeight = Math.abs(lineEndPoint.y - lineStartPoint.y)
            val progressLineEndPoint = getProgressEndPoint(lineWidth, lineHeight)
            canvas.drawLine(lineStartPoint.x, lineStartPoint.y, progressLineEndPoint.x, progressLineEndPoint.y, progressPaint)
        }
    }

    private fun getProgressEndPoint(lineWidth: Float, lineHeight: Float): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(lineWidth * progress + circleSize(), viewVerticalCenter())
            else -> PointF(viewHorizontalCenter(), lineHeight * progress + circleSize())
        }
    }

    private fun circleSize() = (circleRadius * 2)

    private fun viewHorizontalCenter() = width / 2
    private fun viewVerticalCenter() = height / 2

    private fun getStartCircleCenter(): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(circleRadius, viewVerticalCenter())
            else -> PointF(viewHorizontalCenter(), circleRadius)
        }
    }

    private fun getEndCircleCenter(): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(width - circleRadius - 1, viewVerticalCenter())
            else -> PointF(viewHorizontalCenter(), height - circleRadius)
        }
    }

    private fun getLineStartPoint(): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(circleSize(), viewVerticalCenter())
            else -> PointF(viewHorizontalCenter(), circleSize())
        }
    }

    private fun getLineEndPoint(): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(width - circleSize(), viewVerticalCenter())
            else -> PointF(viewHorizontalCenter(), height - circleSize())
        }
    }

    private fun getFirstCirclePaint(): Paint {
        return if (progress == 0f) circlePaint else progressPaint
    }

    private fun getSecondCirclePaint(): Paint {
        return if (progress == 1f) progressPaint else circlePaint
    }
}

data class PointF(var x: Float, var y: Float) {
    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())
}
