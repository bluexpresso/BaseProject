package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ProductListViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository,
    private val configurationRepository: ConfigurationRepository,
    application: Application
) :
    AndroidViewModel(application) {
    var categoryPath: String? = null
    var title: String? = null
    var gender = GENDER_WOMEN
    var categoryId: Int = NO_CATEGORY
    val context = application.applicationContext
    val productIdsLiveData = MutableLiveData<ArrayList<ListingProduct>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    var parentCategoryId: Int = NO_CATEGORY
    suspend fun getProductsFromCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadingLiveData.postValue(true)
                if (!categoryPath.isNullOrEmpty()) {
//                    if (categoryId == NO_CATEGORY) {
//                        val categoryResponse =
//                            productsRepository.getCategoryDetailsFromCategoryId(parentCategoryId)
//                        if (!categoryResponse.body()?.source?.childrenData.isNullOrEmpty()) {
//                            categoryId =
//                                categoryResponse.body()?.source?.childrenData?.find { thisCat ->
//                                    thisCat.name.equals(
//                                        title,
//                                        true
//                                    )
//                                }?.id ?: NO_CATEGORY
//                        }
//                    }
                    val productIdsResponse =
                        productsRepository.getCategoryBasedProductsFromKlevu(categoryPath!!,gender)
                    if (productIdsResponse.isSuccessful && productIdsResponse.body()?.result?.isNotEmpty() == true) {
                        productIdsLiveData.postValue(productIdsResponse.body()!!.result!!)
                    } else {
                        loadingLiveData.postValue(false)
                    }
                } else {
                    val categoryResponse = productsRepository.getCategoryDetailsFromCategoryId(
                        categoryId
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
                                productIdsLiveData.postValue(productIdsResponse.body()!!.result!!)
                            } else {
                                loadingLiveData.postValue(false)
                            }
                        }
                    } else {
                        loadingLiveData.postValue(false)
                    }
                }

            }
        }
    }

    fun getProductPagingLiveData(categoryId: Int): LiveData<PagingData<BaseModel.Hit<Product>>>? {
        productsRepository.initProductsPagerLiveDataSource(
            if (parentCategoryId > 0) parentCategoryId else categoryId, productIdsLiveData.value!!,
            viewModelScope
        )
        return productsRepository.productsPagerLiveData
    }

    fun getSelectedCurrency() = configurationRepository.getCurrency()

    override fun onCleared() {
        super.onCleared()
        productsRepository.productsPagerLiveData = null
    }

    fun getCategoryName(name: String): String {
        val categoryName = name.decapitalize(Locale.ENGLISH)
        val sBuilder = StringBuilder()
        sBuilder.append(
            "${
                when (gender) {
                    GENDER_MEN -> {
                        CATEGORY_MEN
                    }
                    GENDER_WOMEN -> {
                        CATEGORY_WOMEN
                    }
                    GENDER_KIDS -> {
                        CATEGORY_KIDS
                    }
                    GENDER_BOYS -> {
                        CATEGORY_BOY
                    }
                    GENDER_GIRLS -> {
                        CATEGORY_GIRL
                    }
                    GENDER_UNISEX -> {
                        CATEGORY_UNISEX
                    }
                    else -> {
                        ""
                    }
                }
            };$categoryName"
        )
        return sBuilder.toString()
    }
}