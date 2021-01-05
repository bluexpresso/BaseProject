package com.ankuradlakha.baseproject.utils

import android.content.Context
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.ankuradlakha.baseproject.R

fun getEnterSlidingTransition(context: Context) =
    TransitionInflater.from(context).inflateTransition(R.transition.slide_from_left_to_right)

fun getExitSlidingTransition(context: Context): Transition =
    TransitionInflater.from(context).inflateTransition(R.transition.slide_from_right_to_left)