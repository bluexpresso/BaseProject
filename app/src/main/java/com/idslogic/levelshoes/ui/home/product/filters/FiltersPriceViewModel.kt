package com.idslogic.levelshoes.ui.home.product.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltersPriceViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun getCurrency() = configurationRepository.getCurrency()
}