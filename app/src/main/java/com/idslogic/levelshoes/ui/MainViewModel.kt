package com.idslogic.levelshoes.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.models.*
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.utils.*
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val appCache: AppCache,
    application: Application
) :
    AndroidViewModel(application) {
    private val context = application.applicationContext
    val landingLiveData = MutableLiveData<Resource<HashMap<String, ArrayList<Content>>>>()
    suspend fun getLandingData() {
        val mapLandingData = HashMap<String, ArrayList<Content>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                landingLiveData.postValue(Resource.loading())
                val cachedLandingData = configurationRepository.getLandingData()
                if (!cachedLandingData.isNullOrEmpty()) {
                    landingLiveData.postValue(
                        Resource.success(
                            cachedLandingData,
                            200
                        )
                    )
                } else {
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
                                    val parsedContent = getParsedLandingData(content)
                                    parsedContent.forEach {
                                        if (it.boxType.equals(BOX_TYPE_PRODUCT_VIEW, true) or
                                            it.boxType.equals(
                                                BOX_TYPE_ADDITIONAL_PRODUCTS_VIEW,
                                                true
                                            )
                                        ) {
                                            val productIds = it.productIds?.products
                                            if (!productIds.isNullOrEmpty()) {
                                                val productResponse =
                                                    configurationRepository.getLandingProducts(
                                                        /*arrayOf(
                                                            "553770-WHGP7-9061",
                                                            " J000138945",
                                                            "DRWFLAA0-NAP-000",
                                                            " 5XX451-XWH-F0002-B065",
                                                            " G1986000RICVER"
                                                        )*/
                                                        productIds
                                                    )
                                                if (!productResponse.data.isNullOrEmpty()) {
                                                    it.productsList = productResponse.data
                                                } else {
                                                    it.boxType = ""
                                                }
                                            } else {
                                                it.boxType = ""
                                            }
                                        }
                                        /*else if (it.boxType.equals(
                                                BOX_TYPE_ADDITIONAL_PRODUCTS_VIEW,
                                                true
                                            )
                                        ) {
                                            val productIds = it.productIds?.products
                                            if (!productIds.isNullOrEmpty()) {
                                                val productResponse =
                                                    configurationRepository.getLandingProducts(
                                                        *//*arrayOf(
                                                            "553770-WHGP7-9061",
                                                            " J000138945",
                                                            "DRWFLAA0-NAP-000",
                                                            " 5XX451-XWH-F0002-B065",
                                                            " G1986000RICVER"
                                                        )*//*
                                                        productIds
                                                    )
                                                it.productsList = productResponse.data
                                            }
                                        }*/
                                    }
                                    mapLandingData[if (i == 0) GENDER_WOMEN else if (i == 1) GENDER_MEN else GENDER_KIDS] =
                                        parsedContent
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
                    configurationRepository.saveLandingData(mapLandingData)
                }
            }
        }
    }

    private fun getParsedLandingData(content: String): ArrayList<Content> {
        val contentList: ArrayList<Content> = Gson().fromJson(
            JsonParser.parseString(content).asJsonObject.getAsJsonArray("data"),
            object : TypeToken<ArrayList<Content>>() {
            }.type
        )
        if (contentList.size > 1 && appCache.getAuthToken().isEmpty()) {
            contentList.add(
                1, Content(
                    BOX_TYPE_REGISTER_SIGN_IN, 1, context.getString(R.string.register_sign_in),
                    context.getString(R.string.register_sign_in_description), 0, null, null, null
                )
            )
        }
        return contentList
    }

}