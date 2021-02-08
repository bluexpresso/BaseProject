package com.idslogic.levelshoes.ui.home.search

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call

class SearchItemViewModel @ViewModelInject constructor(
    application: Application,
    private val categoryRepository: CategoryRepository,
    private val configurationRepository: ConfigurationRepository

) :
    AndroidViewModel(application) {
    var gender: String = GENDER_WOMEN
    val productSearchResultsLiveData = MutableLiveData<Resource<ListingProductResponse>>()
    suspend fun getProductsFromCategory(searchString: String, gender: String) {
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    productSearchResultsLiveData.postValue(Resource.loading())
                    val searchResultsCall =
                        categoryRepository.getCategoryBasedSearchResultsProductsFromKlevu(
                            searchString,
                            gender
                        )
                    val productIdsResponse = searchResultsCall.execute()
                    if (productIdsResponse.isSuccessful && productIdsResponse.body()?.result?.isNotEmpty() == true) {
                        productSearchResultsLiveData.postValue(
                            Resource.success(
                                productIdsResponse.body()!!,
                                productIdsResponse.code()
                            )
                        )
                    } else {
                        productSearchResultsLiveData.postValue(Resource.error())
                    }
                }
            }
        } catch (e: Exception) {
            productSearchResultsLiveData.postValue(Resource.error())
        }
    }

    fun getSelectedGender() = configurationRepository.getSelectedGender() ?: GENDER_WOMEN
    fun getSelectedCurrency() = configurationRepository.getCurrency()
}