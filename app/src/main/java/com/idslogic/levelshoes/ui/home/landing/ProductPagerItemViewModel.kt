package com.idslogic.levelshoes.ui.home.landing

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.data.repositories.OnboardingRepository

class ProductPagerItemViewModel @ViewModelInject constructor(
    application: Application,
    private val onboardingRepository: OnboardingRepository
) : AndroidViewModel(application) {
    lateinit var product: BaseModel.Hit<Product>
    var selectedCurrency: String? = null
    fun getCurrency(): String {
        if (selectedCurrency == null) {
            selectedCurrency = onboardingRepository.getSelectedCountry().currency ?: "AED"
        }
        return selectedCurrency!!
    }
}