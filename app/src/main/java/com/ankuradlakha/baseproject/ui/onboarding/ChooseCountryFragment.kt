package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentChooseCountryBinding
import com.ankuradlakha.baseproject.utils.SHORT_ANIMATION_DURATION
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseCountryFragment : Fragment() {
    val activityViewModel: OnboardingViewModel by activityViewModels()
    lateinit var viewmodel: ChooseCountryViewModel

    companion object {
        fun newInstance(): ChooseCountryFragment {
            return ChooseCountryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this).get(ChooseCountryViewModel::class.java)
        enterTransition = Slide().apply {
            slideEdge = Gravity.BOTTOM
            duration = SHORT_ANIMATION_DURATION
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
        binding.chooseCountry.onCountrySelected = {
            viewmodel.country = it
            binding.btnContinue.isEnabled = true
        }
    }

    private fun initContinue(binding: FragmentChooseCountryBinding) {
        binding.btnContinue.setOnClickListener {
            if (viewmodel.country != null) {
                val activityViewModel: OnboardingViewModel by activityViewModels()
                activityViewModel.setSelectedCountry(viewmodel.country)
                activityViewModel.onboardingNavigationInteractor.value = true
            } else {
                Snackbar.make(binding.root, "Please select country", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnContinue.isEnabled = viewmodel.country != null
    }
}