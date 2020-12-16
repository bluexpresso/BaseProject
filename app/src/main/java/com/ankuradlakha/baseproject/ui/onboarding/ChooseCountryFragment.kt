package com.ankuradlakha.baseproject.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentChooseCountryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseCountryFragment : Fragment() {
    companion object{
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
        return binding.root
    }
}