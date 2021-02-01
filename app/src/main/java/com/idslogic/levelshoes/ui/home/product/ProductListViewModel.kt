package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProductListViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository,
    private val configurationRepository: ConfigurationRepository,
    application: Application
) :
    AndroidViewModel(application) {
    var gender = GENDER_WOMEN
    var categoryIdLiveData = MutableLiveData<Int>()
    val context = application.applicationContext
    val productIdsLiveData = MutableLiveData<ArrayList<Long>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    suspend fun getProductsFromCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadingLiveData.postValue(true)
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
                                if (listingProduct.itemGroupId != null && index < 10)
                                    productIds.add(listingProduct.itemGroupId!!.toLong())
                            }
                            productIdsLiveData.postValue(productIds)
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

    fun getProductPagingLiveData(categoryId: Int): LiveData<PagingData<BaseModel.Hit<Product>>>? {
        productsRepository.initProductsPagerLiveDataSource(
            categoryId, productIdsLiveData.value!!,
            viewModelScope
        )
        return productsRepository.productsPagerLiveData
    }

    fun getSelectedCurrency() = configurationRepository.getCurrency()

    override fun onCleared() {
        super.onCleared()
        productsRepository.productsPagerLiveData = null
    }
}