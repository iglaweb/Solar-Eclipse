package ru.github.igla.solareclipse

import android.animation.ObjectAnimator
import android.animation.ValueAnimator

/**
 * Created by igor-lashkov on 13/01/2018.
 */
internal class ProgressBarAnimation {
    fun createAnimation(listener: OnProgressChangeListener<Float>): ValueAnimator {
        val floatProperty = object : FloatProperty<ProgressBarAnimation>() {
            override fun setValue(obj: ProgressBarAnimation, value: Float) {
                listener.onChangeValue(value)
            }
        }
        return ObjectAnimator.ofFloat(this, floatProperty, 0f, -540f).apply {
            duration = 800L
            interpolator = FastOutSlowInInterpolator()
            repeatCount = 0
            repeatMode = ValueAnimator.RESTART
        }
    }
}