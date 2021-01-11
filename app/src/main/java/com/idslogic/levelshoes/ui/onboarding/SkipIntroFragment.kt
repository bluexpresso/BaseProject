package com.idslogic.levelshoes.ui.onboarding

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.Fade
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentSkipIntroBinding
import com.idslogic.levelshoes.utils.LONG_ANIMATION_DURATION
import kotlinx.android.synthetic.main.fragment_skip_intro.*

class SkipIntroFragment : Fragment() {
    companion object {
        fun newInstance() = SkipIntroFragment()
    }

    val activityViewModel: OnboardingViewModel by activityViewModels()
    var shouldAutoNavigateToNextScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade().apply {
            mode = Fade.MODE_IN
            duration = LONG_ANIMATION_DURATION
        }
        exitTransition = Fade().apply {
            mode = Fade.MODE_OUT
            duration = LONG_ANIMATION_DURATION
        }
        reenterTransition = Fade().apply {
            mode = Fade.MODE_IN
            duration = LONG_ANIMATION_DURATION
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_skip_intro, container, false)
                    as FragmentSkipIntroBinding
        binding.skipIntro.isEnabled = true
        binding.skipIntro.isClickable = true
        initSkipIntro(binding)
        initTimerTask()
        return binding.root
    }


    private fun initSkipIntro(binding: FragmentSkipIntroBinding) {
        binding.skipIntro.setOnClickListener {
            shouldAutoNavigateToNextScreen = false
            binding.skipIntro.isClickable = false
            binding.root.animate().apply {
                duration = 750
                alpha(0f)
            }.setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    activityViewModel.skipIntroLiveData.value = true
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
        }
    }

    private val autoNavigateRunnable = Runnable {
        if (shouldAutoNavigateToNextScreen) {
            shouldAutoNavigateToNextScreen = false
            skip_intro.performClick()
        }

    }

    private fun initTimerTask() {
        Handler(Looper.getMainLooper()).postDelayed(autoNavigateRunnable, 3000)
    }
}