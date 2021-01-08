package com.ankuradlakha.baseproject.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.ankuradlakha.baseproject.R

fun getEnterSlidingTransition(context: Context) =
    TransitionInflater.from(context).inflateTransition(R.transition.slide_from_left_to_right)

fun getExitSlidingTransition(context: Context): Transition =
    TransitionInflater.from(context).inflateTransition(R.transition.slide_from_right_to_left)

fun View.yAnimate(translateBy: Float) {
    ObjectAnimator.ofFloat(this, "translationY", translateBy).apply {
        duration = MEDIUM_ANIMATION_DURATION
        start()
    }
}
