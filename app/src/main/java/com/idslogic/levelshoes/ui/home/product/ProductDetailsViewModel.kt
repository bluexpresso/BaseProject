package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.data.repositories.OnboardingRepository

class ProductDetailsViewModel @ViewModelInject constructor(
    private val onboardingRepository: OnboardingRepository,
    application: Application
) : AndroidViewModel(application) {
    private var selectedCurrency: String? = null
    lateinit var product: BaseModel.Hit<Product>
    fun getCurrency(): String {
        if (selectedCurrency == null) {
            selectedCurrency = onboardingRepository.getSelectedCountry().currency ?: "AED"
        }
        return selectedCurrency!!
    }
}