package com.idslogic.levelshoes.ui.home.product.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textview.MaterialTextView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentFiltersPriceBinding
import com.idslogic.levelshoes.ui.BaseFragment
import java.text.DecimalFormat
import java.util.*

class FiltersPriceFragment : BaseFragment() {
    val filtersViewModel by activityViewModels<FiltersViewModel>()
    val viewModel by viewModels<FiltersPriceViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentFiltersPriceBinding>(
            inflater, R.layout.fragment_filters_price,
            container, false
        )
        initPriceFilter(binding)
        return binding.root
    }

    private fun initPriceFilter(binding: FragmentFiltersPriceBinding) {
        filtersViewModel.price?.first.let { minPrice ->
            setPrice(binding.minPrice, minPrice?.toFloat() ?: 0f)
            binding.rangeSliderPrice.valueFrom = minPrice?.toFloat() ?: 0f
        }
        filtersViewModel.price?.second.let { maxPrice ->
            setPrice(binding.maxPrice, maxPrice?.toFloat() ?: 10000f)
            binding.rangeSliderPrice.valueTo = maxPrice?.toFloat() ?: 10000f
        }
        binding.rangeSliderPrice.setMinSeparationValue(10f)
        binding.rangeSliderPrice.setValues(
            filtersViewModel.filteredPrice?.first?.toFloat() ?: binding.rangeSliderPrice.valueFrom,
            filtersViewModel.filteredPrice?.second?.toFloat() ?: binding.rangeSliderPrice.valueTo
        )
        binding.rangeSliderPrice.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            setPrice(binding.minPrice, slider.values[0])
            setPrice(binding.maxPrice, slider.values[1])
        })
    }

    private fun setPrice(textView: MaterialTextView, value: Float) {
//        val currency = viewModel.getCurrency()
        textView.text = String.format(
            Locale.ENGLISH, "%s %s", "AED", "${value.toInt()}"
        )
    }
}