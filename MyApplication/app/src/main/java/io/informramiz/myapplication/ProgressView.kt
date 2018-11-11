package io.informramiz.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.DimenRes
import android.util.AttributeSet
import android.view.View


/**
 * Created by Ramiz Raja on 11/11/2018.
 */
class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    enum class Orientation(val value: Int) {
        HORIZONTAL(0),
        VERTICAL(1)
    }

    companion object {
        private val DEFAULT_NORMAL_LINE_THICKNESS = 1.toPx().toFloat()
        private val DEFAULT_PROGRESS_LINE_THICKNESS = 4.toPx().toFloat()
        private val DEFAULT_CIRCLE_RADIUS = 10.toPx().toFloat()
        private const val DEFAULT_NORMAL_COLOR = Color.GRAY
        private const val DEFAULT_PROGRESS_COLOR = Color.BLUE

        private const val VIEW_PADDING = 1
    }

    private val normalLinePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        strokeWidth = DEFAULT_NORMAL_LINE_THICKNESS
        color = DEFAULT_NORMAL_COLOR
    }

    private val progressPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        strokeWidth = DEFAULT_PROGRESS_LINE_THICKNESS
        color = DEFAULT_PROGRESS_COLOR
    }

    private val normalCirclePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_NORMAL_LINE_THICKNESS
        color = DEFAULT_NORMAL_COLOR
    }


    private var circleRadius = DEFAULT_CIRCLE_RADIUS
    private var orientation: Orientation = Orientation.HORIZONTAL
    private var progress = 0f

    init {
        extractAttributes(attrs)
    }

    private fun extractAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
        try {
            circleRadius =
                    typedArray.getDimension(R.styleable.ProgressView_progressView_circle_radius, DEFAULT_CIRCLE_RADIUS)
            val orientationValue = typedArray.getInt(
                    R.styleable.ProgressView_progressView_orientation,
                    Orientation.HORIZONTAL.value
            )
            orientation = Orientation.values().first { it.value == orientationValue }

            typedArray.getDimension(
                R.styleable.ProgressView_progressView_normal_line_thickness,
                DEFAULT_NORMAL_LINE_THICKNESS
            ).let {
                normalCirclePaint.strokeWidth = it
                normalLinePaint.strokeWidth = it
            }

            typedArray.getDimension(
                R.styleable.ProgressView_progressView_progress_line_thickness,
                DEFAULT_PROGRESS_LINE_THICKNESS
            ).let {
                progressPaint.strokeWidth = it
            }

            progress = typedArray.getFloat(R.styleable.ProgressView_progressView_progress, 0f)
        } catch (e: Exception) {
            e.printStackTrace()
            typedArray.recycle()
        }
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
        canvas.drawLine(lineStartPoint.x, lineStartPoint.y, lineEndPoint.x, lineEndPoint.y, normalLinePaint)

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

    private fun circleSize() = (circleRadius * 2) + VIEW_PADDING

    private fun viewHorizontalCenter() = width / 2
    private fun viewVerticalCenter() = height / 2

    private fun getStartCircleCenter(): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(circleRadius + VIEW_PADDING, viewVerticalCenter())
            else -> PointF(viewHorizontalCenter(), circleRadius)
        }
    }

    private fun getEndCircleCenter(): PointF {
        return when (orientation) {
            Orientation.HORIZONTAL -> PointF(width - circleRadius - VIEW_PADDING, viewVerticalCenter())
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
        return if (progress == 0f) normalCirclePaint else progressPaint
    }

    private fun getSecondCirclePaint(): Paint {
        return if (progress == 1f) progressPaint else normalCirclePaint
    }
}

data class PointF(var x: Float, var y: Float) {
    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())
}

fun Context.getDimension(@DimenRes dimen: Int) = resources.getDimension(dimen)

fun Int.toPx(): Int {
    return this * Resources.getSystem().displayMetrics.density.toInt()
}

fun Int.toDp(): Int {
    return this / Resources.getSystem().displayMetrics.density.toInt()
}
