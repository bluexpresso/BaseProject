package com.ankuradlakha.baseproject.ui.splash

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ankuradlakha.baseproject.data.repositories.ConfigurationRepository
import com.ankuradlakha.baseproject.network.Resource
import com.ankuradlakha.baseproject.network.Status.SUCCESS
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import com.ankuradlakha.baseproject.utils.RequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel @ViewModelInject constructor(
    var configurationRepository: ConfigurationRepository,
    application: Application
) : AndroidViewModel(application) {
    val versionInfoLiveData =
        MutableLiveData<Resource<Any>>()
    var hasMenVersionChanged = false
    var hasWomenVersionChanged = false
    var hasKidsVersionChanged = false
    suspend fun getVersionInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                versionInfoLiveData.postValue(Resource.loading())
                val responseMen = configurationRepository.getLandingData(
                    RequestBuilder.buildGetVersionInfoRequest(
                        GENDER_MEN
                    )
                )
                val responseWomen = configurationRepository.getLandingData(
                    RequestBuilder.buildGetVersionInfoRequest(
                        GENDER_WOMEN
                    )
                )
                val responseKids = configurationRepository.getLandingData(
                    RequestBuilder.buildGetVersionInfoRequest(
                        GENDER_KIDS
                    )
                )
                if (responseMen.status == SUCCESS) {
                    if (!responseMen.data?.body()?.hits?.hits.isNullOrEmpty()) {
                        val version = responseMen.data?.body()?.hits?.hits!![0].source?.versionNo
                        val currentVersion = configurationRepository.getCurrentVersion(GENDER_MEN)
                        if (version != currentVersion) {
                            hasMenVersionChanged = true
                            version.let {
                                if (it != null)
                                    configurationRepository.saveCurrentVersion(GENDER_MEN, it)
                            }
                        }
                    }
                }
                if (responseWomen.status == SUCCESS) {
                    if (!responseWomen.data?.body()?.hits?.hits.isNullOrEmpty()) {
                        val version = responseWomen.data?.body()?.hits?.hits!![0].source?.versionNo
                        val currentVersion = configurationRepository.getCurrentVersion(GENDER_WOMEN)
                        if (version != currentVersion) {
                            version.let {
                                if (it != null)
                                    configurationRepository.saveCurrentVersion(GENDER_WOMEN, it)
                            }
                            hasWomenVersionChanged = true
                        }
                    }
                }
                if (responseKids.status == SUCCESS) {
                    if (!responseKids.data?.body()?.hits?.hits.isNullOrEmpty()) {
                        val version = responseKids.data?.body()?.hits?.hits!![0].source?.versionNo
                        val currentVersion = configurationRepository.getCurrentVersion(GENDER_KIDS)
                        if (version != currentVersion) {
                            version.let {
                                if (it != null)
                                    configurationRepository.saveCurrentVersion(GENDER_KIDS, it)
                            }
                            hasKidsVersionChanged = true
                        }
                    }
                }
                versionInfoLiveData.postValue(Resource.success(true, 200))
            }
        }
    }

    fun isOnboardingDone() = configurationRepository.isOnboardingCompleted()
}