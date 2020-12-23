package com.ankuradlakha.baseproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater

open class OnboardingTransitionFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_to_top)
        exitTransition = inflater.inflateTransition(R.transition.slide_to_bottom)
    }
}