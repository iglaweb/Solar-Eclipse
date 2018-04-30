package ru.github.igla.solareclipse

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable

internal fun Context.dp(dp: Float): Double = dpF(dp).toDouble()
internal fun Context.dpF(dp: Float): Float = dp * resources.displayMetrics.density

internal class SolarEclipseDrawable(context: Context) : Drawable() {

    private val drawableCallback = object : Callback {
        override fun invalidateDrawable(who: Drawable?) = invalidateSelf()

        override fun unscheduleDrawable(who: Drawable?, what: Runnable) {
        }

        override fun scheduleDrawable(who: Drawable?, what: Runnable, time: Long) {
        }
    }

    private var animator: AnimatorSet? = null

    private val drawer = Drawer(context, drawableCallback)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        drawer.onBoundsChange(bounds)
    }

    override fun draw(canvas: Canvas) {
        drawer.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = if (paint.alpha < 255) PixelFormat.TRANSLUCENT else PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter) {
        paint.colorFilter = colorFilter
    }

    fun stopAnimation() {
        animator?.apply {
            removeAllListeners()
            cancel()
        }
    }

    fun startAnimation(viewConfig: ViewConfig) {
        drawer.setConfig(viewConfig)
        animator = AnimatorSet().apply {
            playSequentially(drawer.createAnimatorList())
            addListener(object : AnimListener() {
                override fun onAnimationEnd(animation: Animator) {
                    startDelay = 500L
                    start()
                }
            })
            start()
        }
    }
}