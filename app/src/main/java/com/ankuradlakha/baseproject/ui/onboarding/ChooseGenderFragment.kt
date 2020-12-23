package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.OnboardingTransitionFragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentChooseCountryBinding
import com.ankuradlakha.baseproject.databinding.FragmentChooseGenderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGenderFragment : OnboardingTransitionFragment() {
    companion object {
        fun newInstance() = ChooseGenderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_gender, container, false)
                    as FragmentChooseGenderBinding
        return binding.root
    }
}