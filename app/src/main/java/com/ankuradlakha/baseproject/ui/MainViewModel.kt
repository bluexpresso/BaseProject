package com.ankuradlakha.baseproject.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ankuradlakha.baseproject.data.models.*
import com.ankuradlakha.baseproject.data.repositories.ConfigurationRepository
import com.ankuradlakha.baseproject.network.Resource
import com.ankuradlakha.baseproject.network.Status
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.RequestBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(
    private val configurationRepository: ConfigurationRepository,
    application: Application
) :
    AndroidViewModel(application) {
    val landingLiveData = MutableLiveData<Resource<BaseModel<LandingResponse>>>()
    suspend fun getLandingData(gender: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                landingLiveData.postValue(Resource.loading())
                val response = configurationRepository.getLandingData(
                    RequestBuilder.buildGetVersionInfoRequest(
                        gender
                    )
                )
                if (response.status == Status.SUCCESS && response.data?.body() != null) {
                    if (!response.data.body()?.hits?.hits.isNullOrEmpty()) {
                        val version = response.data.body()?.hits?.hits!![0].source?.versionNo
                        val currentVersion = configurationRepository.getCurrentVersion(GENDER_MEN)
                        if (version != currentVersion) {
                            version.let {
                                if (it != null)
                                    configurationRepository.saveCurrentVersion(gender, it)
                            }
                        }
                        val content = response.data.body()?.hits?.hits?.get(0)?.source?.content
                        if (content != null) {
                            response.data.body()?.hits?.hits?.get(0)?.source?.data =
                                getParsedLandingData(content)
                        }
                        landingLiveData.postValue(Resource.success(response.data.body(), 200))
                    }
                }
            }
        }
    }

    fun getSelectedGender() = configurationRepository.getSelectedGender()
    private fun getParsedLandingData(content: String): ArrayList<Content> {
        return Gson().fromJson(
            JsonParser.parseString(content).asJsonObject.getAsJsonArray("data"),
            object : TypeToken<ArrayList<Content>>() {
            }.type
        )
    }

}