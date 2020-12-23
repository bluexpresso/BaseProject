package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.ankuradlakha.baseproject.OnboardingTransitionFragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentChooseCountryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseCountryFragment : OnboardingTransitionFragment() {
    val activityViewModel: OnboardingViewModel by activityViewModels()

    companion object {
        fun newInstance(): ChooseCountryFragment {
            return ChooseCountryFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_country, container, false)
                    as FragmentChooseCountryBinding
        initChooseCountryDropdown(binding)
        initContinue(binding)
        initCountriesList(binding)
        return binding.root
    }

    private fun initCountriesList(binding: FragmentChooseCountryBinding) {
        activityViewModel.countriesListLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Got countries", Toast.LENGTH_SHORT).show()
            binding.chooseCountry.setCountries(it)
        })
    }

    private fun initContinue(binding: FragmentChooseCountryBinding) {
        binding.btnContinue.setOnClickListener {
            val activityViewModel: OnboardingViewModel by activityViewModels()
            activityViewModel.onboardingNavigationInteractor.value = true
        }
    }

    private fun initChooseCountryDropdown(binding: FragmentChooseCountryBinding) {
    }
}