package com.idslogic.levelshoes.ui.splash

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.network.Status.SUCCESS
import com.idslogic.levelshoes.utils.GENDER_KIDS
import com.idslogic.levelshoes.utils.GENDER_MEN
import com.idslogic.levelshoes.utils.GENDER_WOMEN
import com.idslogic.levelshoes.utils.RequestBuilder
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
                            configurationRepository.deleteLandingData(GENDER_MEN)
                            hasMenVersionChanged = true
                            version?.let {
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
                            configurationRepository.deleteLandingData(GENDER_WOMEN)
                            version?.let {
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
                            configurationRepository.deleteLandingData(GENDER_KIDS)
                            version?.let {
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

   suspend fun getAttributes(): MutableLiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                liveData.postValue(configurationRepository.getAttributes())
            }
        }
        return liveData
    }

    fun isOnboardingDone() = configurationRepository.isOnboardingCompleted()
}