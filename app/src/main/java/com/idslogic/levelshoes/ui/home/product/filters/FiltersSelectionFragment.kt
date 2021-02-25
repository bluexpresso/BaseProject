package com.idslogic.levelshoes.ui.home.product.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.FilterData
import com.idslogic.levelshoes.databinding.FragmentFiltersSelectionBinding
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.utils.*

class FiltersSelectionFragment : BaseFragment() {

    private val viewModel by viewModels<FiltersSelectionViewModel>()
    private val filtersViewModel by activityViewModels<FiltersViewModel>()
    private lateinit var adapter: FiltersSelectionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentFiltersSelectionBinding>(
            inflater, R.layout.fragment_filters_selection,
            container, false
        )
        readArguments()
        initToolbar(binding)
        initFiltersList(binding)
        initApplyFilters(binding)
        return binding.root
    }

    private fun initToolbar(binding: FragmentFiltersSelectionBinding) {
        binding.toolbar.onActionButtonClick = {
            adapter.clear()
        }
        binding.toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
        binding.toolbar.setTitle(viewModel.getFilterTitle(filtersViewModel.filtersList) ?: "")
    }

    private fun initApplyFilters(binding: FragmentFiltersSelectionBinding) {
        binding.applyFilters.setOnClickListener {
            if (filtersViewModel.filterData.value == null) {
                filtersViewModel.filterData.value = Event(FilterData())
            }
            when (viewModel.filterType) {
                FILTER_TYPE_CATEGORY -> {
                    filtersViewModel.filterData.value?.peekContent()?.categories =
                        adapter.selectedItems
                }
                FILTER_TYPE_COLOR -> {
                    filtersViewModel.filterData.value?.peekContent()?.colors =
                        adapter.selectedItems
                }
                FILTER_TYPE_GENDER -> {
                    filtersViewModel.filterData.value?.peekContent()?.genders =
                        adapter.selectedItems
                }
                FILTER_TYPE_SIZE -> {
                    filtersViewModel.filterData.value?.peekContent()?.sizes =
                        adapter.selectedItems
                }
                FILTER_TYPE_MANUFACTURER -> {
                    filtersViewModel.filterData.value?.peekContent()?.manufacturers =
                        adapter.selectedItems
                }
            }
            findNavController().navigateUp()
        }
    }

    private fun initFiltersList(binding: FragmentFiltersSelectionBinding) {
        adapter = FiltersSelectionAdapter(
            viewModel.filterType == FILTER_TYPE_COLOR,
            viewModel.getSelectedItems(filtersViewModel.filterData.value?.peekContent())
        )
        binding.viewFilters.adapter = adapter
        adapter.setItems(
            viewModel.getFilters(filtersViewModel.filtersList)?.options ?: arrayListOf()
        )
    }

    private fun readArguments() {
        viewModel.filterType = arguments?.getString(ARG_FILTER_TYPE, FILTER_TYPE_CATEGORY)
    }
}