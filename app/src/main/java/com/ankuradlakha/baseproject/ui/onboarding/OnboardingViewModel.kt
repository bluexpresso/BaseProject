package com.ankuradlakha.baseproject.ui.onboarding

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ankuradlakha.baseproject.data.models.Country
import com.ankuradlakha.baseproject.data.models.OnboardingResponse
import com.ankuradlakha.baseproject.data.repositories.OnboardingRepository
import com.ankuradlakha.baseproject.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class OnboardingViewModel @ViewModelInject constructor(
    private val onboardingRepository: OnboardingRepository, application: Application
) : AndroidViewModel(application) {
    val onboardingNavigationInteractor = MutableLiveData<Boolean>()
    lateinit var selectedCountry: Any
    val onboardingLiveData = MutableLiveData<Resource<OnboardingResponse>>()
    val countriesListLiveData = MutableLiveData<ArrayList<Country>>()
    suspend fun getOnboardingData() {
        try {
            onboardingLiveData.postValue(Resource.loading())
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val response = onboardingRepository.getOnboardingData().execute()
                    if (response.isSuccessful && response.body() != null) {
                        onboardingLiveData.postValue(
                            Resource.success(
                                response.body(),
                                response.code()
                            )
                        )
                    } else {
                        onboardingLiveData.postValue(Resource.error())
                    }
                }
            }
        } catch (e: Exception) {
            onboardingLiveData.postValue(Resource.error())
        }
    }
}