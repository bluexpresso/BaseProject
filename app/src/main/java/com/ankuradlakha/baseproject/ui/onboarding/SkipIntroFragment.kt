package com.ankuradlakha.baseproject.ui.onboarding

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.Fade
import androidx.transition.Slide
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentSkipIntroBinding
import com.ankuradlakha.baseproject.utils.MEDIUM_ANIMATION_DURATION
import com.ankuradlakha.baseproject.utils.SHORT_ANIMATION_DURATION
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
            duration = MEDIUM_ANIMATION_DURATION
        }
        exitTransition = Fade().apply {
            mode = Fade.MODE_OUT
            duration = MEDIUM_ANIMATION_DURATION
        }
        reenterTransition = Fade().apply {
            mode = Fade.MODE_IN
            duration = MEDIUM_ANIMATION_DURATION
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
            activityViewModel.skipIntroLiveData.value = true
        }
    }

    private val autoNavigateRunnable = Runnable {
        if (shouldAutoNavigateToNextScreen) {
            shouldAutoNavigateToNextScreen = false
            skip_intro.isClickable = false
            activityViewModel.skipIntroLiveData.value = true
        }

    }

    private fun initTimerTask() {
        Handler(Looper.getMainLooper()).postDelayed(autoNavigateRunnable, 3000)
    }
}