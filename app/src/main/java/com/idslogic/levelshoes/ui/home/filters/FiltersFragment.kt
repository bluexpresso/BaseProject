package com.idslogic.levelshoes.ui.home.filters

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentFiltersBinding
import com.idslogic.levelshoes.ui.BaseFragment

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