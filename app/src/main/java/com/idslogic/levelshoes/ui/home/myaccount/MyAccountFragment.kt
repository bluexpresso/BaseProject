package com.idslogic.levelshoes.ui.home.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentMyAccountBinding
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.utils.LANGUAGE_ARABIC
import com.idslogic.levelshoes.utils.LANGUAGE_ENGLISH
import com.idslogic.levelshoes.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyAccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = MyAccountFragment()
    }

    private lateinit var viewModel: MyAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_account,
            container, false
        ) as FragmentMyAccountBinding
        changeStatusBarColor(R.color.background)
        if (BuildConfig.DEBUG) {
            binding.root.visibility = View.VISIBLE
        } else {
            binding.root.visibility = View.GONE
        }
        initMyLanguage(binding)
        initGenderSelection(binding)
        initOnboardingData(binding)
        return binding.root
    }

    private fun initGenderSelection(binding: FragmentMyAccountBinding) {
        val selectedGender = viewModel.getSelectedGender()

    }

    private fun initMyLanguage(binding: FragmentMyAccountBinding) {
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
    private fun initOnboardingData(binding: FragmentMyAccountBinding) {
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
        viewModel = ViewModelProvider(this).get(MyAccountViewModel::class.java)
    }

}