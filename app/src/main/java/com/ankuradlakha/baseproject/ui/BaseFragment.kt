package com.ankuradlakha.baseproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.utils.getEnterSlidingTransition
import com.ankuradlakha.baseproject.utils.getExitSlidingTransition

open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enterTransition = getEnterSlidingTransition(requireContext())
//        exitTransition = getExitSlidingTransition(requireContext())
    }
}