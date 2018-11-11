package io.informramiz.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
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
        canvas.drawCircleInt(startCircleCenter.x, startCircleCenter.y, circleRadius, getFirstCirclePaint())

        //draw end circle
        val endCircleCenter = getEndCircleCenter()
        canvas.drawCircleInt(endCircleCenter.x, endCircleCenter.y, circleRadius, getSecondCirclePaint())


        //draw normal line
        val lineStartPoint = getLineStartPoint()
        val lineEndPoint = getLineEndPoint()
        canvas.drawLineInt(lineStartPoint.x, lineStartPoint.y, lineEndPoint.x, lineEndPoint.y, normalPaint)

        //draw progress line
        if (progress > 0f) {
            val progressLineEndPoint = getProgressEndPoint()
            canvas.drawLineInt(lineStartPoint.x, lineStartPoint.y, progressLineEndPoint.x, progressLineEndPoint.y, progressPaint)
        }
    }

    private fun getProgressEndPoint(): Point {
        return when (orientation) {
            Orientation.HORIZONTAL -> Point(((width - 2 * circleSize()) * progress).toInt(), viewVerticalCenter())
            else -> Point(viewHorizontalCenter(), ((height - 2 * circleSize()) * progress).toInt())
        }
    }

    private fun circleSize() = (circleRadius * 2).toInt()

    private fun viewHorizontalCenter() = width / 2
    private fun viewVerticalCenter() = height / 2

    private fun getStartCircleCenter(): Point {
        return when (orientation) {
            Orientation.HORIZONTAL -> Point(circleRadius.toInt(), viewVerticalCenter())
            else -> Point(viewHorizontalCenter(), circleRadius.toInt())
        }
    }

    private fun getEndCircleCenter(): Point {
        return when (orientation) {
            Orientation.HORIZONTAL -> Point(width - circleRadius.toInt(), viewVerticalCenter())
            else -> Point(viewHorizontalCenter(), height - circleRadius.toInt())
        }
    }

    private fun getLineStartPoint(): Point {
        return when (orientation) {
            Orientation.HORIZONTAL -> Point(circleSize(), viewVerticalCenter())
            else -> Point(viewHorizontalCenter(), circleSize())
        }
    }

    private fun getLineEndPoint(): Point {
        return when (orientation) {
            Orientation.HORIZONTAL -> Point(width - circleSize(), viewVerticalCenter())
            else -> Point(viewHorizontalCenter(), height - circleSize())
        }
    }

    private fun getFirstCirclePaint(): Paint {
        return if (progress == 0f) circlePaint else progressPaint
    }

    private fun getSecondCirclePaint(): Paint {
        return if (progress == 1f) progressPaint else circlePaint
    }
}

private fun Canvas.drawLineInt(startX: Int, startY: Int, endX: Int, endY: Int, paint: Paint) {
    drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), paint)
}

private fun Canvas.drawCircleInt(cx: Int, cy: Int, circleRadius: Float, paint: Paint) {
    drawCircle(cx.toFloat(), cy.toFloat(), circleRadius, paint)
}
