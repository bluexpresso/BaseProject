package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentSkipIntroBinding
import com.ankuradlakha.baseproject.ui.OnboardingTransitionFragment
import kotlinx.android.synthetic.main.fragment_skip_intro.*

class SkipIntroFragment : OnboardingTransitionFragment() {
    companion object {
        fun newInstance() = SkipIntroFragment()
    }

    val activityViewModel: OnboardingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_skip_intro, container, false)
                    as FragmentSkipIntroBinding
        initSkipIntro(binding)
        return binding.root
    }

    private fun initSkipIntro(binding: FragmentSkipIntroBinding) {
        binding.skipIntro.setOnClickListener {
            binding.skipIntro.visibility = View.VISIBLE
            activityViewModel.skipIntroLiveData.value = true
        }
    }
}