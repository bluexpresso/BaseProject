package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    application: Application
) : AndroidViewModel(application) {
    private var selectedCurrency: String? = null
    var product: BaseModel.Hit<Product>? = null
    fun getCurrency(): String {
        if (selectedCurrency == null) {
            configurationRepository.getCurrency().also { selectedCurrency = it }
        }
        return selectedCurrency!!
    }
}