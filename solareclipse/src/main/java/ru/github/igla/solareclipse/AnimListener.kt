package ru.github.igla.solareclipse

import android.animation.Animator

internal abstract class AnimListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator) {
    }

    override fun onAnimationEnd(animation: Animator) {
    }

    override fun onAnimationCancel(animation: Animator) {
    }

    override fun onAnimationStart(animation: Animator) {
    }
}