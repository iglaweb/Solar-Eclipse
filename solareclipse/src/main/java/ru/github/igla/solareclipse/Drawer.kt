package ru.github.igla.solareclipse

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable


internal class Drawer(context: Context, callback: Drawable.Callback) {

    private val eclipseAnimation = EclipseAnimation()
    private val progressAnim = ProgressBarAnimation()
    private val alphaAnim = AlphaAnimation()

    private val progressDrawer = ProgressBarDrawer(context)
    private val eclipseDrawer = SolarEclipseDrawer(context)

    private val centerPoint = PointF()

    private val minRadius: Float = context.resources.getDimensionPixelSize(R.dimen.sev_eclipse_size).toFloat()

    private var lastBounds = Rect()

    fun setConfig(viewConfig: ViewConfig) {
        progressDrawer.setConfig(viewConfig)
        eclipseDrawer.setConfig(viewConfig)
        if (viewConfig.eclipseSize != -1) {
            onBoundsChange(lastBounds, viewConfig.eclipseSize.toFloat())
        }
    }

    fun onBoundsChange(rect: Rect, radius: Float = minRadius) {
        this.lastBounds = rect
        val minDiameter = radius * 2f
        val minAvailableWidth = minDiameter
        val minAvailableHeight = minDiameter
        val parentWidth = rect.width()
        val parentHeight = rect.height()
        if (parentWidth < minAvailableWidth || parentHeight < minAvailableHeight) {
            //no space
            return
        }
        val centerX = parentWidth / 2.0f
        val centerY = parentHeight / 2.0f
        this.centerPoint.set(centerX, centerY)

        eclipseDrawer.onBoundsChange(centerPoint, radius)
        progressDrawer.onBoundsChange(centerPoint, radius)
    }

    fun draw(canvas: Canvas) {
        eclipseDrawer.draw(canvas)
        progressDrawer.draw(canvas)
    }

    fun createAnimatorList(): List<Animator> {
        val forward = eclipseAnimation.createAnimationFwd(radiusListener).apply {
            addListener(object : AnimListener() {
                override fun onAnimationEnd(animation: Animator) {
                    progressDrawer.state = ProgressState.ALPHA
                }

                override fun onAnimationStart(animation: Animator) {
                    eclipseDrawer.reverse = false
                }
            })
        }
        val alpha = alphaAnim.createAnimation(animListener).apply {
            addListener(object : AnimListener() {
                override fun onAnimationEnd(animation: Animator) {
                    progressDrawer.state = ProgressState.PROGRESS
                }

                override fun onAnimationStart(animation: Animator) {
                    progressDrawer.state = ProgressState.ALPHA
                }
            })
        }
        val back = eclipseAnimation.createAnimationBack(radiusListener).apply {
            addListener(object : AnimListener() {
                override fun onAnimationStart(animation: Animator) {
                    eclipseDrawer.reverse = true
                }
            })
        }
        val progressAnim = progressAnim.createAnimation(progressListener).apply {
            addListener(object : AnimListener() {
                override fun onAnimationEnd(animation: Animator) {
                    progressDrawer.state = ProgressState.INACTIVE
                }
            })
        }
        return listOf(forward, alpha, progressAnim, back)
    }

    private val radiusListener = object : OnProgressChangeListener<Float> {
        override fun onChangeValue(fraction: Float) {
            eclipseDrawer.setFraction(fraction)
            callback.invalidateDrawable(null)
        }
    }

    private val progressListener = object : OnProgressChangeListener<Float> {
        override fun onChangeValue(fraction: Float) {
            progressDrawer.setAngle(fraction)
            callback.invalidateDrawable(null)
        }
    }

    private val animListener = object : OnProgressChangeListener<Int> {
        override fun onChangeValue(fraction: Int) {
            progressDrawer.setAlpha(fraction)
            callback.invalidateDrawable(null)
        }
    }
}