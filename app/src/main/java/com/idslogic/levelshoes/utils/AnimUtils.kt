package com.idslogic.levelshoes.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.idslogic.levelshoes.R

fun getEnterSlidingTransition(context: Context) =
    TransitionInflater.from(context).inflateTransition(R.transition.slide_from_left_to_right)

fun getExitSlidingTransition(context: Context): Transition =
    TransitionInflater.from(context).inflateTransition(R.transition.slide_from_right_to_left)

fun View.yAnimate(translateBy: Float) {
    ObjectAnimator.ofFloat(this, "translationY", translateBy).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
}
