package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.ankuradlakha.baseproject.ui.OnboardingTransitionFragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentChooseCountryBinding
import com.google.android.material.snackbar.Snackbar
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
        initContinue(binding)
        initCountriesList(binding)
        return binding.root
    }

    private fun initCountriesList(binding: FragmentChooseCountryBinding) {
        activityViewModel.countriesListLiveData.observe(viewLifecycleOwner, {
            binding.chooseCountry.setCountries(it!!)
        })
    }

    private fun initContinue(binding: FragmentChooseCountryBinding) {
        binding.btnContinue.setOnClickListener {
            if (binding.chooseCountry.selectedCountry != null) {
                val activityViewModel: OnboardingViewModel by activityViewModels()
                activityViewModel.setSelectedCountry(binding.chooseCountry.selectedCountry)
                activityViewModel.onboardingNavigationInteractor.value = true
            } else {
                Snackbar.make(binding.root, "Please select country", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}