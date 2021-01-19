package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository,
    application: Application
) :
    AndroidViewModel(application) {
    val productsList = MutableLiveData<Resource<ListingProductResponse>>()
    val context = application.applicationContext

    fun getCategoryProducts(category: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productsList.postValue(Resource.loading())
                val response = productsRepository.getCategoryProduct()
                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
                    productsList.postValue(Resource.success(response.body(), response.code()))
                } else {
                    productsList.postValue(Resource.error())
                }
            }
        }
    }
}