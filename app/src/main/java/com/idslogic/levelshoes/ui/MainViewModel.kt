package com.idslogic.levelshoes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.models.*
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.network.Status.SUCCESS
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val appCache: AppCache, application: Application
) :
    AndroidViewModel(application) {
    private val context = application.applicationContext
    val clearSearchFocusLiveData = MutableLiveData<Boolean>()
    var disableSearchAnimation = false
    var searchCategories: HashMap<String, LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>>? =
        null
    val filterAppliedLiveData = MutableLiveData(false)
    var searchTerm: MutableLiveData<String> = MutableLiveData()
    var selectedSearchTab: Int = -1
    val landingLiveData = MutableLiveData<Resource<HashMap<String, ArrayList<Content>>>>()
    suspend fun getLandingData() {
        val mapLandingData = HashMap<String, ArrayList<Content>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                landingLiveData.postValue(Resource.loading())
//                val cachedLandingData = configurationRepository.getLandingData()
//                if (!BuildConfig.DEBUG && !cachedLandingData.isNullOrEmpty()) {
//                    landingLiveData.postValue(
//                        Resource.success(
//                            cachedLandingData,
//                            200
//                        )
//                    )
//                }

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
                if (responseMen.status == SUCCESS && responseWomen.status == SUCCESS
                    && responseKids.status == SUCCESS
                ) {
                    for (i in 0..3) {
                        val response =
                            if (i == 0) responseWomen else if (i == 1) responseMen else responseKids
                        if (response.status == SUCCESS && response.data?.body() != null) {
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
//                                            val formattedProductIds = arrayListOf<String>()
                                            val mapProductImages = hashMapOf<String, String?>()
                                            if (!productIds.isNullOrEmpty()) {
                                                productIds.forEach { str ->
                                                    if (str.contains("|")) {
                                                        val spltStrings = str.split("|")
                                                        mapProductImages[spltStrings[0]] =
                                                            spltStrings[1]
//                                                        formattedProductIds.add(
//                                                            str.substring(
//                                                                0,
//                                                                str.indexOf("|")
//                                                            )
//                                                        )
                                                    } else {
                                                        mapProductImages[str] = null
//                                                        formattedProductIds.add(str)
                                                    }
                                                }
                                                val productResponse =
                                                    configurationRepository.getLandingProducts(
                                                        when (i) {
                                                            0 -> GENDER_WOMEN
                                                            1 -> GENDER_MEN
                                                            else -> GENDER_KIDS
                                                        },
                                                        mapProductImages.keys.toList()
                                                    )
                                                if (!productResponse.data.isNullOrEmpty()) {
                                                    productResponse.data.forEach { product ->
                                                        product.source?.manufacturer?.let { value ->
                                                            product.source?.manufacturerName =
                                                                configurationRepository.getManufacturerName(
                                                                    value
                                                                )
                                                        }
                                                        if (mapProductImages[product.source?.sku].isNullOrEmpty()) {
                                                            product.source?.displayableImage =
                                                                BuildConfig.IMAGE_URL.plus(
                                                                    product.source?.thumbnail
                                                                )
                                                        } else {
                                                            product.source?.displayableImage =
                                                                mapProductImages[product.source?.sku]
                                                        }
                                                    }
                                                    it.productsList = productResponse.data
                                                    if (it.boxType.equals(
                                                            BOX_TYPE_PRODUCT_VIEW,
                                                            true
                                                        )
                                                    ) {
                                                        val viewAllCollection =
                                                            BaseModel.Hit<Product>()
                                                        viewAllCollection.source = Product(
                                                            VIEW_ALL_COLLECTION
                                                        )
                                                        it.productsList?.add(viewAllCollection)
                                                    }
                                                }
                                            }
                                        }
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
                } else {
                    landingLiveData.postValue(
                        Resource.error(
                            responseMen.message ?: "",
                            code = responseMen.code ?: -1
                        )
                    )
                }
//                configurationRepository.saveLandingData(mapLandingData)
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
                    BOX_TYPE_REGISTER_SIGN_IN, 1, context.getString(R.string.sign_in_to_account),
                    context.getString(R.string.register_sign_in_description), 0, null, null, null
                )
            )
        }
        return contentList
    }

}