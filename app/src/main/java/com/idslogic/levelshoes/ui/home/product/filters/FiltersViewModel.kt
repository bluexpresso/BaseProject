package com.idslogic.levelshoes.ui.home.product.filters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.idslogic.levelshoes.data.models.Filter
import com.idslogic.levelshoes.data.models.FilterData
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var filtersList: ArrayList<Filter>? = null
    var filterData = MutableLiveData<Event<FilterData>>()
    var price: Pair<Double,Double>? = null
    var filteredPrice: Pair<Double,Double>? = null
}