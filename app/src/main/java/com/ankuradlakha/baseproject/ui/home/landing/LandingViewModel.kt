package com.ankuradlakha.baseproject.ui.home.landing

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.data.repositories.ConfigurationRepository
import com.ankuradlakha.baseproject.data.repositories.OnboardingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.HashMap

class LandingViewModel @ViewModelInject constructor(
    application: Application,
    private val onboardingRepository: OnboardingRepository,
    private val configurationRepository: ConfigurationRepository
) : AndroidViewModel(application) {

    fun getSelectedCurrency() = onboardingRepository.getSelectedCountry().currency ?: "AED"
    fun updateCachedLandingData(mapItems: HashMap<String, ArrayList<Content>>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                configurationRepository.saveLandingData(mapItems)
            }
        }
    }
}