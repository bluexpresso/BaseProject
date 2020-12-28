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
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import com.ankuradlakha.baseproject.utils.RequestBuilder
import com.google.gson.Gson
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
    val landingLiveData = MutableLiveData<Resource<HashMap<String, ArrayList<Content>>>>()
    suspend fun getLandingData() {
        val mapLandingData = HashMap<String, ArrayList<Content>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                landingLiveData.postValue(Resource.loading())
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
                for (i in 0..3) {
                    val response =
                        if (i == 0) responseWomen else if (i == 1) responseMen else responseKids
                    if (response.status == Status.SUCCESS && response.data?.body() != null) {
                        if (!response.data.body()?.hits?.hits.isNullOrEmpty()) {
                            val content =
                                response.data.body()?.hits?.hits?.get(0)?.source?.content
                            if (content != null) {
                                mapLandingData[if (i == 0) GENDER_WOMEN else if (i == 1) GENDER_MEN else GENDER_KIDS] =
                                    getParsedLandingData(content)
                            }
                        }
                    }
                }
                landingLiveData.postValue(
                    Resource.success(
                        mapLandingData,
                        200
                    )
                )
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