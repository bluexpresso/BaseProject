package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class ProductListViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository,
    private val configurationRepository: ConfigurationRepository,
    application: Application
) :
    AndroidViewModel(application) {
    var categoryIdLiveData = MutableLiveData<Int>()
    val productsList = MutableLiveData<Resource<ListingProductResponse>>()
    val context = application.applicationContext
    val productsLiveData = MutableLiveData<Resource<ArrayList<BaseModel.Hit<Product>>>>()

//    fun getCategoryProducts(category: String) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                productsList.postValue(Resource.loading())
//                val response = productsRepository.getCategoryProduct()
//                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
//                    productsList.postValue(Resource.success(response.body(), response.code()))
//                } else {
//                    productsList.postValue(Resource.error())
//                }
//            }
//        }
//    }
//
    suspend fun getProductsFromCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productsLiveData.postValue(Resource.loading())
                val categoryResponse = productsRepository.getCategoryDetailsFromCategoryId(
                    categoryIdLiveData.value ?: 0
                )
                if (categoryResponse.isSuccessful) {
                    categoryResponse.body()?.source?.let {
                        val categoryName = it.name.decapitalize(Locale.ENGLISH)
                        val sBuilder = StringBuilder()
                        sBuilder.append(
                            "${
                                when {
                                    it.urlPath.startsWith(CATEGORY_MEN, true) -> {
                                        CATEGORY_MEN
                                    }
                                    it.urlPath.startsWith(CATEGORY_WOMEN, true) -> {
                                        CATEGORY_WOMEN
                                    }
                                    it.urlPath.startsWith(CATEGORY_KIDS, true) -> {
                                        CATEGORY_KIDS
                                    }
                                    it.urlPath.startsWith(CATEGORY_GIRL, true) -> {
                                        CATEGORY_GIRL
                                    }
                                    it.urlPath.startsWith(CATEGORY_BOY, true) -> {
                                        CATEGORY_BOY
                                    }
                                    else -> {
                                        ""
                                    }
                                }
                            };$categoryName"
                        )
                        val productIdsResponse =
                            productsRepository.getCategoryBasedProductsFromKlevu(sBuilder.toString())
                        if (productIdsResponse.isSuccessful && productIdsResponse.body()?.result?.isNotEmpty() == true) {
                            val productIds = arrayListOf<Long>()
                            productIdsResponse.body()?.result?.forEachIndexed { index, listingProduct ->
                                if (listingProduct.itemGroupId != null && index < 20)
                                    productIds.add(listingProduct.itemGroupId!!.toLong())
                            }
                            val productsResponse = productsRepository.getProducts(
                                0,
                                4,
                                productIds
                            )
                            if (productsResponse.isSuccessful && !productsResponse.body()?.hits?.hits.isNullOrEmpty()) {
                                productsLiveData.postValue(
                                    Resource.success(
                                        productsResponse.body()!!.hits.hits,
                                        productsResponse.code()
                                    )
                                )
                            } else {
                                productsLiveData.postValue(Resource.error())
                            }
                        } else {
                            productsLiveData.postValue(Resource.error())
                        }
                    }
                } else {
                    productsLiveData.postValue(Resource.error())
                }
            }
        }
    }

    fun getSelectedCurrency() = configurationRepository.getCurrency()
}