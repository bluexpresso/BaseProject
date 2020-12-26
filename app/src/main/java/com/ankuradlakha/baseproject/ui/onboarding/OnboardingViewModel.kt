package com.ankuradlakha.baseproject.ui.onboarding

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ankuradlakha.baseproject.data.models.Country
import com.ankuradlakha.baseproject.data.models.Source
import com.ankuradlakha.baseproject.data.repositories.OnboardingRepository
import com.ankuradlakha.baseproject.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingViewModel @ViewModelInject constructor(
    private val onboardingRepository: OnboardingRepository, application: Application
) : AndroidViewModel(application) {
    var selectedGender = MutableLiveData<String>()
    val onboardingNavigationInteractor = MutableLiveData<Boolean>()
    val onboardingLiveData = MutableLiveData<Resource<Source>>()
    val countriesListLiveData = MutableLiveData<ArrayList<Country>?>()
    suspend fun getOnboardingData() {
        try {
            onboardingLiveData.postValue(Resource.loading())
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val onboardingData: Source? = onboardingRepository.getOnboardingDataFromCache()
                    if (onboardingData != null) {
                        onboardingLiveData.postValue(
                            Resource.success(
                                onboardingData,
                                200
                            )
                        )
                    } else {
                        val response = onboardingRepository.getOnboardingData().execute()
                        if (response.isSuccessful && response.body() != null) {
                            onboardingRepository.saveOnboardingData(response.body()!!.source)
                            onboardingLiveData.postValue(
                                Resource.success(
                                    response.body()!!.source,
                                    response.code()
                                )
                            )
                        } else {
                            onboardingLiveData.postValue(Resource.error())
                        }
                    }
                }
            }
        } catch (e: Exception) {
            onboardingLiveData.postValue(Resource.error())
        }
    }

    fun setSelectedCountry(country: Country?) {
        onboardingRepository.setSelectedCountry(country)
    }

    fun getSelectedCountry() = onboardingRepository.getSelectedCountry()
    fun saveSelectedGender() {
        onboardingRepository.saveSelectedGender(selectedGender.value)
    }
}