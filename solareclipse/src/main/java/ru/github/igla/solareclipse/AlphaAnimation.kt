package ru.github.igla.solareclipse

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator

/**
 * Created by igor-lashkov on 13/04/2018.
 */
internal class AlphaAnimation {

    fun createAnimation(listener: OnProgressChangeListener<Int>): ValueAnimator {
        val floatProperty = object : IntProperty<AlphaAnimation>() {
            override fun setValue(obj: AlphaAnimation, value: Int) {
                listener.onChangeValue(value)
            }
        }
        return ObjectAnimator.ofInt(this, floatProperty, 0, 127).apply {
            duration = 200L
            interpolator = AccelerateInterpolator()
            repeatCount = 0
            repeatMode = ValueAnimator.RESTART
        }
    }
}