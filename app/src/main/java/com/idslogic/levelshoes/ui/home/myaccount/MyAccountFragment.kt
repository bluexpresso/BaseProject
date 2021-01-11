package com.idslogic.levelshoes.ui.home.myaccount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentMyAccountBinding
import com.idslogic.levelshoes.utils.LANGUAGE_ARABIC
import com.idslogic.levelshoes.utils.LANGUAGE_ENGLISH
import com.idslogic.levelshoes.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : Fragment() {

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
        initMyLanguage(binding)
        return binding.root
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAccountViewModel::class.java)
    }

}