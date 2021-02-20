package com.idslogic.levelshoes.ui.home.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentMyAccountBeforeLoginBinding
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.utils.LANGUAGE_ARABIC
import com.idslogic.levelshoes.utils.LANGUAGE_ENGLISH
import com.idslogic.levelshoes.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyAccountBeforeLoginFragment : Fragment() {
    val activityViewModel: MyAccountBeforeLoginViewModel by activityViewModels()
    private lateinit var viewModel: MyAccountBeforeLoginViewModel
    companion object {
        fun newInstance(): MyAccountBeforeLoginFragment {
            return MyAccountBeforeLoginFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAccountBeforeLoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_account_before_login,
            container,
            false
        ) as FragmentMyAccountBeforeLoginBinding
        initOnboardingData(binding)
        initMyLanguage(binding)
        initMyAccount(binding)
        initCountriesList(binding)
        return binding.root
    }

    private fun initMyAccount(binding: FragmentMyAccountBeforeLoginBinding) {
        binding.register.setOnClickListener {
           //binding.register.setTextAppearance(R.style.PrimaryButton)
            //binding.signIn.setTextAppearance(R.style.SecondaryButton)

            binding.register.isSelected=true
            binding.signIn.isSelected=false

        }
        binding.signIn.setOnClickListener {
            Snackbar.make(binding.root, "Sign in click", Snackbar.LENGTH_SHORT).show()
           //binding.register.setTextAppearance(R.style.SecondaryButton)
            //binding.signIn.setTextAppearance(R.style.PrimaryButton)
            binding.signIn.isSelected=true
            binding.register.isSelected=false
        }
    }

    private fun initMyLanguage(binding: FragmentMyAccountBeforeLoginBinding) {
        val selectedLanguage = viewModel.getSelectedLanguage()
        binding.languageEnglish.isSelected = selectedLanguage == LANGUAGE_ENGLISH
        binding.languageArabic.isSelected = selectedLanguage == LANGUAGE_ARABIC
        binding.languageEnglish.setOnClickListener {
            binding.languageEnglish.isSelected = true
            binding.languageArabic.isSelected = false
            viewModel.setSelectedLanguage(LANGUAGE_ENGLISH)
            setLocale(context)
            requireActivity().recreate()
        }
        binding.languageArabic.setOnClickListener {
            binding.languageEnglish.isSelected = false
            binding.languageArabic.isSelected = true
            viewModel.setSelectedLanguage(LANGUAGE_ARABIC)
            setLocale(context)
            requireActivity().recreate()
        }
    }

    private fun initCountriesList(binding: FragmentMyAccountBeforeLoginBinding) {
        activityViewModel.countriesListLiveData.observe(viewLifecycleOwner, {
            binding.selectLocationCountry.setCountries(it!!)
            Toast.makeText(
                requireView().context,
                "API Called Success in countries" + it.size,
                Toast.LENGTH_SHORT
            ).show()

        })
        binding.selectLocationCountry.onCountrySelected = {
            viewModel.country = it
        }
    }

    private fun initOnboardingData(binding: FragmentMyAccountBeforeLoginBinding) {
        viewModel.onboardingLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    //Toast.makeText(requireView().context , "API Called Success", Toast.LENGTH_SHORT).show()

                    if (!it.data?.countries.isNullOrEmpty()) {
                        viewModel.countriesListLiveData.value =
                            it.data!!.countries ?: arrayListOf()
                        it.data.countries?.let { it1 ->
                            binding.selectLocationCountry.setCountries(
                                it1)
                        }

                    }
                }
                Status.ERROR -> {
                }
            }
        })
        lifecycleScope.launch {
            viewModel.getOnboardingData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAccountBeforeLoginViewModel::class.java)
    }

}