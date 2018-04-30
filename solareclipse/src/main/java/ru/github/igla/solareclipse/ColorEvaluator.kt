package ru.github.igla.solareclipse

import android.animation.TypeEvaluator
import android.graphics.Color

internal object ColorEvaluator : TypeEvaluator<Int> {

    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA: Int = Color.alpha(startValue)
        val startR: Int = Color.red(startValue)
        val startG: Int = Color.green(startValue)
        val startB: Int = Color.blue(startValue)
        val aDelta = ((Color.alpha(endValue) - startA) * fraction).toInt()
        val rDelta = ((Color.red(endValue) - startR) * fraction).toInt()
        val gDelta = ((Color.green(endValue) - startG) * fraction).toInt()
        val bDelta = ((Color.blue(endValue) - startB) * fraction).toInt()
        return Color.argb(startA + aDelta, startR + rDelta, startG + gDelta, startB + bDelta)
    }
}