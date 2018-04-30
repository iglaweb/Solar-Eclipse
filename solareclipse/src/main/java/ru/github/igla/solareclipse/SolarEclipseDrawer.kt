package ru.github.igla.solareclipse

import android.content.Context
import android.graphics.*
import android.os.Build


internal fun smoothPaint(color: Int): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
        }

@Suppress("DEPRECATION")
internal fun Context.getColorRes(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(id)
    } else {
        resources.getColor(id)
    }
}

internal class SolarEclipseDrawer(context: Context) {

    var reverse: Boolean = false
    private var eclipseFraction = 0f

    private val centerPoint = PointF()
    private val animPoint = PointF()

    private var minRadius: Float = context.resources.getDimensionPixelSize(R.dimen.sev_eclipse_size).toFloat()
    private var diameter: Float = 0f

    private var eclipseColorDefault = context.getColorRes(R.color.sev_eclipse_color)
    private var startBgColor = context.getColorRes(R.color.sev_start_bg_color)
    private var endBgColor = context.getColorRes(R.color.sev_end_bg_color)

    fun setConfig(viewConfig: ViewConfig) {
        this.circlePaint1.color = viewConfig.solarColor
        this.startBgColor = viewConfig.startBgColor
        this.endBgColor = viewConfig.endBgColor
    }

    private val circlePaint1 = smoothPaint(eclipseColorDefault).apply {
        style = Paint.Style.FILL
    }

    private val circlePaint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    fun onBoundsChange(centerPoint: PointF, radius: Float) {
        this.centerPoint.set(centerPoint)
        this.animPoint.set(centerPoint.x + minRadius / 2f, centerPoint.y)
        this.diameter = radius * 2
        this.minRadius = radius
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            val fromColor = if (reverse) endBgColor else startBgColor
            val toColor = if (reverse) startBgColor else endBgColor
            val fillColor = ColorEvaluator.evaluate(eclipseFraction, fromColor, toColor)
            drawColor(fillColor)

            drawCircle(centerPoint, minRadius, circlePaint1)

            circlePaint2.color = fillColor

            animPoint.x = if (reverse)
                centerPoint.x - eclipseFraction * diameter
            else
                centerPoint.x + diameter - diameter * eclipseFraction
            drawCircle(animPoint, minRadius, circlePaint2)
        }
    }

    private fun Canvas.drawCircle(point: PointF, radius: Float, paint: Paint) {
        drawCircle(point.x, point.y, radius, paint)
    }

    fun setFraction(eclipseFraction: Float) {
        this.eclipseFraction = eclipseFraction
    }
}