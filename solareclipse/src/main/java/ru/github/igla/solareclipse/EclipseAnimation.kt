package ru.github.igla.solareclipse

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

/**
 * Created by igor-lashkov on 13/01/2018.
 */
internal class EclipseAnimation {

    private fun createAnimation(reverse: Boolean, listener: OnProgressChangeListener<Float>): ValueAnimator {
        val floatProperty = object : FloatProperty<EclipseAnimation>() {
            override fun setValue(obj: EclipseAnimation, value: Float) {
                listener.onChangeValue(value)
            }
        }
        return ObjectAnimator.ofFloat(this, floatProperty, 0f, 1f).apply {
            duration = 1900L
            interpolator = if (reverse) DecelerateInterpolator() else LinearInterpolator()
            repeatCount = 0
            repeatMode = ValueAnimator.RESTART
        }
    }

    fun createAnimationBack(listener: OnProgressChangeListener<Float>): ValueAnimator = createAnimation(true, listener)

    fun createAnimationFwd(listener: OnProgressChangeListener<Float>): ValueAnimator = createAnimation(false, listener)
}