package com.idslogic.levelshoes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enterTransition = getEnterSlidingTransition(requireContext())
//        exitTransition = getExitSlidingTransition(requireContext())
    }
}