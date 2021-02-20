package com.idslogic.levelshoes.ui.home.myaccount

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.models.Country
import com.idslogic.levelshoes.data.models.Source
import com.idslogic.levelshoes.data.repositories.OnboardingRepository
import com.idslogic.levelshoes.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val appCache: AppCache,
    private val onboardingRepository: OnboardingRepository,
    application: Application
) :
    AndroidViewModel(application) {
    fun getSelectedLanguage() = appCache.getSelectedLanguage()
    fun setSelectedLanguage(language: String) {
        appCache.setSelectedLanguage(language)
    }
    val countriesListLiveData = MutableLiveData<ArrayList<Country>?>()
    val onboardingLiveData = MutableLiveData<Resource<Source>>()

    var country : Country? = null


    suspend fun getOnboardingData() {
        try {
            onboardingLiveData.postValue(Resource.loading())
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val onboardingData: Source = onboardingRepository.getOnboardingDataFromCache()
                    onboardingLiveData.postValue(
                        Resource.success(
                            onboardingData,
                            200
                        )
                    )
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
    fun getSelectedGender() =
        onboardingRepository.getSelectedGender()
}