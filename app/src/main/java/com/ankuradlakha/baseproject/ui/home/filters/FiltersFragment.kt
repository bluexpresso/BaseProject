package com.ankuradlakha.baseproject.ui.home.filters

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.transition.TransitionInflater
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentFiltersBinding
import com.ankuradlakha.baseproject.ui.BaseFragment
import com.ankuradlakha.baseproject.utils.getEnterSlidingTransition
import com.ankuradlakha.baseproject.utils.getExitSlidingTransition

class FiltersFragment : BaseFragment() {

    companion object {
        fun newInstance() = FiltersFragment()
    }

    private lateinit var viewModel: FiltersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false)
                as FragmentFiltersBinding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FiltersViewModel::class.java)
    }

}