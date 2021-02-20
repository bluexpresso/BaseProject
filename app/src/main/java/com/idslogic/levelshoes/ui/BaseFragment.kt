package com.idslogic.levelshoes.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.idslogic.levelshoes.R

open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentLabel = findNavController().currentDestination?.label
        fragmentLabel?.let {
            if (it != "LandingFragment") {
                showStatusBar(false)
            }
            if (it == "ProductsListFragment" || it == "SubCategoryFragment") {
                enterTransition = Slide().apply {
                    slideEdge = Gravity.END
                }
            }
        }
    }

    fun changeStatusBarColor(color: Int) {
        activity?.window?.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), color)
        }
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        showStatusBar(color != R.color.background)
    }

    fun hideStatusBar() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity?.window?.statusBarColor = Color.TRANSPARENT
        activity?.window?.navigationBarColor = Color.BLACK
    }

    private fun showStatusBar(isLightStatus: Boolean = true) {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity?.window?.decorView?.apply {
            // Calling setSystemUiVisibility() with a value of 0 clears
            // all flags.
            systemUiVisibility = 0
        }
        if (!isLightStatus) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}