package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.Slide
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentChooseGenderBinding
import com.ankuradlakha.baseproject.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGenderFragment : Fragment() {
    companion object {
        fun newInstance() = ChooseGenderFragment()
    }

    val activityViewModel: OnboardingViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Slide().apply {
            slideEdge = Gravity.BOTTOM
            duration = MEDIUM_ANIMATION_DURATION
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_gender, container, false)
                    as FragmentChooseGenderBinding
        initGenders(binding)
        return binding.root
    }

    private fun initGenders(binding: FragmentChooseGenderBinding) {
        binding.btnMen.setOnClickListener {
            activityViewModel.selectedGender.value = GENDER_MEN
        }
        binding.btnWomen.setOnClickListener {
            activityViewModel.selectedGender.value = GENDER_WOMEN
        }
        binding.btnKids.setOnClickListener {
            activityViewModel.selectedGender.value = GENDER_KIDS
        }
    }
}