package ru.github.igla.solareclipse

import android.content.Context
import android.graphics.*
import ru.github.igla.solareclipse.ProgressState.*
import kotlin.math.abs


internal enum class ProgressState {
    INACTIVE, ALPHA, PROGRESS
}

internal class ProgressBarDrawer(context: Context) {

    var state = INACTIVE

    private var angle = 0f
    private var alphaValue = 0

    private val rectCircle = RectF()
    private val rectEmpty = RectF()

    private val circleStrokeWidth = context.dpF(2f)

    private val primaryColorDefault = context.getColorRes(R.color.sev_primary_progress_color)
    private val secondaryColorDefault = context.getColorRes(R.color.sev_secondary_progress_color)

    private val bgArcPaint = smoothPaint(secondaryColorDefault).apply {
        strokeWidth = circleStrokeWidth
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    private val bgArcPaintAlpha = Paint(bgArcPaint)

    private val arcPaint = smoothPaint(primaryColorDefault).apply {
        strokeWidth = circleStrokeWidth
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    fun setConfig(viewConfig: ViewConfig) {
        this.arcPaint.color = viewConfig.pbPrimaryColor
        this.bgArcPaint.color = viewConfig.pbSecondaryColor
    }

    fun onBoundsChange(centerPoint: PointF, radius: Float) {
        rectCircle.set(centerPoint.x - radius,
                centerPoint.y - radius,
                centerPoint.x + radius,
                centerPoint.y + radius)

        val r = radius - circleStrokeWidth / 2f
        rectEmpty.set(centerPoint.x - r,
                centerPoint.y - r,
                centerPoint.x + r,
                centerPoint.y + r)
    }

    fun draw(canvas: Canvas) {
        when (state) {
            PROGRESS -> {
                canvas.drawArc(rectCircle, 0f, 360f, false, bgArcPaint)
                val arcLen = 180f
                val reverse = abs(angle) > arcLen
                if (reverse) {
                    val startAngle = 270f - (abs(angle) - arcLen)
                    val remainAngle = startAngle + 90f
                    val sweepAngle = if (remainAngle < arcLen) abs(remainAngle) else arcLen
                    canvas.drawArc(rectCircle, startAngle, -sweepAngle, false, arcPaint)
                } else {
                    canvas.drawArc(rectCircle, 270f, angle, false, arcPaint)
                }
            }
            ALPHA ->
                canvas.drawArc(rectCircle, 0f, 360f, false, bgArcPaintAlpha)
        }
    }

    fun setAlpha(alpha: Int) {
        this.alphaValue = alpha
        bgArcPaintAlpha.alpha = alphaValue
    }

    fun setAngle(angle: Float) {
        this.angle = angle
    }
}