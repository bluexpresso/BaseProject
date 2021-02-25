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
import com.idslogic.levelshoes.utils.*

class FiltersFragment : BaseFragment() {

    val activityViewModel by activityViewModels<MainViewModel>()
    private val filtersViewModel by activityViewModels<FiltersViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false)
                as FragmentFiltersBinding
        changeStatusBarColor(R.color.background)
        initToolbar(binding)
        initSortBy(binding)
        initFilters(binding)
        initApplyFilters(binding)
        return binding.root
    }

    private fun initFilters(binding: FragmentFiltersBinding) {
        binding.filterByCategory.setFilter(filtersViewModel.filterData.value?.peekContent()?.categories)
        binding.filterByCategory.setOnClickListener {
            findNavController().navigate(R.id.action_to_filter_selection, Bundle().apply {
                putString(ARG_FILTER_TYPE, FILTER_TYPE_CATEGORY)
            })
        }
        binding.filterByGender.setFilter(filtersViewModel.filterData.value?.peekContent()?.genders)
        binding.filterByGender.setOnClickListener {
            findNavController().navigate(R.id.action_to_filter_selection, Bundle().apply {
                putString(ARG_FILTER_TYPE, FILTER_TYPE_GENDER)
            })
        }
        binding.filterByColor.setFilter(filtersViewModel.filterData.value?.peekContent()?.colors)
        binding.filterByColor.setOnClickListener {
            findNavController().navigate(R.id.action_to_filter_selection, Bundle().apply {
                putString(ARG_FILTER_TYPE, FILTER_TYPE_COLOR)
            })
        }
        binding.filterByDesigner.setFilter(filtersViewModel.filterData.value?.peekContent()?.manufacturers)
        binding.filterByDesigner.setOnClickListener {
            findNavController().navigate(R.id.action_to_filter_selection, Bundle().apply {
                putString(ARG_FILTER_TYPE, FILTER_TYPE_MANUFACTURER)
            })
        }
        binding.filterBySize.setFilter(filtersViewModel.filterData.value?.peekContent()?.sizes)
        binding.filterBySize.setOnClickListener {
            findNavController().navigate(R.id.action_to_filter_selection, Bundle().apply {
                putString(ARG_FILTER_TYPE, FILTER_TYPE_SIZE)
            })
        }
        binding.filterByPrice.setFilter(filtersViewModel.filterData.value?.peekContent()?.price)
        binding.filterByPrice.setOnClickListener {
            findNavController().navigate(R.id.action_to_price_selection, Bundle().apply {
                putString(ARG_FILTER_TYPE, FILTER_TYPE_PRICE)
            })
        }
    }

    private fun initSortBy(binding: FragmentFiltersBinding) {
        binding.sortView.setSortBy(filtersViewModel.filterData.value?.peekContent()?.sortBy)
    }

    private fun initApplyFilters(binding: FragmentFiltersBinding) {
        binding.btnApplyFilter.setOnClickListener {
            var filterData = filtersViewModel.filterData.value?.peekContent()
            if (filterData == null) {
                filterData = FilterData()
            }
            filterData.sortBy = binding.sortView.selectedValue
            filtersViewModel.filterData.value = Event(filterData)
            findNavController().navigateUp()
        }
    }

    private fun initToolbar(binding: FragmentFiltersBinding) {
        binding.toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
    }
}