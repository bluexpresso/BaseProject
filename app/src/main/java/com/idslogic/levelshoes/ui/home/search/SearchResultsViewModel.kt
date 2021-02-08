package com.idslogic.levelshoes.ui.home.search

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.GENDER_WOMEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList

class SearchResultsViewModel @ViewModelInject constructor(
    application: Application,
    private val categoryRepository: CategoryRepository,
    private val configurationRepository: ConfigurationRepository

) :
    AndroidViewModel(application) {

    var categories: HashMap<String, LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>>? =
        null
    private val context = application.applicationContext
    val categorySearchLiveData =
        MutableLiveData<Resource<ArrayList<Pair<CategorySearch, CategorySearch>>>>()
    private var searchResultsCall: Call<ListingProductResponse>? = null
    var gender: String = GENDER_WOMEN
    val recentTrendingList = mutableListOf<SearchRecentTrendingWrapper>()
    val trendingProductsLiveData = MutableLiveData<Resource<ArrayList<ListingProduct>>>()
    val productSearchResultsLiveData = MutableLiveData<Resource<ListingProductResponse>>()
    suspend fun getTendingItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val productIdsResponse =
                    categoryRepository.getCategoryBasedTrendingProductsFromKlevu(null)

                if (productIdsResponse.isSuccessful && productIdsResponse.body()?.popularProducts?.isNotEmpty() == true) {
                    val trendingProducts = arrayListOf<ListingProduct>()
                    productIdsResponse.body()?.popularProducts?.forEach { popularProduct ->

                        popularProduct.name?.let { name -> trendingProducts.add(popularProduct) }
                    }
                    trendingProductsLiveData.postValue(Resource.success(trendingProducts))

                } else {
                    trendingProductsLiveData.postValue(Resource.error())
                }
            }
        }
    }

    fun getSelectedCurrency() = configurationRepository.getCurrency()

    suspend fun getProductsFromCategory(searchString: String, gender: String) {
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    productSearchResultsLiveData.postValue(Resource.loading())
                    searchResultsCall?.cancel()
                    searchResultsCall =
                        categoryRepository.getCategoryBasedSearchResultsProductsFromKlevu(
                            searchString,
                            gender
                        )
                    val productIdsResponse = searchResultsCall!!.execute()
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

    fun getRecentSearches() = configurationRepository.getRecentSearches()
    fun addRecentSearch(text: String) {
        configurationRepository.addRecentSearch(text)
    }

    fun getSelectedGender() = configurationRepository.getSelectedGender() ?: GENDER_WOMEN
    fun getRecentTrendingList() {
        recentTrendingList.clear()
        val recentSearches = getRecentSearches()
        if (!recentSearches.isNullOrEmpty()) {
            recentTrendingList.add(
                SearchRecentTrendingWrapper(
                    null,
                    true,
                    context.getString(R.string.recent_items)
                )
            )
            recentSearches.forEach { element ->
                recentTrendingList.add(
                    SearchRecentTrendingWrapper(
                        null,
                        false,
                        element
                    )
                )
            }
        }

        recentTrendingList.add(
            SearchRecentTrendingWrapper(
                null,
                true,
                context.getString(R.string.trending_items)
            )
        )
        trendingProductsLiveData.value?.data?.forEach { trendingItem ->
            recentTrendingList.add(
                SearchRecentTrendingWrapper(
                    trendingItem,
                    false,
                    trendingItem.name
                )
            )
        }
    }

    suspend fun getCategoriesSearchResults(searchTerm: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categorySearchLiveData.postValue(Resource.loading())
                val searchResults = ArrayList<Pair<CategorySearch, CategorySearch>>()
                categories?.get(gender)?.forEach {
                    it.value?.forEach { catSearch ->
                        if (catSearch.name?.contains(searchTerm,true) == true) {
                            searchResults.add(Pair(it.key, catSearch))
                        }
                        catSearch.childrenData?.forEach { catChildrenData ->
                            if (catChildrenData.name?.contains(searchTerm,true) == true) {
                                searchResults.add(Pair(catSearch, catChildrenData))
                            }
                        }
                    }
                }
                categorySearchLiveData.postValue(
                    if (searchResults.size > 0) Resource.success(searchResults)
                    else Resource.error()
                )
            }
        }
    }

}