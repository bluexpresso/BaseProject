package com.idslogic.levelshoes.ui.home.search

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.HashMap
import java.util.LinkedHashMap
import javax.inject.Inject

@HiltViewModel
class SearchItemViewModel @Inject constructor(
    application: Application,
    private val categoryRepository: CategoryRepository,
    private val configurationRepository: ConfigurationRepository

) :
    AndroidViewModel(application) {
    var gender: String = GENDER_WOMEN
    val productSearchResultsLiveData = MutableLiveData<Resource<ListingProductResponse>>()
    val categorySearchLiveData =
        MutableLiveData<Resource<ArrayList<Pair<CategorySearch, CategorySearch>>>>()
    var categories: HashMap<String, LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>>? =
        null

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

    suspend fun getCategoriesSearchResults(searchTerm: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categorySearchLiveData.postValue(Resource.loading())
                categorySearchLiveData.value?.data?.clear()
                val searchResults = ArrayList<Pair<CategorySearch, CategorySearch>>()
                categories?.get(gender)?.forEach {
                    it.value?.forEach { catSearch ->
//                        if (searchResults.size < 3) {
                        if (catSearch.name?.contains(searchTerm, true) == true) {
                            searchResults.add(Pair(it.key, catSearch))
                        }
                        catSearch.childrenData?.forEach { catChildrenData ->
//                                if (searchResults.size < 3) {
                            if (catChildrenData.name?.contains(searchTerm, true) == true
                                && !catChildrenData.childrenData.isNullOrEmpty()
                            ) {
                                searchResults.add(Pair(catSearch, catChildrenData))
                            }
//                                }
                        }
//                        }
                    }
                }
                categorySearchLiveData.postValue(
                    if (searchResults.size > 0) Resource.success(searchResults)
                    else Resource.error()
                )
            }
        }
    }

    fun getSelectedGender() = configurationRepository.getSelectedGender() ?: GENDER_WOMEN
    fun getSelectedCurrency() = configurationRepository.getCurrency()
}