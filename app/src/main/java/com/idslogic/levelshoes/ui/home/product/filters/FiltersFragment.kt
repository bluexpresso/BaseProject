package com.idslogic.levelshoes.ui.home.product.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.FilterData
import com.idslogic.levelshoes.databinding.FragmentFiltersBinding
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.ui.home.product.ProductListViewModel
import com.idslogic.levelshoes.utils.Event

class FiltersFragment : BaseFragment() {

    val viewModel by activityViewModels<ProductListViewModel>()
    val activityViewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false)
                as FragmentFiltersBinding
        changeStatusBarColor(R.color.background)
        initToolbar(binding)
        initSortBy(binding)
        initApplyFilters(binding)
        return binding.root
    }

    private fun initSortBy(binding: FragmentFiltersBinding) {
        binding.sortView.setSortBy(viewModel.filterData.value?.peekContent()?.sortBy)
    }

    private fun initApplyFilters(binding: FragmentFiltersBinding) {
        binding.btnApplyFilter.setOnClickListener {
            var filterData = viewModel.filterData.value?.peekContent()
            if (filterData == null) {
                filterData = FilterData()
            }
            filterData.sortBy = binding.sortView.selectedValue
            viewModel.filterData.value = Event(filterData)
            findNavController().navigateUp()
        }
    }

    private fun initToolbar(binding: FragmentFiltersBinding) {
        binding.toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
    }
}