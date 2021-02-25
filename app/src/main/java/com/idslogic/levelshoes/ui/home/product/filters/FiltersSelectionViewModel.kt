package com.idslogic.levelshoes.ui.home.product.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Filter
import com.idslogic.levelshoes.data.models.FilterData
import com.idslogic.levelshoes.data.models.FilterOption
import com.idslogic.levelshoes.utils.*
import javax.inject.Inject

class FiltersSelectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var filterType: String? = null
    fun getFilters(filters: ArrayList<Filter>?) =
        filters?.find { it.key == filterType }

    fun getSelectedItems(filterData: FilterData?): HashMap<String, FilterOption> {
        return when (filterType) {
            FILTER_TYPE_CATEGORY -> filterData?.categories ?: hashMapOf()
            FILTER_TYPE_SIZE -> filterData?.sizes ?: hashMapOf()
            FILTER_TYPE_COLOR -> filterData?.colors ?: hashMapOf()
            FILTER_TYPE_GENDER -> filterData?.genders ?: hashMapOf()
            FILTER_TYPE_MANUFACTURER -> filterData?.manufacturers ?: hashMapOf()
            else -> hashMapOf()
        }
    }

    fun getFilterTitle(filters: ArrayList<Filter>?) =
        filters?.find { it.key == filterType }?.label
}